/**
 * 
 */
package com.company.musicscore.db;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.os.Environment;
import android.util.Log;

/**
 * @author 禹彤
 *
 */
public class DatabaseContext extends ContextWrapper {

	/**
	 * @param base
	 */
	public DatabaseContext(Context base) {
		super(base);
		// TODO 自动生成的构造函数存根
	}

	public File getDatabasePath(String name) {
		boolean sdExist = Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState());
		if (!sdExist) {// 如果不存在,
			Log.e("SD卡管理：", "SD卡不存在，请加载SD卡");
			return null;
		} else {// 如果存在
				// 获取sd卡路径
			String dbDir = android.os.Environment.getExternalStorageDirectory()
					.getAbsolutePath();
			dbDir += "/res/raw";// 数据库所在目录
			String dbPath = dbDir + "/" + name;// 数据库路径
			// 判断目录是否存在，不存在则创建该目录
			System.out.println(">>>>>>>>>>>>>" + dbPath);
			File dirFile = new File(dbDir);
			if (!dirFile.exists())
				dirFile.mkdirs();

			// 数据库文件是否创建成功
			boolean isFileCreateSuccess = false;
			// 判断文件是否存在，不存在则创建该文件
			File dbFile = new File(dbPath);
			if (!dbFile.exists()) {
				try {
					isFileCreateSuccess = dbFile.createNewFile();// 创建文件
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else
				isFileCreateSuccess = true;

			// 返回数据库文件对象
			if (isFileCreateSuccess)
				return dbFile;
			else
				return null;
		}
	}

	@Override
	public SQLiteDatabase openOrCreateDatabase(String name, int mode,
			CursorFactory factory, DatabaseErrorHandler errorHandler) {
		SQLiteDatabase result = SQLiteDatabase.openOrCreateDatabase(
				getDatabasePath(name), null);
		System.out.println("ppppppppppppppppppp" + result.getPath());
		return result;
	}

}
