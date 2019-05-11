package cc.hisens.hardboiled.patient;

import android.app.Application;
import android.content.Context;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import androidx.multidex.MultiDex;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.utils.ScreenUtil;
import io.realm.Realm;

public class MyApplication extends Application {
    private static MyApplication instance;
    private static Context mContext;

    private RefWatcher refWatcher;



    @Override
    public void onCreate() {
        super.onCreate();
        ScreenUtil.resetDensity(this);  //初始化屏幕适配的工具类
        init();
    }


    //初始化各种数据
    private void init() {


        //检查内存泄漏
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        refWatcher = LeakCanary.install(this);

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



    public static RefWatcher getRefWatcher(Context context) {
        return  getInstance().refWatcher;
    }



    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }


}
