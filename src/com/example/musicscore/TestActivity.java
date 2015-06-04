package com.example.musicscore;

import com.company.musicscore.util.Ruler;
import com.company.musicscore.util.Ruler.OnRulerChangeListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SeekBar;
import android.widget.TextView;


public class TestActivity extends Activity implements OnRulerChangeListener{

	TextView txt_jishu;
	TextView txt_yuegan;
	TextView txt_zhiliang;
	TextView txt_sangyin;
	
	Ruler jishu_ruler;
	Ruler yuegan_ruler;
	Ruler zhiliang_ruler;
	Ruler sangyin_ruler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		txt_jishu=(TextView)findViewById(R.id.txt_jishu);
		txt_yuegan=(TextView)findViewById(R.id.txt_yuegan);
		txt_zhiliang=(TextView)findViewById(R.id.txt_zhiliang);
		txt_sangyin=(TextView)findViewById(R.id.txt_sangyin);
		jishu_ruler=(Ruler)findViewById(R.id.jishu_ruler);
		yuegan_ruler=(Ruler)findViewById(R.id.yuegan_ruler);
		zhiliang_ruler=(Ruler)findViewById(R.id.zhiliang_ruler);
		sangyin_ruler=(Ruler)findViewById(R.id.sangyin_ruler);
		
		jishu_ruler.setOnRulerChangeListener(this);
		yuegan_ruler.setOnRulerChangeListener(this);
		zhiliang_ruler.setOnRulerChangeListener(this);
		sangyin_ruler.setOnRulerChangeListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.test, menu);
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
	public void onRulerChangeEvent(Ruler ruler,int progress) {
		// TODO Auto-generated method stub
		if(ruler.getId()==R.id.jishu_ruler)
		{
			txt_jishu.setText(String.valueOf(progress));
		}
		else if(ruler.getId()==R.id.yuegan_ruler)
		{
			txt_yuegan.setText(String.valueOf(progress));
		}
		else if(ruler.getId()==R.id.zhiliang_ruler)
		{
			txt_zhiliang.setText(String.valueOf(progress));
		}
		else  if(ruler.getId()==R.id.sangyin_ruler)
		{
			txt_sangyin.setText(String.valueOf(progress));
		}
	}
}
