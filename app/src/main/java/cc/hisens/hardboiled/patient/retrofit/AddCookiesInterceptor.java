package cc.hisens.hardboiled.patient.retrofit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.patient.data.net.cookie
 * @fileName AddCookiesInterceptor
 * @date on 2017/8/9 10:16
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 */

public class AddCookiesInterceptor implements Interceptor {
    private Context context;
    private String lang;

    public AddCookiesInterceptor(Context context, String lang) {
        super();
        this.context = context;
        this.lang = lang;

    }

    @SuppressLint("CheckResult")
    @Override
    public Response intercept(Chain chain) throws IOException {
        if (chain == null)
            Log.d("http", "Addchain == null");
        final Request.Builder builder = chain.request().newBuilder();
        SharedPreferences sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
        Observable.just(sharedPreferences.getString("cookie", ""))
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(@NonNull String cookie) {
                        if (cookie.contains("lang=ch")) {
                            cookie = cookie.replace("lang=ch", "lang=" + lang);
                        }
                        if (cookie.contains("lang=en")) {
                            cookie = cookie.replace("lang=en", "lang=" + lang);
                        }
                        //添加cookie
                        //                        Log.d("http", "AddCookiesInterceptor"+cookie);
                        builder.addHeader("cookie", cookie);
                    }
                });
        return chain.proceed(builder.build());
    }
}
