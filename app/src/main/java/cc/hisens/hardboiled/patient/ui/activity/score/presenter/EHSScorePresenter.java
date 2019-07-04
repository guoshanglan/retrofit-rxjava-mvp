package cc.hisens.hardboiled.patient.ui.activity.score.presenter;

import android.text.TextUtils;

import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.ui.activity.score.model.EHSScore;
import cc.hisens.hardboiled.patient.ui.activity.score.view.ScoreView;

public class EHSScorePresenter extends BasePresenter<ScoreView> {
    public EHSScore ehsScore;

    public EHSScorePresenter(){
        if (ehsScore==null){
            ehsScore=new EHSScore();
        }
    }

    public void  UpLoadehsScore(){
        ehsScore.UploadEHSScore(mView.getContext(),mView.EHSScore(),this);
    }


    //登录成功,这边的mview，已经在basePresenter中定义过了
    public void UploadSuccess(EHSScore ehsScore) {
        if (ehsScore!=null)
            mView.UploadEHSSuccess(ehsScore);

    }


    //登录失败
    public void loginFailed(String str) {
        if (!TextUtils.isEmpty(str)){
            mView.FailedEHSError(str);
        }

    }

}
