package cc.hisens.hardboiled.patient.base;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;


//activity的回收站，可以在这里对activity进行添加和消除
public class ActivityCollector {
    private static List<Activity> sList = new ArrayList<>();

    public static void addActivity(Activity activity) {
        sList.add(activity);
    }   //添加

    public static void removeActivity(Activity activity) {
        sList.remove(activity);
    }  //移除

    public static void finishAll() {
        for (Activity activity : sList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
