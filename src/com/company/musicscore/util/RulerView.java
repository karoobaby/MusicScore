package com.company.musicscore.util;
import com.example.musicscore.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;


public class RulerView extends View {
	
	/**
	 * 标题列表
	 */
	private String[] titles = new String[] { "极差", "很差", "差", "较差", "一般", "较好",
			"好", "很好", "极好" };
	/**
	 * @Fields titles_size:标题字体大小
	 */
	public float titles_Size = 30f;
	public Paint mLinePaint;
	/**
	 * @Fields screen_Height Description:当前屏幕高度
	 */
	public int screen_Height;
	
	private boolean titleShow=true;
	
	public boolean isTitleShow() {
		return titleShow;
	}

	public void setTitleShow(boolean titleShow) {
		this.titleShow = titleShow;
	}

	/**
	 * 控制当前控件宽度
	 */
	public int screen_Width=200;
	
	/**
	 * @Fields scale_Num_Height Description:带数据刻度矩形的高度
	 */
	private float scale_Num_Rect_Height = 3;
	/**
	 * @Fields scale_Max Description:刻度范围最大值
	 */
	private int value_Scale_Max = 25;
	public String[] getTitles() {
		return titles;
	}

	public void setTitles(String[] titles) {
		this.titles = titles;
	}

	public float getTitles_Size() {
		return titles_Size;
	}

	public void setTitles_Size(float titles_Size) {
		this.titles_Size = titles_Size;
	}

	public int getScreen_Width() {
		return screen_Width;
	}

	public void setScreen_Width(int screen_Width) {
		this.screen_Width = screen_Width;
	}

	public int getValue_Scale_Max() {
		return value_Scale_Max;
	}

	public void setValue_Scale_Max(int value_Scale_Max) {
		this.value_Scale_Max = value_Scale_Max;
	}

	public int getValue_Scale_Min() {
		return value_Scale_Min;
	}

	public void setValue_Scale_Min(int value_Scale_Min) {
		this.value_Scale_Min = value_Scale_Min;
	}

	/**
	 * @Fields scale_Min Description:刻度范围最小值
	 */
	private int value_Scale_Min = 0;
	/**
	 * @Fields scale_Mumx Description刻度数量
	 */
	private int scale_Nums = 25;
	/**
	 * @Fields rect_Start_x Description矩形开始x坐标
	 */
	private int rect_Start = 0;
	 
	/**
	 * @Fields rect_End_x Description矩形结束x坐标
	 */
	private int rect_End=110;
	/**
	 * @Fields title_gravity:标题位置
	 */
	private String title_Gravity = "Left";
	
	/**
	 * 控制当前滑动位置显示
	 */
	private int finger=0;
	
	/**
	 * @return the finger
	 */
	public int getFinger() {
		return finger;
	}

	/**
	 * @param finger the finger to set
	 */
	public void setFinger(int finger) {
		this.finger = finger;
	}


	/**
	 * @return the title_Gravity
	 */
	public String getTitle_Gravity() {
		return title_Gravity;
	}

	/**
	 * @param title_Gravity the title_Gravity to set
	 */
	public void setTitle_Gravity(String title_Gravity) {
		this.title_Gravity = title_Gravity;
	}

	/**
	 * @return the scale_Nums
	 */
	public int getScale_Nums() {
		return scale_Nums;
	}

	/**
	 * @param scale_Nums the scale_Nums to set
	 */
	public void setScale_Nums(int scale_Nums) {
		this.scale_Nums = scale_Nums;
	}
	
	/**
	 * 画线方式
	 */
	private boolean ruler_Targe=true;
	

	/**
	 * @return ruler_Targe
	 */
	public boolean isRuler_Targe() {
		return ruler_Targe;
	}

	/**
	 * @param ruler_Targe 要设置的 ruler_Targe
	 */
	public void setRuler_Targe(boolean ruler_Targe) {
		this.ruler_Targe = ruler_Targe;
	}

	public RulerView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public RulerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context, attrs);
	}

	public RulerView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context, attrs);
	}
	
	private void init(Context context) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		screen_Height = displayMetrics.heightPixels;
		mLinePaint = new Paint();
	}
	
	
	
	private void init(Context context, AttributeSet attrs) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		setWillNotDraw(false);
		screen_Height = displayMetrics.heightPixels;
		mLinePaint = new Paint();
		mLinePaint.setColor(Color.RED);  
		mLinePaint.setTextAlign(Align.LEFT);
		
	}
	@Override  
    protected void onDraw(Canvas canvas) { 
		
		super.onDraw(canvas);
		mLinePaint.setTextSize(titles_Size);
		String title = "";
		int title_num = 0;
		int localvalue = 0;
		int position = 0;
		int height = getHeight();
		int width = getWidth();
		float title_Start_x = 0;
		float title_Height=0;
		int rect_Start_x=0;
		int rect_End_x=0;
		rect_Start=0;
		scale_Num_Rect_Height = height / value_Scale_Max;// 一格的距离
		if(titleShow)
		{
			if (getTitle_Gravity() == "Left" || getTitle_Gravity().equalsIgnoreCase("Left")) {
				title_Start_x = 0;
				rect_Start = rect_Start+2 * (int)titles_Size;
			} else if (getTitle_Gravity() == "Right"|| getTitle_Gravity().equalsIgnoreCase("Right")) {
				title_Start_x = width - titles_Size;
				rect_Start = 0;
			}
		}

		for (int i = value_Scale_Min; i < value_Scale_Max; i++) {
			localvalue = i - value_Scale_Min;
			Rect rect=new Rect();//画矩形

			if(getTitle_Gravity() == "Left" || getTitle_Gravity().equalsIgnoreCase("Left"))
			{
				rect_Start_x=rect_Start+(value_Scale_Max-i)*((rect_End-10)/getScale_Nums());
				rect_End_x=rect_Start+rect_End;
			}
			else
			{
				rect_Start_x=rect_Start;
				rect_End_x=rect_End-(value_Scale_Max-i)*((rect_End-20)/getScale_Nums());
			}
			mLinePaint.setColor(getResources().getColor(R.color.gray));
			if(i==getFinger())
			{
				mLinePaint.setColor(getResources().getColor(R.color.green));
			}
			rect.set(rect_Start_x, (int)(height-localvalue*scale_Num_Rect_Height-scale_Num_Rect_Height+10), rect_End_x, (int)(height-localvalue*scale_Num_Rect_Height));
			canvas.drawRect(rect, mLinePaint);  
			if(titleShow)
			{
				if(ruler_Targe)
				{
					if (i == 0) {
						position = i;
						title = titles[title_num];
						title_num += 1;
						title_Height=height-scale_Num_Rect_Height+titles_Size/2;
					} else if (i == 2) {
						position = i;
						title = titles[title_num];
						title_num += 1;
						title_Height=height-position*scale_Num_Rect_Height-3*scale_Num_Rect_Height/2+titles_Size/2;
					} else if (i - position == 3) {
						position = i;
						title = titles[title_num];
						title_num += 1;
						title_Height=height-position*scale_Num_Rect_Height-3*scale_Num_Rect_Height/2+titles_Size/2;
					}
					if(position==23)
					{
						title_Height=height-position*scale_Num_Rect_Height-scale_Num_Rect_Height+titles_Size/2;
					}
				}
				else
				{
					position=i;
					title=titles[title_num];
					title_num+=1;
					title_Height=height-position*scale_Num_Rect_Height-scale_Num_Rect_Height/2+titles_Size/2;
				}
				mLinePaint.setColor(getResources().getColor(R.color.black));
				canvas.drawText(title, title_Start_x, title_Height, mLinePaint);
				canvas.drawLine(title_Start_x, 
						height - position* scale_Num_Rect_Height+5, 
						titles_Size*2, 
						height - position* scale_Num_Rect_Height+5, mLinePaint);
			}
		}
		
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width = widthSize;// calculateLayoutWidth(widthSize, widthMode);
		int height = heightSize;
		if (widthMode == MeasureSpec.EXACTLY) {	  
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            width = screen_Width;
        }
  
        if (heightMode == MeasureSpec.EXACTLY) {
  
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
        	height = heightSize;
        }
		setMeasuredDimension(width, height);
	}
}
