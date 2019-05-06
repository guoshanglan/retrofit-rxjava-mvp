package cc.hisens.hardboiled.patient.base;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import cc.hisens.hardboiled.patient.MyApplication;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.utils.ScreenUtil;
import cc.hisens.hardboiled.patient.utils.SharedUtils;

public abstract class BaseActivity<T extends BasePresenter> extends RxAppCompatActivity implements PresenterCallback{

    protected BasePresenter mPresenter;
    protected MyApplication appLication;
    protected SharedUtils sharedUtils;
    protected ProgressDialog mProgressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
//        //屏幕适配一定要设置在setcontentView之前
        ScreenUtil.resetDensity(this);

        //设置布局内容
        setContentView(getLayoutId());
         ButterKnife.bind(this);

        //绑定初始化application
        appLication=MyApplication.getInstance();
        sharedUtils=new SharedUtils(this);

        mPresenter = getPresenter();
        if (mPresenter != null) {
            mPresenter.attach(this);
        }
        //初始化EventBus
        EventBus.getDefault().register(this);

    }


    //初始化进度加载框
    protected void initProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCanceledOnTouchOutside(false);
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {

                }
            });
            mProgressDialog.setMessage(getString(R.string.is_landing));
        }

        mProgressDialog.show();
    }


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
        EventBus.getDefault().unregister(this);  //取消注册
    }



}
