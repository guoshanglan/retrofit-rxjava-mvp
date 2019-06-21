package cc.hisens.hardboiled.patient;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.tencent.bugly.crashreport.CrashReport;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;
import cc.hisens.hardboiled.patient.ble.BLEManagerWrapper;
import cc.hisens.hardboiled.patient.db.bean.UserConfig;
import cc.hisens.hardboiled.patient.utils.ScreenUtil;
import io.realm.Realm;

public class MyApplication extends MultiDexApplication {
    private static MyApplication instance;
    private static Context mContext;
    private RefWatcher refWatcher;
    public static BLEManagerWrapper getBLEManagerWrapper() {
        return BLEManagerWrapper.getInstance();
    }



    @Override
    public void onCreate() {
        super.onCreate();
        ScreenUtil.resetDensity(this);  //初始化屏幕适配的工具类
        init();
    }


    //初始化各种数据
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
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

        //解决7.0手机拍照的uri问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();

        CrashReport.initCrashReport(getApplicationContext(), "a4af3eeb50", true);


    }


    //返回application对象
    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getmContext() {
        return mContext;
    }


// 内存泄漏的监控
    public static RefWatcher getRefWatcher(Context context) {
        return  getInstance().refWatcher;
    }



    //应用被销毁
    @Override
    public void onTerminate() {
        super.onTerminate();
        getBLEManagerWrapper().recycle();
    }




}
