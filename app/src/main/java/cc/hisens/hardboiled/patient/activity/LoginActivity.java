package cc.hisens.hardboiled.patient.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import androidx.core.content.ContextCompat;
import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.Appconfig;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.db.impl.UserRepositoryImpl;
import cc.hisens.hardboiled.patient.mvp.model.User;
import cc.hisens.hardboiled.patient.mvp.present.LoginPresenter;
import cc.hisens.hardboiled.patient.mvp.view.LoginView;
import cc.hisens.hardboiled.patient.utils.ToastUtils;
import cc.hisens.hardboiled.patient.wideview.TitleBar;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


//登录页面
public class LoginActivity extends BaseActivity implements LoginView {

    @BindView(R.id.title_bar)
    protected TitleBar mTitleBar;
    @BindView(R.id.et_input_phone_num)
    protected EditText mEtInputPhoneNum;   //手机号输入框
    @BindView(R.id.et_input_verification_code)
    protected EditText mEtInputVerificationCode;   //验证码输入框
    @BindView(R.id.tv_get_verification_code)
    protected TextView mTvGetVerificationCode;     //发送验证码
    @BindView(R.id.fl_msg_parent)
    protected FrameLayout mFlMsgParent;

    protected Disposable mDisposable;

    private User mUser;
    private LoginPresenter loginPresenter;  //登录的presenter


    //页面创建生命周期
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initview();
    }


    //初始化控件
    private void initview() {
        mTitleBar.setTitle(getString(R.string.login));
        mTitleBar.setTitleColor(ContextCompat.getColor(this, android.R.color.black));
        mTitleBar.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));

    }


    //点击事件
    @OnClick({R.id.btn_login, R.id.tv_get_verification_code})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:    //登录
                if (TextUtils.isEmpty(mEtInputPhoneNum.getText().toString())) {
                    ToastUtils.show(this, R.string.login_error_hint);
                } else if (TextUtils.isEmpty(mEtInputVerificationCode.getText().toString())) {
                    ToastUtils.show(this, R.string.input_verification_code_hint);
                } else {
                    initProgressDialog();
                    loginPresenter.login();   //进行登录
                }
                break;
            case R.id.tv_get_verification_code:    //获取验证码
                if (TextUtils.isEmpty(mEtInputPhoneNum.getText().toString())) {
                    ToastUtils.show(this, R.string.login_error_hint);
                } else {
                    setTime();
                    loginPresenter.getVerificationCode();   //进行获取手机验证码
                }
                break;

        }

    }
    //重置获取验证码按钮的点击状态
    protected void resetGetVerificationCodeEditText() {
        mTvGetVerificationCode.setText(getString(R.string.get_verification_code));
        mTvGetVerificationCode.setEnabled(true);
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }
    //获取验证码倒计时
    public void setTime() {
        mTvGetVerificationCode.setEnabled(false);
        mDisposable = Observable.interval(0, 1, TimeUnit.SECONDS)
                .compose(this.<Long>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(@NonNull Long aLong) {
                        int count = (int) (60 - aLong);
                        mTvGetVerificationCode.setText(String.format("%ds", Math.max(0, count)));
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

    //获得一个中间Presenter的中间桥梁
    @Override
    public BasePresenter getPresenter() {
        if (loginPresenter == null) {
            loginPresenter = new LoginPresenter();
        }
        return loginPresenter;
    }

    //获取手机号
    @Override
    public String getNumber() {
        return mEtInputPhoneNum.getText().toString().replace(" ", "");
    }

    //获取验证码
    @Override
    public String getVoliatCode() {
        return mEtInputVerificationCode.getText().toString().replace(" ", "");
    }

    @Override
    public Context getContext() {
        return this;
    }


    //登录成功
    @Override
    public void setLoginsuccessful(User loginBean) {
        resetGetVerificationCodeEditText(); //重置
        dismissProgressDialog();
        Log.e("成功", loginBean.name);
        sharedUtils.writeBoolean(UserConfig.UserInfo.EXTRA_IS_LOGIN,true);
        UserConfig.UserInfo.setUid(loginBean.uid);
         new UserRepositoryImpl().saveUser(loginBean);  //将登陆成功的用户信息进行存储

        startActivity(new Intent(LoginActivity.this,MainActivity.class));  //跳转到主界面
    }

    //登录失败
    @Override
    public void setFailedError(String str) {
        ToastUtils.show(this, str);
        Log.e("错误", str);
        dismissProgressDialog();
        resetGetVerificationCodeEditText(); //重置
    }

}
