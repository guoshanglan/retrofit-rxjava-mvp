package cc.hisens.hardboiled.patient.ui.activity.login.present;

import android.text.TextUtils;

import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.ui.activity.login.model.User;
import cc.hisens.hardboiled.patient.ui.activity.login.view.LoginView;
import cc.hisens.hardboiled.patient.ui.activity.login.view.VoliateCodeView;

public class GetVoliatCodePresenter extends BasePresenter<VoliateCodeView> {
    public User user;

    public GetVoliatCodePresenter(){
         user=new User();
    }


    //获取验证码
    public void getVerificationCode(){
        user.getVerificationCode(mView.getContext(),mView.getNumber(),this);
    }

    //登录成功,这边的mview，已经在basePresenter中定义过了
    public void getSuccess(String info) {
        if (info!=null)
            mView.GetSuccessful(info);

    }


    //登录失败
    public void getFailed(String str) {
        if (!TextUtils.isEmpty(str)){
            mView.GetFailedError(str);
        }

    }


}
