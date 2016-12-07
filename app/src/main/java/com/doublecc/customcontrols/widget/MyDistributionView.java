package com.doublecc.customcontrols.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by DoubleCC on 2016/12/8 0008.
 */

public class MyDistributionView extends View {

    /**
     * 基准线/多边形
     */
    private Paint mPaintDatumLine;
    /**
     * 趋势线
     */
    private Paint mPaintTrendLine;
    /**
     * 分割线
     */
    private Paint mPaintSplitLine;
    /**
     * 基准线的宽
     */
    private float mDatumLineWidth = 35;
    /**
     * 分割线的宽度
     */
    private float mSplitLineWidth = 1;
    /**
     * 分割线的宽度
     */
    private float mTrendLineWidth = 1;

    /**
     * 定义View的中心坐标
     */
    private int mCenterX, mCenterY;

    /**
     * 多边形的边数
     */
    private int NUM_EDGE = 8;
    /**
     * 多边形的个数
     */
    private int NUM_DATUM = 5;

    /**
     * 基准多边形路劲
     */
    private Path mPathDatum;
    /**
     * 分割线
     */
    private Path mPathSplit;
    /**
     * 多边形区域
     */
    private Path mPathTrend;

    /**
     * 基准线色值
     */
    private int mColorDatum = 0xff4169E1;

    /** 角度 */
    private float mAngel = (float) (2 * Math.PI / NUM_EDGE);

    /**
     * 多边形区块占比
     */
    private float[] percents = { 1F, 0.3F, 0.7F, 0.9F, 0.2F, 0.9F, 0.5F ,0.8F};

    /**
     * 是否滑动
     */
    private boolean slide = false;
    /**
     * 标记那个滑动
     */
    private int mark;

    /**
     * 初始化
     *
     * @param context
     */
    public MyDistributionView(Context context) {
        this(context, null);
    }

    public MyDistributionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private void init() {
        mPaintDatumLine = new Paint(Paint.ANTI_ALIAS_FLAG);// 抗锯齿
        // mPaintDatumLine.setStyle(Style.STROKE);
        // mPaintDatumLine.setColor(Color.BLACK);
        mPaintDatumLine.setStrokeWidth(mDatumLineWidth);

        mPaintSplitLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintSplitLine.setStyle(Paint.Style.STROKE);
        mPaintSplitLine.setColor(Color.WHITE);
        mPaintSplitLine.setStrokeWidth(mSplitLineWidth);

        mPaintTrendLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTrendLine.setStyle(Paint.Style.STROKE);
        mPaintTrendLine.setAlpha(100);
        mPaintTrendLine.setColor(Color.RED);
        mPaintTrendLine.setStrokeWidth(mTrendLineWidth);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mCenterX = w / 2;
        mCenterY = h / 2;
        mDatumLineWidth = Math.min(mCenterX,mCenterY)/5;
        init();
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPathDatum = new Path();
        mPathSplit = new Path();
        mPathTrend = new Path();
        drawDatum(canvas);
    }

    /**
     * 绘制多边形基准
     */
    private void drawDatum(Canvas canvas) {
        int r = 41;
        int g = 69;
        int b = 225;
        for (int j = 0; j < NUM_DATUM; j++) {
            int mColor = Color.rgb(r, g, b);
            mPaintDatumLine.setColor(mColor);
            if (j == 0) {
                mPaintDatumLine.setStyle(Paint.Style.FILL_AND_STROKE);
            } else {
                mPaintDatumLine.setStyle(Paint.Style.STROKE);
            }
            float mRadiu = mDatumLineWidth * (j + 0.5F);
            mPathDatum.moveTo(mCenterX, mCenterY + mRadiu);
            for (int i = 0; i < NUM_EDGE; i++) {
                float x = (float) (mCenterX + Math.sin(mAngel * i) * mRadiu);
                float y = (float) (mCenterY + Math.cos(mAngel * i) * mRadiu);
                mPathDatum.lineTo(x, y);
            }
            // 首尾闭合
            mPathDatum.close();
            canvas.drawPath(mPathDatum, mPaintDatumLine);
            // 重置
            mPathDatum.reset();
            r += 8;
            g += 25;
        }
        // 绘制分割线
        for (int i = 0; i < NUM_EDGE; i++) {
            mPathSplit.moveTo(mCenterX, mCenterY);
            float mSplitX = (float) (mCenterX + Math.sin(mAngel * i)
                    * mDatumLineWidth * NUM_DATUM);
            float mSplitY = (float) (mCenterY + Math.cos(mAngel * i)
                    * mDatumLineWidth * NUM_DATUM);
            mPathSplit.lineTo(mSplitX, mSplitY);
            canvas.drawPath(mPathSplit, mPaintSplitLine);
            mPathSplit.reset();
        }
        // 绘制区块
        for (int i = 0; i < NUM_EDGE; i++) {
            float mTrendX = (float) (mCenterX + Math.sin(mAngel * i)
                    * mDatumLineWidth * NUM_DATUM * percents[i]);
            float mTrendY = (float) (mCenterY + Math.cos(mAngel * i)
                    * mDatumLineWidth * NUM_DATUM * percents[i]);
            if (i == 0) {
                mPathTrend.moveTo(mTrendX, mTrendY);
            } else {
                mPathTrend.lineTo(mTrendX, mTrendY);
            }
        }
        mPathTrend.close();
        canvas.drawPath(mPathTrend, mPaintTrendLine);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float mRadiu = mDatumLineWidth * (NUM_DATUM + 0.5F);
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                for (int i = 0; i < NUM_EDGE; i++) {
                    int pointX = (int) (mCenterX + Math.sin(mAngel * i) * mRadiu
                            * percents[i]);
                    int pointY = (int) (mCenterY + Math.cos(mAngel * i) * mRadiu
                            * percents[i]);
                    Rect rect = new Rect(pointX - 35, pointY - 35, pointX + 35,
                            pointY + 35);
                    if (rect.contains((int) x, (int) y)) {
                        slide = true;
                        mark = i;
                        mPaintTrendLine.setColor(Color.GREEN);
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                mPaintTrendLine.setColor(Color.RED);
                invalidate();
                slide = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (slide) {
                    // 滑动距离在改分割线上的余弦距离
                    float distance = (float) ((float) Math.sqrt((x - mCenterX)
                            * (x - mCenterX) + (y - mCenterY) * (y - mCenterY)) * Math
                            .cos(Math.abs(Math.atan2(x - mCenterX, y - mCenterY)
                                    - mAngel * mark)));
                    // 滑动的百分比
                    float percent = distance / (mDatumLineWidth * NUM_DATUM);
                    if (percent <= 0) {
                        percents[mark] = 0F;
                    } else if (percent > 1) {
                        percents[mark] = 1F;
                    } else {
                        percents[mark] = percent;
                    }

                    invalidate();
                }

                break;

            default:
                break;
        }
        return true;
    }
}
