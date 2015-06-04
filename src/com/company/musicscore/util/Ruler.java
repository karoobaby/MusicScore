package com.company.musicscore.util;
import com.example.musicscore.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;


public class Ruler extends RelativeLayout implements OnSeekBarChangeListener {

	VerticalSeekBar vSeekBar;
	RulerView rulerScale;
	
	private int min=0;
	private int max=25;
	private String[] titles=new String[] { "极差", "很差", "差", "较差", "一般", "较好", "好",
			"很好", "极好" };
	private float titleSize=30f;
	private String titleGravity="Left";
	private boolean titleShow=true;
	private boolean rulerTarge=true;
	private int scaleNums=25;
	private float scale_Num_Rect_Height = 3;
	private float seekbar_layout_leftmargin=130f;
	
	
	public int getScaleNums() {
		return scaleNums;
	}
	public void setScaleNums(int scaleNums) {
		this.scaleNums = scaleNums;
	}
	/**
	 * @return rulerTarge
	 */
	public boolean isRulerTarge() {
		return rulerTarge;
	}
	/**
	 * @param rulerTarge 要设置的 rulerTarge
	 */
	public void setRulerTarge(boolean rulerTarge) {
		this.rulerTarge = rulerTarge;
	}
	public boolean isTitleShow() {
		return titleShow;
	}
	public void setTitleShow(boolean titleShow) {
		this.titleShow = titleShow;
	}

	LayoutParams params;
	
	/**
	 * 定义接口
	 * @author 禹彤
	 */
	public interface OnRulerChangeListener
	{
		public void onRulerChangeEvent(Ruler ruler,int progress);
	}
	/**
	 * 初始化接口变量
	 */
	OnRulerChangeListener mOnRulerChangeListener;
	/**
	 * 自定义控件的自定义事件
	 * @param mOnRulerChangeListener 接口类型
	 */
	public void setOnRulerChangeListener(OnRulerChangeListener mOnRulerChangeListener)
	{
		this.mOnRulerChangeListener=mOnRulerChangeListener;
	}
	public Ruler(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
	}

	public Ruler(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
		init(context, attrs);
	}

	public Ruler(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO 自动生成的构造函数存根
		init(context, attrs);
	}
	
	public Paint mLinePaint;
	
	private void init(Context context, AttributeSet attrs) {
		LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.rulerview_layout,this);
		setWillNotDraw(false);
		rulerScale=(RulerView)findViewById(R.id.rulerscale);
		vSeekBar=(VerticalSeekBar)findViewById(R.id.vSeekBar);
		params=new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT);
		TypedArray t = getContext().obtainStyledAttributes(attrs,
				R.styleable.ScoreRulerElement);
		try
		{
			min = t.getInteger(
					R.styleable.ScoreRulerElement_value_Scale_Min, 0);
			max = t.getInteger(
					R.styleable.ScoreRulerElement_value_Scale_Max, 25);
			scaleNums=t.getInteger(R.styleable.ScoreRulerElement_scale_Nums, 25);
			vSeekBar.setMax(max-1);
			String score_titles = t.getString(R.styleable.ScoreRulerElement_titles);
			if (score_titles != null && score_titles != "") {
				titles = score_titles.split("\\,");
			} else {
				titles = new String[] { "极差", "很差", "差", "较差", "一般", "较好", "好",
						"很好", "极好" };
			}
			titleSize = t.getDimension(R.styleable.ScoreRulerElement_titles_size,
					30f);
			titleGravity = t
					.getString(R.styleable.ScoreRulerElement_title_Gravity) != null ? t
							.getString(R.styleable.ScoreRulerElement_title_Gravity)
							: "Left";
							titleShow=t.getBoolean(R.styleable.ScoreRulerElement_title_Show, true);
							rulerTarge=t.getBoolean(R.styleable.ScoreRulerElement_ruler_Targe, true);
			seekbar_layout_leftmargin=t.getDimension(R.styleable.ScoreRulerElement_seekBar_layout_leftmargin, 100f);
			
		}finally
		{
			t.recycle();
		}
		if(titleShow==false)
		{
			rulerScale.setScreen_Width(200-(int)(titleSize));
			params.leftMargin=Tools.dip2px(context, seekbar_layout_leftmargin-40f);//70f
			params.topMargin=Tools.dip2px(context, 5.0f);
			vSeekBar.setLayoutParams(params);
		}
		else
		{
			params.leftMargin=Tools.dip2px(context, seekbar_layout_leftmargin);//110f
			params.topMargin=Tools.dip2px(context, 5.0f);
			vSeekBar.setLayoutParams(params);
		}
		rulerScale.setValue_Scale_Min(min);
		rulerScale.setValue_Scale_Max(max);
		rulerScale.setTitles(titles);
		rulerScale.setTitles_Size(titleSize);
		rulerScale.setTitle_Gravity(titleGravity);
		rulerScale.setTitleShow(titleShow);
		rulerScale.setRuler_Targe(rulerTarge);
		rulerScale.setScale_Nums(scaleNums);
		vSeekBar.setOnSeekBarChangeListener(this);
		if(titleGravity=="Right")
		{
			
		}
		mLinePaint = new Paint();
	}
	

	public int getMin() {
		return min;
	}
	public void setMin(int min) {
		this.min = min;
	}
	public int getMax() {
		return max;
	}
	public void setMax(int max) {
		this.max = max;
	}
	public String[] getTitles() {
		return titles;
	}
	public void setTitles(String[] titles) {
		this.titles = titles;
	}
	public float getTitleSize() {
		return titleSize;
	}
	public void setTitleSize(float titleSize) {
		this.titleSize = titleSize;
	}
	public String getTitleGravity() {
		return titleGravity;
	}
	public void setTitleGravity(String titleGravity) {
		this.titleGravity = titleGravity;
	}
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) {
		// TODO 自动生成的方法存根
		if(seekBar.getId()==R.id.vSeekBar)
		{
			rulerScale.setFinger(progress);
			rulerScale.invalidate();
			if(max==9)
				mOnRulerChangeListener.onRulerChangeEvent(this,progress);
			else
				mOnRulerChangeListener.onRulerChangeEvent(this,progress+1);
			
		}
		
	}
	
	
	@Override  
    protected void onDraw(Canvas canvas) { 
		super.onDraw(canvas);
		
		int height=rulerScale.getHeight();
		int position = 0;
		scale_Num_Rect_Height = height / scaleNums;// 一格的距离
		
		
//		Toast.makeText(getContext(), String.valueOf(height), Toast.LENGTH_SHORT).show();
		for (int i = min; i < max; i++) {
			if(rulerTarge)
			{
				if (i == 0) {
					position = i;
				} else if (i == 2) {
					position = i;
				} else if (i - position == 3) {
					position = i;
				}
			}
			else
			{
				position=i;
			}
			mLinePaint.setColor(getResources().getColor(R.color.black));
			canvas.drawLine(0, 
					height - position* scale_Num_Rect_Height+5, 
					getWidth(), 
					height - position* scale_Num_Rect_Height+5, mLinePaint);
		}
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO 自动生成的方法存根
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO 自动生成的方法存根
		
	}
	
	
}
