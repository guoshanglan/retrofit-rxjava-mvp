package cc.hisens.hardboiled.patient.wideview.pickerview.utils;

import android.view.Gravity;

import cc.hisens.hardboiled.patient.R;


/**
 * Created by Sai on 15/8/9.
 */
public class PickerViewAnimateUtil {
    private static final int INVALID = -1;

    /**
     * Get default animation resource when not defined by the user
     *
     * @param gravity       the gravity of the dialog
     * @param isInAnimation determine if is in or out animation. true when is is
     * @return the id of the animation resource
     */
    public static int getAnimationResource(int gravity, boolean isInAnimation) {
        switch (gravity) {
            case Gravity.BOTTOM:
                return isInAnimation ? R.anim.push_bottom_in : R.anim.push_bottom_out;
        }
        return INVALID;
    }
}
