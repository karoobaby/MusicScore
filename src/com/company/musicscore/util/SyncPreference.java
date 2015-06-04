/**
 * 
 */
package com.company.musicscore.util;
import com.company.musicscore.db.DataSync;
import com.example.musicscore.R;
import android.content.Context;
import android.preference.Preference;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * @author 陈昊
 *
 */
public class SyncPreference extends Preference implements OnClickListener{

	private Context mContext;
	private Button sync_student;
	private Button sync_machine;
	private Button sync_delete;
	DataSync ds;
	/**
	 * @param context
	 */
	public SyncPreference(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		this.mContext=context;
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public SyncPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.mContext=context;
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public SyncPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.mContext=context;
	}
	
	@Override
	protected View onCreateView(ViewGroup parent)
	{
		LayoutInflater  layout=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View viewRoot=layout.inflate(R.layout.sync_layout, null);
		sync_student=(Button)viewRoot.findViewById(R.id.btn_student);
		sync_machine=(Button)viewRoot.findViewById(R.id.btn_machine);
		sync_delete=(Button)viewRoot.findViewById(R.id.btn_delete);
		sync_student.setOnClickListener(this);
		sync_machine.setOnClickListener(this);
		sync_delete.setOnClickListener(this);
		ds=new DataSync(mContext);
		return viewRoot;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		switch (v.getId()) {
		case R.id.btn_student:
			ds.playerSync();
			break;
		case R.id.btn_machine:
			ds.DeviceSetSync(Tools.getDeviceNo(mContext, "general_device"));
			break;
		case R.id.btn_delete:
			ds.deleteTable();
		default:
			break;
		}
	}

}
