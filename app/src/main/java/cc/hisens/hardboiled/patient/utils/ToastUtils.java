package cc.hisens.hardboiled.patient.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;



/**
 * @author Ou Weibin
 * @version 1.0
 */
public class ToastUtils {
    public static void show(Context context, CharSequence text) {
        showToast(context, text);
    }

    public static void show(Context context, int resId) {
        show(context, context.getString(resId));
    }

    private static void showToast(Context context, CharSequence text) {

        String name = Thread.currentThread().getName();
        long id = Thread.currentThread().getId();


        if (!TextUtils.isEmpty(text) && text.length() < 10) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show();
        }
    }
}
