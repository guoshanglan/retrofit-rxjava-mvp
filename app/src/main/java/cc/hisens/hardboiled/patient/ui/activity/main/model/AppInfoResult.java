package cc.hisens.hardboiled.patient.ui.activity.main.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.main.present.AppInfoPresenter;
import cc.hisens.hardboiled.patient.utils.AppVersionUtil;

public class AppInfoResult {


    /**
     * client_type : 1
     * version : 1.0.5
     * shop_url : ["www.**","www.aaa"]
     * status : 1
     * title : 安卓二版发行
     * content : ["1.医生模块","2.用户模块","3.诊断模块"]
     * created_time : 1553051661
     */

    //设备类型：1 Android、 2 ios
    @SerializedName("client_type")
    private int client_type;

    //版本号
    @SerializedName("version")
    private String version;

    //0.不需要更新或者硬件版本不支持,1.提示性升级,2.强制升级
    @SerializedName("status")
    private int status;

    //标题
    @SerializedName("title")
    private String title;

    //版本发布时间
    @SerializedName("created_time")
    private int created_time;

    //应用商城Url
    @SerializedName("shop_url")
    private List<String> shop_url;

    //内容描述
    @SerializedName("content")
    private List<String> content;

    @Override
    public String toString() {
        return "AppInfoResult{" +
                "client_type=" + client_type +
                ", version='" + version + '\'' +
                ", status=" + status +
                ", title='" + title + '\'' +
                ", created_time=" + created_time +
                ", shop_url=" + shop_url +
                ", content=" + content +
                '}';
    }



    public int getClient_type() {
        return client_type;
    }

    public void setClient_type(int client_type) {
        this.client_type = client_type;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCreated_time() {
        return created_time;
    }

    public void setCreated_time(int created_time) {
        this.created_time = created_time;
    }

    public List<String> getShop_url() {
        return shop_url;
    }

    public void setShop_url(List<String> shop_url) {
        this.shop_url = shop_url;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }



    //检查版本更新
    public void checkUpdate(Context context, AppInfoPresenter appInfoPresenter){
        Map params=new HashMap();
        params.put("client_type",1);
        params.put("hard_version","1.0.0");
        params.put("app_version", AppVersionUtil.getLocalVersion(context));
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
