package cc.hisens.hardboiled.patient.ui.activity.login.view;

import android.content.Context;

import cc.hisens.hardboiled.patient.ui.activity.login.model.User;

public interface VoliateCodeView {
    String getNumber();  //电话号码

    Context getContext();   //需要的上下文对象


    /**
     * 获取验证码成功跳转
     * @param info
     */
    void GetSuccessful(String info);



    /**
     * 获取验证码失败
     * @param str
     */
    void GetFailedError(String str);
}
