package cc.hisens.hardboiled.patient.fragment;

import butterknife.BindView;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BaseFragment;
import cc.hisens.hardboiled.patient.base.BasePresenter;

public class MyFragment extends BaseFragment {



    @Override
    protected int getLayoutId() {
        return R.layout.layout_introduction_first;
    }

    @Override
    public BasePresenter getPresenter() {

        return null;
    }
}
