package cc.hisens.hardboiled.patient.ui.activity.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.ActivityCollector;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.db.impl.UserRepositoryImpl;
import cc.hisens.hardboiled.patient.ui.activity.UserAgreementActivity;
import cc.hisens.hardboiled.patient.ui.activity.login.model.User;
import cc.hisens.hardboiled.patient.ui.activity.login.present.LoginPresenter;
import cc.hisens.hardboiled.patient.ui.activity.login.view.LoginView;
import cc.hisens.hardboiled.patient.ui.activity.main.MainActivity;
import cc.hisens.hardboiled.patient.utils.ToastUtils;
import cc.hisens.hardboiled.patient.wideview.PhoneCode;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


//登录页
public class LoginActivity extends BaseActivity implements LoginView, PhoneCode.OnInputListener {
    @BindView(R.id.tv_number)
    public TextView tvNumber;   //展示手机号信息
    @BindView(R.id.phonecode_view)
    public PhoneCode phoneCode;    //验证码输入控件
    @BindView(R.id.btn_getCode)
    public Button btnCode;        //获取验证码控件
    @BindView(R.id.btn_login)
    public Button btLogin;      //登录按钮
    @BindView(R.id.tv_back)
    public TextView tvBack;
    public LoginPresenter loginPresenter;   //登录的presenter
    protected Disposable mDisposable;    //操控倒计时
    public String phoneNumber;   //接手前面传递过来的手机号


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        phoneNumber = getIntent().getStringExtra("number");
        tvNumber.setText("验证码已通过短信发送至 " + phoneNumber);
        phoneCode.setOnInputListener(this);
        setTime();

    }


    //点击事件
    @OnClick({R.id.btn_login, R.id.tv_back, R.id.btn_getCode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:    //登录
                if (TextUtils.isEmpty(phoneNumber)) {
                    ToastUtils.show(this, R.string.login_error_hint);
                } else if (TextUtils.isEmpty(phoneCode.getPhoneCode())) {
                    ToastUtils.show(this, R.string.input_verification_code_hint);
                } else {
                    initProgressDialog(getString(R.string.is_landing));
                    loginPresenter.login();   //进行登录
                }
                break;
            case R.id.btn_getCode:
                setTime();
                loginPresenter.getVerificationCode();
                break;

            case R.id.tv_back:  //后退
                finish();
                break;


        }

    }

    //重置获取验证码按钮的点击状态
    protected void resetGetVerificationCodeEditText() {
        btnCode.setText(getString(R.string.get_verification_code));
        btnCode.setEnabled(true);
        btnCode.setBackgroundResource(R.drawable.btn_getverification_code_input_shape);
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    //获取验证码倒计时
    public void setTime() {
        btnCode.setEnabled(false);
        btnCode.setBackgroundResource(R.drawable.btn_getverification_code_uninput_shape);
        mDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .compose(this.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) {
                        int count = (int) (60 - aLong);
                        btnCode.setText(String.format("%ds", Math.max(0, count)));
                        if (count < 0) {
                            resetGetVerificationCodeEditText();
                        }
                    }
                });
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    public BasePresenter getPresenter() {
        if (loginPresenter == null) {
            loginPresenter = new LoginPresenter();
        }
        return loginPresenter;
    }

    @Override
    public String getNumber() {
        return phoneNumber.replace(" ", "");
    }

    @Override
    public String getVoliatCode() {
        return phoneCode.getPhoneCode();
    }

    @Override
    public Context getContext() {
        return this;
    }


    //登录成功
    @Override
    public void setLoginsuccessful(User user) {
        resetGetVerificationCodeEditText(); //重置
        dismissProgressDialog();
        Log.e("成功", user.getUser_name());
        sharedUtils.writeBoolean(UserConfig.UserInfo.EXTRA_IS_LOGIN, true);   //存储已经登录
        sharedUtils.writeString(UserConfig.UserInfo.EXTRA_UID, user.getId() + "");    //存储用户userId
        UserConfig.UserInfo.setUid(user.getId() + "");
        new UserRepositoryImpl().saveUser(user);  //将登陆成功的用户信息进行存储

        startActivity(new Intent(this, MainActivity.class));  //跳转到主界面
        ActivityCollector.finishAll();  //销毁

    }

    @Override
    public void setFailedError(String str) {
        ToastUtils.show(this, str);
        Log.e("错误", str);
        dismissProgressDialog();
        resetGetVerificationCodeEditText(); //重置
    }

    @Override
    public void onSucess(String code) {

        btLogin.setBackgroundResource(R.drawable.btn_getverification_code_input_shape);
        btLogin.setClickable(true);


    }

    @Override
    public void onInput() {
        btLogin.setBackgroundResource(R.drawable.btn_getverification_code_uninput_shape);
        btLogin.setClickable(false);
    }
}
