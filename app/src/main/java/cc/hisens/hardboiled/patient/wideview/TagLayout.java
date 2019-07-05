package cc.hisens.hardboiled.patient.wideview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Px;

public class TagLayout extends ViewGroup {
    private List<int[]> children;

    public TagLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        children = new ArrayList<>();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureChildren(widthMeasureSpec, heightMeasureSpec);

        final int count = getChildCount(); // tag的数量
        int left = 0; // 当前的左边距离
        int top = 0; // 当前的上边距离
        int totalHeight = 0; // WRAP_CONTENT时控件总高度
        int totalWidth = 0; // WRAP_CONTENT时控件总宽度

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            LayoutParams params = (LayoutParams) child.getLayoutParams();

            // 第一行的高度
            if (i == 0) {
                totalHeight = params.topMargin + child.getMeasuredHeight() + params.bottomMargin;
            }
            // 换行
            if (left + params.leftMargin + child.getMeasuredWidth() + params.rightMargin > getMeasuredWidth()) {
                left = 0;
                // 每个TextView的高度都一样，随便取一个都行
                top += params.topMargin + child.getMeasuredHeight() + params.bottomMargin;
                totalHeight += params.topMargin + child.getMeasuredHeight() + params.bottomMargin;
            }

            children.add(new int[]{left + params.leftMargin, top + params.topMargin,
                    left + params.leftMargin + child.getMeasuredWidth(), top + params.topMargin + child.getMeasuredHeight()});

            left += params.leftMargin + child.getMeasuredWidth() + params.rightMargin;

            if (left > totalWidth) { // 当宽度为WRAP_CONTENT时，取宽度最大的一行
                totalWidth = left;
            }
        }

        int height;
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            height = totalHeight;
        }

        int width;
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            width = totalWidth;
        }

        setMeasuredDimension(width, height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();

        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            int[] position = children.get(i);
            child.layout(position[0], position[1], position[2], position[3]);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    /**
     * <p>This set of layout_introduction_first parameters defaults the width and the height of
     * the children to {@link #WRAP_CONTENT} when they are not specified in the
     * XML file. Otherwise, this class ussed the value read from the XML file.</p>
     * <p>
     * <p>See
     * {android.com.android.R.styleable#LinearLayout_Layout LinearLayout Attributes}
     * for a list of all child view attributes that this class supports.</p>
     */
    public static class LayoutParams extends MarginLayoutParams {
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(@Px int width, @Px int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
