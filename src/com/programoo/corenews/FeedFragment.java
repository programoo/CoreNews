package com.programoo.corenews;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.util.XmlDom;
import com.programoo.snews.R;

public class FeedFragment extends SherlockFragment implements
		OnItemClickListener {
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
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		formatter = DateTimeFormat.forPattern("dd/MM/yy HH:mm:ss");
		aq = new AQuery(getActivity());
		imgLocationIdx = 0;
		providerImgIdx = new HashMap<String, Integer>();
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.newsFragment = inflater.inflate(R.layout.news_fragment, container,
				false);
		lv = (ListView) newsFragment.findViewById(R.id.list1Fragment);
		ardap = new FeedListViewAdapter(getActivity(), Info.newsList);
		lv.setAdapter(ardap);
		Log.d(TAG, "onCreateView");
		lv.setOnItemClickListener(this);

		return newsFragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		// new RequestTask().execute("http://m.dailynews.co.th/");
		// new RequestTask().execute("http://m.thairath.co.th/");
		// new RequestTask().execute("http://www.blognone.com/");
		// new RequestTask().execute("http://m.posttoday.com/");
		new SynchronousHtmlFetch().execute("http://www.blognone.com/atom.xml/");

		// using Aquery for RSS feed reader
		// String url = "http://www.blognone.com/atom.xml";
		// String url = "http://www.thairath.co.th/rss/news.xml";
		// String url = "http://www.dailynews.co.th/rss.xml";
		// String url = "http://www.komchadluek.net/rss.php";

		// aq.sync( aq.ajax(blognone, XmlDom.class, this, "picasaCb") );

		// String url = "http://www.google.com/uds/GnewsSearch?q=Obama&v=1.0";
		/*
		 * AjaxCallback<String> cb = new AjaxCallback<String>();
		 * cb.url(blognone).type(String.class);
		 * 
		 * aq.sync(cb);
		 */
		// aq.ajax(thairath, XmlDom.class, this, "picasaCb");
		// aq.ajax(dailynews, XmlDom.class, this, "picasaCb");
		//

	}

	public void picasaCb(String url, XmlDom xml, AjaxStatus status) {

		List<XmlDom> entries;
		tempList = new ArrayList<News>();
		List<String> titles = new ArrayList<String>();

		try {
			entries = xml.tags("item");
		} catch (NullPointerException e) {
			e.printStackTrace();
			return;
		}

		for (XmlDom entry : entries) {

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
			// Log.i(TAG, titleStr + "," +
			// link+","+descriptionParser(description));
			titles.add(titleStr);

		}

		// if null image found try to search for content
		// need 2 index for compare
		if (tempList.size() >= 5) {
			String urlTemp1 = tempList.get(2).link;
			aq.ajax(urlTemp1, String.class, new AjaxCallback<String>() {

				@Override
				public void callback(String url, String html, AjaxStatus status) {
					diffImageUrl(html);
				}

			});

		}

		// reload view
	}

	public void reloadView() {
		FeedListViewAdapter ardap = new FeedListViewAdapter(getActivity(),
				Info.newsList);
		lv.setAdapter(ardap);

		for (int i = 0; i < Info.newsList.size(); i++) {
			Log.i("url: " + Info.newsList.get(i).link,
					"img: " + Info.newsList.get(i).imgUrl);
		}
	}

	public void diffImageUrl(String html) {
		imgUrlList1 = new ArrayList<String>();
		String imgUrl1[] = html.split("<img");
		Log.i(TAG, "real image1: " + imgUrl1.length);

		for (int i = 0; i < imgUrl1.length; i++) {

			if (imgUrl1[i].split("src=\"").length >= 2) {
				String imgUrlReal = imgUrl1[i].split("src=\"")[1].split("\"")[0];
				Log.i(TAG, "real image1 unuse: " + imgUrlReal);

				if (imgUrlReal.indexOf(".js") == -1
						&& imgUrlReal.indexOf("http:") != -1) {
					Log.i(TAG, "real image1: " + imgUrlReal);
					imgUrlList1.add(imgUrlReal);
				}
			}
		}
		// compare with next item

		String urlTemp1 = tempList.get(3).link;

		aq.ajax(urlTemp1, String.class, new AjaxCallback<String>() {
			ArrayList<String> imgUrlList2 = new ArrayList<String>();

			@Override
			public void callback(String url, String html, AjaxStatus status) {
				// fetch synchronous

				String imgUrl[] = html.split("<img");
				Log.i(TAG, "image2: " + imgUrl.length);

				for (int i = 0; i < imgUrl.length; i++) {

					if (imgUrl[i].split("src=\"").length >= 2) {
						String imgUrlReal = imgUrl[i].split("src=\"")[1]
								.split("\"")[0];
						if (imgUrlReal.indexOf(".js") == -1
								&& imgUrlReal.indexOf("http:") != -1) {
							Log.i(TAG, "real image2: " + imgUrlReal);
							imgUrlList2.add(imgUrlReal);
						}
					}
				}
				for (int i = 0; i < imgUrlList1.size(); i++) {
					if (i < imgUrlList2.size()) {
						Log.i(TAG, "COMPARE: " + imgUrlList1.get(i)
								+ ",,,,,,,,,,,,,,,,,,," + imgUrlList2.get(i));
						Log.i(TAG,
								"RESULT: "
										+ imgUrlList1.get(i).equals(
												imgUrlList2.get(i)));
						// FOUND !!
						if (!imgUrlList1.get(i).equals(imgUrlList2.get(i))) {

							providerImgIdx.put(
									Info.getNewsByLink(url).provider, i);
							imgLocationIdx = i;
							// imageId = i;
							Log.i(TAG, "start parsing image id: "
									+ imgLocationIdx);
							break;
						}

					} else {
						break;
					}

				}
			}

		});

	}

	public void callbackByLink(String url, String html, AjaxStatus status) {
		// parser for that URL
		String imgUrl[] = html.split("<img");
		Log.i(TAG, "CALL BACK HTML SIZE: " + html.length());
		Log.i(TAG, "CALL BACK BY IMAGE: " + imgUrl.length);
		int indexFilter = 0;
		for (int i = 0; i < imgUrl.length; i++) {
			// Log.i(TAG,"parse this: "+imgUrl[i].split("src=\"")[1].split("\"")[0]+"");

			if (imgUrl[i].split("src=\"").length >= 2) {

				String imgUrlReal = imgUrl[i].split("src=\"")[1].split("\"")[0];
				if (imgUrlReal.indexOf(".js") == -1
						&& imgUrlReal.indexOf("http:") != -1) {

					// Integer a =
					// providerImgIdx.get(Info.getNewsByLink(url).provider);

					if (indexFilter == imgLocationIdx) {
						Log.i(TAG, "real SETTING THIS IMAGE: " + imgUrlReal);
						Info.getNewsByLink(url).imgUrl = imgUrlReal;
						++indexFilter;
					}

					// imgUrlList2.add(imgUrlReal);
				}
			}
		}

	}

	public String descriptionParser(String description) {
		String result = null;
		if (description.indexOf("img src=\"") != -1) {
			return description.split("img src=\"")[1].split("\"")[0];
		}

		return result;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	public void reloadViewAfterRequestTaskComplete() {
		Log.d(TAG, "reloadViewAfterRequestTaskComplete");

		FeedListViewAdapter ardap = new FeedListViewAdapter(getActivity(),
				Info.newsList);
		lv.setAdapter(ardap);

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		Intent it = new Intent(getActivity(), WebViewActivity.class);
		it.putExtra("url", Info.newsList.get(arg2).urlContent);
		startActivity(it);
		getActivity().overridePendingTransition(R.drawable.fadein,
				R.drawable.fadeout);

	}

	public String getHumanLanguageTime(DateTime newsTime) {
		String alreadyPassTime = "undefined";
		// joda time convert
		DateTime currentTime = new DateTime();
		Duration dur = new Duration(newsTime, currentTime);

		if (dur.getStandardDays() > 0) {
			alreadyPassTime = dur.getStandardDays() + " "
					+ getString(R.string.pass_day_text);
		} else if (dur.getStandardHours() > 0) {
			alreadyPassTime = dur.getStandardHours() + " "
					+ getString(R.string.pass_hour_text);
		} else if (dur.getStandardMinutes() > 0) {
			alreadyPassTime = dur.getStandardMinutes() + " "
					+ getString(R.string.pass_minute_text);
		} else {
			alreadyPassTime = getString(R.string.pass_second_text);
		}

		return alreadyPassTime;
	}

	private class SynchronousHtmlFetch extends
			AsyncTask<String, String, String> {
		private String TAG = getClass().getSimpleName();
		private String url;

		@Override
		protected String doInBackground(String... uri) {
			this.url = uri[0];

			fetchHomePage(thairath);
			fetchHomePage(dailynews);
			fetchHomePage(blognone);
			fetchHomePage("www.posttoday.com/rss/src/breakingnews.xml");
			
			fetchHomePage("http://www.komchadluek.net/rss/news_widget.xml");

			fetchHomePage("http://www.khaosod.co.th/rss/urgent_news.xml");

			fetchHomePage("http://www.matichon.co.th/rss/news_article.xml");

			fetchHomePage("http://www.manager.co.th/RSS/Game/Game.xml");

			
			return "ok";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.i(TAG, "Final fetching: " + result + "," + fileSize);
			reloadView();

		}

		public void fetchHomePage(String urlRssXml) {
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

		public void fetchContentSynchronous() {
			for (int i = 0; i < tempList.size(); i++) {

				AjaxCallback<String> cb = new AjaxCallback<String>();
				cb.url(tempList.get(i).link).type(String.class);
				aq.sync(cb);

				Log.i(TAG, "Starting fetching: " + tempList.get(i).link);
				try {
					Thread.sleep(1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				String xmlResponseContent = cb.getResult();

				AjaxStatus statusContent = cb.getStatus();

				Log.i(TAG, "Result Size: " + xmlResponseContent.length());
				fileSize += xmlResponseContent.length();
				callbackByLink(tempList.get(i).link, xmlResponseContent,
						statusContent);

			}
		}
	}

}