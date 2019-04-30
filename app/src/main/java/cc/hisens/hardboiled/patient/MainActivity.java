package cc.hisens.hardboiled.patient;


import android.os.Bundle;

import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;


//主要的Activity
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
