package cc.hisens.hardboiled.patient.ui.activity.main.present;


import cc.hisens.hardboiled.patient.base.BasePresenter;
import cc.hisens.hardboiled.patient.ui.activity.main.model.AppInfoResult;
import cc.hisens.hardboiled.patient.ui.activity.main.view.AppcheckInfoView;

//版本更新的回调信息
public class AppInfoPresenter extends BasePresenter<AppcheckInfoView> {

    public AppInfoResult appinfoResult;  //版本是否更新的model类


    public AppInfoPresenter() {
        appinfoResult=new AppInfoResult();
    }



    //检查APP版本是否更新
    public void CheckAppUpdate(){
       appinfoResult.checkUpdate(mView.getContext(),this);
    }


    //后台返回结果
    public void setCheckUpdateInfo(AppInfoResult appInfoResult) {
         mView.setCheckUpdateInfo(appInfoResult);
    }



    //检查失败
    public void setFailedError(String error) {
        mView.setFailedError(error);
    }


}
