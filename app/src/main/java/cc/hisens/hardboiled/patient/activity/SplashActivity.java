package cc.hisens.hardboiled.patient.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import cc.hisens.hardboiled.patient.Appconfig;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;

public class SplashActivity extends BaseActivity {

    private  final long SLEEP_TIME = 2 * 1000;   //欢迎页延迟多少秒后跳转

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 11) {
                onNext();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler.sendEmptyMessageDelayed(11,SLEEP_TIME);
        //setContentView(R.layout.activity_splash);
//        //初始化EventBus
//        EventBus.getDefault().register(this);     //   这个要有对应的事件处理
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash;
    }


    private void onNext() {

        //判断是否是第一次打开APP
        if (!isIntroductionFinished()) {
            startActivity(new Intent(this, AppIntroductActivity.class));
            finish();
            return;
        }

        //判断用户是否登录
        if (sharedUtils.readBoolean(UserConfig.UserInfo.EXTRA_IS_LOGIN, false)) {
           startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
        finish();  //销毁当前界面
    }


    private boolean isIntroductionFinished() {

        return sharedUtils.readBoolean(Appconfig.PREF_INTRODUCTION_FINISHED, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        EventBus.getDefault().unregister(this);  //取消注册
    }

    //这里不需要Present
    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
