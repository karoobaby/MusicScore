package com.company.musicscore;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;
import com.example.musicscore.R;
import com.google.gson.Gson;
import com.company.musicscore.db.DataSync;
import com.company.musicscore.model.ExamScore;
import com.company.musicscore.util.Ruler;
import com.company.musicscore.util.Ruler.OnRulerChangeListener;
import com.company.musicscore.util.Tools;
import com.company.musicscore.util.WebServiceHelper;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

public class MainFragment extends BaseFragment implements OnRulerChangeListener {
	private static final String PARAM = "param";
	public Myhandler myhandler;
	private View rootView;
	/**
	 * 初始化MainFragment
	 * @param param 传递的参数
	 * @return
	 */
	public static MainFragment newInstance(String param) {
		MainFragment fragment = new MainFragment();
		Bundle args = new Bundle();
		args.putString(PARAM, param);
		fragment.setArguments(args);
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	/**
	 * 加载布局
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		rootView = inflater.inflate(R.layout.fragment_main, container,
				false);
		bindAll(rootView);
		return rootView;
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		myhandler.removeCallbacks(ScoreRunnable);
		super.onDestroy();
	}
	/**
	 * 初始化基础信息
	 * @param rootView 当前fragment
	 */
	public void bindAll(View rootView)
	{
		
		serviceHelper=new WebServiceHelper();
		myhandler=new Myhandler(this);
		/**
		 * 初始化辅助线以及技术、乐感、质量、嗓音、难度滑块
		 */

		jishu_bar = (Ruler) rootView.findViewById(R.id.jishu_bar);
		yuegan_bar = (Ruler) rootView.findViewById(R.id.yuegan_bar);
		zhiliang_bar = (Ruler) rootView.findViewById(R.id.zhiliang_bar);
		sangyin_bar = (Ruler) rootView.findViewById(R.id.sangyin_bar);
		nand_bar = (Ruler)rootView.findViewById(R.id.nandu_bar);
		/**
		 * 初始化技术、乐感、质量、嗓音、难度分数
		 */
		sangyin_layout=(TableLayout)rootView.findViewById(R.id.sangyin_layout);
		jishu_txt = (TextView) rootView.findViewById(R.id.jishu_txt);
		yuegan_txt = (TextView) rootView.findViewById(R.id.yuegan_txt);
		zhiliang_txt = (TextView) rootView.findViewById(R.id.zhiliang_txt);
		sangyin_txt = (TextView) rootView.findViewById(R.id.sangyin_txt);
		nandu_txt = (TextView) rootView.findViewById(R.id.nandu_txt);
		jishu_txt.setGravity(Gravity.CENTER_HORIZONTAL);
		yuegan_txt.setGravity(Gravity.CENTER_HORIZONTAL);
		zhiliang_txt.setGravity(Gravity.CENTER_HORIZONTAL);
		sangyin_txt.setGravity(Gravity.CENTER_HORIZONTAL);
		nandu_txt.setGravity(Gravity.CENTER_HORIZONTAL);
		if(getArguments().getString(PARAM).equals(Tools.INDEX_TYPE_PLAYFORM))
		{
			setHidden(sangyin_bar, sangyin_layout);
		}
		
		jishu_bar.setOnRulerChangeListener(this);
		yuegan_bar.setOnRulerChangeListener(this);
		zhiliang_bar.setOnRulerChangeListener(this);
		sangyin_bar.setOnRulerChangeListener(this);
		nand_bar.setOnRulerChangeListener(this);
		
	}
	
	/**
	 * 提交考试成绩
	 */
	public Runnable ScoreRunnable=new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			Message msg=new Message();
			// TODO 进行打分：1.插入本地数据库；2.将数据传到服务器
			serviceParams=gson.toJson(examScore);
			serviceMap=new HashMap<String, String>();
			serviceMap.put("jsonData", serviceParams);
			serviceResult=serviceHelper.GetObject(WebServiceHelper.getServiceUrl(getActivity(), getResources().getString(R.string.general_ip)), WebServiceHelper.SERVICE_NAMESPACE, "SaveScore", serviceMap);
			try {
				if(serviceResult!=null)
				{
					JSONObject serviceJsonObj=new JSONObject(serviceResult.getProperty("SaveScoreResult").toString());
					
					if(DataSync.insertExamScore(getActivity(), examScore))
					{
						int tag=serviceJsonObj.getInt("Tag");
						if(tag==0)
							msg.what=Tools.HANDLER_NORMAL;
						else
							msg.what=Tools.SUBMIT_ERROR;
					}
					else
					{
						msg.what=Tools.UPDATE_STU_ERROR;
					}
				}
				else
				{
					msg.what=Tools.SOCKET_ERROR;
				}
			} catch (Exception e) {
				// TODO 自动生成的 catch 块
				msg.what=Tools.SUBMIT_ERROR;
			}
			progressDialog.dismiss();
			myhandler.sendMessage(msg);
		}
	};
	
	
	public static class Myhandler extends Handler{
		WeakReference<MainFragment> mFragment;
		
		
		Myhandler(MainFragment fragment)
		{
			mFragment=new WeakReference<MainFragment>(fragment);
		}
		Myhandler(Looper arg0,MainFragment fragment)
		{
			super(arg0);
			mFragment=new WeakReference<MainFragment>(fragment);
		}
		@Override
		public void handleMessage(Message msg)
		{
			MainFragment theFragment=mFragment.get();
			switch (msg.what) {
			case Tools.HANDLER_NORMAL:
				((MainActivity)theFragment.getActivity()).submitScore();
				break;
			case Tools.UPDATE_STU_ERROR:
				Tools.alert(theFragment.getActivity(), "无法写入本地数据库！");
				break;
			case Tools.SUBMIT_ERROR:
				Tools.alert(theFragment.getActivity(), "无法提交到服务器！");
				break;
			case Tools.SOCKET_ERROR:
				Tools.alert(theFragment.getActivity(), "与服务器失去连接！");
				break;
			default:
				break;
			}
		}
	}
	
}
