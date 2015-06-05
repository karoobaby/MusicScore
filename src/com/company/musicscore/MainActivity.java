package com.company.musicscore;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONException;
import org.json.JSONObject;

import com.company.musicscore.NavigationDrawerFragment.OnScoreButtonClickedListener;
import com.company.musicscore.db.DatabaseHelper;
import com.company.musicscore.model.DeviceSet;
import com.company.musicscore.model.ExamScore;
import com.company.musicscore.model.Player;
import com.company.musicscore.util.MyFragmentPagerAdapter;
import com.company.musicscore.util.MyViewPager;
import com.company.musicscore.util.Tools;
import com.example.musicscore.R;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar.OnNavigationListener;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
public class MainActivity extends ActionBarActivity implements OnScoreButtonClickedListener,OnNavigationListener {
	private NavigationFragment mNavigationFragment;
	private NavigationDrawerFragment mNavigationDrawerFragment;
	private List<Fragment> listFragment;
	private ViewPager mPager;
	private BaseFragment mMainFragment;//当前Fragment;
	private MainFragment mFirstFragment;//第一轮Fragment;
	private SecondFragment mSencondFragment;//第二轮Fragment;
	private ThirdFragment mThirdFragment;//第三轮Fragment;
	private DatagramSocket sUdp = null;// 发送数据Socket
	private DatagramSocket rUdp = null;// 接受数据Socket
	private DatagramPacket sPacket = null;
	private DatagramPacket rPacket = null;
	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;
	private int sid=0;
	private byte[] sBuffer = null;// 发送数据缓存
	private String sRecvData;
	private int sidTemp = 0;
	private String musicNameTemp="";
	private String[] item_values_xuanshou;//左侧导航选手信息
	private String[] item_values_pingwei;//左侧评委信息
	String[] type={"初赛","复赛","决赛"};//比赛轮次
	private Player player;//选手信息
	private DeviceSet deviceSet;//评委和配置信息
	Myhandler myHandler;
	ActionBar actionBar;
	private Cursor cursor;
	private Builder aDialog;
	private Bitmap bitmap;
	private boolean isThstart=true;
	List<ExamScore> esList;
	FrameLayout contain;
	private String musicName="";
	public static final int FLAG_HOMEKEY_DISPATCHED = 0x80000000;
	private SharedPreferences dataCache;
	private SharedPreferences.Editor editor;
	DownImgAsyncTask downImgAsyncTask;
	private ExecutorService scoreThreadExecutor;//获取数据线程池
	private ExecutorService dataThreadExeutor;//打分线程池
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		int flag=WindowManager.LayoutParams.FLAG_FULLSCREEN;
		
//		this.getWindow().setFlags(flag, flag);
		 this.getWindow().setFlags(FLAG_HOMEKEY_DISPATCHED, FLAG_HOMEKEY_DISPATCHED);
		// setContentView(R.layout.activity_main_large);
		setContentView(R.layout.activity_main_large);
		restoreActionBar();
		
		bindActionBarNavigation(actionBar,type,this);
		mNavigationFragment=(NavigationFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer_large);
		mPager=(MyViewPager)findViewById(R.id.mViewPager_large);
		
		//mNavigationFragment=(NavigationFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_drawer_large);
//		contain=(FrameLayout)findViewById(R.id.fragment_container);
		
		aDialog=new AlertDialog.Builder(this);
		myHandler=new Myhandler(this);
		dataCache=getSharedPreferences("cache", Activity.MODE_PRIVATE);
		editor=dataCache.edit();
		sidTemp=dataCache.getInt("sid", 0);
		musicNameTemp=dataCache.getString("musicName", "");
		dataThreadExeutor=Executors.newSingleThreadExecutor();
		
		scoreThreadExecutor=Executors.newSingleThreadExecutor();
		iniUdp();//初始化udp工具
		iniDb();//初始化数据库访问
		getInfo();//开启线程获取选手Id
		searchData();//通过选手Id在本地数据库查找选手信息
		bindAll();//绑定主界面
		bindPingwei();//绑定评委信息
		
	}
	/**
	 * 初始化UDP
	 */
	public void iniUdp()
	{
		byte[] rBuffer = new byte[1024];// 接收数据缓存1024字节
		
			try {
				if (sUdp == null)
					sUdp = new DatagramSocket();
				if (rUdp == null)
				{
					rUdp = new DatagramSocket(null);
					rUdp.setReuseAddress(true);
					rUdp.bind(new InetSocketAddress(Tools.getLocalPort(this, getResources().getString(R.string.general_port))));
				}
				if (rPacket == null)
					rPacket = new DatagramPacket(rBuffer, rBuffer.length);
			} catch (SocketException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
	}
	/**
	 * 初始化数据库
	 */
	public void iniDb()
	{
		if(dbHelper==null)
			dbHelper = new DatabaseHelper(this);
		if(db==null)
			db = dbHelper.getReadableDatabase();
		
	}
	
	public static class Myhandler extends Handler{
		WeakReference<MainActivity> mActivity;
		
		Myhandler(MainActivity activity)
		{
			mActivity=new WeakReference<MainActivity>(activity);
		}
		@Override
		public void handleMessage(Message msg)
		{
			MainActivity theActivity=mActivity.get();
			switch (msg.what) {
			case Tools.HANDLER_NORMAL:
				theActivity.searchData();
				theActivity.bindAll();
				break;
			case Tools.SEARCH_STU_ERROR:
				Tools.alert(theActivity, "没有找到该考生！");
				break;
			case Tools.UPDATE_STU_ERROR:
				Tools.alert(theActivity, "无法更新考生信息！");
				break;
			case Tools.SOCKET_ERROR:
				Tools.alert(theActivity, "无法找到服务器！");
				break;
			case Tools.UNKNOWHOST_ERROR:
				Tools.alert(theActivity, "无法连接服务器！");
				break;
			case Tools.IO_ERROR:
				Tools.alert(theActivity, "无法接收消息！");
				break;
			case Tools.JSON_ERROR:
				Tools.alert(theActivity, "消息格式错误！");
				break;
			default:
				break;
			}
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_HOME)
		{
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
//	@Override
//	public void onAttachedToWindow() {
//	        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);
//	        super.onAttachedToWindow();
//	    }

	/**
	 * 开始获取学生和评委信息
	 */
	public void getInfo() {
		
		dataThreadExeutor.execute(dataRunnable);
	}
	
	private Runnable dataRunnable=new Runnable() {
		
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			while (isThstart) {
				try {
					recvData();
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
		}
	};
	/**
	 * 绑定顶部导航
	 * @param adapterStrs 绑定的内容
	 * @param arg1            选择触发事件
	 */
	public void bindActionBarNavigation(ActionBar actionBar,
			String[] adapterStrs, OnNavigationListener arg1) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				actionBar.getThemedContext(),
				android.R.layout.simple_list_item_1, android.R.id.text1,
				adapterStrs);
		actionBar.setListNavigationCallbacks(adapter, arg1);
	}

	/**
	 * 绑定导航
	 * @param musicList 歌曲列表
	 */
	public void bindAll() {
		if(player!=null){
			actionBar.setTitle(player.getCname()+" "+musicNameTemp);
		}
		else{
			actionBar.setTitle(musicNameTemp);
		}
		item_values_xuanshou = new String[] { 
				player.getCname(),
				player.getZyfclass(), 
				player.getZysclass(),
				String.valueOf(player.getId()) };
		bindValue(item_values_xuanshou);
		InitViewPager(mPager);
	}
	/**
	 * 绑定左侧、导航、内容页
	 * @param item_List 选手信息
	 * @param item_Navigation 导航信息
	 */
	public void bindValue(String[] item_List)
	{
	
		if(player!=null&&player.getPhotopath()!=null&&player.getPhotopath().equals("")==false)
		{
			downImgAsyncTask=new DownImgAsyncTask();
			downImgAsyncTask.execute("http://"+Tools.getRemoteIp(this, "general_ip")+"/musicscoresys/playerphoto/"+player.getPhotopath());
		}
		else
		{
			bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.anshi);
			mNavigationFragment.img_xuanshou.setImageBitmap(bitmap);
		}
		mNavigationFragment.bindList(mNavigationFragment.msg_list,Tools.ITEM_TITLES_XUANSHOU_DEFAULT, item_List);
		mNavigationFragment.submit.setText("提交分数");
		mNavigationFragment.submit.setBackgroundResource(R.drawable.bg_submit);
		//mMainFragment=MainFragment.newInstance(player.getIndexType());
		//getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, mMainFragment).commitAllowingStateLoss();
	}	
	
	/**
	 * 绑定内容页
	 * @param viewPager
	 */
	public void InitViewPager(ViewPager viewPager)
	{
		listFragment=new ArrayList<Fragment>();
		 mFirstFragment=MainFragment.newInstance("0");
		 mSencondFragment=SecondFragment.newInstance("1");
		 mThirdFragment=ThirdFragment.newInstance("2");
		 listFragment.add(mFirstFragment);
		 listFragment.add(mSencondFragment);
		 listFragment.add(mThirdFragment);
		 MyFragmentPagerAdapter adapter=new MyFragmentPagerAdapter(this.getSupportFragmentManager(), viewPager, listFragment);
		 viewPager.setCurrentItem(0);
	}
	/**
	 * 获取网络图片
	 * @author 禹彤
	 */
	class DownImgAsyncTask extends AsyncTask<String, Void, Bitmap>
	{
		@Override
		protected Bitmap doInBackground(String... params) {
			// TODO 自动生成的方法存根
			String param=params[0];
			Bitmap b=Tools.getHttpBitmap(param);
			return b;
		}
		@Override
		protected void onPostExecute(Bitmap result)
		{
			super.onPostExecute(result);
			if(result!=null)
			{
				mNavigationFragment.img_xuanshou.setImageBitmap(result);
			}
		}
		
		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}
	}
	
	/**
	 * 绑定评委信息
	 */
	public void bindPingwei()
	{
		cursor=db.rawQuery("select * from TB_DEVICESET order by _id asc limit 1", null);//查询正在打分的记录
		if(cursor.moveToFirst())
		{
			this.deviceSet=queryDeviceSet(cursor);
			item_values_pingwei=new String[]{
					deviceSet.getJname(),
					deviceSet.getWorkdept(),
					player.getZyfclass()
			};
		}
		cursor.close();
		mNavigationFragment.bindList(mNavigationFragment.msg_list1, Tools.ITEM_TITLES_PINGWEI_DEFAULT, item_values_pingwei	);
	}
	
	/**
	 * 接收选手信息
	 */
	public void recvData() {
		Message msg = new Message();
		try {
			
//			if(sidTemp!=0&&musicNameTemp.equals("")==false)//判断全局变量是否为0，如果不为0则通过udp查询选手id
//			{
				if(rUdp==null)
				{
					iniUdp();
				}
				rUdp.receive(rPacket);
				// sRecvData = recvHexData(rPacket.getLength());
				sRecvData = new String(rPacket.getData(), 0,rPacket.getData().length, "utf-8").trim();
				if (sRecvData != null && sRecvData != "") {
					JSONObject rObj = new JSONObject(sRecvData);
					sid = rObj.getInt("Sid");
					musicName=rObj.getString("MusicName");
					/**
					 * 获得选手Id后，发送设备信息、获取时间和选手Id到服务端
					 */
					JSONObject sObj = new JSONObject();
					sObj.put("Sid", sid);
					sObj.put("SendTime",Tools.getDateTimeStr());
					sObj.put("HostPort", Tools.getDeviceNo(this, getResources().getString(R.string.general_device)));
					String sendStr = sObj.toString();
					sBuffer = sendStr.getBytes();
					sPacket = new DatagramPacket(sBuffer, sBuffer.length,
							InetAddress.getByName(Tools.getRemoteIp(this, getResources().getString(R.string.general_ip))),
							rObj.getInt("HostPort"));//这块发送端口号，是否要改还不太清楚
					sUdp.send(sPacket);
				} else {
					sid = 0;
					musicName="";
				}
//			}
			msg.what=Tools.HANDLER_NORMAL;
			
		} catch (SocketException ie) {
			msg.what = Tools.SOCKET_ERROR;
		}catch (UnknownHostException ie){
			msg.what = Tools.UNKNOWHOST_ERROR;
		}catch (IOException ie){
			msg.what = Tools.IO_ERROR;
		}catch(JSONException ie){
			msg.what=Tools.JSON_ERROR;
		}
		myHandler.sendMessage(msg);
	}
	
	/**
	 * 获取数据
	 */
	private void searchData()
	{
		if(sidTemp==0)//全局变量为0并且曲目为空时候，表示系统刚运行，系统调用第一学生的信息
		{
//			if(db==null)
//			{
//				iniDb();
//			}
//			cursor=db.rawQuery("select * from tb_player order by _id asc limit 1", null);//查询第一条记录
//			if(cursor.moveToFirst())
//			{
//				sid=Integer.valueOf(cursor.getString(cursor.getColumnIndex("_id")));
//				musicName=cursor.getString(cursor.getColumnIndex("MUSICS")).split("\\,")[0];
//			}
			player=new Player("", "等待考试", "", "", "", "", "", Tools.INDEX_TYPE_VOICEFORM, 0);
		}
		if (sidTemp != sid||musicNameTemp.equals(musicName)==false)//根据udp返回来的结果和全局变量进行比对，如果不同则将sid赋给sidTemp 
		{
			if(sid!=0&&musicName.equals("")==false)
			{
				editor.putInt("sid", sid);
				editor.putString("musicName", musicName);
				editor.commit();
				sidTemp=dataCache.getInt("sid", 0);
				musicNameTemp=dataCache.getString("musicName", "");
			}
			cursor= db.rawQuery("select * from tb_player where _id=?",new String[] { String.valueOf(sidTemp) });
			if(cursor.moveToFirst())//如果数据库能查到该条记录则返回player
			{
				this.player=queryPlayer(cursor);
				
			}
			cursor.close();
		}
	}
	
	/**
	 * 将数据库中查找到的数据进行赋值
	 * @param cursor 数据库记录
	 * @return           选手信息
	 */
	public Player queryPlayer(Cursor cursor)
	{
		Player player = new Player();
		if (cursor != null && cursor.moveToFirst()) {
			player.setId(cursor.getString(cursor.getColumnIndex("_id")));
			player.setCname(cursor.getString(cursor
					.getColumnIndex("CNAME")));
			player.setIdcard(cursor.getString(cursor
					.getColumnIndex("IDCARD")));
			player.setZyfclass(cursor.getString(cursor
					.getColumnIndex("ZYFCLASS")));
			player.setZysclass(cursor.getString(cursor
					.getColumnIndex("ZYSCLASS")));
			player.setPhotopath(cursor.getString(cursor
					.getColumnIndex("PHOTOPATH")));
			player.setMusics(cursor.getString(cursor
					.getColumnIndex("MUSICS")));
//			player.setStatus(cursor.getInt(cursor
//					.getColumnIndex("STATUS")));
			player.setIndexType(cursor.getString(cursor
					.getColumnIndex("INDEXTYPE")));
		}
		cursor.close();
		return player;
	}
	/**
	 * 将数据库中查找到的配置信息进行赋值
	 * @param cursor 数据库记录
	 * @return				评委信息
	 */
	public DeviceSet queryDeviceSet(Cursor cursor)
	{
		DeviceSet deviceSet=new DeviceSet();
		deviceSet.setId(cursor.getString(cursor.getColumnIndex("_id")));
		deviceSet.setDno(cursor.getString(cursor.getColumnIndex("DNO")));
		deviceSet.setDname(cursor.getString(cursor.getColumnIndex("DNAME")));
		deviceSet.setEname(cursor.getString(cursor.getColumnIndex("ENAME")));
		deviceSet.setJudgeid(cursor.getString(cursor.getColumnIndex("JUDGEID")));
		deviceSet.setJname(cursor.getString(cursor.getColumnIndex("JNAME")));
		deviceSet.setWorkdept(cursor.getString(cursor.getColumnIndex("WORKDEPT")));
		deviceSet.setPhotopath(cursor.getString(cursor.getColumnIndex("PHOTOPATH")));
		return deviceSet;
	}

	/**
	 * 断开UDP连接
	 */
	public void DisConnectSocket() {
		if (rUdp != null) {
			rUdp.close();
			rUdp = null;
		}
		if (rPacket != null)
			rPacket = null;
	}
	
	/**
	 * 
	 */
	@Override
	protected void onResume(){
		super.onResume();
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
	};
	
	@Override
	protected void onDestroy(){
		isThstart=false;
		super.onDestroy();
	}

	/**
	 * 初始化导航
	 */
	public void restoreActionBar() {
		actionBar = getSupportActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
		actionBar.setDisplayShowTitleEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
//		if (!mNavigationDrawerFragment.isDrawerOpen()) {
//			getMenuInflater().inflate(R.menu.main, menu);
//			return true;
//		}
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent=new Intent(MainActivity.this, SettingsActivity.class);
			MainActivity.this.startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
	
	HandlerThread scoreThread;
	
	

	@Override
	public void onScoreButtonClick(final ExamScore es) {
		// TODO 进行打分
		
		es.setStudentid(player.getId());
		es.setJudgeid(deviceSet.getJudgeid());
		es.setWorksname(musicNameTemp);
		if(player.getStatus()==2)
		{
			Tools.alert(this, "分数已经提交，不用重复打分！");
			return;
		}
		String message=mMainFragment.submitCheck();
		if(message!="")
		{
			SpannableStringBuilder style=new SpannableStringBuilder(message);
			style.setSpan(new ForegroundColorSpan(Color.RED), 0, message.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
			message="当中"+style+"的分数为1，";
		}
		aDialog
		.setTitle("确认提交成绩")
		.setMessage("系统会将该考生的考试成绩提交，"+message+"如果没有问题请点击确定按钮，否则请取消。")
		.setNegativeButton("确定",new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();  
				mMainFragment.setExamScore(es);
				mMainFragment.progressDialog = ProgressDialog.show(mMainFragment.getActivity(), "请稍等...", "正在提交分数...", true);
				scoreThreadExecutor.execute(mMainFragment.ScoreRunnable);
			}
		})
		.setPositiveButton("取消", null).show();
	}
	
	
	

	public void submitScore()
	{
		player.setStatus(2);
		ContentValues values=player.getContentValues();
		db=dbHelper.getWritableDatabase();
		if(db.update(DatabaseHelper.PLAYER_TABLE_NAME, values, "_id=?", new String[]{player.getId()})>=0)
		{
			Tools.alert(this, "打分成功！");
			mNavigationFragment.submit.setText("分数已提交");
			mNavigationFragment.submit.setBackgroundResource(R.drawable.submit1);
		}
		else
		{
			Tools.alert(this, "打分失败！");
		}
	}
	@Override
	public boolean onNavigationItemSelected(int position, long id) {
		// TODO Auto-generated method stub
		mPager.setCurrentItem(position);
		mMainFragment=(BaseFragment)listFragment.get(position);
		return true;
	}
}
