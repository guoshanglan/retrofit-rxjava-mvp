package cc.hisens.hardboiled.patient.base;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BaseFragment extends Fragment {
    protected View mRootView;
    private BaseActivity mActivity;
    private Unbinder mUnbinder;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 初始化黄牛刀
        mUnbinder = ButterKnife.bind(this, view);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mUnbinder.unbind();
    }

}
