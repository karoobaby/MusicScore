package com.company.musicscore;

import com.company.musicscore.util.Tools;
import com.example.musicscore.R;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;


public class InitializeActivity extends Activity implements OnClickListener {

	
	private ImageButton startButton;
	private ImageButton configureButton;
	
	private Builder aDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_initialize);
		
		startButton=(ImageButton)findViewById(R.id.btn_start);
		startButton.setOnClickListener(this);
		configureButton=(ImageButton)findViewById(R.id.btn_configure);
		configureButton.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		if(v.getId()==R.id.btn_start)
		{
			int playerCount=Tools.getCountByPlayers(this);
			int deviceSetCount=Tools.getCountByDeviceSet(this);
			if(playerCount>0&&deviceSetCount>0)
			{
				Intent intent=new Intent(InitializeActivity.this,MainActivity.class);
				InitializeActivity.this.startActivity(intent);
			}
			else
			{
				aDialog=new Builder(this);
				aDialog.setTitle("配置基本信息")
				.setMessage("检查到学生和设备的信息还未同步，建议进入配置菜单同步基本信息！")
				.setNegativeButton("确定",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO 自动生成的方法存根
						Intent intent=new Intent(InitializeActivity.this,SettingsActivity.class);
						InitializeActivity.this.startActivity(intent);
					}
				})
				.setPositiveButton("取消", null).show();
			}
//			Intent intent=new Intent(InitializeActivity.this,MainActivity.class);
//			InitializeActivity.this.startActivity(intent);
		}
		else if(v.getId()==R.id.btn_configure)
		{
			Intent intent=new Intent(InitializeActivity.this,SettingsActivity.class);
			InitializeActivity.this.startActivity(intent);
		}
	}
}
