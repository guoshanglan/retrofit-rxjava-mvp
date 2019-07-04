package cc.hisens.hardboiled.patient.wideview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import java.util.List;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

/**
 * @author Waiban
 * @package cc.hisens.hardboiled.view.component
 * @fileName ViewPagerCompat
 * @date on 2017/6/5 10:15
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 *
 * 自定义viewpager，用来滑动APP量表的
 *
 */

public class ViewPagerCompat extends ViewPager {
    private static final float TRANSLATION_MAX = 67.0f / 720;
    private int mLastXIntercept;
    private int mLastYIntercept;
    private int mTouchSlop;

    public ViewPagerCompat(Context context) {
        super(context);
    }

    public ViewPagerCompat(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    private void initialize() {
        final ViewConfiguration configuration = ViewConfiguration.get(getContext());
        mTouchSlop = configuration.getScaledPagingTouchSlop();
        setPageTransformer(true, new PageTransformerImpl());
        setOffscreenPageLimit(5);
    }


    public static class PagerAdapterImpl extends PagerAdapter {
        private List<AssessView> list;

        public PagerAdapterImpl(List<AssessView> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }
    }

    private class PageTransformerImpl implements PageTransformer {

        @Override
        public void transformPage(View view, float position) {
//            KLog.i("view:" + view +", pos = " + position);
//            view.setTranslationX(50);
            if (position < -2) {
                // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setTranslationX(0);
            } else if (position <= -2) {// [-1,0]
                // Use the default slide transition when moving to the left page
                // a页滑动至b页 ； a页从 0.0 ~ -1 ；b页从1 ~ 0.0
                float translationX = view.getWidth() * position * TRANSLATION_MAX;
                view.setTranslationX(translationX);
            } else if (position <= 1) { // (0,1]
                float translationX = -view.getWidth() * position * TRANSLATION_MAX;
                view.setTranslationX(translationX);
            } else {// (1,+Infinity]
                // This page is way off-screen to the right.
                view.setTranslationX(0);
            }

        }
    }
}
