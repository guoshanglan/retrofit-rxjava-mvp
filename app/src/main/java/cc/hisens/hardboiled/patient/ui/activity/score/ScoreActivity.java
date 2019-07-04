package cc.hisens.hardboiled.patient.ui.activity.score;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.db.EHSScoreRepository;
import cc.hisens.hardboiled.patient.ui.activity.score.model.EHSScore;
import cc.hisens.hardboiled.patient.ui.activity.score.model.IIEF5Score;
import cc.hisens.hardboiled.patient.db.impl.EHSScoreRepositoryImpl;
import cc.hisens.hardboiled.patient.db.impl.IIEF5ScoreRepositoryImpl;
import cc.hisens.hardboiled.patient.ui.activity.score.presenter.EHSScorePresenter;
import cc.hisens.hardboiled.patient.ui.activity.score.presenter.IIEFScorePresenter;
import cc.hisens.hardboiled.patient.ui.activity.score.view.ScoreView;
import cc.hisens.hardboiled.patient.wideview.AssessView;
import cc.hisens.hardboiled.patient.wideview.ViewPagerCompat;

/**
 * 这个Activity是用来给ehs和IIEF_5评分的界面
 */

public class ScoreActivity extends BaseActivity implements AssessView.OnOptionSelectedListener, ScoreView {
    @BindView(R.id.tv_back)
    public TextView tvBack;  //返回上一个界面
    @BindView(R.id.tv_title)
    public TextView tvTitle;  //顶部标题
    @BindView(R.id.viewpager_assess)
    public ViewPagerCompat viewPagerCompat;
    @BindView(R.id.btn_sumbit)
    public Button btnSubmit;  //提交
    public int pageCount, maxOption;  //viewpager的页数，maxoption 最大的选项数
    public String nameStart;  //以什么字符串开头，因为在本地的string文件中定义好了内容
    public int[] IIEF = new int[5];  //总的分数,用数组的形式进行上传
    public ArrayList<Integer>iiefList=new ArrayList<>();   //定义这个集合主要是用来判断提交按钮是否显示，判断他的长度
    public int ehsscore;
    public EHSScorePresenter ehsScorePresenter;     //两个第三方的传输者
    public IIEFScorePresenter iiefScorePresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    //初始化所有的控件
    private void initView() {
        pageCount = Integer.parseInt(getIntent().getStringExtra("pagecount"));
        maxOption = Integer.parseInt(getIntent().getStringExtra("maxoption"));
        nameStart = getIntent().getStringExtra("namestart");
        initViewPager(pageCount, maxOption, nameStart);
    }


    //初始化viewpager内容填充
    protected void initViewPager(int pageCount, int maxOptions, String nameStartWith) {
        final List<AssessView> fragments = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            AssessView view = new AssessView(this, i, maxOptions, nameStartWith, this);
            fragments.add(view);
        }
        viewPagerCompat.setAdapter(new ViewPagerCompat.PagerAdapterImpl(fragments));

    }


    //点击事件
    @OnClick({R.id.tv_back, R.id.btn_sumbit})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_back:
                finish();  //返回到上一层
                break;

            case R.id.btn_sumbit:  //提交评分
                  UploadScore();
                break;


        }

    }


    //上传评分记录,
    public void UploadScore() {
        initProgressDialog("正在提交...");
        if (pageCount == 1) {
            ehsScorePresenter.UpLoadehsScore();
        }else{
            iiefScorePresenter.UpLoadIIefScore();
        }


    }


    //存储EHS评分记录到数据库中
    private void saveScore(EHSScore score) {
        EHSScoreRepository repo = new EHSScoreRepositoryImpl();
        repo.saveEHSScore(score);
    }

    //存储IIEF_5的评分记录到数据库中
    private void saveIIEFScore(IIEF5Score score) {
        IIEF5ScoreRepositoryImpl repo = new IIEF5ScoreRepositoryImpl();
        repo.saveScore(score);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_assess;
    }

    @Override
    public BasePresenter getPresenter() {
        if (pageCount == 1) {
            if (ehsScorePresenter == null) {
                ehsScorePresenter = new EHSScorePresenter();
            }

            return ehsScorePresenter;
        } else {
            if (iiefScorePresenter == null) {
                iiefScorePresenter = new IIEFScorePresenter();
            }

            return iiefScorePresenter;
        }

    }

    //viewpager里面内容按钮选中回调事件
    @Override
    public void onOptionSelected(int score, int subjectIndex) {

        //通过这个来判断是从ehs还是从IIEF_5界面跳转过来的
        if (pageCount == 1) {
            this.ehsscore = score;
        } else {
            iiefList.add(subjectIndex,score);
            IIEF[subjectIndex]=score;
        }
        if (subjectIndex < viewPagerCompat.getAdapter().getCount() - 1) {
            viewPagerCompat.setCurrentItem(subjectIndex + 1);
        } else {
        }
        if (iiefList.size()==5){
           btnSubmit.setVisibility(View.VISIBLE);
        }else{
            btnSubmit.setVisibility(View.GONE);
        }

    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public int EHSScore() {  //返回ehs的分
        return ehsscore;
    }


    //上传成功保存ehs的数据
    @Override
    public void UploadEHSSuccess(EHSScore ehsScore) {
           if (ehsScore!=null){
               dismissProgressDialog();
               saveScore(ehsScore);
           }
    }

    @Override
    public void FailedEHSError(String str) {
        if (!TextUtils.isEmpty(str)) {
            dismissProgressDialog();
            ShowToast(str);
        }
    }


    //下面是IIEF_5所需要的
    @Override
    public int[] IIEFScore() {
        return IIEF;
    }

    @Override
    public void UploadIIEFSuccess(IIEF5Score iief5Score) {
        //保存数据
        if (iief5Score!=null){
            dismissProgressDialog();
            saveIIEFScore(iief5Score);
        }

    }

    @Override
    public void FailedIIEFError(String str) {
        if (!TextUtils.isEmpty(str)) {
            dismissProgressDialog();
            ShowToast(str);
        }
    }

}
