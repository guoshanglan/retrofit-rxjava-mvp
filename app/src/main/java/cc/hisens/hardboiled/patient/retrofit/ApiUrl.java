package cc.hisens.hardboiled.patient.retrofit;

import android.util.Log;

import java.util.List;
import java.util.Map;

import cc.hisens.hardboiled.patient.ui.activity.login.model.User;
import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface ApiUrl {

    /**
     * TODO Get请求
     */
    @GET
    Observable<BaseResponse> getUser(@Url String url,@QueryMap Map<String, String> info); //简洁方式   直接获取所需数据


    /**
     * TODO POST请求
     * @parmas url  请求地址
     * @params Body  利用body进行参数封装
     * @params headsMap  请求所需要的特殊请求头map<key,value>集合
     */
    @POST
    //多个参数
    Observable<BaseResponse> postUser(@Url String url, @Body RequestBody body, @HeaderMap Map<String,String>headsMap);


    /**
     * TODO DELETE
     */
    @DELETE
    Observable<BaseResponse> delete(@Url String url, @Body RequestBody body,@HeaderMap Map<String,String>headsMap);

    /**
     * TODO PUT
     */
    @PUT()
    Observable<BaseResponse> put(@Url String url, @Body RequestBody body,@HeaderMap Map<String,String>headsMap);

    /**
     * TODO 文件上传
     */
    //亲测可用
    @Multipart
    @POST
    Observable<BaseResponse> uploadImage(@Url String url,@HeaderMap Map<String, String> headers, @Part MultipartBody.Part file);

    /**
     * 多文件上传
     */


    @Multipart
    @POST
    Observable<BaseResponse> uploadImages(@Url String url,@HeaderMap Map<String, String> headers, @Part List<MultipartBody.Part> files);

    /**
     * 来自https://blog.csdn.net/impure/article/details/79658098
     * @Streaming 这个注解必须添加，否则文件全部写入内存，文件过大会造成内存溢出
     */
    @Streaming
    @GET
    Observable<BaseResponse> download(@Header("RANGE") String start, @Url String url);
}
