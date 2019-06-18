package cc.hisens.hardboiled.patient.retrofit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.socks.library.KLog;

import java.io.IOException;
import java.util.HashSet;

import androidx.annotation.NonNull;
import cc.hisens.hardboiled.patient.MyApplication;
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
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();
            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }

            //存储登录的cookie信息
            SharedPreferences.Editor config = MyApplication.getInstance().getSharedPreferences("cookie", MyApplication.getInstance().MODE_PRIVATE).edit();
            config.putStringSet("cookie", cookies);
            config.commit();
        }
        return originalResponse;
    }
}


