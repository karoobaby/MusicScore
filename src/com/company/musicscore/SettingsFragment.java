package com.company.musicscore;
import com.company.musicscore.util.Tools;
import com.example.musicscore.R;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.text.TextUtils;
public class SettingsFragment extends PreferenceFragment implements OnPreferenceChangeListener {

	private static final String SETTINGS_PARAM = "param"; 
	String key_general_ip;
	String key_general_port;
	String key_general_device;
	String key_local_ip;
	EditTextPreference pref_general_ip;
	EditTextPreference pref_general_port;
	EditTextPreference pref_general_device;
	EditTextPreference pref_local_ip;
	
	
	public static SettingsFragment newInstance(String param) {
		
		SettingsFragment fragment=new SettingsFragment();
		Bundle args = new Bundle();
		args.putString(SETTINGS_PARAM, param);
		fragment.setArguments(args);
		
		return fragment;
	}
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //从xml文件中添加Preference项
        addPreferencesFromResource(R.xml.setting);

        //获取各个Preference
        key_general_ip=getResources().getString(R.string.general_ip);
        key_general_port=getResources().getString(R.string.general_port);
        key_general_device=getResources().getString(R.string.general_device);
        key_local_ip=getResources().getString(R.string.local_ip);
        pref_general_ip=(EditTextPreference)findPreference(key_general_ip);
        pref_general_port=(EditTextPreference)findPreference(key_general_port);
        pref_general_device=(EditTextPreference)findPreference(key_general_device);
        pref_local_ip=(EditTextPreference)findPreference(key_local_ip);
        pref_local_ip.setEnabled(false);
        pref_general_ip.setSummary(Tools.getRemoteIp(this.getActivity(), key_general_ip));
        pref_general_port.setSummary(String.valueOf(Tools.getLocalPort(this.getActivity(), key_general_port)));
        pref_general_device.setSummary(String.valueOf(Tools.getDeviceNo(this.getActivity(), key_general_device)));
        pref_local_ip.setSummary(Tools.getLocalIP(this.getActivity()));
        pref_general_ip.setOnPreferenceChangeListener(this);
        pref_general_port.setOnPreferenceChangeListener(this);
        pref_general_device.setOnPreferenceChangeListener(this);
	}
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		// TODO Auto-generated method stub
		String valueStr=newValue.toString();
		if(preference.getKey().equals(key_general_ip))
		{
			if(!TextUtils.isEmpty(valueStr))
			{
				preference.setSummary(valueStr);
			}
		}
		else if(preference.getKey().equals(key_general_port))
		{
			if(!TextUtils.isEmpty(valueStr))
			{
				preference.setSummary(valueStr);
			}
		}
		else if(preference.getKey().equals(key_general_device))
		{
			if(!TextUtils.isEmpty(valueStr))
			{
				preference.setSummary(valueStr);
				Tools.alert(this.getActivity(), "修改机器标识后需要同步机器配置才能生效！");
			}
		}
		else
		{
			return false;
		}
		return true;
	}

	
	
	

}
