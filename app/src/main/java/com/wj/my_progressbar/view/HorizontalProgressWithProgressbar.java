package com.wj.my_progressbar.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.ProgressBar;

import com.wj.my_progressbar.R;

/**
 * Created by hannah on 2018/7/28.
 */

public class HorizontalProgressWithProgressbar extends ProgressBar {

    //设置默认参数
    private static final int DEFAULT_TEXT_SIZE = 2;//sp
    private static final int DEFAULT_TEXT_COLOR = 0xFFFC00D1;
    private static final int DEFAULT_UNREACH_COLOR = 0xFFD3D6DA;
    private static final int DEFAULT_UNREACH_HEIGHT = 2;//dp
    private static final int DEFAULT_REACH_COLOR = DEFAULT_TEXT_COLOR;
    private static final int DEFAULT_REACH_HEIGHT = 2;//dp
    private static final int DEFAULT_TEXT_OFFSET = 10;//dp

    //设置默认值的定义变量
    protected int mTextSize = sp2px(DEFAULT_TEXT_SIZE);
    protected int mTextColor = DEFAULT_TEXT_COLOR;
    protected int mUnreachColor = DEFAULT_UNREACH_COLOR;
    protected int mUnreachHeight = dp2px(DEFAULT_UNREACH_HEIGHT);
    protected int mReachColor = DEFAULT_REACH_COLOR;
    protected int mReachHeight = dp2px(DEFAULT_REACH_HEIGHT);
    protected int mTextOffset = dp2px(DEFAULT_TEXT_OFFSET);

    //画笔
    protected Paint mPaint;

    private int mRealWidth;


    //无属性的构造方法
    public HorizontalProgressWithProgressbar(Context context) {
        this(context, null);
    }

    //有设置属性的构造方法
    public HorizontalProgressWithProgressbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalProgressWithProgressbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取attr.xml文件中的属性
        getAttrs(attrs);
    }

    /**
     * 获取属性
     *
     * @param attrs
     */
    private void getAttrs(AttributeSet attrs) {
        mPaint = new Paint();

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.HorizontalProgressWithProgressbar);
        mTextSize = (int) typedArray.getDimension(R.styleable.HorizontalProgressWithProgressbar_progress_text_size, mTextSize);
        mTextOffset = (int) typedArray.getDimension(R.styleable.HorizontalProgressWithProgressbar_progress_text_offset, mTextOffset);
        mTextColor = typedArray.getColor(R.styleable.HorizontalProgressWithProgressbar_progress_text_color, mTextColor);
        mUnreachHeight = (int) typedArray.getDimension(R.styleable.HorizontalProgressWithProgressbar_progress_unreach_height, mUnreachHeight);
        mUnreachColor = typedArray.getColor(R.styleable.HorizontalProgressWithProgressbar_progress_text_color, mUnreachColor);
        mReachHeight = (int) typedArray.getDimension(R.styleable.HorizontalProgressWithProgressbar_progress_reach_height, mReachHeight);
        mReachColor = typedArray.getColor(R.styleable.HorizontalProgressWithProgressbar_progress_reach_cololr, mReachColor);
        //回收
        typedArray.recycle();

        mPaint.setTextSize(mTextSize);
    }


    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //具体测量值
        //获取测量值
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int height = measureHeight(heightMeasureSpec);

        //设置宽高
        setMeasuredDimension(widthSize, height);

        mRealWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        canvas.save();

        canvas.translate(getPaddingLeft(), getHeight() / 2);

        //是否需要绘制后面的进度条
        boolean isNeedUnreach = true;

        String text = getProgress() + "%";
        int textWidth = (int) mPaint.measureText(text);
        //进度
        float progressVal = getProgress() * 1.0f / getMax();
        float progressX = progressVal * mRealWidth;
        if (progressX + textWidth > mRealWidth) {
            progressX = mRealWidth - textWidth;
            isNeedUnreach = false;
        }

        /**绘制前面的进度条**/
        float endX = progressX - mTextOffset / 2;
        if (endX > 0) {
            mPaint.setColor(mReachColor);
            mPaint.setStrokeWidth(mReachHeight);
            canvas.drawLine(0, 0, endX, 0, mPaint);
        }


        /**绘制文字**/
        mPaint.setColor(mTextColor);
        int y = (int) (-(mPaint.descent() + mPaint.ascent()) / 2);
        canvas.drawText(text, progressX, y, mPaint);

        /**绘制后面的进度条**/
        if (isNeedUnreach) {
            mPaint.setColor(mUnreachColor);
            mPaint.setStrokeWidth(mUnreachHeight);
            canvas.drawLine(progressX + mTextOffset / 2 + textWidth, 0, mRealWidth, 0, mPaint);
        }
        canvas.restore();
    }

    /**
     * 具体测量高度
     *
     * @param heightMeasureSpec
     * @return
     */
    private int measureHeight(int heightMeasureSpec) {
        //获取测量值
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int result = 0;

        //根据不同测量模式处理
        if (heightMode == MeasureSpec.EXACTLY) {//精确值
            result = heightSize;
        } else {
            int textHeight = (int) (mPaint.descent() - mPaint.ascent());
            result = getPaddingTop() + getPaddingBottom() + Math.max(Math.max(mUnreachHeight, mReachHeight), Math.abs(textHeight));
            if (heightMode == MeasureSpec.AT_MOST) {//相当于wrap_content
                result = Math.min(result, heightSize);
            }
        }

        return result;
    }

    /**
     * sp转换成px
     *
     * @param spVal
     * @return
     */
    protected int sp2px(int spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, getResources().getDisplayMetrics());
    }

    /**
     * dp转换成px
     *
     * @param dpVal
     * @return
     */
    protected int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, getResources().getDisplayMetrics());

    }
}
