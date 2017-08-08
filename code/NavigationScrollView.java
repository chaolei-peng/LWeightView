package com.nibaooo.nibazhuang.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.HorizontalScrollView;

import com.nibaooo.nibazhuang.R;

/**
 * Created by Administrator on 2017/2/23.
 */

public class NavigationScrollView extends HorizontalScrollView {
    //拿到HorizontalScrollView的唯一一个子view
    private View contentView;
    //定义一个阻尼系数
    private float factor;
    //最后Down保存的最后一个X坐标
    private int lastX;
    //保存一个初始位置信息始终不变
    private Rect rect=new Rect();
    //动画耗时
    private int animation_timer;
    //当进入此view时是否先开启动画效果
    private boolean is_animation;
    //是否移动了布局
    private boolean isMove=false;
    public NavigationScrollView(Context context) {
        this(context,null);
    }

    public NavigationScrollView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NavigationScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //获取自定义属性
        TypedArray typedArray=context.obtainStyledAttributes(attrs,R.styleable.NavigationScrollView);
        //获取阻尼系数
        factor=typedArray.getFloat(R.styleable.NavigationScrollView_factor_coefficient,0.5f);
        //获取动画耗时
        animation_timer=typedArray.getInteger(R.styleable.NavigationScrollView_animation_time,300);
        //是否开启自动动画
        is_animation=typedArray.getBoolean(R.styleable.NavigationScrollView_is_auto_animation,true);
    }
    private void init(){
        //获取唯一子View
        contentView=getChildAt(0);
        contentView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                startOnceAnimation();
                contentView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

    }
//所有子空间被映射成XML时调用
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        init();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int nowX= (int) ev.getRawX();
        //判断手指是否滑动到ScrollView外面
        boolean isActionInScrollView=ev.getY()<=0||ev.getY()>=contentView.getHeight();
        if(isActionInScrollView){
            if (isMove){
                startAnimation(contentView,0);
                isMove=false;
            }
            return true;
        }
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                lastX= (int) ev.getRawX();
                break;
            case MotionEvent.ACTION_UP:
                startAnimation(contentView,0);
                break;
            case MotionEvent.ACTION_MOVE:
                //首先处理当既不能达到左拉又不能达到右拉的条件即是正常滑动处理则break跳出交由HorizontalScrollView来处理正常滑动
                int offsetX=nowX-lastX;
                lastX= (int) ev.getRawX();
                if(!isLeftPull()&&!isRightPull()){
                    break;
                }
                //得到按下到移动时的偏移量
                Log.d("offsetX",offsetX+"");
                //判断是否移动该布局，确定用户动作是否是左滑还是右滑
                Log.d("isPull","isLeftPull:"+isLeftPull()+"--isRightPull:"+isRightPull()+"--offsetX"+offsetX);
                boolean action=
                        (isLeftPull()&&offsetX>0)||     //意图是当前已经处于左边已到边界且用户动作也是向右滑动的处理
                                (isRightPull()&&offsetX<0)||       //意图是当前已处于右边已到边界且用户动作也是向左滑动的处理
                                (isLeftPull()&&isRightPull());//这种是当前所有的子view宽度都没有超过HorizontalScrollView时的处理即左右都可以滑
                if(action){
                    int finalOffsetX= (int) (offsetX*factor);//最终的偏移量等于当前偏移量乘阻尼系数
                    contentView.layout(contentView.getLeft()+finalOffsetX,contentView.getTop(),contentView.getRight()+finalOffsetX,contentView.getBottom());
                    isMove=true;
                }
                Log.d("layout","left:"+contentView.getLeft()+"top:"+contentView.getTop()+"right:"+contentView.getRight()+"bottom:"+contentView.getBottom());
                Log.d("getLeft",getLeft()+"--"+getRight()+"--"+getScrollX());
                break;
        }

        //调用父类onTouchEvent将滑动逻辑处理交给HorizontalScrollView处理，只需处理越界时的左右拉的逻辑
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        return super.onTouchEvent(ev);
    }
    //是否滑到最左边
    private boolean isLeftPull(){
        return getScrollX()<=0;
    }
    //是否滑到最右边
    private boolean isRightPull(){
        return contentView.getWidth()<=getWidth()+getScrollX();
    }
    private void startAnimation(View view, int distance){
        //如果不需要一次模拟动画执行则按照常规执行动画
        TranslateAnimation translateAnimation=new TranslateAnimation(contentView.getLeft(),distance==0?rect.left:rect.left+distance,0,0);
        //手动滑动的默认动画时长
        translateAnimation.setDuration(1000);
        if(distance!=0){
            translateAnimation.setDuration(animation_timer);
            translateAnimation.setRepeatCount(1);
            translateAnimation.setRepeatMode(Animation.REVERSE);
        }
        view.startAnimation(translateAnimation);
        contentView.layout(rect.left,rect.top,rect.right,rect.bottom);
        isMove=true;

    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (contentView!=null){
            rect.set(contentView.getLeft(),contentView.getTop(),contentView.getRight(),contentView.getBottom());
        }
    }
    //对外提供一次模拟动画执行

    public void startOnceAnimation(){
        if(contentView!=null)
        startAnimation(contentView,-(contentView.getWidth()-getWidth()));
    }

}
