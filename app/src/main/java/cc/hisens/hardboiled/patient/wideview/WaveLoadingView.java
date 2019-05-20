package cc.hisens.hardboiled.patient.wideview;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import cc.hisens.hardboiled.patient.utils.ScreenUtils;


/**
 * @author Waiban
 * @package cc.hisens.hardboiled.utils
 * @fileName WaveLoadingView
 * @date on 2017/5/16 12:17
 * @describe TODO
 * @org www.hisens.cc
 * @email wb.hisens.cc
 *
 *
 * 首页展示的波纹控件
 */
public class WaveLoadingView extends View {
    // 波纹颜色
    private static final int WAVE_ONE_COLOR = Color.WHITE;
    private static final int WAVE_TWO_COLOR = Color.parseColor("#79d2e5");
    // y = Asin(wx+b)+h
    private static final float STRETCH_FACTOR_A = 50;
    // 第一条水波移动速度
    private static final int TRANSLATE_X_SPEED_ONE = 10;
    // 第二条水波移动速度
    private static final int TRANSLATE_X_SPEED_TWO = 25;

    private int mTotalWidth, mTotalHeight;  //控件总共的长度和宽度
    private float[] mOneYPositions;
    private float[] mTwoYPositions;
    private float[] mResetOneYPositions;
    private float[] mResetTwoYPositions;
    private int mXOffsetSpeedOne;
    private int mXOffsetSpeedTwo;
    private int mXOneOffset;
    private int mXTwoOffset;

    private Paint mWavePaint;
    private DrawFilter mDrawFilter;
    private Path mOnePath = new Path();
    private Path mTwoPath = new Path();

    private boolean mAnimate = false;

    public WaveLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 将dp转化为px，用于控制不同分辨率上移动速度基本一致
        mXOffsetSpeedOne = (int) ScreenUtils.dp2px(context, TRANSLATE_X_SPEED_ONE);
        mXOffsetSpeedTwo = (int) ScreenUtils.dp2px(context, TRANSLATE_X_SPEED_TWO);

        // 初始绘制波纹的画笔
        mWavePaint = new Paint();
        // 去除画笔锯齿
        mWavePaint.setAntiAlias(true);
        // 设置风格为实线
        mWavePaint.setStyle(Style.FILL);
        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 从canvas层面去除绘制时锯齿
        canvas.setDrawFilter(mDrawFilter);
        resetPositonY();
        mOnePath.reset();
        mTwoPath.reset();
        mOnePath.moveTo(0, mResetOneYPositions[0]);
        mTwoPath.moveTo(0, mResetTwoYPositions[0]);

        for (int i = 1; i < mTotalWidth - 1; i++) {
            mOnePath.lineTo(i, mResetOneYPositions[i]);
            mTwoPath.lineTo(i, mResetTwoYPositions[i]);
        }
        mOnePath.lineTo(mTotalWidth, mTotalHeight + 1);
        mOnePath.lineTo(0, mTotalHeight + 1);
        mTwoPath.lineTo(mTotalWidth, mTotalHeight + 1);
        mTwoPath.lineTo(0, mTotalHeight + 1);

        mWavePaint.setColor(WAVE_TWO_COLOR);
        canvas.drawPath(mTwoPath, mWavePaint);
        mWavePaint.setColor(WAVE_ONE_COLOR);
        canvas.drawPath(mOnePath, mWavePaint);

        // 改变两条波纹的移动点
        mXOneOffset += mXOffsetSpeedOne;
        mXTwoOffset += mXOffsetSpeedTwo;

        // 如果已经移动到结尾处，则重头记录
        if (mXOneOffset >= mTotalWidth) {
            mXOneOffset = 0;
        }
        if (mXTwoOffset > mTotalWidth) {
            mXTwoOffset = 0;
        }

        // 引发view重绘，一般可以考虑延迟20-30ms重绘，空出时间片
        if (mAnimate) {
            postInvalidateDelayed(180);
        }
    }

    private void resetPositonY() {
        // mXOneOffset代表当前第一条水波纹要移动的距离
        int yOneInterval = mOneYPositions.length - mXOneOffset;
        // 使用System.arraycopy方式重新填充第一条波纹的数据
        System.arraycopy(mOneYPositions, mXOneOffset, mResetOneYPositions, 0, yOneInterval);
        System.arraycopy(mOneYPositions, 0, mResetOneYPositions, yOneInterval, mXOneOffset);

        int yTwoInterval = mTwoYPositions.length - mXTwoOffset;
        System.arraycopy(mTwoYPositions, mXTwoOffset, mResetTwoYPositions, 0,
                yTwoInterval);
        System.arraycopy(mTwoYPositions, 0, mResetTwoYPositions, yTwoInterval, mXTwoOffset);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 记录下view的宽高
        mTotalWidth = w * 2;
        mTotalHeight = h;
        // 用于保存原始波纹一的y值
        mOneYPositions = new float[mTotalWidth];
        // 用于保存原始波纹二的y值
        mTwoYPositions = new float[mTotalWidth];
        // 用于保存波纹一的y值
        mResetOneYPositions = new float[mTotalWidth];
        // 用于保存波纹二的y值
        mResetTwoYPositions = new float[mTotalWidth];

        resetData();
    }

    private void resetData() {
        mXOneOffset = 0;
        mXTwoOffset = 0;
        // 将周期定为view总宽度
        float W = (float) (2 * Math.PI / mTotalWidth);

        float aOne = mTotalHeight / 2;
        float aTwo = aOne / 3.0f;
        double phiOne = -Math.PI / 2;
        double phiTwo = phiOne + (-Math.PI / 4);
        // 周期
        float wOne = W;
        float wTwo = wOne * 4.0f;

        // Y坐标的原点
        float originYOne = mTotalHeight / 2.0f;

        float offsetYOne = originYOne;
        float offsetYTwo = originYOne - (aOne - aTwo);

        // 静止界面
        if (!mAnimate) {
            phiTwo = phiOne + (-Math.PI / 4);
            aTwo = aOne * 0.6f;
            wTwo = wOne * 1f;
        }
        // 根据view总宽度得出所有对应的y值
        for (int i = 0; i < mTotalWidth; i++) {
            mOneYPositions[i] = (float) (-aOne * Math.sin(wOne * i + phiOne) + offsetYOne);
        }

        for (int i = 0; i < mTotalWidth; i++) {
            mTwoYPositions[i] = (float) (-aTwo * Math.sin(wTwo * i + phiTwo) + offsetYTwo);
        }
        postInvalidateDelayed(20);
    }

    public boolean isLoading() {
        return mAnimate;
    }

    public void setLoading(boolean isLoading) {
        mAnimate = isLoading;
        resetData();
    }

}
