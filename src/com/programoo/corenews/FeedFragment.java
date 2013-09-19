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
	private int imageId;
	private News newsUnImgFocus;
	private HashMap<String,Integer> providerImgIdx ;

	String blognone = "http://www.blognone.com/atom.xml";
	String thairath = "http://www.thairath.co.th/rss/news.xml";
	String dailynews = "http://www.dailynews.co.th/rss.xml";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		formatter = DateTimeFormat.forPattern("dd/MM/yy HH:mm:ss");
		aq = new AQuery(getActivity());
		imageId = 0;
		providerImgIdx = new HashMap<String,Integer>();
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
		//String url = "http://www.blognone.com/atom.xml";
		//String url = "http://www.thairath.co.th/rss/news.xml";
		//String url = "http://www.dailynews.co.th/rss.xml";
		//String url = "http://www.komchadluek.net/rss.php";

		//aq.sync(  aq.ajax(blognone, XmlDom.class, this, "picasaCb") );
		
		//String url = "http://www.google.com/uds/GnewsSearch?q=Obama&v=1.0";
        /*
		AjaxCallback<String> cb = new AjaxCallback<String>();           
		cb.url(blognone).type(String.class);  
		
		aq.sync(cb);
		*/
		//aq.ajax(thairath, XmlDom.class, this, "picasaCb");
		//aq.ajax(dailynews, XmlDom.class, this, "picasaCb");
		//

	}

	public void picasaCb(String url, XmlDom xml, AjaxStatus status) {

		List<XmlDom> entries = xml.tags("item");
		List<String> titles = new ArrayList<String>();

		tempList = new ArrayList<News>();

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
	
	public void reloadView(){
		FeedListViewAdapter ardap = new FeedListViewAdapter(getActivity(),
				Info.newsList);
		lv.setAdapter(ardap);
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
				//fetch synchronous
				
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
							
							
							
							
							
							providerImgIdx.put(Info.getNewsByLink(url).provider, i);
							
							//imageId = i;
							parseImageForSpecificProviderByImageId();
							Log.i(TAG,"start parsing image id: "+imageId);
							break;
						}

					} else {
						break;
					}

				}
			}

		});

	}

	public void parseImageForSpecificProviderByImageId() {

		for (int i = 0; i < this.tempList.size(); i++) {
			//if (this.tempList.get(i).imgUrl == null) {
				newsUnImgFocus = this.tempList.get(i);
				//if(false)
				aq.ajax(this.tempList.get(i).link, String.class,
						new AjaxCallback<String>() {

							@Override
							public void callback(String url, String html,
									AjaxStatus status) {
								// parser for that URL
								String imgUrl[] = html.split("<img");
								Log.i(TAG, "image2: " + imgUrl.length);
								int indexFilter=0;
								for (int i = 0; i < imgUrl.length; i++) {
									//Log.i(TAG,"parse this: "+imgUrl[i].split("src=\"")[1].split("\"")[0]+"");
									
									if (imgUrl[i].split("src=\"").length >= 2) {
										
										String imgUrlReal = imgUrl[i].split("src=\"")[1]
												.split("\"")[0];
										if (imgUrlReal.indexOf(".js") == -1
												&& imgUrlReal.indexOf("http:") != -1) {
											
											//Integer a = providerImgIdx.get(Info.getNewsByLink(url).provider);
											
											if(indexFilter == providerImgIdx.get(Info.getNewsByLink(url).provider) ){
												Log.i(TAG, "real SETTING THIS IMAGE: " + imgUrlReal);
												Info.getNewsByLink(url).imgUrl = imgUrlReal;
												reloadView();
												
												
												++indexFilter;
											}
											
											//imgUrlList2.add(imgUrlReal);
										}
									}
								}
								
								
								
								
								

							}
						});

			//}
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

	private class RequestNewsContent extends AsyncTask<String, String, String> {
		private String tag = this.getClass().getSimpleName();
		private News n;
		private HttpClient httpclient;
		private HttpResponse response;
		private String responseString = null;
		private StatusLine statusLine;
		private ByteArrayOutputStream out;
		private String extract[];
		private String newsContent;
		private String title;
		private String time;
		private String imgUrl;

		public RequestNewsContent(News n) {
			this.n = n;
		}

		@Override
		protected String doInBackground(String... uri) {

			httpclient = new DefaultHttpClient();

			try {
				response = httpclient.execute(new HttpGet(uri[0]));
				statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);

			if (n.provider.equalsIgnoreCase("http://m.thairath.co.th/")) {
				this.thairathParser(result);
			}
			if (n.provider.equalsIgnoreCase("http://m.dailynews.co.th/")) {
				this.dailyNewsParser(result);
			}
			if (n.provider.equalsIgnoreCase("http://www.blognone.com/")) {

			}

			Info.sortNewsList();
			reloadViewAfterRequestTaskComplete();
		}

		public void thairathParser(String result) {

			// prevent error when internet connection down
			try {
				extract = result.split("<div id=\"content\">");

				newsContent = extract[2].split("</div>")[0];

				title = result.split("<title>")[1].split("</title>")[0];
				title = title.replace("&quot;", "");

				time = result.split("<p class=\"time\">")[1].split("</p>")[0];

				imgUrl = newsContent
						.split("<img class=\"heading center\" src=\"")[1]
						.split("\"")[0];

				// re-scale img size
				imgUrl = imgUrl.replace("40.jpg", "256.jpg");

				Log.i(tag, imgUrl);

				// format <div class="time">15 กันยายน 2556, 15:08 น.</div>

				int year = (Integer.parseInt(time.split(",")[0].split(" ")[2])) - 543;// convert
																						// to
																						// bc
				int month = this
						.getMonthInteger(time.split(",")[0].split(" ")[1]);
				int day = Integer.parseInt(time.split(",")[0].split(" ")[0]);
				int hour = Integer.parseInt(time.split(",")[1].split(":")[0]
						.trim());
				int minute = Integer.parseInt(time.split(":")[1].trim().split(
						" ")[0]);

				// joda time
				DateTime newsTime = new DateTime(year, month, day, hour,
						minute, 0, 0);
				DateTime currentTime = new DateTime();

				Log.i(tag, "start: " + newsTime.getMillis() + "");
				Log.i(tag, "end: " + currentTime.getMillis() + "");

				Duration dur = new Duration(newsTime, currentTime);

				if (dur.getStandardDays() > 0) {
					time = dur.getStandardDays() + " "
							+ getString(R.string.pass_day_text);
				} else if (dur.getStandardHours() > 0) {
					time = dur.getStandardHours() + " "
							+ getString(R.string.pass_hour_text);
				} else if (dur.getStandardMinutes() > 0) {
					time = dur.getStandardMinutes() + " "
							+ getString(R.string.pass_minute_text);
				} else {
					time = getString(R.string.pass_second_text);
				}

				this.n.title = title;
				this.n.imgUrl = imgUrl;
				this.n.description = newsContent;
				this.n.showtime = time;
				this.n.unixTime = newsTime.getMillis();
				this.n.stampTime = currentTime.getMillis();

			} catch (Exception e) {
				e.printStackTrace();
				Log.e(tag, "null response");
				Info.newsList.remove(n);
				return;
			}

		}

		public void dailyNewsParser(String result) {

			try {
				title = result.split("<title>")[1].split("</title>")[0];
				title = title.replace("&quot;", "");

				imgUrl = result.split("<div class=\"main-image\"><img src=\"")[1]
						.split("\"")[0];
				time = result.split("<div class=\"published-date\">")[1]
						.split("</div>")[0];
				Log.i(tag, imgUrl);
				int year = (Integer.parseInt(time.split(" ")[3])) - 543;// convert
				int month = this.getMonthInteger(time.split(" ")[2]);
				int day = Integer.parseInt(time.split(" ")[1]);
				// <div class="published-date">วันศุกร์ที่ 13 กันยายน 2556 เวลา
				// 22:09 น.</div>
				int minute = Integer.parseInt(time.split(" ")[5].split(":")[1]);
				int hour = Integer.parseInt(time.split(" ")[5].split(":")[0]);

				DateTime newsTime = new DateTime(year, month, day, hour,
						minute, 0, 0);
				DateTime currentTime = new DateTime();

				Duration dur = new Duration(newsTime, currentTime);

				if (dur.getStandardDays() > 0) {
					time = dur.getStandardDays() + " "
							+ getString(R.string.pass_day_text);
				} else if (dur.getStandardHours() > 0) {
					time = dur.getStandardHours() + " "
							+ getString(R.string.pass_hour_text);
				} else if (dur.getStandardMinutes() > 0) {
					time = dur.getStandardMinutes() + " "
							+ getString(R.string.pass_minute_text);
				} else {
					time = getString(R.string.pass_second_text);
				}

				this.n.title = title;
				this.n.imgUrl = imgUrl;
				this.n.description = newsContent;
				this.n.showtime = time;
				this.n.unixTime = newsTime.getMillis();
				this.n.stampTime = currentTime.getMillis();

			} catch (Exception e) {
				e.printStackTrace();
				Info.newsList.remove(this.n);
				return;
			}

		}

		public int getMonthInteger(String month) {
			if (month.equalsIgnoreCase(getString(R.string.january))) {
				return 1;
			} else if (month.equalsIgnoreCase(getString(R.string.february))) {
				return 2;
			} else if (month.equalsIgnoreCase(getString(R.string.march))) {
				return 3;
			} else if (month.equalsIgnoreCase(getString(R.string.april))) {
				return 4;
			} else if (month.equalsIgnoreCase(getString(R.string.may))) {
				return 5;
			} else if (month.equalsIgnoreCase(getString(R.string.june))) {
				return 6;
			} else if (month.equalsIgnoreCase(getString(R.string.july))) {
				return 7;
			} else if (month.equalsIgnoreCase(getString(R.string.august))) {
				return 8;
			} else if (month.equalsIgnoreCase(getString(R.string.september))) {
				return 9;
			} else if (month.equalsIgnoreCase(getString(R.string.october))) {
				return 10;
			} else if (month.equalsIgnoreCase(getString(R.string.november))) {
				return 11;
			} else if (month.equalsIgnoreCase(getString(R.string.december))) {
				return 12;
			}

			return 0;
		}

	}

	private class RequestTask extends AsyncTask<String, String, String> {
		private String tag = getClass().getSimpleName();
		private String providerUrl;

		@Override
		protected String doInBackground(String... uri) {
			this.providerUrl = uri[0];
			Log.i(tag, "request: " + this.providerUrl);
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				response = httpclient.execute(new HttpGet(uri[0]));
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// if result == null aleart message to user
			if (result == null) {
				new AlertDialog.Builder(getActivity())
						.setMessage(
								getString(R.string.internet_disconnect_aleart_msg))
						.setPositiveButton("OK",
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {
										// continue with delete
										return;
									}
								}).show();

			}

			Log.d(tag, "result: " + result);
			Log.d(tag, "response: " + "onPostExecute");
			if (this.providerUrl.equalsIgnoreCase("http://m.thairath.co.th/")) {
				this.mThairathParser(result);
			}

			if (this.providerUrl.equalsIgnoreCase("http://m.dailynews.co.th/")) {
				this.mDailyNewsParser(result);
			}

			if (this.providerUrl.equalsIgnoreCase("http://www.blognone.com/")) {
				this.mBlognoneParser(result);
			}

			if (this.providerUrl.equalsIgnoreCase("http://m.posttoday.com/")) {
				this.mPostodayParser(result);
			}

			if (this.providerUrl
					.equalsIgnoreCase("http://www.blognone.com/atom.xml/")) {
				this.blognoneXmlParser(result);
			}
		}

		public void blognoneXmlParser(String result) {
			// Log.i(tag,result);
			Info.longInfo(tag, result);
		}

		public void mBlognoneParser(String result) {
			try {

				// special case in blognone we parse complete in first step

				String a[] = result.split("<h2 itemprop=\"name\"><a href=\"");
				String img[] = result
						.split("<div class=\"node-image\" ><img src=\"");
				String newsTime[] = result.split("</span> on ");

				for (int i = 1; i < a.length; i++) {
					String link = a[i].split("\"")[0];
					String imgUrl = img[i].split("\"")[0];
					String splitWord = "<h2 itemprop=\"name\"><a href=\""
							+ link + "\" title=\"";
					String title = result.split(splitWord)[1].split("\"")[0];

					String time = newsTime[i].split("</span>")[0];
					time = time + ":00";
					DateTime dt = formatter.parseDateTime(time);
					Log.i(tag, time);
					Log.i(tag, link);
					String urlDetail = "http://m.blognone.com/" + link;

					News n = new News(this.providerUrl, link, link, urlDetail,
							"loading...");
					n.title = "Loading...";
					n.imgUrl = imgUrl;
					n.title = title;
					n.unixTime = dt.getMillis();
					n.showtime = getHumanLanguageTime(dt);

					Info.uniqueAdd(n);

				}
				Log.i(tag, "responseStringParser: " + this.providerUrl);
				/*
				 * // on post exucute for (int i = 0; i < Info.newsList.size();
				 * i++) { Log.i(tag, Info.newsList.get(i).urlContent); new
				 * RequestNewsContent(Info.newsList.get(i))
				 * .execute(Info.newsList.get(i).urlContent); }
				 */
				reloadViewAfterRequestTaskComplete();

			} catch (Exception e) {
				e.printStackTrace();

			}
		}

		public void mPostodayParser(String result) {
			try {

				// special case in blognone we parse complete in first step

				String a[] = result.split("<p class=\"thumbnail\"><a href=\"");

				for (int i = 1; i < a.length; i++) {
					String link = a[i].split("\"")[0];

					String imgUrl = a[i].split("src=\"")[1].split("\"")[0];
					String title = a[i].split("<img alt=\"")[1]
							.split("width=\"")[0];

					String time = a[i].split("<li class=\"date\">")[1]
							.split("</li>")[0];
					Log.i(tag, time);

					int day = Integer.parseInt(time.split(" ")[1]);
					int month = new DateTime().getMonthOfYear();// lazy code
																// edit later
					int year = new DateTime().getYear();

					String timeNews = time.split(", ")[1].replace(
							getString(R.string.n_string) + ".", ":00");
					int hour = Integer.parseInt(timeNews.split(":")[0]);
					int minute = Integer.parseInt(timeNews.split(":")[1]);

					DateTime dt = new DateTime(year, month, day, hour, minute,
							0, 0);

					Log.i(tag, time);
					Log.i(tag, timeNews);

					Log.i(tag, link);

					String urlDetail = link;

					News n = new News(this.providerUrl, link, link, urlDetail,
							"loading...");
					n.title = "Loading...";
					n.imgUrl = imgUrl;
					n.title = title;
					n.unixTime = dt.getMillis();
					n.showtime = getHumanLanguageTime(dt);

					Info.uniqueAdd(n);

				}
				Log.i(tag, "responseStringParser: " + this.providerUrl);
				/*
				 * // on post exucute for (int i = 0; i < Info.newsList.size();
				 * i++) { Log.i(tag, Info.newsList.get(i).urlContent); new
				 * RequestNewsContent(Info.newsList.get(i))
				 * .execute(Info.newsList.get(i).urlContent); }
				 */
				reloadViewAfterRequestTaskComplete();

			} catch (Exception e) {
				e.printStackTrace();

			}
		}

		public void mDailyNewsParser(String result) {
			try {

				String a[] = result.split(" <a href=\"/");
				for (int i = 1; i < a.length; i++) {
					String link = a[i].split("\"")[0];
					Log.i(tag, link);
					String urlDetail = this.providerUrl + link;

					News n = new News(this.providerUrl, link, link, urlDetail,
							"loading...");
					n.title = "Loading...";
					Info.uniqueAdd(n);

				}
				Log.i(tag, "responseStringParser");

				// on post exucute
				for (int i = 0; i < Info.newsList.size(); i++) {
					Log.i(tag, Info.newsList.get(i).urlContent);
					new RequestNewsContent(Info.newsList.get(i))
							.execute(Info.newsList.get(i).urlContent);
				}

			} catch (Exception e) {
				e.printStackTrace();

			}

		}

		public void mThairathParser(String result) {
			try {
				String a[] = result.split("<a href=\"/content/");
				for (int i = 1; i < a.length; i++) {
					String link = a[i].split("\">")[0];
					link = "/content/" + link;

					News n = new News(this.providerUrl, link, link,
							this.providerUrl + link, "loading...");
					n.title = "Loading...";
					Info.uniqueAdd(n);
				}
				Log.i(tag, "responseStringParser");

				// on post exucute
				for (int i = 0; i < Info.newsList.size(); i++) {
					new RequestNewsContent(Info.newsList.get(i))
							.execute(Info.newsList.get(i).urlContent);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	private class SynchronousHtmlFetch extends AsyncTask<String, String, String> {
		private String TAG = getClass().getSimpleName();
		private String url;

		@Override
		protected String doInBackground(String... uri) {
			this.url = uri[0];
			
			AjaxCallback<XmlDom> cb = new AjaxCallback<XmlDom>();           
			cb.url(blognone).type(XmlDom.class);  
			Log.i(TAG,"Starting fetching");
			aq.sync(cb);
			XmlDom xmlResponse = cb.getResult();
			AjaxStatus status = cb.getStatus();
			picasaCb(url,xmlResponse,status);
			
			AjaxCallback<XmlDom> cb1 = new AjaxCallback<XmlDom>();           
			cb1.url(dailynews).type(XmlDom.class);  
			Log.i(TAG,"Starting fetching");
			aq.sync(cb1);
			xmlResponse = cb1.getResult();
			status = cb1.getStatus();
			picasaCb(url,xmlResponse,status);
			/*
			cb.url(blognone).type(XmlDom.class);  
			Log.i(TAG,"Starting fetching");
			aq.sync(cb);
			xmlResponse = cb.getResult();
			status = cb.getStatus();
			picasaCb(url,xmlResponse,status);
			*/
			return "ok";
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Log.i(TAG,"Final fetching: "+result);
			reloadView();
			

		}

	}

	
}