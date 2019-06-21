package cc.hisens.hardboiled.patient.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

import androidx.viewpager.widget.ViewPager;
import butterknife.BindView;
import cc.hisens.hardboiled.patient.Appconfig;
import cc.hisens.hardboiled.patient.R;
import cc.hisens.hardboiled.patient.adapter.IntroductionViewAdapter;
import cc.hisens.hardboiled.patient.base.BaseActivity;
import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.ui.activity.login.GetVoliatCodeActivity;
import cc.hisens.hardboiled.patient.ui.activity.login.LoginActivity;
import cc.hisens.hardboiled.patient.ui.activity.main.MainActivity;


/**
 *
 * App介绍页，第一次打开APP是进入
 */
public class AppIntroductActivity extends BaseActivity {

    @BindView(R.id.vp_introduction)
    ViewPager mVpIntroduction;
    @BindView(R.id.rb_first_page)
    RadioButton mRbFirstPage;
    @BindView(R.id.rb_second_page)
    RadioButton mRbSecondPage;
    @BindView(R.id.rb_third_page)
    RadioButton mRbThirdPage;

    private IntroductionViewAdapter mAdapter;
    private List<View> mListView = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setData();
        initialView();
    }



    //设置数据
    private void setData(){
        View first = LayoutInflater.from(this).inflate(R.layout.layout_introduction_first,null);
        View second = LayoutInflater.from(this).inflate(R.layout.layout_introduction_second,null);
        View third = LayoutInflater.from(this).inflate(R.layout.layout_introduction_third,null);

        third.findViewById(R.id.bt_experience).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedUtils.writeBoolean(Appconfig.PREF_INTRODUCTION_FINISHED,true);
                if (UserConfig.UserInfo.isLogin()) {
                   startActivity(new Intent(AppIntroductActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(AppIntroductActivity.this, GetVoliatCodeActivity.class));
                }
                finish();
            }
        });
        mListView.add(first);
        mListView.add(second);
        mListView.add(third);
    }

    private void initialView(){

        mVpIntroduction = (ViewPager) findViewById(R.id.vp_introduction);
        mRbFirstPage = (RadioButton) findViewById(R.id.rb_first_page);
        mRbSecondPage = (RadioButton) findViewById(R.id.rb_second_page);
        mRbThirdPage = (RadioButton) findViewById(R.id.rb_third_page);

        mRbFirstPage.setChecked(true);
        mAdapter = new IntroductionViewAdapter(mListView);
        mVpIntroduction.setAdapter(mAdapter);
        mVpIntroduction.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                updatePageIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    //更新页面滑动时的小点
    private void updatePageIndex(int index) {
        switch (index) {
            case 0:
                mRbFirstPage.setChecked(true);
                updatePageIndexWidth(mRbFirstPage,60);
                updatePageIndexWidth(mRbSecondPage,30);
                updatePageIndexWidth(mRbThirdPage,30);

                break;
            case 1:
                mRbSecondPage.setChecked(true);
                updatePageIndexWidth(mRbFirstPage,30);
                updatePageIndexWidth(mRbSecondPage,60);
                updatePageIndexWidth(mRbThirdPage,30);
                break;
            case 2:
                mRbThirdPage.setChecked(true);
                updatePageIndexWidth(mRbFirstPage,30);
                updatePageIndexWidth(mRbSecondPage,30);
                updatePageIndexWidth(mRbThirdPage,60);
                break;
        }

    }

    private void updatePageIndexWidth(RadioButton page, int width) {

        ViewGroup.LayoutParams params = page.getLayoutParams();
        params.width = width;
        page.setLayoutParams(params);
    }


    //返回布局id
    @Override
    protected int getLayoutId() {
        return R.layout.activity_app_introduction;
    }

    @Override
    public BasePresenter getPresenter() {
        return null;
    }


}
