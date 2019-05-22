package cc.hisens.hardboiled.patient.ui.activity.main;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.tencent.bugly.crashreport.CrashReport;

import java.net.URI;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.ActivityCollector;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.ble.BLEManagerWrapper;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.retrofit.Url;
import cc.hisens.hardboiled.patient.ui.activity.login.LoginActivity;
import cc.hisens.hardboiled.patient.ui.activity.main.model.AppInfoResult;
import cc.hisens.hardboiled.patient.ui.activity.main.present.AppInfoPresenter;
import cc.hisens.hardboiled.patient.ui.activity.main.view.AppcheckInfoView;
import cc.hisens.hardboiled.patient.ui.fragment.FristFragment;
import cc.hisens.hardboiled.patient.ui.fragment.me.MeFragment;
import cc.hisens.hardboiled.patient.ui.fragment.doctor.DoctorFragment;
import cc.hisens.hardboiled.patient.ui.fragment.ThirdFragment;
import cc.hisens.hardboiled.patient.utils.AppUpdateUtils;
import cc.hisens.hardboiled.patient.utils.ToastUtils;
import cc.hisens.hardboiled.patient.websocket.ChatClient;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;


//主要的Activity
public class MainActivity extends BaseActivity implements AppcheckInfoView {
    @BindView(R.id.fl_container)
    FrameLayout myFrameLayout;  //用来展示fragment的
    @BindViews({R.id.rbtn_monitor, R.id.rbtn_doctor, R.id.rbtn_helper, R.id.rbtn_me})
    //底部四个按钮,ButterKnife一次性注解多个
    public List<RadioButton> buttonList;
    @BindView(R.id.tv_doctor_message_count)
    TextView tvDoctormessageCount;  //医生消息
    @BindView(R.id.tv_helper_message_count)
    TextView tvHelp_message_count;   //
    private Fragment firstFragment, secondFragment, thirdFragment, meFragment;
    private Fragment[] fragments;
    private FragmentManager fragmentmanager;
    private FragmentTransaction ft;
    private int currentTabIndex;  //当前切换的是第几个fragment
    private static Boolean mIsExit = false;
    private AppInfoPresenter appInfoPresenter;   //检查APP版本更新的第三方桥梁present
    private ChatClient mChatClient;  //webSocket客户端

    private BLEManagerWrapper bleManagerWrapper;  //蓝牙操作管理者


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        bleManagerWrapper = BLEManagerWrapper.getInstance();
        bleManagerWrapper.initialize(this);

        super.onCreate(savedInstanceState);
        initView();
        grantPermissions();

        ConnectedWebSocket();   //进行websocket的长连接

        appInfoPresenter.CheckAppUpdate(); //检查App版本更新

    }

    //初始化控件和界面
    private void initView() {
        firstFragment = new FristFragment();
        secondFragment = new DoctorFragment();
        thirdFragment = new ThirdFragment();
        meFragment = new MeFragment();
        fragments = new Fragment[]{firstFragment, secondFragment, thirdFragment, meFragment};
        fragmentmanager = getSupportFragmentManager();
        ft = fragmentmanager.beginTransaction();
        buttonList.get(0).setChecked(true);
        ft.add(R.id.fl_container, firstFragment);
        ft.show(firstFragment).commit();
        tvDoctormessageCount.setText("12");
        tvHelp_message_count.setText("2");


    }

    //Butterknife 注解点击事件,进行底部Fragment的替换
    @OnClick({R.id.rbtn_monitor, R.id.rbtn_doctor, R.id.rbtn_helper, R.id.rbtn_me})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rbtn_monitor:
                SwitchSkip(0);
                break;
            case R.id.rbtn_doctor:
                SwitchSkip(1);
                break;
            case R.id.rbtn_helper:
                SwitchSkip(2);
                break;
            case R.id.rbtn_me:
                SwitchSkip(3);
                break;
        }
    }


    //切换底部tab页面
    private void SwitchSkip(int index) {
        ft = fragmentmanager.beginTransaction();
        if (currentTabIndex != index) {
            if (!fragments[index].isAdded()) {
                ft.add(R.id.fl_container, fragments[index]);
            }
            ft.hide(fragments[currentTabIndex]).show(fragments[index]).commit();

            for (int i = 0; i < buttonList.size(); i++) {
                if (i == index) {
                    buttonList.get(index).setChecked(true);
                    buttonList.get(index).setTextColor(Color.parseColor("#2ab5d7"));

                } else {
                    buttonList.get(i).setChecked(false);
                    buttonList.get(i).setTextColor(Color.parseColor("#999999"));

                }
            }

            currentTabIndex = index;
        }
    }


    //动态申请蓝牙权限，判断是否含有蓝牙权限，这里显示的是位置信息
    @SuppressLint("CheckResult")
    private void grantPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        if (!rxPermissions.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION) && !rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            rxPermissions.request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(@NonNull Boolean isGranted) {
                            if (isGranted) {
                                // 判断蓝牙所需的权限是否已经有了是否开启
                            } else {
                                ToastUtils.show(MainActivity.this, "permission not granted");
                            }
                        }
                    });
              }
       }


    //监控系统返回键，按两次退出当前应用
    @Override
    public void onBackPressed() {
        exitByDoubleClick();
    }


    private void exitByDoubleClick() {
        if (!mIsExit) {
            mIsExit = true; // 准备退出
            ToastUtils.show(this, R.string.double_click_exit);
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    mIsExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            ActivityCollector.finishAll();
            System.exit(0);
        }
    }


    //用websocket与服务器进行长连接，目前服务器还没开
    public void ConnectedWebSocket() {
        URI uri = null;
        try {
            uri = URI.create(Url.WEB_SOCKET_URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mChatClient = ChatClient.getInstance(uri);
        mChatClient.connect();
    }


    @Override
    protected int getLayoutId() {

        return R.layout.activity_main;
    }


    //返回一个检查APP版本的present操作对象
    @Override
    public BasePresenter getPresenter() {
        if (appInfoPresenter == null) {
            appInfoPresenter = new AppInfoPresenter();
        }
        return appInfoPresenter;
    }


    //返回上下文对象
    @Override
    public Context getContext() {
        return this;
    }


    //查询APP版本信息有更新
    @Override
    public void setCheckUpdateInfo(AppInfoResult appInfoResult) {

        AppUpdateUtils.getGetInstence().popUpdateDialog(appInfoResult.getContent(), appInfoResult.getShop_url(), this);
    }

    //Session失效，需要重新登录
    @Override
    public void setFailedError(String str) {
        Log.e("错误", str);
        ToastUtils.show(this, str);
       // navigateToLogin();  //跳转到登录页面

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        bleManagerWrapper.recycle();


    }
}
