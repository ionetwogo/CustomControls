package com.doublecc.customcontrols.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 闪动TextView
 * Created by DoubleCC on 2016/12/5 0005.
 */

public class lusterTextView extends TextView {

    private int mViewWidth =0;
    private int mTranslate=0;
    private Paint mPaint;
    private LinearGradient mLinearGradient;
    private Matrix mGradientMatrix;

    public lusterTextView(Context context) {
        super(context);
    }

    public lusterTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public lusterTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /*
    组件大小改变时回调
     */
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mViewWidth == 0){
            mViewWidth = getMeasuredWidth();
            if (mViewWidth > 0){
                // 获取当前绘制TextView的Paint对象，
                mPaint = getPaint();
                // 给Paint对象添加原生没有的LinearGradient属性，此处色值数量可以在int数组中添加
                mLinearGradient = new LinearGradient(0,0,mViewWidth,0,new int[]{getCurrentTextColor(),0xffffffff,getCurrentTextColor()},null, Shader.TileMode.CLAMP);
                mPaint.setShader(mLinearGradient);
                mGradientMatrix = new Matrix();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mGradientMatrix != null){
            mTranslate += mViewWidth/10;
            if (mTranslate > mViewWidth){
                mTranslate = -mViewWidth;
            }
            mGradientMatrix.setTranslate(mTranslate,0);
            mLinearGradient.setLocalMatrix(mGradientMatrix);
            // 刷新速率
            postInvalidateDelayed(100);
        }
    }
}
