package cc.hisens.hardboiled.patient;

import android.app.Application;

import cc.hisens.hardboiled.patient.utils.ScreenUtil;

public class MyApplication extends Application {
    public static MyApplication application;






    @Override
    public void onCreate() {
        super.onCreate();
        ScreenUtil.resetDensity(this);

    }


    //返回application对象
    public static MyApplication getInstance() {
        if(application==null){
            application=new MyApplication();
        }
        return application ;
    }


}
