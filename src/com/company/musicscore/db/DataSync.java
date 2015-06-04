package com.company.musicscore.db;



import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ksoap2.serialization.SoapObject;

import com.company.musicscore.model.DeviceSet;
import com.company.musicscore.model.ExamScore;
import com.company.musicscore.model.Player;
import com.company.musicscore.util.Tools;
import com.company.musicscore.util.WebServiceHelper;
import com.example.musicscore.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

public class DataSync {

	private DatabaseHelper dbHelper;//数据库访问
	private SQLiteDatabase db;//数据库实例
	private WebServiceHelper wbHelper;//服务访问
	private Gson gson;
	private static Context mContext;
	private static final String SQL_INSERT_PLAYER="insert into TB_PLAYER(_id,cname,idcard,zyfclass,zysclass,photopath,musics,indextype,status) values(?,?,?,?,?,?,?,?,?)";
	
	private static final String SQL_INSERT_EXAMSCORE="insert into TB_EXAMSCORE(JUDGEID,STUDENTID,WORKSNAME,TECHABILITY,MUSICPERFORM,VOICECONDITION,EXECUTION,DIFFICULTYDEGREE,CREATEDATE,DEVICENO) values(?,?,?,?,?,?,?,?,?,?)";
	Handler myHandler;
	private ProgressDialog progressDialog = null;
	private String deviceNo="";
	HandlerThread thread;
	public DataSync(Context context) {
		// TODO Auto-generated constructor stub
		gson=new Gson();
		dbHelper=new DatabaseHelper(context);
		db=dbHelper.getWritableDatabase();
		wbHelper=new WebServiceHelper();
		mContext=context;
		thread=new HandlerThread("DataSyncThread");
		thread.start();
		myHandler=new MyHandler(thread.getLooper());
//		thread=new HandlerThread("DeviceThread");
//		thread.start();
//		myHandler=new MyHandler(thread.getLooper());
	}
	
	public static class MyHandler extends Handler
	{
		public MyHandler(Looper arg0) {
			// TODO Auto-generated constructor stub
			super(arg0);
		}
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what) {
			case Tools.HANDLER_NORMAL:
				Tools.alert(mContext, "同步成功！");
				break;
			case Tools.SOCKET_ERROR:
				Tools.alert(mContext, "无法连接服务器！");
				break;
			case Tools.DEVICENO_ERROR:
				Tools.alert(mContext, "机器标识码不正确！");
				break;
			default:
				break;
				
			}
		}
	}
	/**
	 * 选手同步
	 */
	public void playerSync()
	{
		
		progressDialog = ProgressDialog.show(mContext, "请稍等...", "正在同步考生信息...", true);
		
		myHandler.post(playerRunnable);
	}
	/**
	 * 同步配置信息
	 */
	public void DeviceSetSync(String deviceNo)
	{
		if(deviceNo.equals("")){
			Tools.alert(mContext, "请先填写机器标识！");
			return;
		}
		else
		{
			this.deviceNo=deviceNo;
		}
		progressDialog = ProgressDialog.show(mContext, "请稍等...", "正在同步机器配置...", true);	
		
		myHandler.post(deviceSetRunnable);
		
	}
	
	public void destroyPlayerThread()
	{
		myHandler.removeCallbacks(playerRunnable);
	}
	
	public void destroyDeviceThread()
	{
		myHandler.removeCallbacks(deviceSetRunnable);
	}
	
	private Runnable playerRunnable=new Runnable() {
		
		@Override
		public void run() {
			// TODO 自动生成的方法存根
			Message msg=new Message();
			
			SoapObject soapObject=wbHelper.GetObject(WebServiceHelper.getServiceUrl(mContext, mContext.getResources().getString(R.string.general_ip)), WebServiceHelper.SERVICE_NAMESPACE, "GetPlayersData",null);

			JSONObject jsonObject;
			try {
				if(soapObject!=null)
				{
					jsonObject = new JSONObject(soapObject.getProperty("GetPlayersDataResult").toString());
					JSONArray jsonArray=new JSONArray(jsonObject.getString("Result"));
					insertPlayer(jsonArray);
					
					msg.what=Tools.HANDLER_NORMAL;
				}
				else
				{
					msg.what=Tools.SOCKET_ERROR;
				}
				
			} catch (JSONException e) {
				// TODO 自动生成的 catch 块
//				e.printStackTrace();
				msg.what=Tools.SOCKET_ERROR;
			}
			progressDialog.dismiss();
			myHandler.sendMessage(msg);
		}
	};
	
	private Runnable deviceSetRunnable=new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Message msg=new Message();
			Map<String, String> devMap=new HashMap<String, String>();
			devMap.put("devNo", deviceNo);
			SoapObject soapObject=wbHelper.GetObject(WebServiceHelper.getServiceUrl(
					mContext, 
					mContext.getResources().getString(R.string.general_ip)), 
					WebServiceHelper.SERVICE_NAMESPACE, "GetDevsetData",devMap);

			JSONObject jsonObject;
			try {
				if(soapObject!=null)
				{
					
					jsonObject = new JSONObject(soapObject.getProperty("GetDevsetDataResult").toString());
					JSONObject newObj=new JSONObject(jsonObject.getString("Result"));
					
					if(newObj.getString("Id")!=null&&newObj.getString("Id").equals("null")==false)
					{
						
						insertDeviceSet(newObj);
						msg.what=Tools.HANDLER_NORMAL;
					}
					else
					{
						msg.what=Tools.DEVICENO_ERROR;
					}
				}
				else
				{
					msg.what=Tools.SOCKET_ERROR;
				}
				
			} catch (JSONException e) {
				// TODO 自动生成的 catch
				msg.what=Tools.SOCKET_ERROR;
			}
			progressDialog.dismiss();
			myHandler.sendMessage(msg);
		}
		
	};
	
	
	
	/***
	 * 获取选手列表插入数据库
	 * @param jsonPlayer 获取的选手Json
	 */
	public void insertPlayer(String jsonPlayer)
	{
		Type type = new TypeToken<List<Player>>(){}.getType();
		List<Player> list=gson.fromJson(jsonPlayer, type);
		SQLiteStatement stat=db.compileStatement(SQL_INSERT_PLAYER);
		db.beginTransaction();
		db.delete("TB_PLAYER", null, null);
		for (Player pl : list) {
			stat.bindString(1, pl.getId());
			stat.bindString(2, pl.getCname());
			stat.bindString(3, pl.getIdcard());
			stat.bindString(4, pl.getZyfclass());
			stat.bindString(5, pl.getZysclass());
			stat.bindString(6, pl.getPhotopath());
			stat.bindString(7, pl.getMusics());
			stat.bindString(8, pl.getIndexType());
			stat.bindString(9, String.valueOf(0));
			stat.executeInsert();
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
		
	}
	
	/**
	 * 删除表数据
	 */
	public void deleteTable()
	{
		db.delete("TB_PLAYER", null, null);
		db.delete("TB_DEVICESET", null, null);
		db.delete("TB_EXAMSCORE", null, null);
		SharedPreferences dataCache=mContext.getSharedPreferences("cache", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editor=dataCache.edit();
		editor.putInt("sid", 0);
		editor.putString("musicName", "");
		editor.commit();
		Tools.alert(mContext, "考生和机器配置信息已清空！");
	}
	
	
	public String JSONTokener(String in) {  
        // consume an optional byte order mark (BOM) if it exists  
        if (in != null && in.startsWith("\ufeff")) {  
        in = in.substring(1);  
        }  
        return in;  
   }  
	/**
	 * 获得选手列表插入数据库（JsonArray）
	 * @param jsonArray 获取选手JsonArray
	 */
	public void insertPlayer(JSONArray jsonArray)
	{
		SQLiteStatement stat=db.compileStatement(SQL_INSERT_PLAYER);
		
		try {
			db.beginTransaction();
			db.delete("TB_PLAYER", null, null);
			for(int i=0;i<jsonArray.length();i++)
			{
				JSONObject obj=jsonArray.getJSONObject(i);
				stat.bindString(1, obj.getString("Id"));
				stat.bindString(2, obj.getString("Cname"));
				stat.bindString(3, obj.getString("Idcard"));
				stat.bindString(4, obj.getString("Zyfclass"));
				stat.bindString(5, obj.getString("Zysclass"));
				stat.bindString(6, obj.getString("Photopath"));
				stat.bindString(7, obj.getString("Musics"));
				stat.bindString(8, obj.getString("IndexType"));
				stat.bindString(9, String.valueOf(0));
				stat.executeInsert();
			}
			db.setTransactionSuccessful();
			db.endTransaction();
		} catch (Exception e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
	}
	
	/**
	 * 插入考试信息
	 * @param context
	 * @param esList
	 * @return
	 */
	public static boolean insertExamScore(Context context,List<ExamScore> esList)
	{
		boolean flag=true;
		DatabaseHelper dbHelper=new DatabaseHelper(context);
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		SQLiteStatement stat=db.compileStatement(SQL_INSERT_EXAMSCORE);
		db.beginTransaction();
		for (ExamScore examScore : esList) {
			stat.bindString(1, examScore.getJudgeid());
			stat.bindString(2, examScore.getStudentid());
			stat.bindString(3, examScore.getWorksname());
			stat.bindString(4, examScore.getTechablity());
			stat.bindString(5, examScore.getMusicperform());
			stat.bindString(6, examScore.getVoicecondition());
			stat.bindString(7, examScore.getExecution());
			stat.bindString(8, examScore.getDifficulity());
			stat.bindString(9, examScore.getCreatedate());
			stat.bindString(10, examScore.getDeviceno());
			stat.executeInsert();
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
		dbHelper.close();
		db=null;
		dbHelper=null;
		return flag;
	}
	
	/**
	 * 插入单条数据到ExamScore表
	 * @param context
	 * @param examScore
	 * @return
	 */
	public static boolean insertExamScore(Context context,ExamScore examScore)
	{
		boolean flag=true;
		DatabaseHelper dbHelper=new DatabaseHelper(context);
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		long num=db.insert(DatabaseHelper.EXAMSCORE_TABLE_NAME, null, examScore.getContentValues());
		if(num>0)
		{
			flag=true;
		}
		else
		{
			flag=false;
		}
		db.close();
		dbHelper.close();
		db=null;
		dbHelper=null;
		return flag;
		
	}
	
	/**
	 * 插入配置信息
	 * @param jsonObj 服务器获得的JsonObj
	 */
	public void insertDeviceSet(JSONObject jsonObj)
	{
		int delRows=db.delete("TB_DEVICESET", null, null);
		if(delRows==0)
		{
			DeviceSet ds=new DeviceSet();
			try {
				ds.setId(jsonObj.getString("Id"));
				ds.setDno(jsonObj.getString("Dno"));
				ds.setDname(jsonObj.getString("Dname"));
				ds.setEname(jsonObj.getString("Ename"));
				ds.setJudgeid(jsonObj.getString("Judgeid"));
				ds.setJname(jsonObj.getString("Jname"));
				ds.setWorkdept(jsonObj.getString("Workdept"));
				ds.setPhotopath(jsonObj.getString("Photopath"));
				ContentValues values=ds.getContentValues();
				db.insert("TB_DEVICESET", null, values);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	
	

}
