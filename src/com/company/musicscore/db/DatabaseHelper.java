package com.company.musicscore.db;

import java.io.File;

import java.io.FileOutputStream;
import java.io.InputStream;
import com.example.musicscore.R;
import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	// 得到SD卡路径
	private final String DATABASE_PATH = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/music";
	private static final String DATABASE_NAME = "musicscore.db";
	private static final int DATABASE_VERSION = 1;
	
	/**
	 * 选手表
	 */
	public static final String PLAYER_TABLE_NAME="TB_PLAYER";
	/**
	 * 配置表
	 */
	public static final String DEVICESET_TABLE_NAME="TB_DBVICESET";
	/**
	 * 考试分数表
	 */
	public static final String EXAMSCORE_TABLE_NAME="TB_EXAMSCORE";
	

	private final Activity activity;
	// 创建比赛设置信息表
	final String CREAT_TABLE_SET = "create table TB_DEVICESET(_id integer primary key autoincrement,"
			+ " DNO, DNAME,ENAME,JUDGEID,JNAME,WORKDEPT,PHOTOPATH);";
	// 创建选手表
	final String CREAT_TABLE_PLAYER = "create table TB_PLAYER(_id integer not null,"
			+ " CNAME, IDCARD,ZYFCLASS,ZYSCLASS,PHOTOPATH,MUSICS,INDEXTYPE,STATUS integer not null);";
	// 创建得分表
	final String CREAT_TABLE_SCORE = "create table TB_EXAMSCORE(_id integer primary key autoincrement,"
			+ " JUDGEID, STUDENTID,WORKSNAME,SCORE,TECHABILITY,MUSICPERFORM,"
			+ " VOICECONDITION,EXECUTION,DIFFICULTYDEGREE,CREATEDATE,DEVICENO);";

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		activity = (Activity) context;
	}

	public SQLiteDatabase openDatabase() {
		try {
			boolean b = false;
			String databaseFilename = DATABASE_PATH + "/" + DATABASE_NAME;
			// 将数据库文件从资源文件放到合适地方（资源文件也就是数据库文件放在项目的res下的raw目录中）
			// 将数据库文件复制到SD卡中
			File dir = new File(DATABASE_PATH);
			if (!dir.exists()) {
				b = dir.mkdir();
			}
			// 判断是否存在该文件
			if (!(new File(databaseFilename)).exists()) {
				// 不存在得到数据库输入流对象
				InputStream is = activity.getResources().openRawResource(
						R.raw.musicscore);
				// 创建输出流
				FileOutputStream fos;

				fos = new FileOutputStream(databaseFilename);

				// 将数据输出
				byte[] buffer = new byte[8192];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}
				// 关闭资源
				fos.close();
				is.close();
			}
			// 得到SQLDatabase对象
			SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
					databaseFilename, null);
			return database;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		// 第一次 使用数据库时自动创建表
		db.execSQL(CREAT_TABLE_SET);
		db.execSQL(CREAT_TABLE_PLAYER);
		db.execSQL(CREAT_TABLE_SCORE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		System.out.print("------onUpdate Called----------" + oldVersion
				+ "--->" + newVersion);
	}

}
