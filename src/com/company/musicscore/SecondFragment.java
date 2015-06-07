package com.company.musicscore;

import com.company.musicscore.util.Ruler.OnRulerChangeListener;
import com.example.musicscore.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.NumberPicker.OnValueChangeListener;

public class SecondFragment extends BaseFragment implements OnRulerChangeListener,OnValueChangeListener {
	// TODO: Rename parameter arguments, choose names that match
	private static final String ARG_PARAM = "param";
	private View rootView;
	private NumberPicker yuegan_picker;
	private TextView yuegan_value;
	/**
	 * 初始化
	 * @param param
	 * @return
	 */
	public static SecondFragment newInstance(String param) {
		SecondFragment fragment = new SecondFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM, param);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		rootView=inflater.inflate(R.layout.fragment_second, container, false);
		bindAll(rootView);
		yuegan_picker=(NumberPicker)rootView.findViewById(R.id.yuegan_picker);
		yuegan_picker.setOnValueChangedListener(this);
		yuegan_value=(TextView)rootView.findViewById(R.id.yuegan_value);
		yuegan_picker.setMinValue(0);
		yuegan_picker.setMaxValue(9);
		yuegan_picker.setValue(5);
		return rootView;
	}
	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		// TODO Auto-generated method stub
		yuegan_value.setText("."+String.valueOf(newVal));
	}
	
	
	
	
}
