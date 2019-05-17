package cc.hisens.hardboiled.patient.retrofit;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.socks.library.KLog;

import java.io.IOException;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.patient.data.net.cookie
 * @fileName ReceivedCookiesInterceptor
 * @date on 2017/8/9 10:19
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 */

public class ReceivedCookiesInterceptor implements Interceptor {
    SharedPreferences sharedPreferences;
    private Context context;

    public ReceivedCookiesInterceptor(Context context) {
        super();
        this.context = context;
        sharedPreferences = context.getSharedPreferences("cookie", Context.MODE_PRIVATE);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (chain == null)
            KLog.d("http", "Receivedchain == null");
        Response originalResponse = chain.proceed(chain.request());
        Log.d("http", "originalResponse" + originalResponse.toString());
        if (!originalResponse.headers("set-cookie").isEmpty()) {
            final StringBuffer cookieBuffer = new StringBuffer();
            Observable.fromIterable(originalResponse.headers("set-cookie"))
                    .map(new Function<String, String>() {
                        @Override
                        public String apply(@NonNull String s) {
                            String[] cookieArray = s.split(";");
                            return cookieArray[0];
                        }
                    })
                    .subscribe(new Consumer<String>() {
                        @Override
                        public void accept(@NonNull String cookie) {
                            cookieBuffer.append(cookie).append(";");
                        }
                    });
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("cookie", cookieBuffer.toString());
            KLog.d("http", "ReceivedCookiesInterceptor" + cookieBuffer.toString());
            editor.commit();
        }
        return originalResponse;
    }
}
