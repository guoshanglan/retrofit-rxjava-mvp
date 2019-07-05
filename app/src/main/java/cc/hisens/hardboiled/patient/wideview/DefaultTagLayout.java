package cc.hisens.hardboiled.patient.wideview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.socks.library.KLog;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.IdRes;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.view.component
 * @fileName AddNoteLagLauyout
 * @date on 2017/7/17 16:52
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 */

public class DefaultTagLayout extends TagLayout {

    // holds the checked id; the selection is empty by default
    private int mCheckedId = -1;
    private int mCurClickedId = -1;
    // tracks children radio buttons checked state
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
    // when true, mOnCheckedChangeListener discards events
    private boolean mProtectFromCheckedChange = false;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private PassThroughHierarchyChangeListener mPassThroughListener;

    public DefaultTagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setOnHierarchyChangeListener(ViewGroup.OnHierarchyChangeListener listener) {
        // the user listener is delegated to our pass-through listener
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // checks the appropriate radio button as requested in the XML file
        if (mCheckedId != -1) {
            mProtectFromCheckedChange = true;
            setCheckedStateForView(mCheckedId, true);
            mProtectFromCheckedChange = false;
            setCheckedId(mCheckedId);
        }
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        if (child instanceof CheckBox) {
            final CheckBox button = (CheckBox) child;
            if (button.isChecked()) {
                mProtectFromCheckedChange = true;
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;
                setCheckedId(button.getId());
            }
        }
        super.addView(child, index, params);
    }

    /**
     * <p>Sets the selection to the radio button whose identifier is passed in
     * parameter. Using -1 as the selection identifier clears the selection;
     * such an operation is equivalent to invoking {@link #clearCheck()}.</p>
     *
     * @param id the unique id of the radio button to select in this group
     * @see #getCheckedCheckBoxId()
     * @see #clearCheck()
     */
    public void check(@IdRes int id) {
        // don't even bother
        if (id != -1 && (id == mCheckedId)) {
            return;
        }

        if (mCheckedId != -1) {
            setCheckedStateForView(mCheckedId, false);
        }

        if (id != -1) {
            setCheckedStateForView(id, true);
        }

        setCheckedId(id);
    }

    private void setCheckedId(@IdRes int id) {
        mCheckedId = id;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
        }
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(mCurClickedId);
        if (checkedView != null && checkedView instanceof CheckBox) {
            if (checkedView == getChildAt(0)) {
                if (!((CheckBox) checkedView).isChecked()) {
                    ((CheckBox) checkedView).setChecked(true);
                }
                // 清除其他被选中的按钮
                for (int i = 1; i < getChildCount(); i++) {
                    View child = getChildAt(i);
                    if (child != null && child instanceof CheckBox) {
                        ((CheckBox) child).setChecked(false);
                    }
                }
            } else {
                View firstChild = getChildAt(0);
                if (firstChild != null && firstChild instanceof CheckBox && ((CheckBox) firstChild).isChecked()) {
                    ((CheckBox) firstChild).setChecked(false);
                }
            }
        }
    }

    /**
     * <p>Returns the identifier of the selected radio button in this group.
     * Upon empty selection, the returned value is -1.</p>
     *
     * @return the unique id of the selected radio button in this group
     * @attr ref android.R.styleable#RadioGroup_checkedButton
     * @see #check(int)
     * @see #clearCheck()
     */
    @IdRes
    public int getCheckedCheckBoxId() {
        return mCheckedId;
    }

    /**
     * <p>Clears the selection. When the selection is cleared, no radio button
     * in this group is selected and {@link #getCheckedCheckBoxId()} returns
     * null.</p>
     *
     * @see #check(int)
     * @see #getCheckedCheckBoxId()
     */
    public void clearCheck() {
        check(-1);
    }

    public String getText() {
        StringBuilder sb = new StringBuilder();
        View firstChild = getChildAt(0);
        if (firstChild != null && firstChild instanceof CheckBox && !((CheckBox) firstChild).isChecked()) {
            for (int j = 1; j < getChildCount(); j++) {
                View child = getChildAt(j);
                if (child != null && child instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) child;
                    if (checkBox.isChecked()) {
                        sb.append(checkBox.getText().toString()).append(",");
                    }
                }
            }
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
    }

    public byte[] getCheckedIndexes() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            CheckBox child = (CheckBox) getChildAt(i);
            if (child.isChecked()) {
                KLog.i("clicked = " + child.getText() + ", index = " + i);
                list.add(i);
            }
        }
        byte[] indexes = new byte[list.size()];
        for (int i = 0; i < list.size(); i++) {
            indexes[i] = list.get(i).byteValue();
        }
        return indexes;
    }

    public void setChildViews(byte[] indexes) {
        if (indexes != null) {
            if (indexes.length == 1 && indexes[0] == 0) {
                return;
            } else {
                for (int i = 0; i < indexes.length; i++) {
                    if (indexes[i] < getChildCount()) {
                        ((CheckBox) getChildAt(indexes[i])).setChecked(true);
                    }
                }
                invalidate();
            }
        }
    }

    /**
     * <p>Register a callback to be invoked when the checked radio button
     * changes in this group.</p>
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }


    private List<View> getAllChildViews() {
        List<View> allChildren = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            allChildren.add(childView);
            //再次 调用本身（递归）
            allChildren.addAll(getAllChildViews());
        }
        return allChildren;
    }

    @Override
    public CharSequence getAccessibilityClassName() {
        return DefaultTagLayout.class.getName();
    }

    public Method getMethod(Class clazz, String methodName,
                            final Class[] classes) {
        Method method = null;
        try {
            method = clazz.getDeclaredMethod(methodName, classes);
        } catch (NoSuchMethodException e) {
            try {
                method = clazz.getMethod(methodName, classes);
            } catch (NoSuchMethodException ex) {
                if (clazz.getSuperclass() == null) {
                    return method;
                } else {
                    method = getMethod(clazz.getSuperclass(), methodName,
                            classes);
                }
            }
        }
        return method;
    }

    /**
     * @param obj        调整方法的对象
     * @param methodName 方法名
     * @param classes    参数类型数组
     * @param objects    参数数组
     * @return 方法的返回值
     */
    public Object invoke(final Object obj, final String methodName,
                         final Class[] classes, final Object[] objects) {
        try {
            Method method = getMethod(obj.getClass(), methodName, classes);
            method.setAccessible(true);// 调用private方法的关键一句话
            return method.invoke(obj, objects);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>Interface definition for a callback to be invoked when the checked
     * radio button changed in this group.</p>
     */
    public interface OnCheckedChangeListener {
        /**
         * <p>Called when the checked radio button has changed. When the
         * selection is cleared, checkedId is -1.</p>
         *
         * @param group     the group in which the checked radio button has changed
         * @param checkedId the unique identifier of the newly checked radio button
         */
        void onCheckedChanged(DefaultTagLayout group, @IdRes int checkedId);
    }

    private class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mCurClickedId = buttonView.getId();

            // prevents from infinite recursion
            if (mProtectFromCheckedChange) {
                return;
            }

            mProtectFromCheckedChange = true;
            setCheckedStateForView(mCheckedId, false);
            mProtectFromCheckedChange = false;

            int id = buttonView.getId();
            setCheckedId(id);
        }
    }

    /**
     * <p>A pass-through listener acts upon the events and dispatches them
     * to another listener. This allows the table layout to set its own internal
     * hierarchy change listener without preventing the user to setup his.</p>
     */
    private class PassThroughHierarchyChangeListener implements
            ViewGroup.OnHierarchyChangeListener {
        private ViewGroup.OnHierarchyChangeListener mOnHierarchyChangeListener;

        /**
         * {@inheritDoc}
         */
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
        public void onChildViewAdded(View parent, View child) {
            if (parent == DefaultTagLayout.this && child instanceof CheckBox) {
                int id = child.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
                    id = View.generateViewId();
                    child.setId(id);
                }
                invoke(child, "setOnCheckedChangeWidgetListener", new Class[]{CompoundButton.OnCheckedChangeListener.class},
                        new CompoundButton.OnCheckedChangeListener[]{mChildOnCheckedChangeListener});
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        /**
         * {@inheritDoc}
         */
        public void onChildViewRemoved(View parent, View child) {
            if (parent == DefaultTagLayout.this && child instanceof CheckBox) {
                invoke(child, "setOnCheckedChangeWidgetListener", new Class[]{CompoundButton.OnCheckedChangeListener.class},
                        new CompoundButton.OnCheckedChangeListener[]{null});
            }

            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }
    }
}
