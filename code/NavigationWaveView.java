package com.nibaooo.nibazhuang.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

import com.nibaooo.nibazhuang.R;
import com.nibaooo.nibazhuang.utlis.ScreenUtils;

/**
 * Created by Administrator on 2017/6/22.
 */

public class NavigationWaveView extends View{
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what==1){
                startMainAnimation();
            }else{
                startBgAnimation();
            }
        }
    };
//  主波浪
    private Paint paint;
//  副波浪画笔
    private Paint bg_paint;
//    波浪颜色
    private int wave_color;
//  波峰
    private int wave_crest;
//  波槽
    private int wave_duct;
//  水位高度
    private int wave_height;
//  背景波浪透明度
    private int wave_bg_pellucidity;
//   主波浪path
    private Path path;
//    副波浪path
    private Path bg_path;
//   view的宽
    private int width;
//    view的高
    private int height;
//  初始标记背景动画是否执行

    public NavigationWaveView(Context context) {
        this(context,null);

    }

    public NavigationWaveView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NavigationWaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.NavigationWaveView);
        wave_crest= ScreenUtils.dp2px(context,typedArray.getInteger(R.styleable.NavigationWaveView_wave_crest,30));
        wave_duct=ScreenUtils.dp2px(context,typedArray.getInteger(R.styleable.NavigationWaveView_wave_duct,30));
        wave_height=ScreenUtils.dp2px(context,typedArray.getInteger(R.styleable.NavigationWaveView_wave_height,80));
        wave_bg_pellucidity=typedArray.getInteger(R.styleable.NavigationWaveView_wave_bg_pellucidity,155);
        wave_color=typedArray.getInteger(R.styleable.NavigationWaveView_wave_color,R.color.colorPrimary);
        typedArray.recycle();
        typedArray=null;
        init();
    }

    /**
     * 参数初始化
     */
    private void init(){
        paint=new Paint();
        paint.setColor(wave_color);
        //消除锯齿
        paint.setAntiAlias(true);
        //设置样式 实心
        paint.setStyle(Paint.Style.FILL);
        bg_paint=new Paint();
        bg_paint.setColor(wave_color);
        //消除锯齿
        bg_paint.setAntiAlias(true);
        //设置样式 实心
        bg_paint.setStyle(Paint.Style.FILL);
        //设置透明度
        bg_paint.setAlpha(wave_bg_pellucidity);

        //创建主波浪path对象
        path=new Path();
        //创建福波浪path
        bg_path=new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSpecMode=MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode=MeasureSpec.getMode(heightMeasureSpec);
        int widthResult=0;
        int heightResult=0;
        switch (widthSpecMode){
            //精准模式
            case MeasureSpec.EXACTLY:
                widthResult=MeasureSpec.getSize(widthMeasureSpec);
                break;
            //
            case MeasureSpec.AT_MOST:
                widthResult=400;
                break;
            case MeasureSpec.UNSPECIFIED:
                widthResult=400;
                break;
        }
        switch (heightSpecMode){
            case MeasureSpec.EXACTLY:
                heightResult=MeasureSpec.getSize(heightMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
                heightResult=100;
                break;
            case MeasureSpec.UNSPECIFIED:
                heightResult=100;
                break;
        }

        //具体测量
        setMeasuredDimension(widthResult,heightResult);
    }

    /***
     * 处理具体绘制
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //清除所有轨迹
        path.reset();
        path.moveTo(pointFOne.x,pointFOne.y);
//        Log.d("point:","Width:"+width+"--Height:"+height);
        path.quadTo(controlOne.x,controlOne.y,pointFTwo.x,pointFTwo.y);
        path.quadTo(controlTwo.x,controlTwo.y,pointFThree.x,pointFThree.y);
        path.quadTo(controlThree.x,controlThree.y,pointFFour.x,pointFFour.y);
        path.quadTo(controlFour.x,controlFour.y,pointFFive.x,pointFFive.y);
        path.lineTo(pointFFive.x,height);
        path.lineTo(pointFOne.x,height);

        bg_path.reset();
        bg_path.moveTo(bg_pointFOne.x,bg_pointFOne.y);
        bg_path.quadTo(bg_controlOne.x,bg_controlOne.y,bg_pointFTwo.x,bg_pointFTwo.y);
        bg_path.quadTo(bg_controlTwo.x,bg_controlTwo.y,bg_pointFThree.x,bg_pointFThree.y);
        bg_path.quadTo(bg_controlThree.x,bg_controlThree.y,bg_pointFFour.x,bg_pointFFour.y);
        bg_path.quadTo(bg_controlFour.x,bg_controlFour.y,bg_pointFFive.x,bg_pointFFive.y);
        bg_path.lineTo(bg_pointFFive.x,height);
        bg_path.lineTo(bg_pointFOne.x,height);

        canvas.drawPath(bg_path,bg_paint);
        canvas.drawPath(path,paint);


    }
    PointF pointFOne;
    PointF controlOne;
    PointF pointFTwo;
    PointF controlTwo;
    PointF pointFThree;
    PointF controlThree;
    PointF pointFFour;
    PointF controlFour;
    PointF pointFFive;


    PointF bg_pointFOne;
    PointF bg_controlOne;
    PointF bg_pointFTwo;
    PointF bg_controlTwo;
    PointF bg_pointFThree;
    PointF bg_controlThree;
    PointF bg_pointFFour;
    PointF bg_controlFour;
    PointF bg_pointFFive;

    private PointF test;
    /**
     * 初始设置起始点
     */
    private void reSetPoint(){
        test=new PointF(-width,height-wave_height);
        //起始点1
        pointFOne=new PointF(-width,height-wave_height);
        //控制点1
        controlOne=new PointF(-(width*(3f/4f)),height-(wave_height+wave_crest));
        //起始点2
        pointFTwo=new PointF(-(width/2f),height-wave_height);
        //控制点2
        controlTwo=new PointF(-(width*(1f/4f)),height-(wave_height-wave_duct));
        //起始点3
        pointFThree=new PointF(0,height-wave_height);
        //控制点3
        controlThree=new PointF(width*((1f/4f)),height-(wave_height+wave_crest));
        //起始点4
        pointFFour=new PointF(width/2f,height-wave_height);
        //控制点4
        controlFour=new PointF(width*(3f/4f),height-(wave_height-wave_duct));
        //终点(起始点5)
        pointFFive=new PointF(width,height-wave_height);


        //起始点1
        bg_pointFOne=new PointF(-width,height-wave_height);
        //控制点1
        bg_controlOne=new PointF(-(width*(3f/4f)),height-(wave_height+wave_crest));
        //起始点2
        bg_pointFTwo=new PointF(-(width/2f),height-wave_height);
        //控制点2
        bg_controlTwo=new PointF(-(width*(1f/4f)),height-(wave_height-wave_duct));
        //起始点3
        bg_pointFThree=new PointF(0,height-wave_height);
        //控制点3
        bg_controlThree=new PointF(width*((1f/4f)),height-(wave_height+wave_crest));
        //起始点4
        bg_pointFFour=new PointF(width/2f,height-wave_height);
        //控制点4
        bg_controlFour=new PointF(width*(3f/4f),height-(wave_height-wave_duct));
        //终点(起始点5)
        bg_pointFFive=new PointF(width,height-wave_height);



    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d("NNNonSizeChanged","onSizeChanged");

        width=w;
        height=h;
        reSetPoint();
    }
    private void startBgAnimation(){
        ValueAnimator valueAnimator=ValueAnimator.ofFloat(test.x);
        valueAnimator.setDuration(3000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(Animation.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bg_pointFOne.x= (float) animation.getAnimatedValue();
//                Log.d("pointFOne",pointFOne.x+"");
                bg_pointFTwo.x=bg_pointFOne.x+width/2f;
                bg_pointFThree.x=bg_pointFTwo.x+width/2f;
                bg_pointFFour.x=bg_pointFThree.x+width/2f;
                bg_pointFFive.x=bg_pointFFour.x+width/2f;
                //控制点
                bg_controlOne.x=bg_pointFOne.x+width/4f;
                bg_controlTwo.x=bg_pointFTwo.x+width/4f;
                bg_controlThree.x=bg_pointFThree.x+width/4f;
                bg_controlFour.x=bg_pointFFour.x+width/4f;
                invalidate();
            }
        });
        valueAnimator.start();
    }
    private void startMainAnimation(){

        ValueAnimator valueAnimator=ValueAnimator.ofFloat(test.x);
        valueAnimator.setDuration(5000);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(Animation.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pointFOne.x= (float) animation.getAnimatedValue();
                /**
                 *  为何一直是width/2
                 *  通过直接获取前一个点的位置+前一个值获得的的计算条件等于下一个值
                 */
                pointFTwo.x=pointFOne.x+width/2f;
                pointFThree.x=pointFTwo.x+width/2f;
                pointFFour.x=pointFThree.x+width/2f;
                pointFFive.x=pointFFour.x+width/2f;
                //控制点
                controlOne.x=pointFOne.x+width/4f;
                controlTwo.x=pointFTwo.x+width/4f;
                controlThree.x=pointFThree.x+width/4f;
                controlFour.x=pointFFour.x+width/4f;
                invalidate();
            }
        });
        valueAnimator.start();
    }

    public void startAnimation(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message=new Message();
                message.what=1;
                handler.sendMessageDelayed(message,500);
                message=new Message();
                message.what=2;
                handler.sendMessageDelayed(message,1000);
            }
        }).start();
    }
    public int getWave_color() {
        return wave_color;
    }

    public void setWave_color(int wave_color) {
        this.wave_color = wave_color;
    }

    public int getWave_crest() {
        return wave_crest;
    }

    public void setWave_crest(int wave_crest) {
        this.wave_crest = wave_crest;
    }

    public int getWave_duct() {
        return wave_duct;
    }

    public void setWave_duct(int wave_duct) {
        this.wave_duct = wave_duct;
    }

    public int getWave_height() {
        return wave_height;
    }

    public void setWave_height(int wave_height) {
        this.wave_height = wave_height;
    }

    public int getWave_bg_pellucidity() {
        return wave_bg_pellucidity;
    }

    public void setWave_bg_pellucidity(int wave_bg_pellucidity) {
        this.wave_bg_pellucidity = wave_bg_pellucidity;
    }
}
