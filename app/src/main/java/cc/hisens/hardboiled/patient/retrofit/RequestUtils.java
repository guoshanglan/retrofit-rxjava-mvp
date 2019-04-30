package cc.hisens.hardboiled.patient.retrofit;

import android.annotation.SuppressLint;
import android.content.Context;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * 提交参数方式
 */
public class RequestUtils {


    /**
     * Post 请求demo
     * @param context
     * @param
     */
    public static void post(Context context, String url, Map<String,String>params, Map<String,String>headsMap, MyObserver<BaseResponse> observer){
        RetrofitUtils.getApiUrl()
                .postUser(url,params,headsMap).compose(RxHelper.observableIO2Main(context))
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                         observer.onSuccess(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e,e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
    /**
     * Put 请求demo
     * @param context
     * @param
     */
    @SuppressLint("CheckResult")
    public static void put(Context context, String url, Map<String,String>params, Map<String,String>headsMap, MyObserver<BaseResponse> observer){
        Map<String, String> headers = new HashMap<String, String>();
        RetrofitUtils.getApiUrl()
                .put(url,params,headsMap).compose(RxHelper.observableIO2Main(context))
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        observer.onSuccess(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e,e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }
    /**
     * Delete 请求demo
     * @param context
     * @param consumer
     */
    @SuppressLint("CheckResult")
    public static void delete(Context context, String url, Map<String,String>params, Map<String,String>headsMap, MyObserver<BaseResponse> observer){
        RetrofitUtils.getApiUrl()
                .delete(url,params,headsMap).compose(RxHelper.observableIO2Main(context))
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        observer.onSuccess(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e,e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 上传图片
     * @param context
     * @param observer
     */
    public static void upImagView(Context context, String  url, String pathName, MyObserver<BaseResponse>  observer){
        File file = new File(pathName);
//        File file = new File(imgPath);
        Map<String,String> header = new HashMap<String, String>();
        header.put("Accept","application/json");

//        File file =new File(filePath);
        RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), file);
//        RequestBody requestFile =
//                RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), reqFile);
        RetrofitUtils.getApiUrl().uploadImage(url,header,body).compose(RxHelper.observableIO2Main(context))
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        observer.onSuccess(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e,e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 上传多张图片
     * @param files
     */
    public static void upLoadImg(Context context, String url, List<File> files, MyObserver<BaseResponse>  observer){
        Map<String,String> header = new HashMap<String, String>();
        header.put("Accept","application/json");
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/*"), file);
            builder.addFormDataPart("file", file.getName(), photoRequestBody);
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        RetrofitUtils.getApiUrl().uploadImage1(url,header,parts).compose(RxHelper.observableIO2Main(context))
                .subscribe(new Observer<BaseResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseResponse baseResponse) {
                        observer.onSuccess(baseResponse);
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e,e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}

