package cc.hisens.hardboiled.patient.mvp.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

import cc.hisens.hardboiled.patient.mvp.present.LoginPresenter;
import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.utils.ToastUtils;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.patient.data.net.model.result
 * @fileName PaientUser
 * @date on 2017/8/8 14:44
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 *
 * 患者用户的model类
 */
@RealmClass
public class PaientUser implements RealmModel {

    @PrimaryKey
    @SerializedName("uid")
    public String uid;

    @SerializedName("phone")
    public String phone;

    @SerializedName("name")
    public String name;

    @SerializedName("headurl")
    public String headhUrl;

    @SerializedName("thumburl")
    public String thumbUrl;

    @SerializedName("level")
    public int level;
    @SerializedName("_id")
    public String _id;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHeadhUrl() {
        return headhUrl;
    }

    public void setHeadhUrl(String headhUrl) {
        this.headhUrl = headhUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }


    @Override
    public String toString() {
        return "PaientUser{" +
                "uid='" + uid + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                ", headhUrl='" + headhUrl + '\'' +
                ", thumbUrl='" + thumbUrl + '\'' +
                ", level=" + level +
                '}';
    }

    //登录的网络请求
    public void login(final Context context, String number, String VerificationCode, final LoginPresenter listener) {
        Map<String, String> params = new HashMap<>();
        params.put("phone", number);
        params.put("code", VerificationCode);

        RequestUtils.post(context, Url.paientLogin, params, new HashMap<>(), new MyObserver<BaseResponse>(context) {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result != null) {

                    if (result.result == 0) {
                        Gson gson = new Gson();
                        PaientUser paientUser = new Gson().fromJson(gson.toJson(result.getDatas()), PaientUser.class);
                        listener.loginSuccess(paientUser);
                    } else {

                        listener.loginFailed(result.message);
                    }

                }
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                listener.loginFailed(errorMsg);
            }
        });


    }

    //发送验证码的网络请求
    public void getVerificationCode(final Context context, String number, final LoginPresenter listener) {
        Map<String, String> params = new HashMap<>();
        params.put("phone", number);
        RequestUtils.post(context, Url.getVerificationCode, params, new HashMap<>(), new MyObserver<BaseResponse>(context) {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result != null) {

                    if (result.result == 0) {

                        ToastUtils.show(context, "发送成功");
                    } else {

                        listener.loginFailed(result.message);
                    }
                }
            }

            @Override
            public void onFailure(Throwable e, String errorMsg) {
                listener.loginFailed(errorMsg);
            }
        });

    }


}
