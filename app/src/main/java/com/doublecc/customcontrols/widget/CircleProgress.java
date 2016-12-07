package com.doublecc.customcontrols.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by DoubleCC on 2016/12/7 0007.
 */

public class CircleProgress extends View {

    private float padding = 10;

    private int mViewWidth;
    private int mViewHeight;

    private float mViewCenterX;
    private float mViewCenterY;

    // 圆弧Paint
    private Paint mArcPaint;
    private float mArcPaintStroke = 10;
    private int progressCircleColor = Color.GREEN;

    private Paint mArcBgPaint;
    private float mArcBgPaintStroke = 20;
    private int progressCircleBgColor = Color.LTGRAY;

    // 中心进度文字
    private Paint mProgressPaint;
    private String mProgressText = "50%";

    private float progress = 50;
    private float progressTextSize = 40;
    private int progressTextColor = Color.GREEN;

    private float speed;

    // 定义圆弧的外围矩形区域
    private RectF mArcRectF;
    private RectF mArcBgRectF;

    public CircleProgress(Context context) {
        super(context);
    }

    public CircleProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CircleProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mViewWidth = MeasureSpec.getSize(widthMeasureSpec);
        mViewHeight = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(mViewWidth,mViewHeight);
        initView();
    }

    private void initView() {
        // 圆弧半径
        float circleRadius = Math.min(mViewWidth,mViewHeight)/2;
        mViewCenterX = mViewWidth/2;
        mViewCenterY = mViewHeight/2;

        mArcPaint = new Paint();
        // 抗锯齿
        mArcPaint.setAntiAlias(true);
        // 设置颜色
        mArcPaint.setColor(progressCircleColor);
        // 设置宽
        mArcPaint.setStrokeWidth(mArcPaintStroke);
        // 设置风格，STROKE空心只有线宽，FILL实心除去线宽,FILL_AND_STROKE实心包括线宽
        mArcPaint.setStyle(Paint.Style.STROKE);
        // 矩形的左上右下
        mArcRectF = new RectF(mViewCenterX -circleRadius+(padding+mArcPaintStroke/2)
                , mViewCenterY -circleRadius+(padding+mArcPaintStroke/2)
                , mViewCenterX +circleRadius-(padding+mArcPaintStroke/2)
                , mViewCenterY +circleRadius-(padding+mArcPaintStroke/2));

        mArcBgPaint = new Paint();
        // 抗锯齿
        mArcBgPaint.setAntiAlias(true);
        // 设置颜色
        mArcBgPaint.setColor(progressCircleBgColor);
        // 设置宽
        mArcBgPaint.setStrokeWidth(mArcBgPaintStroke);
        // 设置风格，STROKE空心只有线宽，FILL实心除去线宽,FILL_AND_STROKE实心包括线宽
        mArcBgPaint.setStyle(Paint.Style.STROKE);
        mArcBgRectF = new RectF(mViewCenterX -circleRadius+(padding+mArcPaintStroke/2)
                , mViewCenterY -circleRadius+(padding+mArcPaintStroke/2)
                , mViewCenterX +circleRadius-(padding+mArcPaintStroke/2)
                , mViewCenterY +circleRadius-(padding+mArcPaintStroke/2));

        mProgressPaint = new Paint();
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setColor(progressTextColor);
        // 文字的位置设置为居中
        mProgressPaint.setTextAlign(Paint.Align.CENTER);
        mProgressPaint.setTextSize(progressTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Paint.FontMetrics fontMetrics = mProgressPaint.getFontMetrics();
        float baseLine = mViewCenterY - (fontMetrics.bottom + fontMetrics.top)/2;
        // 绘制文字参数为：文字、要显示的文字从0位置开始、文字总长度位置结束（即显示正串文字）、文字中心的x值（因为上面设为Align.CENTER）、文字底部的y值、Paont
        canvas.drawText(mProgressText, 0, mProgressText.length(), mViewCenterX, baseLine, mProgressPaint);

        // 圆弧矩形，起点角度（Y轴方向顺时针），终点角度，paint
        canvas.drawArc(mArcBgRectF,-90,360f,false,mArcBgPaint);

        canvas.drawArc(mArcRectF,-90,progress/100*360,false,mArcPaint);
    }

    public void setProgress(float progress){
        this.progress = progress;
        this.mProgressText = (int)progress+"%";
        invalidate();
    }

    public void setProgressTextSize(float progressTextSize){
        this.progressTextSize = progressTextSize;
        invalidate();
    }

    public void setProgressTextColor(int progressTextColor){
        this.progressTextColor = progressTextColor;
        invalidate();
    }

    public void setProgressCircleColor(int progressCircleColor){
        this.progressCircleColor = progressCircleColor;
        invalidate();
    }

    public void setCircleWidth(float circleWidth){
        this.mArcPaintStroke = circleWidth;
    }

    public float getprogress(){
        return progress;
    }

}
