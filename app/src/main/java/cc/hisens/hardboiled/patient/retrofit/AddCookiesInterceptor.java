package cc.hisens.hardboiled.patient.retrofit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.HashSet;

import androidx.annotation.NonNull;
import cc.hisens.hardboiled.patient.MyApplication;
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


//添加cookies，实现登录了免登录，如webview（不过这边需要同步cookie给webview）

public class AddCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        HashSet<String> preferences = (HashSet) MyApplication.getInstance().getSharedPreferences("cookie", MyApplication.getInstance().MODE_PRIVATE).getStringSet("cookie", null);
        if (preferences != null) {
            for (String cookie : preferences) {
                builder.addHeader("Cookie", cookie);
                Log.v("OkHttp", "Adding Header: " + cookie);
                // This is done so I know which headers are being added; this interceptor is used after the normal logging of OkHttp
            }
        }
        return chain.proceed(builder.build());
    }
}


