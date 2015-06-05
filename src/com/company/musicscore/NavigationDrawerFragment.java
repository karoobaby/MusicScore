package com.company.musicscore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.company.musicscore.model.ExamScore;
import com.company.musicscore.util.Tools;
import com.example.musicscore.R;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class NavigationDrawerFragment extends Fragment implements OnClickListener{


    private static final String STATE_SELECTED_POSITION = "selected_navigation_drawer_position";

    private static final String PREF_USER_LEARNED_DRAWER = "navigation_drawer_learned";

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private View mFragmentContainerView;
    
    public ListView msg_list;
    
    public ListView msg_list1;
    
    
    
    public ImageView img_xuanshou;
    
    private Button submit;
    
    ExamScore esModel;
    
    OnScoreButtonClickedListener mCallback;
    
    

    private int mCurrentSelectedPosition = 0;
    private boolean mFromSavedInstanceState;
    private boolean mUserLearnedDrawer;

    public NavigationDrawerFragment() {
    }
    
    public interface OnScoreButtonClickedListener{
    	public void onScoreButtonClick(ExamScore es);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mUserLearnedDrawer = sp.getBoolean(PREF_USER_LEARNED_DRAWER, false);

        if (savedInstanceState != null) {
            mCurrentSelectedPosition = savedInstanceState.getInt(STATE_SELECTED_POSITION);
            mFromSavedInstanceState = true;
        }
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	View rootView = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
    	msg_list=(ListView)rootView.findViewById(R.id.msg_list);
    	msg_list1=(ListView)rootView.findViewById(R.id.msg_list1);
    	img_xuanshou=(ImageView)rootView.findViewById(R.id.img_xuanshou);
    	img_xuanshou.setImageResource(R.drawable.anshi);
    	submit=(Button)rootView.findViewById(R.id.score_submit);
    	submit.setOnClickListener(this);
    	bindList(msg_list, Tools.ITEM_TITLES_XUANSHOU_DEFAULT, Tools.ITEM_VALUES_XUANSHOU_DEFAULT);
    	bindList(msg_list1, Tools.ITEM_TITLES_PINGWEI_DEFAULT, Tools.ITEM_VALUES_PINGWEI_DEFAULT);
        return rootView;
    }
    /**
     * 绑定学生和评委列表
     * @param list
     * @param item_titles
     * @param item_values
     */
    public void bindList(ListView list,String[] item_titles,String[] item_values)
    {
    	List<Map<String,Object>> listItems=new ArrayList<Map<String,Object>>();
    	for (int i = 0; i < item_titles.length; i++) {
			Map<String,Object> listItem = new HashMap<String, Object>();
			listItem.put("item_title", item_titles[i]);
			listItem.put("item_value", item_values[i]);
			listItems.add(listItem);
		}
    	SimpleAdapter simpleAdapter=new SimpleAdapter(
    			this.getActivity(), 
    			listItems, 
    			R.layout.simple_item, 
    			new String[]{"item_title","item_value"}, 
    			new int[]{R.id.item_title,R.id.item_value});
    	list.setAdapter(simpleAdapter);
    }

    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mFragmentContainerView);
    }

    /**
     * 加载导航栏
     * @param fragmentId
     * @param drawerLayout
     */
    public void setUp(int fragmentId, DrawerLayout drawerLayout) {
        mFragmentContainerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;


        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);


        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                getActivity(),                    /* host Activity */
                mDrawerLayout,                    /* DrawerLayout object */
                R.drawable.ic_drawer,             /* nav drawer image to replace 'Up' caret */
                R.string.navigation_drawer_open,  /* "open drawer" description for accessibility */
                R.string.navigation_drawer_close  /* "close drawer" description for accessibility */
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu(); 
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                if (!mUserLearnedDrawer) {

                    mUserLearnedDrawer = true;
                    SharedPreferences sp = PreferenceManager
                            .getDefaultSharedPreferences(getActivity());
                    sp.edit().putBoolean(PREF_USER_LEARNED_DRAWER, true).commit();
                }

                getActivity().supportInvalidateOptionsMenu(); // calls onPrepareOptionsMenu()
            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            mDrawerLayout.openDrawer(mFragmentContainerView);
        }

        // Defer code dependent on restoration of previous instance state.
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }
    
    @Override
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
    	try {
    		mCallback=(OnScoreButtonClickedListener)activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()+" must implement OnScoreButtonClickedListener");
		}
    }
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(STATE_SELECTED_POSITION, mCurrentSelectedPosition);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        if (mDrawerLayout != null && isDrawerOpen()) {
            inflater.inflate(R.menu.main, menu);

        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Per the navigation drawer design guidelines, updates the action bar to show the global app
     * 'context', rather than just what's in the current screen.
     */
//    private void showGlobalContextActionBar() {
//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayShowTitleEnabled(true);
//        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
////        actionBar.setTitle(R.string.app_name);
//    }

    private ActionBar getActionBar() {
        return ((ActionBarActivity) getActivity()).getSupportActionBar();
    }

	@Override
	public void onClick(View v) {
		// TODO 自动生成的方法存根
		if(v.getId()==R.id.score_submit)
		{
			
			esModel=new ExamScore();
			esModel.setJudgeid("1");
			
//			Toast.makeText(this.getActivity(), esModel.getJudgeid(), Toast.LENGTH_LONG).show();
			mCallback.onScoreButtonClick(esModel);
		}
	}
}
