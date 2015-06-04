package com.company.musicscore;

import com.example.musicscore.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SecondFragment extends BaseFragment {
	// TODO: Rename parameter arguments, choose names that match
	private static final String ARG_PARAM = "param";

	// TODO: Rename and change types of parameters
	private String mParam;
	/**
	 * 初始化
	 * @param param
	 * @return
	 */
	// TODO: Rename and change types and number of parameters
	public static SecondFragment newInstance(String param) {
		SecondFragment fragment = new SecondFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM, param);
		fragment.setArguments(args);
		return fragment;
	}

	public SecondFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam = getArguments().getString(ARG_PARAM);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_second, container, false);
	}
}
