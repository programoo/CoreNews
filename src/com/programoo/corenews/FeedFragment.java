package com.programoo.corenews;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.joda.time.DateTime;
import org.joda.time.Duration;

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
import com.programoo.snews.R;

public class FeedFragment extends SherlockFragment implements
		OnItemClickListener {
	private String tag = this.getClass().getSimpleName();
	private View newsFragment;
	FeedListViewAdapter ardap;
	private ListView lv;
	private int counter = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.newsFragment = inflater.inflate(R.layout.news_fragment, container,
				false);
		lv = (ListView) newsFragment.findViewById(R.id.list1Fragment);
		ardap = new FeedListViewAdapter(getActivity(), Info.newsList);
		lv.setAdapter(ardap);
		Log.d(tag, "onCreateView");
		lv.setOnItemClickListener(this);

		return newsFragment;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		new RequestTask().execute("http://m.dailynews.co.th/");
		new RequestTask().execute("http://m.thairath.co.th/");

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	public void reloadViewAfterRequestTaskComplete() {
		Log.d(tag, "reloadViewAfterRequestTaskComplete");

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

			// ++counter;

			// if (counter == Info.newsList.size()) {
			Info.sortNewsList();
			// }

			reloadViewAfterRequestTaskComplete();
		}

		public void thairathParser(String result) {

			// prevent error when internet connection down
			try {
				extract = result.split("<div id=\"content\">");
			} catch (NullPointerException e) {
				e.printStackTrace();
				Log.e(tag, "null response");
				Info.newsList.remove(n);
				return;
			}

			newsContent = extract[2].split("</div>")[0];

			title = result.split("<title>")[1].split(" - ")[0];
			time = result.split("<p class=\"time\">")[1].split("</p>")[0];

			imgUrl = newsContent.split("<img class=\"heading center\" src=\"")[1]
					.split("/>")[0].replaceAll("\"", "").replaceAll(" ", "");

			// format <div class="time">15 กันยายน 2556, 15:08 น.</div>

			int year = (Integer.parseInt(time.split(",")[0].split(" ")[2])) - 543;// convert
																					// to
																					// bc
			int month = this.getMonthInteger(time.split(",")[0].split(" ")[1]);
			int day = Integer.parseInt(time.split(",")[0].split(" ")[0]);
			int hour = Integer
					.parseInt(time.split(",")[1].split(":")[0].trim());
			int minute = Integer
					.parseInt(time.split(":")[1].trim().split(" ")[0]);

			// joda time
			DateTime newsTime = new DateTime(year, month, day, hour, minute, 0,
					0);
			DateTime currentTime = new DateTime();

			Log.i(tag, "start: " + newsTime.getMillis() + "");
			Log.i(tag, "end: " + currentTime.getMillis() + "");

			Duration dur = new Duration(newsTime, currentTime);
			System.out.println("input time: " + year + "," + month + "," + day
					+ "," + hour + "," + minute);
			System.out.println("joda time: " + dur.getMillis() / 1000);

			Log.i(tag,
					dur.getStandardSeconds() + "," + dur.getStandardMinutes()
							+ "," + dur.getStandardHours() + ","
							+ dur.getStandardDays());

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
			this.n.time = time;
			this.n.unixTime = newsTime.getMillis();
			this.n.stampTime = currentTime.getMillis();

		}

		public void dailyNewsParser(String result) {

			try {
				title = result.split("<title>")[1].split(" | ")[0];
				imgUrl = result.split("<div class=\"main-image\"><img src=\"")[1]
						.split("\"")[0];
				time = result.split("<div class=\"published-date\">")[1]
						.split("</div>")[0];

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
				this.n.time = time;
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
			Log.d(tag, "response: " + "onPostExecute");

			this.responseStringParser(result);
		}

		public void responseStringParser(String result) {

			if (this.providerUrl.equalsIgnoreCase("http://m.thairath.co.th/")) {
				this.mThairathParser(result);
			}

			if (this.providerUrl.equalsIgnoreCase("http://m.dailynews.co.th/")) {
				this.mDailyNewsParser(result);
			}

		}

		public void mDailyNewsParser(String result) {
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
		}

		public void mThairathParser(String result) {
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
		}

	}

}