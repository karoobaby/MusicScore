package com.company.musicscore;
import com.example.musicscore.R;
import android.view.Window;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class WelcomeActivity extends Activity {
	private Handler handler;
	
	Intent intent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);  
		setContentView(R.layout.activity_welcome);
		handler=new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				intent=new Intent(WelcomeActivity.this,InitializeActivity.class);
				WelcomeActivity.this.startActivity(intent);
				WelcomeActivity.this.finish();
			}
		}, 2000);
	}
}
	