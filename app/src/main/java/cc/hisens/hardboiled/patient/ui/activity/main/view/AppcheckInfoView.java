package cc.hisens.hardboiled.patient.ui.activity.main.view;

import android.content.Context;

import cc.hisens.hardboiled.patient.ui.activity.login.model.User;
import cc.hisens.hardboiled.patient.ui.activity.main.model.AppInfoResult;

public interface AppcheckInfoView {

    Context getContext();   //需要的上下文对象


    /**
     * 版本有更新
     * @param  appInfoResult  版本更新的返回信息model类
     */
    void setCheckUpdateInfo(AppInfoResult appInfoResult);




    /**
     *出现错误，可能需要重新登陆
     * @param str
     */
    void setFailedError(String str);


}
