package com.pcl.baierpad.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.pcl.baierpad.R;

/**
 * Created by Administrator on 2017/3/21.
 */

public class ProgressView extends View{
    private int text_size;
    private int max;
    private int now_progress_color;
    private int old_progress_color;
    private int progress_background_color;
    private int text_color;
    //at_most测量模式下的默认值
    private static final int AT_MOST_DEFAULE_VALUE=300;
    public ProgressView(Context context) {
        this(context,null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidthMode=MeasureSpec.getMode(widthMeasureSpec);
        int measureHeightMode=MeasureSpec.getMode(heightMeasureSpec);
        int resultWdith=0;
        int resultHeight=0;
        switch (MeasureSpec.getMode(widthMeasureSpec)){
            case MeasureSpec.AT_MOST:
                resultWdith=AT_MOST_DEFAULE_VALUE;
                break;
            case MeasureSpec.EXACTLY:
                resultWdith=MeasureSpec.getSize(widthMeasureSpec);
                break;
            case MeasureSpec.UNSPECIFIED:
                resultWdith=MeasureSpec.getSize(widthMeasureSpec);
                break;
        }
        switch (MeasureSpec.getMode(heightMeasureSpec)){
            case MeasureSpec.AT_MOST:
                resultHeight=AT_MOST_DEFAULE_VALUE;
                break;
            case MeasureSpec.EXACTLY:
                resultHeight=MeasureSpec.getSize(heightMeasureSpec);
                break;
            case MeasureSpec.UNSPECIFIED:
                resultHeight=MeasureSpec.getSize(heightMeasureSpec);
                break;
        }
        setMeasuredDimension(resultWdith,resultHeight);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.ProgressView);
        text_size=typedArray.getInt(R.styleable.ProgressView_text_size,20);
        max=typedArray.getInt(R.styleable.ProgressView_max,100);
        now_progress_color=typedArray.getColor(R.styleable.ProgressView_now_progress_color, ContextCompat.getColor(context,R.color.slidingTabLayout_color));
        old_progress_color=typedArray.getColor(R.styleable.ProgressView_old_progress_color,ContextCompat.getColor(context,R.color.slidingTabLayout_text_color));
        progress_background_color=typedArray.getColor(R.styleable.ProgressView_progress_background_color,ContextCompat.getColor(context,R.color.slidingTabLayout_text_color));
        text_color=typedArray.getColor(R.styleable.ProgressView_text_color,ContextCompat.getColor(context,R.color.slidingTabLayout_text_color));
        typedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


    }
}
