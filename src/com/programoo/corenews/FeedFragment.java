package com.programoo.corenews;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.XmlDom;
import com.programoo.snews.R;

public class FeedFragment extends Fragment implements OnItemClickListener
{
	private String TAG = this.getClass().getSimpleName();
	private View newsFragment;
	FeedListViewAdapter ardap;
	private ListView lv;
	private AQuery aq;
	private ArrayList<News> tempList;
	private DateTimeFormatter formatter;
	private ArrayList<String> imgUrlList1;
	private int imgLocationIdx;
	private News newsUnImgFocus;
	private HashMap<String, Integer> providerImgIdx;
	
	String blognone = "http://www.blognone.com/atom.xml";
	String thairath = "http://www.thairath.co.th/rss/news.xml";
	String dailynews = "http://www.dailynews.co.th/rss.xml";
	int fileSize = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		formatter = DateTimeFormat.forPattern("dd/MM/yy HH:mm:ss");
		aq = new AQuery(getActivity());
		imgLocationIdx = 0;
		providerImgIdx = new HashMap<String, Integer>();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		this.newsFragment = inflater.inflate(R.layout.news_fragment, container,
				false);
		lv = (ListView) newsFragment.findViewById(R.id.list1Fragment);
		ardap = new FeedListViewAdapter(getActivity(), Info.newsList, aq);
		lv.setAdapter(ardap);
		Log.d(TAG, "onCreateView");
		lv.setOnItemClickListener(this);
		
		return newsFragment;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState)
	{
		super.onViewCreated(view, savedInstanceState);
		new SynchronousHtmlFetch().execute("http://www.blognone.com/atom.xml/");
	}
	
	public void picasaCb(String url, XmlDom xml, AjaxStatus status)
	{
		
		List<XmlDom> entries;
		tempList = new ArrayList<News>();
		List<String> titles = new ArrayList<String>();
		
		try
		{
			entries = xml.tags("item");
		} catch (NullPointerException e)
		{
			e.printStackTrace();
			return;
		}
		
		for (XmlDom entry : entries)
		{
			
			String titleStr = entry.text("title");
			String link = entry.text("link");
			String description = entry.text("description");
			String pubDate = entry.text("pubDate");
			String dcCreator = entry.text("dc:creator");
			
			News n = new News(url, titleStr, link, description, pubDate,
					dcCreator);
			n.imgUrl = descriptionParser(description);
			n.urlContent = link;
			tempList.add(n);
			Info.uniqueAdd(n);
			titles.add(titleStr);
			
		}
		
		// if null image found try to search for content
		// need 2 index for compare
		if (tempList.size() >= 5)
		{
			String urlTemp1 = tempList.get(tempList.size() / 2).link;
			AjaxCallback<String> cb = new AjaxCallback<String>();
			cb.url(urlTemp1).type(String.class);
			aq.sync(cb);
			Log.i(TAG, "fetching TEMPLATE1: " + urlTemp1);
			String xmlResponseContent = cb.getResult();
			AjaxStatus statusContent = cb.getStatus();
			diffImageUrlFirst(urlTemp1, xmlResponseContent, statusContent);
			
		}
		
		// reload view
	}
	
	public void reloadView()
	{
		FeedListViewAdapter ardap = new FeedListViewAdapter(getActivity(),
				Info.newsList, aq);
		lv.setAdapter(ardap);
		
		for (int i = 0; i < Info.newsList.size(); i++)
		{
			Log.i(TAG, "url: " + Info.newsList.get(i).link + ",,,,,,,,"
					+ "img: " + Info.newsList.get(i).imgUrl);
		}
	}
	
	public void diffImageUrlFirst(String url, String html, AjaxStatus status)
	{
		imgUrlList1 = new ArrayList<String>();
		String imgUrl1[] = html.split("<img");
		Log.i(TAG, "template image1: " + imgUrl1.length);
		
		for (int i = 0; i < imgUrl1.length; i++)
		{
			
			if (imgUrl1[i].split("src=\"").length >= 2)
			{
				String imgUrlReal = imgUrl1[i].split("src=\"")[1].split("\"")[0];
				
				if (imgUrlReal.indexOf(".js") == -1
						&& imgUrlReal.indexOf("http:") != -1)
				{
					Log.i(TAG, "template image1" + imgUrlReal);
					imgUrlList1.add(imgUrlReal);
				}
			}
		}
		// compare with next item
		// sampling and middle news list link
		String urlTemp1 = tempList.get((tempList.size() / 2) + 1).link;
		
		AjaxCallback<String> cb = new AjaxCallback<String>();
		cb.url(urlTemp1).type(String.class);
		aq.sync(cb);
		Log.i(TAG, "fetching TEMPLATE2: " + urlTemp1);
		String xmlResponseContent = cb.getResult();
		AjaxStatus statusContent = cb.getStatus();
		diffImageUrlSecond(urlTemp1, xmlResponseContent, statusContent);
		
	}
	
	public void diffImageUrlSecond(String url, String html, AjaxStatus status)
	{
		ArrayList<String> imgUrlList2 = new ArrayList<String>();
		
		String imgUrl[] = html.split("<img");
		Log.i(TAG, "template image2" + imgUrl.length);
		
		for (int i = 0; i < imgUrl.length; i++)
		{
			
			if (imgUrl[i].split("src=\"").length >= 2)
			{
				String imgUrlReal = imgUrl[i].split("src=\"")[1].split("\"")[0];
				if (imgUrlReal.indexOf(".js") == -1
						&& imgUrlReal.indexOf("http:") != -1)
				{
					Log.i(TAG, "template image2" + imgUrlReal);
					imgUrlList2.add(imgUrlReal);
				}
			}
		}
		for (int i = 0; i < imgUrlList1.size(); i++)
		{
			if (i < imgUrlList2.size())
			{
				Log.i(TAG, "COMPARE: " + imgUrlList1.get(i)
						+ ",,,,,,,,,,,,,,,,,,," + imgUrlList2.get(i));
				Log.i(TAG,
						"RESULT: "
								+ imgUrlList1.get(i).equals(imgUrlList2.get(i)));
				// FOUND !!
				if (!imgUrlList1.get(i).equals(imgUrlList2.get(i)))
				{
					
					providerImgIdx.put(Info.getNewsByLink(url).provider, i);
					imgLocationIdx = i;
					// imageId = i;
					Log.i(TAG, "FOUND!! start parsing image id: "
							+ imgLocationIdx);
					break;
				}
				
			} else
			{
				break;
			}
			
		}
	}
	
	public void callbackByLink(String url, String html, AjaxStatus status)
	{
		// parser for that URL
		String imgUrl[] = html.split("<img");
		Log.i(TAG, "CALL BACK HTML SIZE: " + html.length());
		Log.i(TAG, "CALL BACK BY IMAGE: " + imgUrl.length);
		int indexFilter = 0;
		boolean isFound = false;
		for (int i = 0; i < imgUrl.length; i++)
		{
			if (imgUrl[i].split("src=\"").length >= 2)
			{
				
				String imgUrlReal = imgUrl[i].split("src=\"")[1].split("\"")[0];
				Log.i(TAG, "image: " + imgUrlReal);
				if (imgUrlReal.indexOf(".js") == -1
						&& imgUrlReal.indexOf("http:") != -1)
				{
					
					if (indexFilter == imgLocationIdx)
					{
						Log.i(TAG, "real SETTING THIS IMAGE: " + imgUrlReal);
						Info.getNewsByLink(url).imgUrl = imgUrlReal;
						isFound = true;
					}
					++indexFilter;
					
				}
			}
		}
		
		// if don't find any image for some url select image that have longest
		// name to show user
		// this process below can remove it only use with MATICHON case
		int maxLongest = 0;
		
		if (isFound == false)
			for (int i = 0; i < imgUrl.length; i++)
			{
				
				if (Info.getNewsByLink(url).imgUrl == null)
					if (imgUrl[i].split("src=\"").length >= 2)
					{
						
						String imgUrlReal = imgUrl[i].split("src=\"")[1]
								.split("\"")[0];
						Log.i(TAG, "image: " + imgUrlReal);
						if (imgUrlReal.indexOf(".js") == -1
								&& imgUrlReal.indexOf("http:") != -1)
						{
							if (imgUrlReal.length() > maxLongest)
							{
								maxLongest = imgUrlReal.length();
								Info.getNewsByLink(url).imgUrl = imgUrlReal;
								Log.i(TAG, "real SETTING THIS NULL IMAGE: "
										+ imgUrlReal);
							}
							
						}
					}
			}
		
	}
	
	public String descriptionParser(String description)
	{
		String result = null;
		if (description.indexOf("img src=\"") != -1)
		{
			return description.split("img src=\"")[1].split("\"")[0];
		}
		
		return result;
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
	}
	
	public void reloadViewAfterRequestTaskComplete()
	{
		Log.d(TAG, "reloadViewAfterRequestTaskComplete");
		
		FeedListViewAdapter ardap = new FeedListViewAdapter(getActivity(),
				Info.newsList, aq);
		lv.setAdapter(ardap);
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
	{
		/*
		 * Intent it = new Intent(getActivity(), WebViewActivity.class);
		 * it.putExtra("url", Info.newsList.get(arg2).urlContent);
		 * it.putExtra("description", Info.newsList.get(arg2).description);
		 * startActivity(it);
		 * getActivity().overridePendingTransition(R.drawable.fadein,
		 * R.drawable.fadeout);
		 */
		final Dialog dialog = new Dialog(getActivity());
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.activity_webview);
		dialog.setCancelable(true);
		dialog.show();
		
		ImageView iv = (ImageView) dialog.findViewById(R.id.imageDialog);
		News n = (News) this.lv.getItemAtPosition(arg2);
		aq.id(iv).image(n.imgUrl, true, true, 200, 0);
		
		// WEBVIEW settings
		String html = "" + "<html>" + "<head>" + "<style type='text/css'>"
				+ "body {" + "font-family:serif;" + "color: #aaaaaa;"
				+ "background-color: #222222 }" + "</style>" + "</head>"
				+ "<body>" + Info.newsList.get(arg2).description
				+ "</body></html>";
		WebView myWebView = (WebView) dialog.findViewById(R.id.webview);
		WebSettings webSettings = myWebView.getSettings();
		webSettings.setDefaultFontSize(12);
		myWebView.loadDataWithBaseURL("file:///assets/", html, "text/html",
				"utf-8", null);
		
	}
	
	public String getHumanLanguageTime(DateTime newsTime)
	{
		String alreadyPassTime = "undefined";
		// joda time convert
		DateTime currentTime = new DateTime();
		Duration dur = new Duration(newsTime, currentTime);
		
		if (dur.getStandardDays() > 0)
		{
			alreadyPassTime = dur.getStandardDays() + " "
					+ getString(R.string.pass_day_text);
		} else if (dur.getStandardHours() > 0)
		{
			alreadyPassTime = dur.getStandardHours() + " "
					+ getString(R.string.pass_hour_text);
		} else if (dur.getStandardMinutes() > 0)
		{
			alreadyPassTime = dur.getStandardMinutes() + " "
					+ getString(R.string.pass_minute_text);
		} else
		{
			alreadyPassTime = getString(R.string.pass_second_text);
		}
		
		return alreadyPassTime;
	}
	
	private class SynchronousHtmlFetch extends
			AsyncTask<String, String, String>
	{
		private String TAG = getClass().getSimpleName();
		private String url;
		
		@Override
		protected String doInBackground(String... uri)
		{
			this.url = uri[0];
			ArrayList<String> urlList = new ArrayList<String>();
			// urlList.add(thairath);
			// urlList.add(dailynews);
			//urlList.add(blognone);
			// urlList.add("http://www.matichon.co.th/rss/news_article.xml");
			// urlList.add("www.posttoday.com/rss/src/breakingnews.xml");
			
			for (int i = 0; i < urlList.size(); i++)
			{
				fetchHomePage(urlList.get(i));
			}
			
			fetchHomePage(thairath);
			fetchHomePage(dailynews);
			fetchHomePage(blognone);
			fetchHomePage("www.posttoday.com/rss/src/breakingnews.xml");
			fetchHomePage("http://www.khaosod.co.th/rss/urgent_news.xml");
			fetchHomePage("http://www.matichon.co.th/rss/news_article.xml");
			fetchHomePage("http://www.manager.co.th/RSS/Game/Game.xml");
			fetchHomePage("http://www.komchadluek.net/rss/news_widget.xml");
			
			return "ok";
		}
		
		@Override
		protected void onPostExecute(String result)
		{
			super.onPostExecute(result);
			Log.i(TAG, "Final fetching: " + result + "," + fileSize);
			reloadView();
			
		}
		
		public void fetchHomePage(String urlRssXml)
		{
			AjaxCallback<XmlDom> cb1 = new AjaxCallback<XmlDom>();
			cb1.url(urlRssXml).type(XmlDom.class);
			Log.i(TAG, "Starting fetching");
			aq.sync(cb1);
			XmlDom xmlResponse1 = cb1.getResult();
			AjaxStatus status1 = cb1.getStatus();
			picasaCb(urlRssXml, xmlResponse1, status1);
			// fetch image content
			this.fetchContentSynchronous();
			
		}
		
		public void fetchContentSynchronous()
		{
			for (int i = 0; i < tempList.size(); i++)
			{
				
				AjaxCallback<String> cb = new AjaxCallback<String>();
				cb.url(tempList.get(i).link).type(String.class);
				aq.sync(cb);
				
				Log.i(TAG, "fetching content: " + tempList.get(i).link);
				
				String xmlResponseContent = cb.getResult();
				
				AjaxStatus statusContent = cb.getStatus();
				
				try{
					Log.i(TAG, "Result Size: " + xmlResponseContent.length());
					fileSize += xmlResponseContent.length();
					callbackByLink(tempList.get(i).link, xmlResponseContent,
							statusContent);
				}
				catch(NullPointerException e){
					e.printStackTrace();
				}
				
				
			}
		}
		
	}
	
}