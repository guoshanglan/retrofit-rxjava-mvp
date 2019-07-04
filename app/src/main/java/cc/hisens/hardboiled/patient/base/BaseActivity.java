package cc.hisens.hardboiled.patient.base;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;
import cc.hisens.hardboiled.patient.MyApplication;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.ui.activity.login.LoginActivity;
import cc.hisens.hardboiled.patient.utils.ScreenUtil;
import cc.hisens.hardboiled.patient.utils.ScreenUtils;
import cc.hisens.hardboiled.patient.utils.SharedUtils;

public abstract class BaseActivity<T extends BasePresenter> extends RxAppCompatActivity implements PresenterCallback{

    protected BasePresenter mPresenter;   //基本交互类presenter
    protected MyApplication appLication;
    protected SharedUtils sharedUtils;     //共享参数sp的对象
    protected ProgressDialog mProgressDialog;   //加载框
    protected Toast mToast;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
       //屏幕适配一定要设置在setcontentView之前
        ScreenUtil.resetDensity(this);  //Android屏幕适配
        setContentView(getLayoutId());
        ButterKnife.bind(this);

        init();

    }


    //初始化我们所需要的配置
    protected  void init(){
        //绑定初始化application
        appLication=MyApplication.getInstance();
        sharedUtils=new SharedUtils(this);

        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attach(this);
        }

    }


    public void navigateToLogin(){
        finish();
        ActivityCollector.finishAll();
        UserConfig.UserInfo.setLogin(false);
        startActivity(new Intent(this, LoginActivity.class));
    }



    //初始化进度加载框
    protected void initProgressDialog( String info) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {

                }
            });
            mProgressDialog.setMessage(info);
        }

        mProgressDialog.show();
    }

    //初始化自定义Toast

    protected  void  ShowToast(String msg){
        WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
        View toastView = LayoutInflater.from(this).inflate(R.layout.toast_view, null);
        TextView tv=toastView.findViewById(R.id.txtToastMessage);
        tv.setText(msg);
        if (mToast == null) {
            mToast = new Toast(this);
        }
        LinearLayout relativeLayout = (LinearLayout) toastView.findViewById(R.id.test);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(wm
                .getDefaultDisplay().getWidth(), (int) ScreenUtils.dp2px(this, 40));
        relativeLayout.setLayoutParams(layoutParams);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);
        mToast.setView(toastView);
        mToast.getView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);//设置Toast可以布局到系统状态栏的下面
        mToast.show();
    }




    //进度框消失
    protected void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    //返回布局的Id
    protected abstract int getLayoutId();


    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ScreenUtil.resetDensity(this);
    }



    //页面销毁
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter=null;
        }
        ActivityCollector.removeActivity(this);

        RefWatcher refWatcher = appLication.getRefWatcher(this);
        refWatcher.watch(this);

    }

}
