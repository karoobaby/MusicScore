package com.company.musicscore;

import java.lang.ref.WeakReference;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;

import com.company.musicscore.MainFragment.Myhandler;
import com.company.musicscore.model.ExamScore;
import com.company.musicscore.util.Ruler;
import com.company.musicscore.util.WebServiceHelper;
import com.company.musicscore.util.Ruler.OnRulerChangeListener;
import com.company.musicscore.util.Tools;
import com.example.musicscore.R;
import com.google.gson.Gson;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

public class BaseFragment extends Fragment implements OnRulerChangeListener {
	private static final String PARAM= "param";
	public TextView jishu_txt;//技术分数
	public TextView yuegan_txt;//乐感分数
	public TextView zhiliang_txt;//质量分数
	public TextView sangyin_txt;//嗓音分数
	public TextView nandu_txt;//难度分数
	public Ruler jishu_bar;//技术滑块
	public Ruler yuegan_bar;//乐感滑块
	public Ruler zhiliang_bar;//质量滑块
	public Ruler sangyin_bar;//嗓音滑块
	public Ruler nand_bar;//难度滑块
	public TableLayout sangyin_layout;//嗓音模块
	public WebServiceHelper serviceHelper;//调用WebService;
	public String serviceParams;//服务参数;
	public SoapObject serviceResult;//服务状态;
	public Map<String, String> serviceMap;//参数
	public boolean serviceStatus=false;//返回状态
	public Gson gson=new Gson();
	public Myhandler myhandler;
	private View rootView;
	public ProgressDialog progressDialog = null;
	public ExamScore examScore;
	public ExamScore getExamScore() {
		return examScore;
	}
	
	public static BaseFragment newInstance(String param) {
		BaseFragment fragment = new BaseFragment();
		Bundle args = new Bundle();
		args.putString(PARAM, param);
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
		return inflater.inflate(R.layout.fragment_base, container, false);
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
	
	public Runnable ScoreRunnable=new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			
		}
	};
	/**
	 * 初始化基础信息
	 * @param rootView 当前fragment
	 */
	public void bindAll(View rootView)
	{
		serviceHelper=new WebServiceHelper();
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
