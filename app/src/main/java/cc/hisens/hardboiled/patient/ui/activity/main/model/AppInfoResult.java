package cc.hisens.hardboiled.patient.ui.activity.main.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.login.model.User;
import cc.hisens.hardboiled.patient.ui.activity.main.present.AppInfoPresenter;
import cc.hisens.hardboiled.patient.utils.ToastUtils;

public class AppInfoResult {

    @SerializedName("supportSysVersion")
    public String supportSysVersion;

    @SerializedName("newestAppVersion")
    public String newestAppVersion;

    @SerializedName("forceUpgrade")
    public int forceUpgrade;

    @SerializedName("content")
    public String content;

    @SerializedName("link")
    public String link;

    @Override
    public String toString() {
        return "AppInfoResult{" +
                "supportSysVersion='" + supportSysVersion + '\'' +
                ", newestAppVersion='" + newestAppVersion + '\'' +
                ", forceUpgrade=" + forceUpgrade +
                ", content='" + content + '\'' +
                ", link='" + link + '\'' +
                '}';
    }



    //检查版本更新
    public void checkUpdate(Context context, AppInfoPresenter appInfoPresenter){
        Map params=new HashMap();
        params.put("appid",1);
        params.put("platform",2);

        RequestUtils.get(context, Url.getAppInfo, params, new MyObserver<BaseResponse>(context){
            @Override
            public void onSuccess(BaseResponse result) {
                if (result != null) {

                    if (result.result == 0) {
                        Gson gson = new Gson();
                        AppInfoResult appInfoResult = new Gson().fromJson(gson.toJson(result.getDatas()),AppInfoResult .class);
                        appInfoPresenter.setCheckUpdateInfo(appInfoResult);   //返回成功数据

                    } else {

                           appInfoPresenter.setFailedError(result.message);
                    }

                }

            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                appInfoPresenter.setFailedError(errorMsg);
            }
        });

    }




}
