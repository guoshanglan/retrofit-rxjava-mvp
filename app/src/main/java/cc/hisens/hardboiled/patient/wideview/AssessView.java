package cc.hisens.hardboiled.patient.wideview;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import cc.hisens.hardboiled.patient.R;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.view.component
 * @fileName AssessView
 * @date on 2017/6/5 17:36
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 *
 * APP的量表，需要填写的
 */

public class AssessView extends FrameLayout {

    @BindView(R.id.tv_assess_title)
    TextView mTvAssessTitle;
    @BindView(R.id.rgrp_assess)
    RadioGroup mRgrpAssess;
    private int mIndex;
    private int mMaxOptions;
    private OnOptionSelectedListener mOnOptionSelectedListener;

    public AssessView(Context context, int index, int maxOptions, String nameStartWith, OnOptionSelectedListener listener) {
        super(context);
        initialize();
        setIndex(index, maxOptions, nameStartWith);
        mOnOptionSelectedListener = listener;
    }

    private void initialize() {
        View root = LayoutInflater.from(getContext()).inflate(R.layout.layout_assess, this);
        ButterKnife.bind(root);
    }

    public void setIndex(int index, int maxOptions, String nameStartWith) {
        mIndex = index;
        mMaxOptions = maxOptions;
        Resources res = getResources();
        String packageName = getContext().getPackageName();
        String titleName = String.format(Locale.getDefault(), nameStartWith + "%d_title", mIndex);
        mTvAssessTitle.setText(res.getIdentifier(titleName, "string", packageName));

        int childCount = mRgrpAssess.getChildCount();
        int max = mMaxOptions > childCount ? childCount : mMaxOptions;

        for (int i = 0; i < max; i++) {
            String optionName = String.format(Locale.getDefault(), nameStartWith + "%d_option%d", mIndex, i);
            RadioButton child = ((RadioButton) mRgrpAssess.getChildAt(i));
            child.setVisibility(VISIBLE);
            child.setText(res.getIdentifier(optionName, "string", packageName));
        }
    }

    @OnCheckedChanged({R.id.rbtn_assess_option0, R.id.rbtn_assess_option1, R.id.rbtn_assess_option2,
            R.id.rbtn_assess_option3, R.id.rbtn_assess_option4, R.id.rbtn_assess_option5})
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            final int score;
            switch (compoundButton.getId()) {
                case R.id.rbtn_assess_option0:
                    score = 0;
                    break;
                case R.id.rbtn_assess_option1:
                    score = 1;
                    break;
                case R.id.rbtn_assess_option2:
                    score = 2;
                    break;
                case R.id.rbtn_assess_option3:
                    score = 3;
                    break;
                case R.id.rbtn_assess_option4:
                    score = 4;
                    break;
                case R.id.rbtn_assess_option5:
                    score = 5;
                    break;
                default:
                    score = 0;
                    break;
            }

            postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (mOnOptionSelectedListener != null) {
                        mOnOptionSelectedListener.onOptionSelected(score, mIndex);
                    }
                }
            }, 500);
        }
    }

    public interface OnOptionSelectedListener {
        void onOptionSelected(int score, int subjectIndex);
    }
}
