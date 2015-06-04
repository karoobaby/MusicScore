package com.company.musicscore.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.preference.PreferenceManager;
import android.text.format.DateFormat;
import android.util.Log;

import com.company.musicscore.db.DatabaseHelper;

public class Tools {
	
	public static final String[] ITEM_TITLES_XUANSHOU_DEFAULT=new String[]{"姓名","科目","类型","排序"};
	public static final String[] ITEM_TITLES_PINGWEI_DEFAULT=new String[]{"评委","单位","类别"};
	public static final String[] ITEM_VALUES_XUANSHOU_DEFAULT=new String[]{"李小璐","音乐表演","器乐表演","1"};
    public static final String[] ITEM_VALUES_PINGWEI_DEFAULT=new String[]{"吴振瑞","北京","小提琴"};
    public static final String[] ITEM_VALUES_MUSIC_DEFAULT=new String[] { "曲目1", "曲目2", "曲目3","曲目4" };
    public static final String INDEX_TYPE_VOICEFORM="DIC_VOICEFORM";//声乐考试
    public static final String INDEX_TYPE_PLAYFORM="DIC_PLAYFORM";//器乐考试
    
    /**
     * 一切正常
     */
    public static final int HANDLER_NORMAL=0;
    /**
     * 查询不到选手
     */
    public static final int SEARCH_STU_ERROR=1;
    /**
     * 无法更新选手信息到本地数据库
     */
    public static final int UPDATE_STU_ERROR=2;
    /**
     * 找不到服务器
     */
    public static final int SOCKET_ERROR=3;
    /**
     * 连接不了服务器
     */
    public static final int UNKNOWHOST_ERROR=4;
    /**
     * 不能正常接收消息
     */
    public static final int IO_ERROR=5;
    /**
     *	转换Json格式有问题
     */
    public static final int JSON_ERROR=6;
    /**
     * 提交服务器失败
     */
    public static final int SUBMIT_ERROR=7;
    /**
     * 机器标识码错误
     */
    public static final int DEVICENO_ERROR=8;
    private static String remoteIp="192.168.1.54";
	private static int remotePort=10000;
    private static int localPort=10001;
    private static String deviceNo="10000";
    
    /**
     * 获取远程IP
     * @param context
     * @param keyRemoteIp IP配置
     * @return
     */
	public static String getRemoteIp(Context context,String keyRemoteIp) {
		remoteIp=PreferenceManager.getDefaultSharedPreferences(context).getString(keyRemoteIp, "192.168.1.122");
		return remoteIp;
	}
	/**
	 * 获取远程端口
	 * @param context
	 * @param keyRemotePort 端口配置
	 * @return
	 */
	public static int getRemotePort(Context context,String keyRemotePort) {
		remotePort=Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString(keyRemotePort, "10000"));
		return remotePort;
	}
	/**
	 * 获取本机端口
	 * @param context
	 * @param keyLocalPort 端口配置
	 * @return
	 */
	public static int getLocalPort(Context context,String keyLocalPort) {
		localPort=Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString(keyLocalPort, "10001"));
		return localPort;
	}
	/**
	 * 获取本机标识
	 * @param context
	 * @param keyDeviceNo 标识配置
	 * @return
	 */
	public static String getDeviceNo(Context context,String keyDeviceNo) {
		deviceNo=PreferenceManager.getDefaultSharedPreferences(context).getString(keyDeviceNo, "1");
		return deviceNo;
	}

	private static final String TAG="System";	
	private static DatabaseHelper dbHelper;
	private static SQLiteDatabase db;
	
	/**
	 * 获取考生数量
	 */
	public static int getCountByPlayers(Context context) {
		dbHelper=new DatabaseHelper(context);
		db=dbHelper.getReadableDatabase();
		int count=0;
		Cursor cursor=db.rawQuery("select _id from tb_player", null);
		count=cursor.getCount();
		cursor.close();
		closeDB(db, dbHelper);
		return count;
	}
	
	/**
	 * 获取配置信息
	 * @param context
	 * @return 配置数量
	 */
	public static int getCountByDeviceSet(Context context)
	{
		dbHelper=new DatabaseHelper(context);
		db=dbHelper.getReadableDatabase();
		int count=0;
		Cursor cursor=db.rawQuery("select _id from tb_deviceset", null);
		count=cursor.getCount();
		cursor.close();
		closeDB(db, dbHelper);
		return count;
	}
	
	
	/**
	 * 关闭数据库连接
	 * @param db 数据库
	 * @param dbHelper 数据库连接
	 */
	public static void closeDB(SQLiteDatabase db,DatabaseHelper dbHelper)
	{
		if(db!=null)
		{
			db.close();
			dbHelper.close();
			db=null;
			dbHelper=null;
		}
	}
	/**
	 * 获取本地IP
	 * @param context
	 * @return
	 */
	public static String getLocalIP(Context context){
		WifiManager wifiManager = (WifiManager)context.getSystemService(Context.WIFI_SERVICE);  
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();  
		int ipAddress = wifiInfo.getIpAddress(); 
		Log.d(TAG, "int ip "+ipAddress);
		if(ipAddress==0)return null;
		return ((ipAddress & 0xff)+"."+(ipAddress>>8 & 0xff)+"."
				+(ipAddress>>16 & 0xff)+"."+(ipAddress>>24 & 0xff));
	}
	
	/**
	* 加载本地图片
	* http://bbs.3gstdy.com
	* @param url
	* @return
	*/
	public static Bitmap getLoacalBitmap(String url) {
	     try {
	          FileInputStream fis = new FileInputStream(url);
	          return BitmapFactory.decodeStream(fis);
	     } catch (FileNotFoundException e) {
	          e.printStackTrace();
	          return null;
	     }
	}
	/**
	* 从服务器取图片
	* @param url
	* @return
	*/
	public static Bitmap getHttpBitmap(String url) {
	     URL myFileUrl = null;
	     Bitmap bitmap = null;
	     try {
	          Log.d(TAG, url);
	          myFileUrl = new URL(url);
	     } catch (MalformedURLException e) {
	          e.printStackTrace();
	     }
	     try {
	          HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
	          conn.setConnectTimeout(0);
	          conn.setDoInput(true);
	          conn.connect();
	          InputStream is = conn.getInputStream();
	          bitmap = BitmapFactory.decodeStream(is);
	          is.close();
	     } catch (IOException e) {
	          e.printStackTrace();
	     }
	     return bitmap;
	}
	
	
	/**
	 * 判断网络是否存在
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnect(Context context){
        try{
            ConnectivityManager conManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWifi = conManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            return mWifi.isConnected();
        }catch(Exception e){
            return false;
        }
	}
	
	/**
	 * 弹出消息框
	 * @param context
	 * @param msg 消息内容
	 */
	public static void alert(Context context,String msg)
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("消息");
		builder.setMessage(msg);
		builder.setCancelable(true);
		final AlertDialog dlg = builder.create();
		dlg.show();
		final Timer t = new Timer();
		t.schedule(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				dlg.dismiss();
				t.cancel();
			}
		}, 2000);
	}
	
	/**
	 * 获取时间
	 * @return 时间字符串
	 */
	public static String getDateTimeStr()
	{
		return DateFormat.format("yyyy-MM-dd kk:mm:ss", new Date()).toString();
	}
	
	/** 
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */  
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    /** 
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }  
    /**
     * 将px值转换为sp值，保证文字大小不变
     * 
     * @param pxValue
     * @param fontScale（DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int px2sp(float pxValue, float fontScale) {
     return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     * 
     * @param spValue
     * @param fontScale（DisplayMetrics类中属性scaledDensity）
     * @return
     */
    public static int sp2px(float spValue, float fontScale) {
     return (int) (spValue * fontScale + 0.5f);
    }
}
