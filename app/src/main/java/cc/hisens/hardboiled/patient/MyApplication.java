package cc.hisens.hardboiled.patient;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;
import cc.hisens.hardboiled.patient.bean.UserConfig;
import cc.hisens.hardboiled.patient.utils.ScreenUtil;
import io.realm.Realm;

public class MyApplication extends Application {
    private static MyApplication instance;
    private static Context mContext;




    @Override
    public void onCreate() {
        super.onCreate();
        ScreenUtil.resetDensity(this);  //初始化屏幕适配的工具类
        init();
    }


    //初始化各种数据
    private void init() {
        Realm.init(this);   //数据库初始化
        UserConfig.init(this);
        instance = this;
        mContext = getApplicationContext();
    }


    //返回application对象
    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getmContext() {
        return mContext;
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
