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
	private TextView jishu_txt;//技术分数
	private TextView yuegan_txt;//乐感分数
	private TextView zhiliang_txt;//质量分数
	private TextView sangyin_txt;//嗓音分数
	private TextView nandu_txt;//难度分数
	private Ruler jishu_bar;//技术滑块
	private Ruler yuegan_bar;//乐感滑块
	private Ruler zhiliang_bar;//质量滑块
	private Ruler sangyin_bar;//嗓音滑块
	private Ruler nand_bar;//难度滑块
	private TableLayout sangyin_layout;//嗓音模块
	private WebServiceHelper serviceHelper;//调用WebService;
	private String serviceParams;//服务参数;
	private SoapObject serviceResult;//服务状态;
	private Map<String, String> serviceMap;//参数
	public boolean serviceStatus=false;//返回状态
	private Gson gson=new Gson();
	public Myhandler myhandler;
	private View rootView;
	public ProgressDialog progressDialog = null;
	private ExamScore examScore;
	public ExamScore getExamScore() {
		return examScore;
	}

	/**
	 * 给ExamScore赋值
	 * @param examScore
	 */
	public void setExamScore(ExamScore examScore) {
		this.examScore=new ExamScore();
		this.examScore.setJudgeid(examScore.getJudgeid());
		this.examScore.setStudentid(examScore.getStudentid());
		this.examScore.setWorksname(examScore.getWorksname());
		this.examScore.setTechablity(jishu_txt.getText().toString());
		this.examScore.setMusicperform(yuegan_txt.getText().toString());
		if(sangyin_layout.getVisibility()!=View.GONE)
		{
			this.examScore.setVoicecondition(sangyin_txt.getText().toString());
		}
		else
		{
			this.examScore.setVoicecondition("0");
		}
		this.examScore.setExecution(zhiliang_txt.getText().toString());
		this.examScore.setDifficulity(nandu_txt.getText().toString());
		this.examScore.setCreatedate(Tools.getDateTimeStr());
		this.examScore.setDeviceno(Tools.getDeviceNo(this.getActivity(), getResources().getString(R.string.general_device)));
	}
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
	 * 检查技术、乐感、质量、嗓音分数是否为1
	 * @return
	 */
	public String submitCheck()
	{
		String message="";
		if(jishu_txt.getText()=="1"||jishu_txt.getText().equals("1"))
		{
			message+="技术,";
		}
		if(yuegan_txt.getText()=="1"||yuegan_txt.getText().equals("1"))
		{
			message+="乐感,";
		}
		if(zhiliang_txt.getText()=="1"||zhiliang_txt.getText().equals("1"))
		{
			message+="质量,";
		}
		if(sangyin_layout.getVisibility()!=View.GONE)
		{
			if(sangyin_txt.getText()=="1"||sangyin_txt.getText().equals("1"))
			{
				message+="嗓音,";
			}
		}
		if(message!="")
		{
			message=message.substring(0, message.length()-1);
		}
		return message;
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
	 * 是否显示功能
	 * @param ruler 进度条
	 * @param layout 顶部布局
	 */
	public void setHidden(Ruler ruler,TableLayout layout)
	{
		ruler.setVisibility(View.GONE);
		layout.setVisibility(View.GONE);
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
	/**
	 * 打分方法
	 */
	@Override
	public void onRulerChangeEvent(Ruler ruler, int progress) {
		// TODO 通过滑块进行打分
		switch (ruler.getId()) {
		case R.id.jishu_bar:
			jishu_txt.setText(String.valueOf(progress));
			break;
		case R.id.yuegan_bar:
			yuegan_txt.setText(String.valueOf(progress));
			break;
		case R.id.zhiliang_bar:
			zhiliang_txt.setText(String.valueOf(progress));
			break;
		case R.id.sangyin_bar:
			sangyin_txt.setText(String.valueOf(progress));
			break;
		case R.id.nandu_bar:
			nandu_txt.setText(String.valueOf(progress + 1));
			break;
		default:
			break;
		}
	}
}
