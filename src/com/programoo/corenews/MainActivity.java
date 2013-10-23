package com.programoo.corenews;

import object.Feeder;
import object.IsRead;
import object.Kind;
import object.News;
import object.SArrayList;
import object.UFeeder;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;

import com.google.analytics.tracking.android.EasyTracker;
import com.programoo.snews.R;

public class MainActivity extends FragmentActivity
{
	private String TAG = this.getClass().getSimpleName();
	private ViewPager mViewPager;
	private TabHost mTabHost;
	public SArrayList fList;
	public SArrayList newsList;
	public SArrayList filterNewsList;
	public SArrayList typeList;
	public SArrayList isReadList;
	public SArrayList uFeederList;
	public FeedFragment feedFragObj;
	private ImageView splashScreen;
	private MainActivity mCtx;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);
		
		mCtx = this;
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mTabHost = (TabHost) findViewById(R.id.tabhostMain);
		splashScreen = (ImageView) findViewById(R.id.splashScreenIv);
		
		new Thread(new Runnable()
		{
			public void run()
			{
				try
				{
					Thread.sleep(5000);
					
					mCtx.runOnUiThread(new Runnable()
					{
						public void run()
						{
							try
							{
								splashScreen.setVisibility(View.GONE);
								mTabHost.setVisibility(View.VISIBLE);
							} catch (Exception e)
							{
								
							}
						}
					});
					
				} catch (InterruptedException e)
				{
				}
				
			}
		}).start();
		
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
		
		addFeeder();
		
		//tracker
		EasyTracker.getInstance(this).activityStart(this);  // Add this method.
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
	}
	
	@Override
	protected void onDestroy()
	{
		Log.i(TAG, "onDestroy");
		saveSettings();
		EasyTracker.getInstance(this).activityStop(this);
		super.onDestroy();
	}
	
	@Override
	protected void onPause()
	{
		super.onPause();
	}
	
	@Override
	protected void onStop()
	{
		super.onStop();
	}
	
	public void addFeeder()
	{
		feedFragObj = null;
		// loading data first
		fList = new SArrayList();
		newsList = new SArrayList();
		typeList = new SArrayList();
		uFeederList = new SArrayList();
		isReadList = new SArrayList();
		
		Info.deserializeToGenericArrayList(this, "IsRead.csv", isReadList,
				IsRead.class);
		
		//compose isReadList
		/*
		Info.deserializeToGenericArrayList(this, "News.csv", newsList,
				News.class);
		newsList.sortByPriority();
		*/
		
		Info.deserializeToGenericArrayList(this, "UFeeder.csv", uFeederList,
				UFeeder.class);
		
		Info.deserializeToGenericArrayList(this, "Type.csv", typeList,
				Kind.class);
		
		// MANAGER RSS
		fList.add(new Feeder(getString(R.string.breaking_news_text),
				"http://www.manager.co.th/RSS/Home/Breakingnews.xml", true));
		fList.add(new Feeder(getString(R.string.other_text),
				"http://www.manager.co.th/RSS/Interview/Interview.xml", true));
		
		fList.add(new Feeder(getString(R.string.political_text),
				"http://www.manager.co.th/RSS/Politics/Politics.xml", true));
		
		fList.add(new Feeder(getString(R.string.country_text),
				"http://www.manager.co.th/RSS/Crime/Crime.xml", true));
		
		fList.add(new Feeder(getString(R.string.other_text),
				"http://www.manager.co.th/RSS/QOL/QOL.xml", true));
		
		fList.add(new Feeder(getString(R.string.country_text),
				"http://www.manager.co.th/RSS/Local/Local.xml", true));
		fList.add(new Feeder(getString(R.string.oversea_text),
				"http://www.manager.co.th/RSS/Around/Around.xml", true));
		
		fList.add(new Feeder(getString(R.string.oversea_text),
				"http://www.manager.co.th/RSS/IndoChina/IndoChina.xml", true));
		
		fList.add(new Feeder(getString(R.string.oversea_text),
				"http://www.manager.co.th/RSS/China/China.xml", true));
		
		fList.add(new Feeder(getString(R.string.economic_text),
				"http://www.manager.co.th/RSS/Business/Business.xml", true));
		fList.add(new Feeder(getString(R.string.economic_text),
				"http://www.manager.co.th/RSS/iBizChannel/iBizChannel.xml",
				true));
		
		fList.add(new Feeder(getString(R.string.economic_text),
				"http://www.manager.co.th/RSS/StockMarket/StockMarket.xml",
				true));
		fList.add(new Feeder(getString(R.string.economic_text),
				"http://www.manager.co.th/RSS/MutualFund/MutualFund.xml", true));
		
		fList.add(new Feeder(getString(R.string.economic_text),
				"http://www.manager.co.th/RSS/SMEs/SMEs.xml", true));
		
		fList.add(new Feeder(getString(R.string.economic_text),
				"http://www.manager.co.th/RSS/Motoring/Motoring.xml", true));
		
		fList.add(new Feeder(getString(R.string.techonology_text),
				"http://www.manager.co.th/RSS/Cyberbiz/Cyberbiz.xml", true));
		
		fList.add(new Feeder(getString(R.string.techonology_text),
				"http://www.manager.co.th/RSS/CBizReview/CBizReview.xml", true));
		
		fList.add(new Feeder(getString(R.string.techonology_text),
				"http://www.manager.co.th/RSS/Telecom/Telecom.xml", true));
		
		fList.add(new Feeder(getString(R.string.techonology_text),
				"http://www.manager.co.th/RSS/Telecom/Telecom.xml", true));
		
		fList.add(new Feeder(getString(R.string.techonology_text),
				"http://www.manager.co.th/RSS/Game/Game.xml", true));
		
		fList.add(new Feeder(getString(R.string.sport_text),
				"http://www.manager.co.th/RSS/Sport/Sport.xml", true));
		
		fList.add(new Feeder(getString(R.string.entertainment_text),
				"http://www.manager.co.th/RSS/Entertainment/Entertainment.xml",
				true));
		
		fList.add(new Feeder(getString(R.string.study_text),
				"http://www.manager.co.th/RSS/Campus/Campus.xml", true));
		
		fList.add(new Feeder(getString(R.string.other_text),
				"http://www.manager.co.th/RSS/Celeb/Celeb.xml", true));
		
		fList.add(new Feeder(getString(R.string.lifestyle_text),
				"http://www.manager.co.th/RSS/Family/Family.xml", true));
		
		fList.add(new Feeder(getString(R.string.lifestyle_text),
				"http://www.manager.co.th/RSS/Lady/Lady.xml", true));
		
		fList.add(new Feeder(getString(R.string.lifestyle_text),
				"http://www.manager.co.th/RSS/Travel/Travel.xml", true));
		// THAIRATH RSS
		fList.add(new Feeder(getString(R.string.breaking_news_text),
				"http://www.thairath.co.th/rss/news.xml", true));
		fList.add(new Feeder(getString(R.string.political_text),
				"http://www.thairath.co.th/rss/pol.xml", true));
		fList.add(new Feeder(getString(R.string.country_text),
				"http://www.thairath.co.th/rss/region.xml", true));
		fList.add(new Feeder(getString(R.string.entertainment_text),
				"http://www.thairath.co.th/rss/ent.xml", true));
		fList.add(new Feeder(getString(R.string.sport_text),
				"http://www.thairath.co.th/rss/sport.xml", true));
		fList.add(new Feeder(getString(R.string.lifestyle_text),
				"http://www.thairath.co.th/rss/life.xml", true));
		fList.add(new Feeder(getString(R.string.lifestyle_text),
				"http://www.thairath.co.th/rss/life.xml ", true));
		fList.add(new Feeder(getString(R.string.techonology_text),
				"http://www.thairath.co.th/rss/tech.xml", true));
		fList.add(new Feeder(getString(R.string.economic_text),
				"http://www.thairath.co.th/rss/eco.xml", true));
		fList.add(new Feeder(getString(R.string.study_text),
				"http://www.thairath.co.th/rss/edu.xml", true));
		fList.add(new Feeder(getString(R.string.oversea_text),
				"http://www.thairath.co.th/rss/oversea.xml", true));
		// DAILYNEWS RSS
				fList.add(new Feeder(getString(R.string.breaking_news_text),
						"http://www.dailynews.co.th/rss/rss.xml", true));
				fList.add(new Feeder(getString(R.string.breaking_news_text),
						"http://www.dailynews.co.th/rss/popular.xml", true));
				
				
				
				fList.add(new Feeder(getString(R.string.political_text),
						"http://www.dailynews.co.th/rss/news_politics.xml", true));
				
				fList.add(new Feeder(getString(R.string.country_text),
						"http://www.dailynews.co.th/rss/news_crime.xml", true));
				
				fList.add(new Feeder(getString(R.string.country_text),
						"http://www.dailynews.co.th/rss/news_thailand.xml", true));
				fList.add(new Feeder(getString(R.string.oversea_text),
						"http://www.dailynews.co.th/rss/news_world.xml", true));
				fList.add(new Feeder(getString(R.string.sport_text),
						"http://www.dailynews.co.th/rss/news_sport.xml", true));
				fList.add(new Feeder(getString(R.string.techonology_text),
						"http://www.dailynews.co.th/rss/news_technology.xml", true));
				fList.add(new Feeder(getString(R.string.entertainment_text),
						"http://www.dailynews.co.th/rss/news_entertainment.xml", true));
				fList.add(new Feeder(getString(R.string.economic_text),
						"http://www.dailynews.co.th/rss/news_business.xml", true));
				fList.add(new Feeder(getString(R.string.lifestyle_text),
						"http://www.dailynews.co.th/rss/news_society.xml", true));
				fList.add(new Feeder(getString(R.string.economic_text),
						"http://www.dailynews.co.th/rss/news_business.xml", true));
				
				fList.add(new Feeder(getString(R.string.study_text),
						"http://www.dailynews.co.th/rss/news_education.xml", true));
				fList.add(new Feeder(getString(R.string.other_text),
						"http://www.dailynews.co.th/rss/news_agriculture.xml", true));
		
		// MATICHON RSS
		fList.add(new Feeder(getString(R.string.breaking_news_text),
				"http://www.matichon.co.th/rss/news_article.xml", true));
		fList.add(new Feeder(getString(R.string.breaking_news_text),
				"http://www.matichon.co.th/rss/news_conspicuous.xml", true));
		fList.add(new Feeder(getString(R.string.country_text),
				"http://www.matichon.co.th/rss/news_monitor.xml", true));
		fList.add(new Feeder(getString(R.string.other_text),
				"http://www.matichon.co.th/rss/news_columns.xml", true));
		fList.add(new Feeder(getString(R.string.political_text),
				"http://www.matichon.co.th/rss/news_politic.xml", true));
		fList.add(new Feeder(getString(R.string.country_text),
				"http://www.matichon.co.th/rss/news_inbound.xml", true));
		fList.add(new Feeder(getString(R.string.economic_text),
				"http://www.matichon.co.th/rss/news_business.xml", true));
		fList.add(new Feeder(getString(R.string.oversea_text),
				"http://www.matichon.co.th/rss/news_world.xml", true));
		fList.add(new Feeder(getString(R.string.sport_text),
				"http://www.matichon.co.th/rss/news_sport.xml", true));
		fList.add(new Feeder(getString(R.string.entertainment_text),
				"http://www.matichon.co.th/rss/news_entertainment.xml", true));
		fList.add(new Feeder(getString(R.string.lifestyle_text),
				"http://www.matichon.co.th/rss/news_lifestyle.xml", true));
		fList.add(new Feeder(getString(R.string.other_text),
				"http://www.matichon.co.th/rss/news_cover.xml", true));
		
		// KOMSHUDLEOK
		fList.add(new Feeder(getString(R.string.breaking_news_text),
				"http://www.komchadluek.net/rss/news_widget.xml", true));
		fList.add(new Feeder(getString(R.string.political_text),
				"http://www.komchadluek.net/rss/politic.xml", true));
		fList.add(new Feeder(getString(R.string.entertainment_text),
				"http://www.komchadluek.net/rss/entertainment.xml", true));
		fList.add(new Feeder(getString(R.string.country_text),
				"http://www.komchadluek.net/rss/crime.xml", true));
		fList.add(new Feeder(getString(R.string.sport_text),
				"http://www.komchadluek.net/rss/sport.xml", true));
		fList.add(new Feeder(getString(R.string.lifestyle_text),
				"http://www.komchadluek.net/rss/lifestyle.xml", true));
		fList.add(new Feeder(getString(R.string.lifestyle_text),
				"http://www.komchadluek.net/rss/drink-eat-travel.xml", true));
		fList.add(new Feeder(getString(R.string.economic_text),
				"http://www.komchadluek.net/rss/agriculture.xml", true));
		fList.add(new Feeder(getString(R.string.oversea_text),
				"http://www.komchadluek.net/rss/foreign.xml", true));
		fList.add(new Feeder(getString(R.string.other_text),
				"http://www.komchadluek.net/rss/amulet.xml", true));
		fList.add(new Feeder(getString(R.string.other_text),
				"http://www.komchadluek.net/rss/horoscope.xml", true));
		fList.add(new Feeder(getString(R.string.country_text),
				"http://www.komchadluek.net/rss/local.xml", true));
		fList.add(new Feeder(getString(R.string.other_text),
				"http://www.komchadluek.net/rss/unclecham.xml", true));
		fList.add(new Feeder(getString(R.string.economic_text),
				"http://www.komchadluek.net/rss/economic.xml", true));
		fList.add(new Feeder(getString(R.string.lifestyle_text),
				"http://www.komchadluek.net/rss/homecar.xml", true));
		
		fList.add(new Feeder(getString(R.string.techonology_text),
				"http://www.komchadluek.net/rss/scienceit.xml", true));
		fList.add(new Feeder(getString(R.string.study_text),
				"http://www.komchadluek.net/rss/artculture.xml", true));
		
		fList.add(new Feeder(getString(R.string.study_text),
				"http://www.komchadluek.net/rss/education.xml", true));
		
		// BLOGNONE
		fList.add(new Feeder(getString(R.string.techonology_text),
				"http://www.blognone.com/atom.xml", true));
		// THUM UP
		fList.add(new Feeder(getString(R.string.techonology_text),
				"http://www.thumbsup.in.th/feed/", true));
		// CH3
		fList.add(new Feeder(getString(R.string.breaking_news_text),
				"http://www.krobkruakao.com/rss/News.rss", true));
		fList.add(new Feeder(getString(R.string.country_text),
				"http://www.krobkruakao.com/rss/SocialNews.rss", true));
		fList.add(new Feeder(getString(R.string.economic_text),
				"http://www.krobkruakao.com/rss/EconomicNews.rss", true));
		fList.add(new Feeder(getString(R.string.political_text),
				"http://www.krobkruakao.com/rss/PoliticsNews.rss", true));
		fList.add(new Feeder(getString(R.string.oversea_text),
				"http://www.krobkruakao.com/rss/InternationalNews.rss", true));
		fList.add(new Feeder(getString(R.string.entertainment_text),
				"http://www.krobkruakao.com/rss/EntertainmentNews.rss", true));
		
		fList.add(new Feeder(getString(R.string.other_text),
				"http://www.krobkruakao.com/rss/PRNews.rss", true));
		
		fList.add(new Feeder(getString(R.string.country_text),
				"http://www.krobkruakao.com/rss/CrimeNews.rss", true));
		
		fList.add(new Feeder(getString(R.string.other_text),
				"http://www.krobkruakao.com/rss/RoyalNews.rss", true));
		fList.add(new Feeder(getString(R.string.other_text),
				"http://www.krobkruakao.com/rss/ArticleHonorNews.rss", true));
		fList.add(new Feeder(getString(R.string.other_text),
				"http://www.krobkruakao.com/rss/RoyalRemarkNews.rss", true));
		
		fList.add(new Feeder(getString(R.string.sport_text),
				"http://www.krobkruakao.com/rss/SportNews.rss", true));
		fList.add(new Feeder(getString(R.string.country_text),
				"http://www.krobkruakao.com/rss/RegionalNews.rss", true));
		
		fList.add(new Feeder(getString(R.string.techonology_text),
				"http://www.krobkruakao.com/rss/TechnologyNews.rss", true));
		
		fList.add(new Feeder(getString(R.string.other_text),
				"http://www.krobkruakao.com/rss/OtherNews.rss", true));
		fList.add(new Feeder(getString(R.string.political_text),
				"http://www.krobkruakao.com/rss/AnalysisNews.rss", true));
		
		fList.add(new Feeder(getString(R.string.science_text),
				"http://www.krobkruakao.com/rss/EnvironmentalNews.rss", true));
		// Sanook
		fList.add(new Feeder(getString(R.string.breaking_news_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/news.index.xml",
				true));
		fList.add(new Feeder(getString(R.string.political_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/news.politic.xml",
				true));
		
		fList.add(new Feeder(getString(R.string.country_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/news.crime.xml",
				true));
		
		fList.add(new Feeder(getString(R.string.oversea_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/news.world.xml",
				true));
		fList.add(new Feeder(
				getString(R.string.economic_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/news.economic.xml",
				true));
		
		fList.add(new Feeder(
				getString(R.string.entertainment_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/news.entertain.xml",
				true));
		
		fList.add(new Feeder(getString(R.string.breaking_news_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/hot.news.xml",
				true));
		
		fList.add(new Feeder(
				getString(R.string.entertainment_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/hot.entertain.xml",
				true));
		
		fList.add(new Feeder(getString(R.string.lifestyle_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/hot.variety.xml",
				true));
		
		fList.add(new Feeder(
				getString(R.string.techonology_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/hot.technology.xml",
				true));
		
		fList.add(new Feeder(getString(R.string.sport_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/sport.world.xml",
				true));
		fList.add(new Feeder(getString(R.string.sport_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/sport.scoop.xml",
				true));
		fList.add(new Feeder(
				getString(R.string.sport_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/sport.football.xml",
				true));
		fList.add(new Feeder(getString(R.string.sport_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/sport.result.xml",
				true));
		fList.add(new Feeder(getString(R.string.sport_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/sport.golf.xml",
				true));
		fList.add(new Feeder(getString(R.string.sport_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/sport.tennis.xml",
				true));
		fList.add(new Feeder(getString(R.string.sport_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/sport.boxing.xml",
				true));
		fList.add(new Feeder(getString(R.string.sport_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/sport.f1.xml",
				true));
		
		fList.add(new Feeder(getString(R.string.entertainment_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/movie.news.xml",
				true));
		fList.add(new Feeder(getString(R.string.entertainment_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/movie.now.xml",
				true));
		fList.add(new Feeder(getString(R.string.entertainment_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/movie.coming.xml",
				true));
		fList.add(new Feeder(getString(R.string.entertainment_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/movie.drama.xml",
				true));
		fList.add(new Feeder(getString(R.string.entertainment_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/music.news.xml",
				true));
		
		fList.add(new Feeder(
				getString(R.string.techonology_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/hitech.computer.index.xml",
				true));
		fList.add(new Feeder(
				getString(R.string.techonology_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/hitech.download.index.xml",
				true));
		fList.add(new Feeder(
				getString(R.string.techonology_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/hitech.topdownload.index.xml",
				true));
		fList.add(new Feeder(
				getString(R.string.techonology_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/hitech.digital_camera.index.xml",
				true));
		fList.add(new Feeder(
				getString(R.string.techonology_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/hitech.mobile.index.xml",
				true));
		fList.add(new Feeder(
				getString(R.string.techonology_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/hitech.gadget.index.xml",
				true));
		fList.add(new Feeder(
				getString(R.string.techonology_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/hitech.home_electronic.index.xml",
				true));
		fList.add(new Feeder(getString(R.string.techonology_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/hitech.news.xml",
				true));
		fList.add(new Feeder(getString(R.string.techonology_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/game.news.xml",
				true));
		fList.add(new Feeder(getString(R.string.techonology_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/game.hit.xml",
				true));
		fList.add(new Feeder(
				getString(R.string.techonology_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/game.tournament.xml",
				true));
		
		fList.add(new Feeder(getString(R.string.lifestyle_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/men.instyle.xml",
				true));
		fList.add(new Feeder(getString(R.string.lifestyle_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/men.grooming.xml",
				true));
		fList.add(new Feeder(getString(R.string.lifestyle_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/men.work.xml",
				true));
		fList.add(new Feeder(getString(R.string.lifestyle_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/men.play.xml",
				true));
		fList.add(new Feeder(getString(R.string.lifestyle_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/men.health.xml",
				true));
		fList.add(new Feeder(
				getString(R.string.lifestyle_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/horoscope.index.xml",
				true));
		fList.add(new Feeder(getString(R.string.lifestyle_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/travel.index.xml",
				true));
		fList.add(new Feeder(getString(R.string.lifestyle_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/women.mom.xml",
				true));
		fList.add(new Feeder(
				getString(R.string.study_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/campus.teenzone.xml",
				true));
		fList.add(new Feeder(getString(R.string.study_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/campus.ulife.xml",
				true));
		fList.add(new Feeder(
				getString(R.string.other_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/campus.uwrite.xml",
				true));
		fList.add(new Feeder(getString(R.string.other_text),
				"http://rssfeeds.sanook.com/rss/feeds/sanook/sex.index.xml",
				true));
		
		
		
		
		// regenerate typeList from feeder
		for (int i = 0; i < this.fList.size(); i++)
		{
			Feeder fdObj = (Feeder) this.fList.get(i);
			typeList.add(new Kind(fdObj.kind, true));
			
		}
		// regenerate typeList from feeder
		for (int i = 0; i < this.fList.size(); i++)
		{
			Feeder fdObj = (Feeder) this.fList.get(i);
			if (fdObj.name.equalsIgnoreCase("thairath"))
			{
				uFeederList.add(new UFeeder(fdObj.name, true));
			} else
			{
				uFeederList.add(new UFeeder(fdObj.name, false));
			}
		}
		
	}
	
	public void saveSettings(){
		
		if(filterNewsList !=null && uFeederList !=null && typeList != null){
			
			for(int i=0;i<filterNewsList.size();i++){
				News newObj =  (News) filterNewsList.get(i) ;
				if(newObj.isRead){
					isReadList.add(new IsRead(newObj.link));				
				}
			}
			
			Info.serializeFromArrayList(this, "IsRead.csv", isReadList,
					IsRead.class);
			
			Info.serializeFromArrayList(this, "UFeeder.csv", uFeederList,
					UFeeder.class);
			Info.serializeFromArrayList(this, "Type.csv", typeList, Kind.class);
			
		}
	}
}
