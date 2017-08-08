
package com.nibaooo.nibazhuang.widget;
import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.Bitmap.Config;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.nibaooo.nibazhuang.R;
import com.nibaooo.nibazhuang.utlis.PreferencesUtils;


/*
 *
 */
public class RoundProgressBar extends View {
	/**
	 * 画笔对象的引用
	 */
	private Paint paint;

	/**
	 * 圆环的颜色
	 */
	private int roundColor;

	/**
	 * 圆环进度的颜色
	 */
	private int roundProgressColor;

	/**
	 * 中间进度百分比的字符串的颜色
	 */
	private int textColor;

	/**
	 * 中间进度百分比的字符串的字体
	 */
	private float textSize;

	/**
	 * 圆环的宽度
	 */
	private float roundWidth;

	/**
	 * 最大进度
	 */
	private int max;

	/**
	 * 当前进度
	 */
	private float progress;
	/**
	 * 是否显示中间的进度
	 */
	private boolean textIsDisplayable;

	/**
	 * 进度的风格，实心或者空心
	 */
	private int style;
	public static final int STROKE = 0;
	public static final int FILL = 1;
	private Context context;
	private int flag;
	private int centerX;
	private int radius;
	private int centerY;
	//点击与中心点的距离
	private int clickDistance;
	private int cosDistance;
	private Double clickAngle;
	private int cosAngle;
	private float click_X;
	private float click_Y;
	private int clickFlag;
	//根据不同分辨率的文字大小
	private int scale_text;
	//内环半径
	private int in_radius;
	public RoundProgressBar(Context context) {
		this(context, null);
	}

	public RoundProgressBar(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		paint = new Paint();
		this.context=context;
		if(PreferencesUtils.getInt(context,"width")>=1080){
			scale_text=35;
		}else{
			scale_text=23;
		}

		TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
				R.styleable.RoundProgressBar);

		//获取自定义属性和默认值
		roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
		roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
		textColor = mTypedArray.getColor(R.styleable.RoundProgressBar_textColor, context.getResources().getColor(R.color.blueeeeee));
		textSize = mTypedArray.getDimension(R.styleable.RoundProgressBar_textSize, 15);
		roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);
		max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
		textIsDisplayable = mTypedArray.getBoolean(R.styleable.RoundProgressBar_textIsDisplayable, true);
		style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, 0);

		mTypedArray.recycle();
	}


	@SuppressLint("ResourceAsColor")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		/**
		 * 画最外层的大圆环
		 */
		centerX = getWidth()/2; //获取圆心的x坐标
		centerY=getHeight()/2;
		Log.d("dincesss","centerX:"+centerX+"centerY:"+centerY);
		in_radius=(int) (centerX - roundWidth*2)-20;
		radius = (int) (centerX - roundWidth)-20; //圆环的半径
		Log.d("内环半径",in_radius+"");
		/**
		 * 画进度百分比
		 */
		paint.setStrokeWidth(0);
		paint.setColor(context.getResources().getColor(R.color.blueeeeee));
		paint.setTextSize(80);
		paint.setTypeface(Typeface.DEFAULT_BOLD); //设置字体

		int percent = Math.round(((float)textprocess / (float)33)*100);  //中间的进度百分比，先转换成float在进行除法运算，不然都为0
		float textWidth = paint.measureText(percent + "%");   //测量字体宽度，我们需要根据字体的宽度设置在圆环中间

		if(textIsDisplayable  && style == STROKE){
			canvas.drawText(percent + "%", centerX - textWidth / 2, centerX + textSize/2, paint); //画出进度百分比
		}

		/**
		 * 画圆弧 ，画圆环的进度
		 */

		//设置进度是实心还是空心
		paint.setStrokeWidth(roundWidth * 2); //设置圆环的宽度
		paint.setColor(roundProgressColor);  //设置进度的颜色
		RectF oval = new RectF(centerX - radius, centerX - radius, centerX
				+ radius, centerX + radius);  //用于定义的圆弧的形状和大小的界限

		switch (style) {
			case STROKE: {
				paint.setStyle(Paint.Style.STROKE);
				float angle=360 * progress / max;
				paint.setColor(context.getResources().getColor(R.color.huise));
				canvas.drawArc(oval, 0, 360, false, paint);  //根据进度画圆弧
				paint.setColor(context.getResources().getColor(R.color.blueeeeee));
				/**
				 * 画阶段文字A
				 * */
				Paint textPaint=new Paint();
				textPaint.setTextSize(scale_text);
				textPaint.setColor(context.getResources().getColor(R.color.black));
				textPaint.setTypeface(Typeface.DEFAULT_BOLD);
				textPaint.setStrokeWidth(0);
				//根据已知的角度算出（A阶段）ax即27度的sin值为0.97
				//ax的cos值为0.23
				//cos等于领边/斜边
				//sin等于对边/斜边
				//以此为例往下推
				float ax=(float) (centerX+ (0.23*(radius-40+roundWidth)));
				float ay= (float) (centerY- (0.97*(radius-40+roundWidth)));
				canvas.drawText("A", ax, ay, textPaint);
				float bx= (centerX+radius-40+roundWidth);
				float by= centerY;
				canvas.drawText("B", bx, by, textPaint);
				float cx=(float)centerX;
				float cy= (float) centerY+(radius+(centerY-radius-15)/2);
				canvas.drawText("C", cx, cy, textPaint);
				float dx=(float) (centerX- (0.94*(radius-10+roundWidth)));
				float dy= (float) (centerY+(0.33*(radius-40+roundWidth)));
				canvas.drawText("D", dx, dy, textPaint);
				float ex=(float) (centerX- (0.78*(radius-roundWidth)));
				float ey= (float) (centerY-(0.63*(radius+roundWidth)));
				canvas.drawText("E", ex, ey, textPaint);
				/**
				 * */
				//angle》90因为视图界面是从水平线0度开始，目前需求从上方的正中间开始所有需要判断进度条是否大于90，如果大于则需要从270度开始画

				if(angle>90){
					////click_X：当点击的区域大于中心点x轴表明点击的区域为右半边

					//当没有点击任何区域则根据正常进度显示进度条
					canvas.drawArc(oval, 270, angle, false, paint);  //根据进度画圆弧
					canvas.drawArc(oval, 0, angle - 90, false, paint);  //根据进度画圆弧
					/***/
					textPaint.setColor(context.getResources().getColor(R.color.black));
					textPaint.setTextSize(scale_text);
					canvas.drawText("A", ax, ay, textPaint);
					canvas.drawText("B", bx, by, textPaint);
					canvas.drawText("C",  cx, cy, textPaint);
					canvas.drawText("D", dx, dy, textPaint);
					canvas.drawText("E",  ex, ey, textPaint);
					if(angle>=25){
						textPaint.setColor(context.getResources().getColor(R.color.writeeeee));
						textPaint.setTextSize(scale_text);
						canvas.drawText("A",  ax, ay,  textPaint);
					}
					if(angle>=127){
						textPaint.setColor(context.getResources().getColor(R.color.writeeeee));
						textPaint.setTextSize(scale_text);
						canvas.drawText("B", bx, by, textPaint);
					}
					if(angle>=201){
						textPaint.setColor(context.getResources().getColor(R.color.writeeeee));
						textPaint.setTextSize(scale_text);
						canvas.drawText("C",  cx, cy, textPaint);
					}
					if(angle>=259){
						textPaint.setColor(context.getResources().getColor(R.color.writeeeee));
						textPaint.setTextSize(scale_text);
						canvas.drawText("D", dx, dy, textPaint);
					}
					if(angle>=331){
						textPaint.setColor(context.getResources().getColor(R.color.writeeeee));
						textPaint.setTextSize(scale_text);
						canvas.drawText("E",  ex, ey, textPaint);
					}



				}else {
					canvas.drawArc(oval, 270, angle, false, paint);  //根据进度画圆弧
					if(angle>1){
						textPaint.setColor(context.getResources().getColor(R.color.black));
						textPaint.setTextSize(scale_text);
						canvas.drawText("A",  ax, ay,  textPaint);
						canvas.drawText("B", bx, by, textPaint);
					}
					if(angle>=25){
						textPaint.setColor(context.getResources().getColor(R.color.writeeeee));
						textPaint.setTextSize(scale_text);
						canvas.drawText("A",  ax, ay,  textPaint);
					}

				}
				//求点击的距离是否在圆环内，不在则不处理
				int click_distance=(int) Math.sqrt((click_Y-centerY)*(click_Y-centerY)+(click_X-centerX)*(click_X-centerX))+20;
				if(clickFlag==0){
					//283-360即E阶段的角度
					if(angle>283&&angle<=360){
						//E阶段
						cosAngle=70;
						Log.d("jiaodu",angle+"if"+cosAngle);
						//模拟数据
						click_X=150;
					}else if(angle>221&&angle<=283){
						//D阶段
						cosAngle=80;
						click_X=150;
					}else if(angle>180&&angle<=221){
						//
						cosAngle=200;
						click_X=150;
					}else{
						cosAngle=(int)angle;
						click_X=360;
						Log.d("jiaodu",angle+"else"+cosAngle);

					};
					Log.d("执行cosAngle",cosAngle+"");

				}
				Log.d("半边","click_X:"+click_X+"--centerX:"+centerX+"--cosAngle:"+cosAngle+"--click_distance"+click_distance+"--radius"+radius);
				if(click_X>=centerX&&cosAngle!=0&&click_distance>=radius){
					Log.d("右半边","click_X:"+click_X+"--centerX:"+centerX+"--cosAngle:"+cosAngle+"--click_distance"+click_distance+"--radius"+radius);
					if(cosAngle<=25){
						//点击回调参数
						if(clickProjectSection!=null){
							clickProjectSection.clickSection(1);
						}
						//当点击角度大于24
						if(angle<=25){
							paint.setStrokeWidth(roundWidth*3);
							paint.setColor(context.getResources().getColor(R.color.blueeeeee));
							canvas.drawArc(oval, 271, angle, false, paint);
							paint.setColor(context.getResources().getColor(R.color.huise));
							canvas.drawArc(oval, 271 + angle, 24 - angle, false, paint);

							textPaint.setColor(context.getResources().getColor(R.color.black));
							textPaint.setTextSize(scale_text+7);
							canvas.drawText("A",  ax, ay,  textPaint);
						}else{
							paint.setStrokeWidth(roundWidth * 3);
//						paint.setColor(context.getResources().getColor(R.color.blueeeeee));
							canvas.drawArc(oval, 271, 24, false, paint);

							textPaint.setColor(context.getResources().getColor(R.color.writeeeee));
							textPaint.setTextSize(scale_text+7);
							canvas.drawText("A",  ax, ay,  textPaint);
						}

					}else if(cosAngle<=24+114+3){
						Log.d("执行cosAnglebbbbbbbbbbbb",cosAngle+"");
						if(clickProjectSection!=null){
							clickProjectSection.clickSection(2);
						}
						if(angle<=24+114+3){
							if(angle<=90){
								if(angle<24){
									//当进度小于24
									paint.setStrokeWidth(roundWidth*3);
									paint.setColor(context.getResources().getColor(R.color.huise));
									canvas.drawArc(oval, 297, 63, false, paint);
									//51=114-63
									canvas.drawArc(oval, 0, 51, false, paint);
								}else{

									paint.setStrokeWidth(roundWidth*3);
									paint.setColor(context.getResources().getColor(R.color.blueeeeee));
									canvas.drawArc(oval, 297, angle-(24+3), false, paint);
									paint.setColor(context.getResources().getColor(R.color.huise));
									canvas.drawArc(oval, 297+(angle-(24+3)), (24+114+3)-angle, false, paint);
								}


							}else{
								//当点击角度大于24+114
								paint.setStrokeWidth(roundWidth*3);
								paint.setColor(context.getResources().getColor(R.color.blueeeeee));
								canvas.drawArc(oval, 297, 63, false, paint);
								paint.setColor(context.getResources().getColor(R.color.blueeeeee));
								canvas.drawArc(oval, 0, angle-63-24-3, false, paint);
								paint.setColor(context.getResources().getColor(R.color.huise));
								canvas.drawArc(oval, angle-63-24-3,(24+114+3)-angle, false, paint);
							}
							textPaint.setColor(context.getResources().getColor(R.color.black));
							textPaint.setTextSize(scale_text+7);
							canvas.drawText("B", bx, by, textPaint);

						}else{

							//当点击角度大于24+102
							paint.setStrokeWidth(roundWidth*3);
							paint.setColor(context.getResources().getColor(R.color.blueeeeee));
							canvas.drawArc(oval, 297, 63, false, paint);
							//51=114-63
							canvas.drawArc(oval, 0, 51, false, paint);
							textPaint.setColor(context.getResources().getColor(R.color.writeeeee));
							textPaint.setTextSize(scale_text+7);
							canvas.drawText("B", bx, by, textPaint);
						}

					}else if(cosAngle<=24+114+76+5){
						Log.d("angleangle",angle+"");
						if(clickProjectSection!=null){
							clickProjectSection.clickSection(3);
						}
						if(angle<=24+114+76+5){
							if(angle<=24+114+5){
								paint.setStrokeWidth(roundWidth*3);
								paint.setColor(context.getResources().getColor(R.color.huise));
								canvas.drawArc(oval, 51, 78, false, paint);
							}else{
								paint.setStrokeWidth(roundWidth*3);
								paint.setColor(context.getResources().getColor(R.color.blueeeeee));
								canvas.drawArc(oval, 51, angle-(24+114+5), false, paint);
								paint.setColor(context.getResources().getColor(R.color.huise));
								canvas.drawArc(oval, 51+(angle-(24+114+5)), (24+114+76+7)-angle, false, paint);
							}
							textPaint.setColor(context.getResources().getColor(R.color.black));
							textPaint.setTextSize(scale_text+7);
							canvas.drawText("C",  cx, cy, textPaint);


						}else{
							//以此类推
							paint.setStrokeWidth(roundWidth*3);
							paint.setColor(context.getResources().getColor(R.color.blueeeeee));
							canvas.drawArc(oval, 51, 78, false, paint);

							textPaint.setColor(context.getResources().getColor(R.color.writeeeee));
							textPaint.setTextSize(scale_text+7);
							canvas.drawText("C", cx, cy, textPaint);

						}


					}
				}else if(click_X<=centerX&&cosAngle!=0&&click_distance>=radius){
					Log.d("左半边","click_X:"+click_X+"--centerX:"+centerX+"--cosAngle:"+cosAngle+"--click_distance"+click_distance+"--radius"+radius);
					//click_X：当点击的区域小于中心点x轴表明点击的区域为左半边
					if(cosAngle<=76){
						if(clickProjectSection!=null){
							clickProjectSection.clickSection(5);
						}
						if(angle<24+114+76+60+76+9){
							if(angle<24+114+76+60+9){
								paint.setStrokeWidth(roundWidth*3);
								paint.setColor(context.getResources().getColor(R.color.huise));
								canvas.drawArc(oval, 193, 76, false, paint);

							}else{
								paint.setStrokeWidth(roundWidth*3);
								paint.setColor(context.getResources().getColor(R.color.blueeeeee));
								canvas.drawArc(oval, 193, angle-(24+114+76+60+9), false, paint);
								paint.setColor(context.getResources().getColor(R.color.huise));
								canvas.drawArc(oval, 193+(angle-(24+114+76+60+9)),(24+114+76+60+9+76+1)-angle , false, paint);

							}
							textPaint.setColor(context.getResources().getColor(R.color.black));
							textPaint.setTextSize(scale_text+7);
							canvas.drawText("E",  ex, ey, textPaint);

						}else{
							paint.setStrokeWidth(roundWidth*3);
							paint.setColor(context.getResources().getColor(R.color.blueeeeee));
							canvas.drawArc(oval, 193, 76, false, paint);

							textPaint.setColor(context.getResources().getColor(R.color.writeeeee));
							textPaint.setTextSize(scale_text+7);
							canvas.drawText("E", ex, ey, textPaint);
						}

					}else if(cosAngle<=139){
						if(clickProjectSection!=null){
							clickProjectSection.clickSection(4);
						}
						Log.d("angleangleeeeeee",angle+"");
						if(angle<=24+114+76+60+7){
							if(angle<24+114+76+7){
								paint.setStrokeWidth(roundWidth*3);
								paint.setColor(context.getResources().getColor(R.color.huise));
								canvas.drawArc(oval, 131, 60, false, paint);
							}else{
								paint.setStrokeWidth(roundWidth*3);
								paint.setColor(context.getResources().getColor(R.color.blueeeeee));
								canvas.drawArc(oval, 131, angle-(24+114+76+7), false, paint);
								paint.setColor(context.getResources().getColor(R.color.huise));
								canvas.drawArc(oval, 131+(angle-(24+114+76+7)), (24+114+76+7+60)-angle, false, paint);
							}
							textPaint.setColor(context.getResources().getColor(R.color.black));
							textPaint.setTextSize(scale_text+7);
							canvas.drawText("D", dx, dy, textPaint);

						}else{
							paint.setStrokeWidth(roundWidth*3);
							paint.setColor(context.getResources().getColor(R.color.blueeeeee));
							canvas.drawArc(oval, 131, 60, false, paint);

							textPaint.setColor(context.getResources().getColor(R.color.writeeeee));
							textPaint.setTextSize(scale_text+7);
							canvas.drawText("D", dx, dy, textPaint);

						}
					}else if(cosAngle<=24+114+76+5){
						Log.d("angleangle",angle+"");
						if(clickProjectSection!=null){
							clickProjectSection.clickSection(3);
						}
						if(angle<=24+114+76+5){
							if(angle<24+114+5){
								paint.setStrokeWidth(roundWidth*3);
								paint.setColor(context.getResources().getColor(R.color.huise));
								canvas.drawArc(oval, 51, 78, false, paint);
							}else{
								paint.setStrokeWidth(roundWidth*3);
								paint.setColor(context.getResources().getColor(R.color.blueeeeee));
								canvas.drawArc(oval, 51, angle-(24+114+5), false, paint);
								paint.setColor(context.getResources().getColor(R.color.huise));
								canvas.drawArc(oval, 51+(angle-(24+114+5)), (24+114+76+7)-angle, false, paint);
							}
							textPaint.setColor(context.getResources().getColor(R.color.black));
							textPaint.setTextSize(scale_text+7);
							canvas.drawText("C",  cx, cy, textPaint);


						}else{
							//以此类推
							paint.setStrokeWidth(roundWidth*3);
							paint.setColor(context.getResources().getColor(R.color.blueeeeee));
							canvas.drawArc(oval, 51, 78, false, paint);

							textPaint.setColor(context.getResources().getColor(R.color.writeeeee));
							textPaint.setTextSize(scale_text+7);
							canvas.drawText("C", cx, cy, textPaint);

						}


					}
				}

//			if(cosAngle!=0){
//				paint.setColor(context.getResources().getColor(R.color.blueeeeee));
//				canvas.drawArc(oval, 0, 360, false, paint);  //根据进度画圆弧
//			}



				paint.setColor(context.getResources().getColor(R.color.writeeeee));
				canvas.drawArc(oval, 269, 2, false, paint);  //根据进度画圆弧
				paint.setColor(context.getResources().getColor(R.color.writeeeee));
				canvas.drawArc(oval, 295, 2, false, paint);  //根据进度画圆弧
				paint.setColor(context.getResources().getColor(R.color.writeeeee));
				canvas.drawArc(oval, 51, 2, false, paint);  //根据进度画圆弧
				paint.setColor(context.getResources().getColor(R.color.writeeeee));
				canvas.drawArc(oval, 129, 2, false, paint);  //根据进度画圆弧
				paint.setColor(context.getResources().getColor(R.color.writeeeee));
				canvas.drawArc(oval, 191, 2, false, paint);  //根据进度画圆弧
				cosAngle=0;
				break;
			}
			case FILL:{

				paint.setStyle(Paint.Style.FILL_AND_STROKE);
				if(progress !=0)
					canvas.drawArc(oval, 0, 360 * progress / max, true, paint);  //根据进度画圆弧
				break;
			}
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		cosAngle=0;
		clickFlag++;
		// TODO Auto-generated method stub
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				Log.d("positonnnn", "Y:"+event.getY()+"--X:"+event.getX()+"clickFlag"+clickFlag);
				clickDistance=(int) Math.sqrt(Math.pow(event.getX()-centerX, 2)+Math.pow(event.getY()-centerY, 2));
				Log.d("执行","clickDistance:"+clickDistance+"--in_radius:"+in_radius);
				if(clickDistance<in_radius){
					clickDistance=0;
					cosAngle=0;
					cosDistance=0;
					clickAngle=0.0;
					click_X=0;
					click_Y=0;
					clickFlag=0;
					Log.d("执行","----------------");
					return super.onTouchEvent(event);
				}
				cosDistance=(int)Math.sqrt(Math.pow(event.getX()-centerX, 2)+Math.pow(event.getY()-0, 2));
				clickAngle=(double) ((centerY*centerY+clickDistance*clickDistance-cosDistance*cosDistance)*1.00/(2*centerY*clickDistance));
//				Toast.makeText(context, "点击长度："+clickDistance+"cos长度："+cosDistance+"中心点长度："+centerY+"cos角度："+clickAngle, Toast.LENGTH_LONG).show();
				double B=Math.acos(clickAngle);
				DecimalFormat df = new java.text.DecimalFormat("#0");
				B= Math.toDegrees(B);
				// 格式化数据，保留两位小数
				String temp = df.format(B);
				try{
					cosAngle=Integer.valueOf(temp).intValue();
				}catch (NumberFormatException exception){
					Log.d("NumberFormatException","类型转换错误");
				}

				click_X=0;
				click_X=event.getX();
				click_Y =event.getY();
				invalidate();

				break;
			case MotionEvent.ACTION_UP:

				break;
			case MotionEvent.ACTION_MOVE:
				Log.d("positonnnn", "Y:"+event.getY()+"--X:"+event.getX());
				break;

		}
		return super.onTouchEvent(event);
	}


	public synchronized int getMax() {
		return max;
	}

	/**
	 * 设置进度的最大值
	 * @param max
	 */
	public synchronized void setMax(int max) {
		if(max < 0){
			throw new IllegalArgumentException("max not less than 0");
		}
		this.max = max;
	}

	/**
	 * 获取进度.需要同步
	 * @return
	 */
	public synchronized float getProgress() {
		return progress;
	}

	/**
	 * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
	 * 刷新界面调用postInvalidate()能在非UI线程刷新
	 * @param progress
	 */
	public synchronized void setProgress(float progress,int textprocess) {
		if(progress < 0){
			throw new IllegalArgumentException("progress not less than 0");
		}
		if(progress > max){
			progress = max;
		}
		if(progress <= max){
			this.progress = progress;
			this.textprocess=textprocess;
			postInvalidate();
		}

	}
	private int textprocess;



	public int getCricleColor() {
		return roundColor;
	}

	public void setCricleColor(int cricleColor) {
		this.roundColor = cricleColor;
	}

	public int getCricleProgressColor() {
		return roundProgressColor;
	}

	public void setCricleProgressColor(int cricleProgressColor) {
		this.roundProgressColor = cricleProgressColor;
	}

	public int getTextColor() {
		return textColor;
	}

	public void setTextColor(int textColor) {
		this.textColor = textColor;
	}

	public float getTextSize() {
		return textSize;
	}

	public void setTextSize(float textSize) {
		this.textSize = textSize;
	}

	public float getRoundWidth() {
		return roundWidth;
	}

	public void setRoundWidth(float roundWidth) {
		this.roundWidth = roundWidth;
	}

	public  void getYdistance(float angle,Canvas canvas){

	}
	public interface ClickProjectSection{
		void clickSection(int flag);
	}
	private ClickProjectSection clickProjectSection;
	public void setClickProjectSection(ClickProjectSection clickProjectSection){
		this.clickProjectSection=clickProjectSection;
	}
	public void setInitClickFlag(){
		clickFlag=0;
	}
}
