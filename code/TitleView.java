package com.nibaooo.nibazhuang.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nibaooo.nibazhuang.ApplicationManage;
import com.nibaooo.nibazhuang.R;


/**
 * Created by lightning-PCL on 2016/4/5.
 * maybe someday ,i hope i see the bast scenery in the world!
 * it's butiofault！ helloworld?
 */
public class TitleView extends LinearLayout implements View.OnClickListener{
//    private TextView tv_left;
    private ImageView iv_left;
    private TextView tv_right;
    private TextView tv_center;
    private int flagText;
    private int flagImg;
    private Context context;
    public TitleView(Context context) {
        super(context);
        initView(context);
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        this.context=context;
        TypedArray typedArray=context.obtainStyledAttributes(attrs, R.styleable.TitleView);

        String right=typedArray.getString(R.styleable.TitleView_tv_right);
        Drawable dRight=typedArray.getDrawable(R.styleable.TitleView_tv_rightImg);
        if(dRight!=null){
            dRight.setBounds(0, 0, dRight.getMinimumWidth(), dRight.getMinimumHeight());
            tv_right.setCompoundDrawables(null,null,dRight, null);
        }
        tv_right.setText(right);
        //获取中间字符和资源标识
        String center=typedArray.getString(R.styleable.TitleView_tv_center);
        Drawable dCenter=typedArray.getDrawable(R.styleable.TitleView_tv_centerImg);
        if(dCenter!=null){
            dCenter.setBounds(0, 0, dCenter.getMinimumWidth(), dCenter.getMinimumHeight());
            tv_center.setCompoundDrawables(dCenter,null,null, null);
        }
        tv_center.setText(center);
        Drawable imgLeft=typedArray.getDrawable(R.styleable.TitleView_tv_leftImg);
        if(imgLeft!=null){
            iv_left.setImageDrawable(imgLeft);

        }
        //共享资源回收
        typedArray.recycle();

    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 初始化view
     * */
    public void initView(Context context){
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.title_view,this);
//        tv_left=(TextView)view.findViewById(R.id.tv_Left);
        iv_left = (ImageView) view.findViewById(R.id.iv_back);
        tv_right=(TextView)view.findViewById(R.id.tv_right);
        tv_center=(TextView)view.findViewById(R.id.tv_title);
//        tv_left.setOnClickListener(this);
//        tv_right.setOnClickListener(this);
//        tv_center.setOnClickListener(this);
        iv_left.setOnClickListener(this);
        tv_center.setTextSize(18);

    }
//    public void setTv_LeftText(String text){
//        tv_left.setText(text);
//    }
    public void setTv_RightText(String text){
        tv_right.setText(text);
    }
    public  void setTv_CenterText(String text){
        tv_center.setText(text);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_back:
                ApplicationManage.getApplicationManage().finishActivity((Activity) context);
                break;
        }

    }
    public void setOnRightClickListener(OnClickListener listener){
        tv_right.setOnClickListener(listener);
    }

//    public void setOneLeftClickListener(OnClickListener listener){
//        tv_left.setOnClickListener(listener);
//    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }
}
