package cc.hisens.hardboiled.patient.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;

import android.text.TextUtils;
import android.util.Log;
import com.google.gson.Gson;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.HashMap;

import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;

import io.reactivex.functions.Consumer;

public class DownLoadEdBean {
    public String create_time;  //创建时间
    public String url; //下载路径
    public String version; //当前ed文件的版本
    public CallBack callBack;


    public DownLoadEdBean(CallBack callBack) {
        this.callBack = callBack;
    }


    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }


    //检查后台Ed文件是否有更新
    public void checkEdUpdate(Context context, SharedUtils sp) {

        RequestUtils.get(context, Url.getEdFile, new HashMap<>(), new MyObserver<BaseResponse>() {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result != null) {

                    if (result.result == 0) {
                        Gson gson = new Gson();
                        DownLoadEdBean bean = new Gson().fromJson(gson.toJson(result.getDatas()), DownLoadEdBean.class);

                        if (TextUtils.isEmpty(sp.readString("edfile"))) {
                            rxPermissionForWrite((Activity) context, sp, bean.url);
                        } else {
                            if (!sp.readString("edfile").equals(bean.url)) {
                                rxPermissionForWrite((Activity) context, sp, bean.url);
                            } else {
                                callBack.Fail("无更新");
                            }

                        }

                    }

                }
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                callBack.Fail(errorMsg);
            }
        });

    }


    //下载文件
    public void DownLoadFile(String url) {

        RequestUtils.DownLoad(url, "ed_file.js", new MyObserver<File>() {
            @Override
            public void onSuccess(File result) {
                if (result != null) {

                    callBack.Success(result);
                }
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                Log.e("下载失败", errorMsg);
                callBack.Fail(errorMsg);
            }
        });
    }


    //检查相机权限
    @SuppressLint("CheckResult")
    public void rxPermissionForWrite(Activity context, SharedUtils sp, String url) {
        sp.writeString("edfile", url);
        RxPermissions rxPermissions = new RxPermissions(context);
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean granted) throws Exception {
                if (granted) {

                    DownLoadFile(url);  //下载文件

                } else {
                    // 权限被拒绝

                    ToastUtils.show(context, "拒绝可能导致某些功能无法使用");
                    callBack.Fail("权限被拒绝");
                }
            }
        });

    }


    public interface CallBack {
        void Success(File file);

        void Fail(String err);
    }

}
