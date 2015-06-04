package com.company.musicscore.util;

import com.example.musicscore.R;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

public class ScoreRuler extends View {

	private Paint mLinePaint;

	public String[] titles = new String[] { "极差", "很差", "差", "较差", "一般", "较好",
			"好", "很好", "极好" };

	/**
	 * @Fields titles_size:标题字体大小
	 */
	public float titles_Size = 30f;

	/**
	 * @Fields ruler_Targe:是否为间隔画线
	 */
	public boolean ruler_Targe = false;

	/**
	 * @Fields line_Offset:网格的左边偏移量
	 */
	public float line_Offset = 30;

	/**
	 * @Fields title_gravity:标题位置
	 */
	public String title_Gravity = "Left";

	/** The last scroll y. */
	private float lastX;
	private float lastY;
	/**
	 * @Fields scale_Mumx Description刻度数量
	 */
	private int scale_Nums = 25;
	/**
	 * @Fields scale_Num_Width Description:带数据刻度矩形的宽度
	 */
	private float scale_Num_Rect_Width = 3;
	/**
	 * @Fields scale_Max Description:刻度范围最大值
	 */
	private int value_Scale_Max = 100;
	/**
	 * @Fields scale_Min Description:刻度范围最小值
	 */
	private int value_Scale_Min = 0;
	/**
	 * @Fields screen_Width Description:当前屏幕宽度
	 */
	public int screen_Hight;

	public float getLastX() {
		return lastX;
	}

	public float getLastY() {
		return lastY;
	}

	public ScoreRuler(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public ScoreRuler(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context, attrs);
	}

	public ScoreRuler(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init(context, attrs);
	}

	private void init(Context context) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		screen_Hight = displayMetrics.heightPixels;
		mLinePaint = new Paint();
	}

	private void init(Context context, AttributeSet attrs) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) context).getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		screen_Hight = displayMetrics.heightPixels;
		mLinePaint = new Paint();
		TypedArray t = getContext().obtainStyledAttributes(attrs,
				R.styleable.ScoreRulerElement);
		try
		{
			value_Scale_Min = t.getInteger(
					R.styleable.ScoreRulerElement_value_Scale_Min, 0);
			value_Scale_Max = t.getInteger(
					R.styleable.ScoreRulerElement_value_Scale_Max, 100);
			ruler_Targe = t.getBoolean(R.styleable.ScoreRulerElement_ruler_Targe,
					false);
			String score_titles = t.getString(R.styleable.ScoreRulerElement_titles);
			if (score_titles != null && score_titles != "") {
				titles = score_titles.split("\\,");
			} else {
				titles = new String[] { "极差", "很差", "差", "较差", "一般", "较好", "好",
						"很好", "极好" };
			}
			titles_Size = t.getDimension(R.styleable.ScoreRulerElement_titles_size,
					30f);
			title_Gravity = t
					.getString(R.styleable.ScoreRulerElement_title_Gravity) != null ? t
							.getString(R.styleable.ScoreRulerElement_title_Gravity)
							: "Left";
							mLinePaint.setColor(t.getColor(
									R.styleable.ScoreRulerElement_titles_color, Color.BLACK));
		}finally
		{
			t.recycle();
		}
		mLinePaint.setTextAlign(Align.LEFT);
		mLinePaint.setTextSize(titles_Size);
	}

	/**
	 * 画间隔和不间隔线分开考虑
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		String title = "";
		int height = getHeight() - 5;
		int width = getWidth();
		scale_Num_Rect_Width = height / value_Scale_Max;// 一格的距离
		int localvalue = 0;
		int position = 0;
		int title_num = 0;
		int scale_Num = value_Scale_Max / scale_Nums;
		float line_Start_x = 0;
		float line_End_x = 0;
		float title_Start_x = 0;

		if (title_Gravity == "Left" || title_Gravity.equalsIgnoreCase("Left")) {
			title_Start_x = 0;
			line_Start_x = 2 * titles_Size;
			line_End_x = width;

		} else if (title_Gravity == "Right"
				|| title_Gravity.equalsIgnoreCase("Right")) {
			title_Start_x = width - titles_Size;
			line_Start_x = 0;
			line_End_x = width - titles_Size;

		}

		if (!ruler_Targe) {
			for (int i = value_Scale_Min * scale_Num; i <= value_Scale_Max; i++) {
				localvalue = i - value_Scale_Min;
				if (i % scale_Num == 0) {
					canvas.drawLine(line_Start_x, height - localvalue
							* scale_Num_Rect_Width, line_End_x, height
							- localvalue * scale_Num_Rect_Width, mLinePaint);
					if (i == 0) {
						position = i;
						title = titles[title_num];
						title_num += 1;
					} else if (i == 8) {
						position = i;
						title = titles[title_num];
						title_num += 1;
					} else if (i - position == 12) {
						position = i;
						title = titles[title_num];
						title_num += 1;
					}

					canvas.drawText(title, title_Start_x, height - position
							* scale_Num_Rect_Width, mLinePaint);
				}
			}
		} else {
			for (int i = value_Scale_Min; i <= value_Scale_Max; i++) {

				localvalue = i - value_Scale_Min;

				canvas.drawLine(line_Start_x, height - localvalue
						* scale_Num_Rect_Width, line_End_x, height - localvalue
						* scale_Num_Rect_Width, mLinePaint);
				if (title_num <= titles.length) {
					title = titles[title_num];
					title_num += 1;
				} else {
					title_num = titles.length - 1;
				}
				canvas.drawText(title, title_Start_x, height - localvalue
						* scale_Num_Rect_Width, mLinePaint);
			}
		}

	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
//		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width = widthSize;// calculateLayoutWidth(widthSize, widthMode);
		int height = heightSize;
		setMeasuredDimension(width, height);
	}
}
