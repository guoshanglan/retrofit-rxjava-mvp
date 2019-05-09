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
     * Get 请求
     * @param context
     * @param
     */
    public static void get(Context context, String url, Map<String,String>params, MyObserver<BaseResponse> observer){
        RetrofitUtils.getApiUrl()
                .getUser(url,params).compose(RxHelper.observableIO2Main(context))
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
     * Post 请求
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
     * Put 请求
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
     * @param context  上下文对象
     * @param observer   观察者
     * @param params  查询参数map集合
     * @param headsMap  请求头参数
     * @param observer   观察者
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
     * 上传图片，文件
     * @param context
     * @param observer
     */
    public static void upImage(Context context, String  url, String pathName, MyObserver<BaseResponse>  observer){
        File file = new File(pathName);
//        File file = new File(imgPath);
        Map<String,String> header = new HashMap<String, String>();
        header.put("Accept","application/json");

//        File file =new File(filePath);
        RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
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
     * 上传多张图片，多份文件
     * @param files
     */
    public static void upLoadImgs(Context context, String url, List<File> files, MyObserver<BaseResponse>  observer){
        Map<String,String> header = new HashMap<String, String>();
        header.put("Accept","application/json");
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            RequestBody photoRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.addFormDataPart("file", file.getName(), photoRequestBody);
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        RetrofitUtils.getApiUrl().uploadImages(url,header,parts).compose(RxHelper.observableIO2Main(context))
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

