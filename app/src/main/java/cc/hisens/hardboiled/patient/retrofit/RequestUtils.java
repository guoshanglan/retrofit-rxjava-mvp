package cc.hisens.hardboiled.patient.retrofit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.ui.activity.login.model.User;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;

import static java.lang.System.currentTimeMillis;

/**
 * 提交参数方式
 */
public class RequestUtils {


    /**
     * Get 请求
     *
     * @param context
     */
    public static void get(Context context, String url, Map<String, String> params, MyObserver<BaseResponse> observer) {

        RetrofitUtils.getApiUrl()
                .getUser(url, params).compose(RxHelper.observableIO2Main(context))
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
                        observer.onFailure(e, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * Post 请求
     *
     * @param context
     * @param
     */
    public static void post(Context context, String url, Map<String, ?> params, Map<String, String> headsMap, MyObserver<BaseResponse> observer) {

        RetrofitUtils.getApiUrl()
                .postUser(url, convertMapToBody(params), headsMap).compose(RxHelper.observableIO2Main(context))
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
                        observer.onFailure(e, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * Put 请求 ,这个可能是属于表单类型的提交
     *
     * @param context
     * @param
     */
    @SuppressLint("CheckResult")
    public static void put(Context context, String url, Map<String, String> params, Map<String, String> headsMap, MyObserver<BaseResponse> observer) {
        Map<String, String> headers = new HashMap<String, String>();
        RetrofitUtils.getApiUrl()
                .put(url, convertMapToMediaBody(params), headsMap).compose(RxHelper.observableIO2Main(context))
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
                        observer.onFailure(e, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    /**
     * Delete 请求
     *
     * @param context  上下文对象
     * @param observer 观察者
     * @param params   查询参数map集合
     * @param headsMap 请求头参数
     * @param observer 观察者
     */
    @SuppressLint("CheckResult")
    public static void delete(Context context, String url, Map<String, String> params, Map<String, String> headsMap, MyObserver<BaseResponse> observer) {
        RetrofitUtils.getApiUrl()
                .delete(url, convertMapToBody(params), headsMap).compose(RxHelper.observableIO2Main(context))
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
                        observer.onFailure(e, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 上传图片，文件
     *
     * @param context
     * @param observer
     */
    public static void upImage(Context context, String url, String pathName, MyObserver<BaseResponse> observer) {
        File file = new File(pathName);
//        File file = new File(imgPath);
        Map<String, String> header = new HashMap<String, String>();
////        header.put("Accept","application/json");

//        File file =new File(filePath);
        RequestBody reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), reqFile);
        RetrofitUtils.getApiUrl().uploadImage(url, header, body).compose(RxHelper.observableIO2Main(context))
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
                        observer.onFailure(e, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 上传多张图片，多份文件
     *
     * @param files
     */
    public static void upLoadImgs(Context context, String url, List<File> files, MyObserver<BaseResponse> observer) {
        Map<String, String> header = new HashMap<String, String>();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);//表单类型
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            RequestBody photoRequestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            builder.addFormDataPart("file", file.getName(), photoRequestBody);
        }
        List<MultipartBody.Part> parts = builder.build().parts();
        RetrofitUtils.getApiUrl().uploadImages(url, header, parts).compose(RxHelper.observableIO2Main(context))
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
                        observer.onFailure(e, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    //下载文件,url  接口地址   filename:文件名称   observer：回调
    public static void DownLoad(String url, final String filename,MyObserver<File>observer) {
        RetrofitUtils.getApiUrl().download(url).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new Observer<ResponseBody>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ResponseBody baseResponse) {
                        Log.e("下载文件",baseResponse.contentLength()+"");
                      observer.onSuccess(saveFile(filename,baseResponse));
                    }

                    @Override
                    public void onError(Throwable e) {
                        observer.onFailure(e, e.toString());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }


    /**
     * 将map数据转换为 普通的 json RequestBody上传参数给服务器
     *
     * @param map 以前的请求参数
     * @return
     */
    public static RequestBody convertMapToBody(Map<?, ?> map) {
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), new JSONObject(map).toString());
    }


    /**
     * 将map数据转换为图片，文件类型的  RequestBody上传参数给服务器
     *
     * @param map 以前的请求参数
     * @return 待测试
     */
    public static RequestBody convertMapToMediaBody(Map<?, ?> map) {
        return RequestBody.create(MediaType.parse("multipart/form-data; charset=utf-8"), new JSONObject(map).toString());
    }





    //保存文件
    public static File saveFile(String fileName, ResponseBody body) {

        InputStream inputStream = null;
        OutputStream outputStream = null;
        File file = null;
        try {
            if (fileName == null) {
                return null;
            }
            file = new File(Environment.getExternalStorageDirectory().getPath(),  fileName);
            if (file == null || !file.exists()) {
                file.createNewFile();
            }
            long fileSize = body.contentLength();
            long fileSizeDownloaded = 0;
            byte[] fileReader = new byte[4096];
            inputStream = body.byteStream();
            outputStream = new FileOutputStream(file);
            while (true) {
                int read = inputStream.read(fileReader);
                if (read == -1) {
                    break;
                }
                outputStream.write(fileReader, 0, read);
                fileSizeDownloaded += read;
            }
            outputStream.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }


}

