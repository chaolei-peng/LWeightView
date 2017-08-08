package com.nibaooo.nibazhuang.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;


import com.nibaooo.nibazhuang.ApplicationManage;
import com.nibaooo.nibazhuang.R;

import java.util.ArrayList;
import java.util.List;

public class CustomSliderView extends FrameLayout {
    private int count;
    private List<ImageView> imageViews;
    private Context context;
    private ViewPager vp;
    private boolean isAutoPlay;
    private int currentItem;
    private int delayTime;
    private LinearLayout ll_dot;
    private List<ImageView> iv_dots;
    private ItemClickListener mItemClickListener;
    private boolean is_animation;
    private Handler handler = new Handler();

    public CustomSliderView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initData();
    }

    public CustomSliderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomSliderView(Context context) {
        this(context, null);
    }

    private void initData() {
        imageViews = new ArrayList<>();
        iv_dots = new ArrayList<>();
        delayTime = 3500;
    }

    public void setImagesUrl(List<String> imagesUrl, ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
        initLayout();
        initImgFromNet(imagesUrl);

        showTime();
    }

    public void setImagesRes(int[] imagesRes, ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
        initLayout();
        initImgFromRes(imagesRes);
        showTime();
    }
    private View view;
    private void initLayout() {
        imageViews.clear();
        if(view==null){
            view = LayoutInflater.from(context).inflate(R.layout.custom_slider_view, this, true);
            vp = (ViewPager) view.findViewById(R.id.vp);
            ll_dot = (LinearLayout) view.findViewById(R.id.ll_dot);
        }


        ll_dot.removeAllViews();
    }

    private void initImgFromRes(int[] imagesRes) {
        count = imagesRes.length;
        for (int i = 0; i < count; i++) {
            ImageView iv_dot = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            iv_dot.setImageResource(R.mipmap.dot_blur);
            ll_dot.addView(iv_dot, params);
            iv_dots.add(iv_dot);
        }
        iv_dots.get(0).setImageResource(R.mipmap.dot_focus);

        for (int i = 0; i <= count + 1; i++) {
            ImageView iv = new ImageView(context);

            iv.setScaleType(ScaleType.FIT_XY);
//			iv.setBackgroundResource(R.drawable.loading);
            if (i == 0) {
                iv.setImageResource(imagesRes[count - 1]);
            } else if (i == count + 1) {
                iv.setImageResource(imagesRes[0]);
            } else {
                iv.setImageResource(imagesRes[i - 1]);
            }
            imageViews.add(iv);
        }

    }

    private void initImgFromNet(List<String> imagesUrl) {
        count = imagesUrl.size();

        for (int i = 0; i < count; i++) {
            ImageView iv_dot = new ImageView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            iv_dot.setImageResource(R.mipmap.dot_blur);
            ll_dot.addView(iv_dot, params);
            iv_dots.add(iv_dot);
        }
        iv_dots.get(0).setImageResource(R.mipmap.dot_focus);

        for (int i = 0; i <= count + 1; i++) {
            final ImageView imageView = new ImageView(context);

            imageView.setScaleType(ScaleType.FIT_XY);
            if (i == 0) {
                imageView.setTag(count - 1);
                ApplicationManage.getApplicationManage().getImageLoader().displayImage(imagesUrl.get(count - 1),imageView,ApplicationManage.getApplicationManage().getDisplayImageOptions());
            } else if (i == count + 1) {
                imageView.setTag(0);
                ApplicationManage.getApplicationManage().getImageLoader().displayImage(imagesUrl.get(0), imageView,ApplicationManage.getApplicationManage().getDisplayImageOptions());
            } else {
                imageView.setTag(i - 1);
                ApplicationManage.getApplicationManage().getImageLoader().displayImage(imagesUrl.get(i - 1), imageView,ApplicationManage.getApplicationManage().getDisplayImageOptions());
            }

            if (mItemClickListener != null)
                imageView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mItemClickListener.onItemClick(imageView, Integer.parseInt(view.getTag().toString()));
                    }
                });

            imageViews.add(imageView);
        }
        Log.d("imagesUrla",imageViews.size()+"");
    }

    public ViewPager getViewPager() {
        if (vp != null)
            return vp;
        return null;
    }

    public void removeAll() {
        if (imageViews != null)
            imageViews.clear();
        if (iv_dots != null)
            iv_dots.clear();
        if (vp != null)
            vp.removeAllViews();
    }

    private void showTime() {
        if(is_animation){
            vp.setPageTransformer(false, new CubeTransformer());
        }
        vp.setAdapter(new CustomPagerAdapter());
        vp.setFocusable(true);
        vp.setCurrentItem(1);
        currentItem = 1;
        vp.addOnPageChangeListener(new MyOnPageChangeListener());
        if (count > 1)
            startPlay();
    }

    private void startPlay() {
        if(isAutoPlay){
            handler.postDelayed(task, delayTime);
        }

    }

    public void set_isAutoPlay(boolean boo){
        isAutoPlay=boo;
    }
    private final Runnable task = new Runnable() {

        @Override
        public void run() {
            if (isAutoPlay) {
                currentItem = currentItem % (count + 1) + 1;
                if (currentItem == 1) {
                    vp.setCurrentItem(currentItem, false);
                    handler.post(task);
                } else {
                    vp.setCurrentItem(currentItem);
                    handler.postDelayed(task, 3000);
                }
            } else {
                handler.postDelayed(task, 5000);
            }
        }
    };

    class CustomPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViews.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(imageViews.get(position));
            return imageViews.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViews.get(position));
        }

    }
    private PagerChangeStateListener pagerChangeStateListener;
    public void setOnPageChangeListener( PagerChangeStateListener pagerChangeStateListener){
        this.pagerChangeStateListener=pagerChangeStateListener;
    }
    public interface PagerChangeStateListener{
        public void stateListener(int state);
    }
    class MyOnPageChangeListener implements OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {
            if(pagerChangeStateListener!=null){
                pagerChangeStateListener.stateListener(arg0);
            }
            switch (arg0) {
                case 1:
                    isAutoPlay = false;
                    break;
                case 2:
                    isAutoPlay = true;
                    break;
                case 0:
                    if (vp.getCurrentItem() == 0) {
                        vp.setCurrentItem(count, false);
                    } else if (vp.getCurrentItem() == count + 1) {
                        vp.setCurrentItem(1, false);
                    }
                    currentItem = vp.getCurrentItem();
                    isAutoPlay = true;
                    break;
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            //回调位置变化
            if(pagerListener!=null){
                pagerListener.onPagerListener(arg0-1);
            }
            for (int i = 0; i < iv_dots.size(); i++) {
                if (i == arg0 - 1) {
                    iv_dots.get(i).setImageResource(R.mipmap.dot_focus);
                } else {
                    iv_dots.get(i).setImageResource(R.mipmap.dot_blur);
                }
            }
        }
    }

    public void setAnimation(boolean boo){
        is_animation=boo;
    }
    public interface ItemClickListener {

        public void onItemClick(ImageView imageView, int position);
    }
    public void setPagerListener(PagerListener pagerListener){
        this.pagerListener=pagerListener;
    }
    private PagerListener pagerListener;
    public interface PagerListener{
        public void onPagerListener(int position);
    }
}
