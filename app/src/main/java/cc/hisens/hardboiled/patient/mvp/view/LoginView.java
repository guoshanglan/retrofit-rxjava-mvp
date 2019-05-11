package cc.hisens.hardboiled.patient.mvp.view;

import android.content.Context;





import cc.hisens.hardboiled.patient.mvp.model.User;


/**
 * Created by Administrator on 2018/8/27.
 */

//登录的view，用来更新UI
public interface LoginView {
    String getNumber();  //电话号码
    String getVoliatCode();  //验证码
    Context getContext();   //需要的上下文对象


    /**
     * 用户登录成功跳转
     * @param loginBean
     */
    void setLoginsuccessful(User loginBean);



    /**
     * 登录失败
     * @param str
     */
    void setFailedError(String str);

}
