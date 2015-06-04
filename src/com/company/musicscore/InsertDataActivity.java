package com.company.musicscore;

import com.company.musicscore.db.DataSync;
import com.company.musicscore.db.DatabaseHelper;
import com.example.musicscore.R;
import android.support.v7.app.ActionBarActivity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class InsertDataActivity extends ActionBarActivity implements OnClickListener {

	
	
	
	Button btn;
	
	Button btn1;
	
	Button btn2;
	
	Button btn3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_insert_data);
		btn=(Button)findViewById(R.id.button1);
		btn1=(Button)findViewById(R.id.button2);
		btn2=(Button)findViewById(R.id.button3);
		btn3=(Button)findViewById(R.id.button4);
		btn.setOnClickListener(this);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.insert_data, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		DatabaseHelper dbHelper=new DatabaseHelper(this);
		SQLiteDatabase db=dbHelper.getWritableDatabase();
		if(v.getId()==R.id.button1)
		{
			long rawid=0;

			ContentValues values=new ContentValues();
			values.put("_id", 1);
			values.put("cname", "安室奈美惠");
			values.put("idcard", "1400000000");
			values.put("zyfclass", "音乐表演");
			values.put("zysclass", "器乐表演");
			values.put("photopath", "anshi.jpg");
			values.put("musics", "天黑黑;一路上有你;吻别;天意");
			values.put("indextype", "DIC_PLAYFORM");
			values.put("status", 0);//没打分
			rawid=db.insert("TB_PLAYER", null, values);
//			Toast.makeText(this, "添加成功！", Toast.LENGTH_LONG).show();
			Toast.makeText(this, String.valueOf(rawid), Toast.LENGTH_SHORT).show();
		}
		else if(v.getId()==R.id.button2)
		{
			db.delete("TB_PLAYER", null, null);
			db.delete("TB_EXAMSCORE", null, null);
			Toast.makeText(this, "删除成功！", Toast.LENGTH_SHORT).show();
		}
		else if(v.getId()==R.id.button3)
		{
			DataSync ds=new DataSync(this);		
			ds.playerSync();
		}
		
			
	}
}
