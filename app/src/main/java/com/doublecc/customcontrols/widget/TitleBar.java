package com.doublecc.customcontrols.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.doublecc.customcontrols.R;

/**
 * 自定义简单 标题栏 titlebar
 * Created by DoubleCC on 2016/12/5 0005.
 */

public class TitleBar extends RelativeLayout {

    // 取出attrs中styleable的值
    // 文字
    private String titleText,leftText,rightText;
    // titilebar背景颜色和三个控件文字颜色
    private int titlebarBackgroundColor,titleTextColor,leftTextColor,rightTextColor;
    // 左右两个TextView的背景资源
    private int leftBackground,rightBackground;
    // 标题的文字字体大小
    private float titleTextSize;

    // 定义组件
    private TextView leftButton,rightButton;
    private TextView titleTextView;

    // 布局属性，用来控制组件元素在ViewGroup中的位置
    private LayoutParams mLeftParams, mTitlepParams, mRightParams;

    // 点击监听接口
    private titlebarClickListener clickListener;

    public TitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initVariable(context,attrs);
        initView(context);
    }

    /**
     初始化变量
     */
    private void initVariable(Context context, AttributeSet attrs) {
        // 将attrs中的值存储到TypedArray中
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);
        titleText = ta.getString(R.styleable.TitleBar_title);
        leftText = ta.getString(R.styleable.TitleBar_leftText);
        rightText = ta.getString(R.styleable.TitleBar_rightText);
        titlebarBackgroundColor = ta.getColor(R.styleable.TitleBar_titlebarBackground, 0);
        titleTextColor = ta.getColor(R.styleable.TitleBar_titleTextColor,
                ContextCompat.getColor(context,R.color.titleTextColor));
        leftTextColor = ta.getColor(R.styleable.TitleBar_leftTextColor,
                ContextCompat.getColor(context,R.color.leftTextColor));
        rightTextColor = ta.getColor(R.styleable.TitleBar_rightTextColor,
                ContextCompat.getColor(context,R.color.rightTextColor));
        leftBackground = ta.getResourceId(R.styleable.TitleBar_leftBackground,
                R.drawable.titlebar_left_btn_bg);
        rightBackground = ta.getResourceId(R.styleable.TitleBar_rightBackground,
                R.drawable.titlebar_right_btn_bg);
        titleTextSize = ta.getFloat(R.styleable.TitleBar_titleTextSize,15);
        // 注意！此处获取完属性值后要添加recycle()方法，避免重新创建时发生错误
        ta.recycle();
    }
    /**
    初始化控件
     */
    private void initView(Context context) {
        setBackgroundColor(titlebarBackgroundColor);

        // 创建childView
        leftButton = new TextView(context);
        rightButton = new TextView(context);
        titleTextView = new TextView(context);

        // 设置childview属性
        leftButton.setTextColor(leftTextColor);
        leftButton.setBackgroundResource(leftBackground);
        leftButton.setText(leftText);
        leftButton.setPadding(20,10,20,10);

        rightButton.setTextColor(rightTextColor);
        rightButton.setBackgroundResource(rightBackground);
        rightButton.setText(rightText);
        rightButton.setPadding(20,10,20,10);

        titleTextView.setText(titleText);
        titleTextView.setTextColor(titleTextColor);
        titleTextView.setTextSize(titleTextSize);
        titleTextView.setGravity(Gravity.CENTER);

        // 设置布局并添加到ViewGroup中,此处最好是WRAP_CONTENT，不然Measure时控件会过大，会使设置控件高度为wrap_content时失效
        mLeftParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        mLeftParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        mLeftParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        mLeftParams.setMargins(10,10,10,10);
        // 添加到ViewGroup
        addView(leftButton, mLeftParams);

        mRightParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        mRightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, TRUE);
        mRightParams.addRule(RelativeLayout.CENTER_VERTICAL, TRUE);
        mRightParams.setMargins(10,10,10,10);
        addView(rightButton, mRightParams);

        mTitlepParams = new LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
        mTitlepParams.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        addView(titleTextView, mTitlepParams);

        // 设置监听
        leftButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.leftClick();
            }
        });
        rightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                clickListener.rightClick();
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,
                             int heightMeasureSpec) {
        // 如果当前ViewGroup的宽高为wrap_content的情况
        int width = 0; // 自己测量的宽度
        int height = 0;// 自己测量的高度

        // 获取子view的个数
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            // 测量子View的宽和高
            measureChildWithMargins(child, widthMeasureSpec,0, heightMeasureSpec,0);
            // 得到LayoutParams
            MarginLayoutParams lp = (MarginLayoutParams) getLayoutParams();
            // 子View占据的宽度
            int childWidth = child.getMeasuredWidth() + lp.leftMargin
                    + lp.rightMargin;
            // 子View占据的高度
            int childHeight = child.getMeasuredHeight() + lp.topMargin
                    + lp.bottomMargin;
            width += childWidth;
            if (childHeight > height){
                height = childHeight;
            }
        }

        setMeasuredDimension(
                measureDimension(width,widthMeasureSpec),
                measureDimension(height,heightMeasureSpec));

        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
    }

    private int measureDimension(int defaultDimension,int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = defaultDimension;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }
        return result;
    }

    /**
    添加按钮监听
     */
    public void setOnTitleBarClickListener(titlebarClickListener titlebarListener){
        this.clickListener = titlebarListener;
    }

    /**
    设置按钮点击监听回调接口
     */
    public interface titlebarClickListener{
        void leftClick();
        void rightClick();
    }

    /**
    设置按钮是否显示
     */
    public void setButtonVisable(boolean leftBtnVisable,boolean rightBtnVisable){
        if (leftBtnVisable){
            leftButton.setVisibility(View.VISIBLE);
        }else{
            leftButton.setVisibility(View.GONE);
        }
        if (rightBtnVisable){
            rightButton.setVisibility(View.VISIBLE);
        }else{
            rightButton.setVisibility(View.GONE);
        }
    }
}
