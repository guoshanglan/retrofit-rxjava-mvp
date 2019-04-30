package cc.hisens.hardboiled.patient.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.WindowManager;

public class ScreenUtils {

    public static float dp2px(Resources resources, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                resources.getDisplayMetrics());
    }

    /**
     * dp转px
     *
     * @param dpValue dp
     * @return int px
     */
    public static float dp2px(Context context, float dpValue) {
        return dp2px(context.getResources(), dpValue);
    }

    /**
     * dp转dip
     *
     * @param context
     * @param dp
     * @return
     */
    public static int dp2Dip(Context context, float dp) {
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5F);
    }

    public static float sp2px(Resources resources, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                resources.getDisplayMetrics());
    }

    public static float sp2px(Context context, float sp) {
        return sp2px(context.getResources(), sp);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕的大小
     *
     * @param context 当前上下文
     * @return 屏幕尺寸对象
     */
    public static Screen getScreenPix(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return new Screen(dm.widthPixels, dm.heightPixels);
    }

    /**
     * 获取屏幕的宽
     *
     * @param context 当前上下文
     * @return 屏幕宽
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏幕的高
     *
     * @param context 当前上下文
     * @return 屏幕高
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 获取屏幕密度
     *
     * @return float
     * @throws
     */
    public static float getDensity(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        dm = context.getResources().getDisplayMetrics();
        return dm.density;
    }

    /**
     * 获取状态栏高度
     *
     * @return int
     * @throws
     */
    public static int getStatusBarHeight() {
        return Resources.getSystem().getDimensionPixelSize(Resources.getSystem().getIdentifier("status_bar_height", "dimen", "android"));
    }


    /**
     * 屏幕信息
     *
     * @author wangyang
     */
    private static class Screen {
        // 屏幕宽
        int widthPixels;
        // 屏幕高
        int heightPixels;

        Screen(int widthPixels, int heightPixels) {
            this.widthPixels = widthPixels;
            this.heightPixels = heightPixels;
        }
    }
}
