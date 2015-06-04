package com.company.musicscore.util;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {

	private boolean isCanScroll = false;  
	public MyViewPager(Context context) {
		super(context);
		// TODO 自动生成的构造函数存根
	}

	public MyViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO 自动生成的构造函数存根
	}
	@Override
	public boolean onTouchEvent(MotionEvent arg0)
	{
		if(isCanScroll)
			return super.onTouchEvent(arg0);
		else
			return false;
	}

}
