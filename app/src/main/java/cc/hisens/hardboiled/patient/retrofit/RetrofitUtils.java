package cc.hisens.hardboiled.patient.retrofit;


import java.util.concurrent.TimeUnit;

import cc.hisens.hardboiled.patient.MyApplication;
import io.reactivex.annotations.NonNull;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit封装
 */
public class RetrofitUtils {
    private static final String TAG = "RetrofitUtils";
    private static ApiService mApiSerivce;
    /**
     * 单例模式
     */
    public static ApiService getApiUrl() {
        if (mApiSerivce == null) {
            synchronized (RetrofitUtils.class) {
                if (mApiSerivce == null) {
                    mApiSerivce = new RetrofitUtils().getRetrofit();
                }
            }
        }
        return mApiSerivce;
    }
    private RetrofitUtils(){}

    public ApiService getRetrofit() {
        // 初始化Retrofit
        ApiService apiUrl = initRetrofit(initOkHttp()) .create(ApiService.class);
        return apiUrl;
    }

    /**
     * 初始化Retrofit
     */
    @NonNull
    private Retrofit initRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                    .client(client)
                    .baseUrl(Url.BaseUrl2)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
    }

    /**
     * 初始化okhttp
     */
    @NonNull
    private OkHttpClient initOkHttp() {
        return new OkHttpClient().newBuilder()
                    .readTimeout(Url.DEFAULT_TIME, TimeUnit.SECONDS)//设置读取超时时间
                    .connectTimeout(Url.DEFAULT_TIME, TimeUnit.SECONDS)//设置请求超时时间
                    .writeTimeout(Url.DEFAULT_TIME,TimeUnit.SECONDS)//设置写入超时时间
                    .addInterceptor(new LogInterceptor())//添加打印拦截
                    .addInterceptor(new AddCookiesInterceptor())
                    .addInterceptor(new ReceivedCookiesInterceptor())
                    .retryOnConnectionFailure(true)//设置出现错误进行重新连接。
                    .build();
    }
}

