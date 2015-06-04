package com.company.musicscore;


import com.example.musicscore.R;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
public class SettingsActivity extends Activity {

	ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		restoreActionBar();
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, SettingsFragment.newInstance("settings")).commit();
		}
		
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
	
	/**
	 * 初始化导航
	 */
	public void restoreActionBar() {
		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		actionBar.setHomeButtonEnabled(true);
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
		if(item.getItemId()==android.R.id.home)
		{
			SettingsActivity.this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
    }
	
	
}
