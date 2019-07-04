package cc.hisens.hardboiled.patient.ui.activity.ehsassess;

import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.wideview.ViewPagerCompat;

//量表的问券调查
public class EHSAssessActivity extends BaseActivity {
    @BindView(R.id.tv_back)
    public TextView tvBack;  //返回上一个界面
    @BindView(R.id.tv_title)
    public TextView tvTitle;
    @BindView(R.id.viewpager_assess)
    public ViewPagerCompat viewPagerCompat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    //初始化所有的控件
    private void initView() {


    }


    //点击事件


    @Override
    protected int getLayoutId() {
        return R.layout.activity_assess;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }
}
