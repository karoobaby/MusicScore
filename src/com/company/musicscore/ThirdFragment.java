package com.company.musicscore;

import com.example.musicscore.R;

import android.os.Bundle;
import android.sax.RootElement;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.NumberPicker.Formatter;
import android.widget.NumberPicker.OnValueChangeListener;

public class ThirdFragment extends BaseFragment implements OnValueChangeListener,Formatter {
	private static final String ARG_PARAM = "param";
	private NumberPicker yuegan_picker;
	private TextView yuegan_value;
	private View rootView;
	public static ThirdFragment newInstance(String param) {
		ThirdFragment fragment = new ThirdFragment();
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
		rootView=inflater.inflate(R.layout.fragment_third, container, false);
		bindAll(rootView);
		yuegan_picker=(NumberPicker)rootView.findViewById(R.id.yuegan_picker);
		yuegan_value=(TextView)rootView.findViewById(R.id.yuegan_value);
		yuegan_picker.setOnValueChangedListener(this);
		yuegan_picker.setMinValue(0);
		yuegan_picker.setMaxValue(99);
		yuegan_picker.setFormatter(this);
		yuegan_picker.setValue(50);
		return rootView;
	}

	@Override
	public String format(int value) {
		String tmpStr = String.valueOf(value);
        if (value < 10) {
            tmpStr = "0" + tmpStr;
        }
        return tmpStr;
	}

	@Override
	public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
		// TODO Auto-generated method stub
		yuegan_value.setText("."+format(newVal));
	}
}
