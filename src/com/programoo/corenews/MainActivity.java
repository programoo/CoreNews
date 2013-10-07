package com.programoo.corenews;

import object.Feeder;
import object.SArrayList;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TabHost;

import com.programoo.snews.R;

public class MainActivity extends FragmentActivity
{
	private String TAG = this.getClass().getSimpleName();
	private ViewPager mViewPager;
	private TabHost mTabHost;
	public SArrayList fList;
	public SArrayList newsList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_main);
		// initialize global variable
		Log.d(TAG, "onCreate");
		
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
				.getDrawable(R.drawable.news_tabbar_img), FeedFragment.class,
				tabArgs, getString(R.string.news_text));
		
		tabArgs = new Bundle();
		tabArgs.putString("collection", "tab_setting");
		tabArgs.putInt("id", 1);
		mTabsAdapter.addTab(mTabHost.newTabSpec("tab_settings"), getResources()
				.getDrawable(R.drawable.settings_tabbar_img),
				SettingFragment.class, tabArgs,
				getString(R.string.settings_text));
		
		// preparing container without static object
		fList = new SArrayList();
		newsList = new SArrayList();
		// read save serialize object
		Log.i(TAG, "size: " + fList.size());
		
		if (Info.deserializeToGenericArrayList(this, "providers.csv", fList,
				Feeder.class))
		{
			
		} else
		{
			fList.add(new Feeder("technology",
					"http://www.blognone.com/atom.xml", true));
			fList.add(new Feeder("other",
					"http://www.thairath.co.th/rss/news.xml", true));
			fList.add(new Feeder("news",
					"http://www.komchadluek.net/rss/news_widget.xml", true));
			
			fList.add(new Feeder("kak",
					"http://www.matichon.co.th/rss/news_conspicuous.xml", true));
			fList.add(new Feeder(
					"sanook",
					"http://rssfeeds.sanook.com/rss/feeds/sanook/news.index.xml",
					true));
		}
		
		// loading first config
		/*
		 * uniqueAddProvider(new Feeder("technology",
		 * "http://www.blognone.com/atom.xml", true)); uniqueAddProvider(new
		 * Feeder("other", "http://www.thairath.co.th/rss/news.xml", true));
		 * uniqueAddProvider(new Feeder("news",
		 * "http://www.komchadluek.net/rss/news_widget.xml", true));
		 * uniqueAddProvider(new Feeder("kak",
		 * "http://www.matichon.co.th/rss/news_conspicuous.xml", true)); // //
		 * http: // www.krobkruakao.com/rss/News.rss // // http: //
		 * rssfeeds.sanook.com/rss/feeds/sanook/news.index.xml
		 * uniqueAddProvider(new Feeder("sanook",
		 * "http://rssfeeds.sanook.com/rss/feeds/sanook/news.index.xml", true));
		 */
		Log.i(TAG, "size: " + fList.size());
	}
	
	@Override
	protected void onStart()
	{
		Log.i(TAG, "onStart");
		super.onStart();
	}
	
	@Override
	protected void onDestroy()
	{
		Log.i(TAG, "onDestroy");
		// save user already read news before exit
		String result = Info.serializeFromArrayList(this, "providers.csv",
				fList, Feeder.class);
		Log.i(TAG, "result isss: " + result);
		Log.i(TAG, "Clean complete");
		super.onDestroy();
	}
	
	@Override
	protected void onPause()
	{
		Log.i(TAG, "onPAUSE");
		super.onPause();
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
	}
	
}
