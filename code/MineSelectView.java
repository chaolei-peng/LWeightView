package com.nibaooo.nibazhuang.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nibaooo.nibazhuang.R;

/**
 * 个人中心Item控件
 */
public class MineSelectView extends LinearLayout
{
    private ImageView mIvHotel = null;
    private TextView mTvTitle = null;
    private TextView mTvContent = null;

    public MineSelectView(Context context)
    {
        this(context,null);
    }

    public MineSelectView(Context context, AttributeSet attrs)
    {
        super(context, attrs);

        LayoutInflater inflater = LayoutInflater.from(context);

        inflater.inflate(R.layout.layout_mine_select,this);

        mIvHotel = (ImageView) findViewById(R.id.iv_item);

        mTvTitle = (TextView) findViewById(R.id.tv_tip);

        initAttribute(context, attrs);
    }

    /**
     * 初始化属性
     * @param context
     * @param attrs
     */
    private void initAttribute(Context context, AttributeSet attrs)
    {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.mine_select);

        // 获取图片
        Drawable icon =
                array.getDrawable(R.styleable.mine_select_mine_icon);

        mIvHotel.setImageDrawable(icon);


        // 获取文本
        String title =
                array.getString(R.styleable.mine_select_mine_type);

        mTvTitle.setText(title);


        array.recycle();
    }

    /**
     * 设置条件
     * @param content
     */
    public void setContent(String content)
    {
        mTvContent.setText(content);
    }

    public String getContent()
    {
        return mTvContent.getText().toString();
    }


    public void setImgRes(int res)
    {
        mIvHotel.setImageResource(res);
    }


}
