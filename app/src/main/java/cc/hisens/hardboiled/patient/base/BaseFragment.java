package cc.hisens.hardboiled.patient.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.hisens.hardboiled.patient.MyApplication;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.utils.ScreenUtils;
import cc.hisens.hardboiled.patient.utils.SharedUtils;

public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements PresenterCallback {
    protected View mRootView;  //返回的布局
    private Unbinder mUnbinder;
    protected BasePresenter mPresenter;
    protected MyApplication appLication;
    protected SharedUtils sharedUtils;  //共享参数sp
    protected  Toast mToast;



    //初始化布局
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(getLayoutId(), container, false);
        // 初始化黄牛刀
        mUnbinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }


    //初始化所需要的东西
    protected void init(View view) {
        appLication=MyApplication.getInstance();
        sharedUtils=new SharedUtils(getActivity());
        if(mPresenter==null) {
            mPresenter = getPresenter();
        }
        if (mPresenter != null) {
            mPresenter.attach(this);
        }
//        //初始化EventBus
//        EventBus.getDefault().register(this);

        mUnbinder = ButterKnife.bind(this, view);// 初始化黄牛刀


    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init(view);

    }



  //toast提示信息
    protected  void  ShowToast(String msg){
        WindowManager wm = (WindowManager)getActivity().getSystemService(Context.WINDOW_SERVICE);
        View toastView = LayoutInflater.from(getActivity()).inflate(R.layout.toast_view, null);
        TextView tv=toastView.findViewById(R.id.txtToastMessage);
        tv.setText(msg);

        if (mToast == null) {
            mToast = new Toast(getActivity());
        }
        LinearLayout relativeLayout = (LinearLayout) toastView.findViewById(R.id.test);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(wm
                .getDefaultDisplay().getWidth(), (int) ScreenUtils.dp2px(getActivity(), 40));
        relativeLayout.setLayoutParams(layoutParams);
        mToast.setDuration(Toast.LENGTH_SHORT);
        mToast.setGravity(Gravity.TOP | Gravity.FILL_HORIZONTAL, 0, 0);
        mToast.setView(toastView);
        mToast.getView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);//设置Toast可以布局到系统状态栏的下面
        mToast.show();
    }


    //返回布局的Id
    protected abstract int getLayoutId();


    //页面销毁
    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter=null;
        }
//        EventBus.getDefault().unregister(this);  //取消注册
    }






}
