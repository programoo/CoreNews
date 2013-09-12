package com.programoo.corenews;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.programoo.snews.R;

public class MainActivity extends SherlockFragmentActivity {
	private String tag = this.getClass().getSimpleName();
	private ViewPager mViewPager;
	private TabHost mTabHost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_main);
		
		Log.d(tag, "onCreate");

		mViewPager = (ViewPager) findViewById(R.id.pager);
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		
		TabsAdapter mTabsAdapter = new TabsAdapter(this, mTabHost, mViewPager);
		mTabHost.clearAllTabs();

		Bundle tabArgs = null;

		tabArgs = new Bundle();
		tabArgs.putString("collection", "tab_news");
		tabArgs.putInt("id", 0);
		mTabsAdapter.addTab(mTabHost.newTabSpec("tab_news"), getResources()
				.getDrawable(R.drawable.news_icon), NewsFragment.class,
				tabArgs, getString(R.string.news_title));
				
		tabArgs = new Bundle();
		tabArgs.putString("collection", "tab_setting");
		tabArgs.putInt("id", 1);
		mTabsAdapter.addTab(mTabHost.newTabSpec("tab_news"), getResources()
				.getDrawable(R.drawable.settings_icon), NewsFragment.class,
				tabArgs, getString(R.string.settings_title));
		
		
		
	}

}
