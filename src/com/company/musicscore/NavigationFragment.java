package com.company.musicscore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.company.musicscore.NavigationDrawerFragment.OnScoreButtonClickedListener;
import com.company.musicscore.model.ExamScore;
import com.company.musicscore.util.Tools;
import com.example.musicscore.R;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class NavigationFragment extends Fragment implements OnClickListener {

	
	 private View rootView;
	 public ListView msg_list;  
	 public ListView msg_list1;
	 public ImageView img_xuanshou;
	 public Button submit;
	 ExamScore esModel;

	 OnScoreButtonClickedListener mCallback;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
		msg_list=(ListView)rootView.findViewById(R.id.msg_list);
    	msg_list1=(ListView)rootView.findViewById(R.id.msg_list1);
    	img_xuanshou=(ImageView)rootView.findViewById(R.id.img_xuanshou);
    	img_xuanshou.setImageResource(R.drawable.anshi);
    	submit=(Button)rootView.findViewById(R.id.score_submit);
    	submit.setOnClickListener(this);
    	bindList(msg_list, Tools.ITEM_TITLES_XUANSHOU_DEFAULT, Tools.ITEM_VALUES_XUANSHOU_DEFAULT);
    	bindList(msg_list1, Tools.ITEM_TITLES_PINGWEI_DEFAULT, Tools.ITEM_VALUES_PINGWEI_DEFAULT);
		return rootView;
	}
	
	public void bindList(ListView list,String[] item_titles,String[] item_values)
    {
    	List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
    	for (int i = 0; i < item_titles.length; i++) {
			Map<String,Object> listItem = new HashMap<String, Object>();
			listItem.put("item_title", item_titles[i]);
			listItem.put("item_value", item_values[i]);
			listItems.add(listItem);
		}
    	SimpleAdapter simpleAdapter=new SimpleAdapter(
    			this.getActivity(), 
    			listItems, 
    			R.layout.simple_item, 
    			new String[]{"item_title","item_value"}, 
    			new int[]{R.id.item_title,R.id.item_value});
    	list.setAdapter(simpleAdapter);
    }
	
	@Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
    	try {
    		mCallback=(OnScoreButtonClickedListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+" must implement OnScoreButtonClickedListener");
		}
    }
	
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		
		if(v.getId()==R.id.score_submit)
		{
			
			esModel=new ExamScore();
			esModel.setJudgeid("1");
			
//			Toast.makeText(this.getActivity(), esModel.getJudgeid(), Toast.LENGTH_LONG).show();
			mCallback.onScoreButtonClick(esModel);
		}
	}
	
	
	

}
