package cc.hisens.hardboiled.patient.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cc.hisens.hardboiled.patient.MyApplication;
import cc.hisens.hardboiled.patient.utils.SharedUtils;

public abstract class BaseFragment<T extends BasePresenter> extends Fragment implements PresenterCallback {
    protected View mRootView;  //返回的布局
    private Unbinder mUnbinder;
    protected BasePresenter mPresenter;
    protected MyApplication appLication;
    protected SharedUtils sharedUtils;  //共享参数sp



    //初始化布局
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mRootView = inflater.inflate(getLayoutId(), container, false);

        return mRootView;
    }


    //初始化所需要的东西
    private void init(View view) {
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
