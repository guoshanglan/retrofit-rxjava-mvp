package cc.hisens.hardboiled.patient.ui.activity.score.presenter;

import android.text.TextUtils;

import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.ui.activity.score.model.IIEF5Score;
import cc.hisens.hardboiled.patient.ui.activity.score.view.ScoreView;

public class IIEFScorePresenter extends BasePresenter<ScoreView> {
    public IIEF5Score iief5Score;

    public IIEFScorePresenter(){
        if (iief5Score==null){
            iief5Score=new IIEF5Score();
        }
    }

    public void  UpLoadIIefScore(){
        iief5Score.UploadIIEFScore(mView.getContext(),mView.IIEFScore(),this);
    }


    //登录成功,这边的mview，已经在basePresenter中定义过了
    public void UploadSuccess(IIEF5Score iief5Score) {
        if (iief5Score!=null)
            mView.UploadIIEFSuccess(iief5Score);

    }


    //登录失败
    public void loginFailed(String str) {
        if (!TextUtils.isEmpty(str)){
            mView.FailedIIEFError(str);
        }

    }




}
