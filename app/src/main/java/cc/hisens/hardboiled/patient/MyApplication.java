package cc.hisens.hardboiled.patient;

import android.app.Application;

import cc.hisens.hardboiled.patient.bean.UserConfig;
import cc.hisens.hardboiled.patient.utils.ScreenUtil;
import io.realm.Realm;

public class MyApplication extends Application {
    public static MyApplication application;






    @Override
    public void onCreate() {
        super.onCreate();


        ScreenUtil.resetDensity(this);  //初始化屏幕适配的工具类
        Realm.init(this);   //数据库初始化
        UserConfig.init(this);

    }


    //返回application对象
    public static MyApplication getInstance() {
        if(application==null){
            application=new MyApplication();
        }
        return application ;
    }


}
