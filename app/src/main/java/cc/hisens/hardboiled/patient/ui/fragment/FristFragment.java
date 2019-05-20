package cc.hisens.hardboiled.patient.ui.fragment;

import android.os.Bundle;

import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BaseFragment;
import cc.hisens.hardboiled.patient.base.BasePresenter;

public class FristFragment  extends BaseFragment{





    @Override
    protected int getLayoutId() {
        return R.layout.get_verify_code_activity;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
