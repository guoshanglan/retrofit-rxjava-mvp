package cc.hisens.hardboiled.patient.ui.activity.login.model;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

import cc.hisens.hardboiled.patient.retrofit.BaseResponse;
import cc.hisens.hardboiled.patient.retrofit.MyObserver;
import cc.hisens.hardboiled.patient.retrofit.RequestUtils;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.login.present.LoginPresenter;
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
public class User implements RealmModel {


    /**
     * id : 50015
     * phone : 18025494242
     * user_name : init_name
     * head_url :
     * thumb_url :
     * birthday : 0
     * gender : 0
     * country :
     * state :
     * city :
     * created_time : 1558496892
     * update_time : 1558496892
     */
    @PrimaryKey
    @SerializedName("id")
    private int id;     //用户id


    //用户手机号
    @SerializedName("phone")
    private String phone;

    //用户姓名
    @SerializedName("user_name")
    private String user_name;

    //用户头像
    @SerializedName("head_url")
    private String head_url;
    //缩略图
    @SerializedName("thumb_url")
    private String thumb_url;

    //生日
    @SerializedName("birthday")
    private int birthday;
    //性别 ： 1男 2女
    @SerializedName("gender")
    private int gender;
    //国家
    @SerializedName("country")
    private String country;

    //洲
    @SerializedName("state")
    private String state;

    //城市
    @SerializedName("city")
    private String city;

    //用户创建时间
    @SerializedName("created_time")
    private int created_time;

    //用户更新时间
    @SerializedName("update_time")
    private int update_time;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getHead_url() {
        return head_url;
    }

    public void setHead_url(String head_url) {
        this.head_url = head_url;
    }

    public String getThumb_url() {
        return thumb_url;
    }

    public void setThumb_url(String thumb_url) {
        this.thumb_url = thumb_url;
    }

    public int getBirthday() {
        return birthday;
    }

    public void setBirthday(int birthday) {
        this.birthday = birthday;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getCreated_time() {
        return created_time;
    }

    public void setCreated_time(int created_time) {
        this.created_time = created_time;
    }

    public int getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(int update_time) {
        this.update_time = update_time;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", phone='" + phone + '\'' +
                ", user_name='" + user_name + '\'' +
                ", head_url='" + head_url + '\'' +
                ", thumb_url='" + thumb_url + '\'' +
                ", birthday=" + birthday +
                ", gender=" + gender +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", created_time=" + created_time +
                ", update_time=" + update_time +
                '}';
    }

    //登录的网络请求
    public void login(final Context context, String number, String VerificationCode, final LoginPresenter listener) {
        HashMap<String, String> params = new HashMap<>();

        params.put("phone", number);
        params.put("code", VerificationCode);

        RequestUtils.post(context, Url.paientLogin, params, new HashMap<>(), new MyObserver<BaseResponse>(context) {
            @Override
            public void onSuccess(BaseResponse result) {
                if (result != null) {

                    if (result.result == 0) {
                        Gson gson = new Gson();
                        User paientUser = new Gson().fromJson(gson.toJson(result.getDatas()), User.class);
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
        HashMap<String, String> params = new HashMap<>();

        params.put("phone", number);
        RequestUtils.post(context, Url.getVerificationCode,params , new HashMap<>(), new MyObserver<BaseResponse>(context) {
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
                String msg="网络异常";
                listener.loginFailed(msg);
            }
        });

    }
}
