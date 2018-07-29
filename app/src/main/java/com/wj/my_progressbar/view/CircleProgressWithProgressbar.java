package com.wj.my_progressbar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.wj.my_progressbar.R;

/**
 * Author: WangJing
 * Date: 2018/7/29
 * Des:
 */

public class CircleProgressWithProgressbar extends HorizontalProgressWithProgressbar {

    private int mRadius;

    private int mMaxPaintWidth;

    public CircleProgressWithProgressbar(Context context) {
        this(context, null);
    }

    public CircleProgressWithProgressbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressWithProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //指定已经加载的进度宽度大一些
        mReachHeight = (int) (mUnreachHeight * 2.5);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CircleProgressWithProgressbar);
        mRadius = (int) typedArray.getDimension(R.styleable.CircleProgressWithProgressbar_radius, dp2px(20));
        typedArray.recycle();

        //设置画笔
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        mMaxPaintWidth = Math.max(mReachHeight, mUnreachHeight);
        int expect = mRadius * 2 + getPaddingLeft() + getPaddingRight() + mMaxPaintWidth;

        int width = resolveSize(expect, widthMeasureSpec);
        int height = resolveSize(expect, heightMeasureSpec);

        //得到真正的宽度 高度
        int mRealWidth = Math.min(width, height);
        //计算得到真正的半径值 mMaxPaintWidth是因为每个是mMaxPaintWidth/2 要减去两个mMaxPaintWidth/2
        mRadius = (mRealWidth - getPaddingRight() - getPaddingLeft() - mMaxPaintWidth) / 2;

        setMeasuredDimension(mRealWidth, mRealWidth);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        //设置绘制的内容
        String textProgress = getProgress() + "%";
        float textWidth = mPaint.measureText(textProgress);
        float textHeight = (mPaint.descent() + mPaint.ascent()) / 2;

        canvas.save();
        //移动坐标
        canvas.translate(getPaddingLeft() + mMaxPaintWidth / 2, getPaddingTop() + mMaxPaintWidth / 2);

        mPaint.setStyle(Paint.Style.STROKE);

        /**绘制未加载部分**/
        mPaint.setColor(mUnreachColor);
        mPaint.setStrokeWidth(mUnreachHeight);
        canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
        /**绘制已加载部分**/
        mPaint.setColor(mReachColor);
        mPaint.setStrokeWidth(mReachHeight);
        float sweepAngle = getProgress() * 1.0f / getMax() * 360;
        //绘制弧度
        canvas.drawArc(new RectF(0, 0, mRadius * 2, mRadius * 2), 0, sweepAngle, false
                , mPaint);

        /**绘制文字**/
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(textProgress, mRadius - textWidth / 2, mRadius - textHeight, mPaint);

        canvas.restore();
    }
}

