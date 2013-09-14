package com.programoo.corenews;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		new RequestTask().execute("http://m.thairath.co.th/");
		// request follow new in list

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.newsFragment = inflater.inflate(R.layout.news_fragment, container,
				false);
		lv = (ListView) newsFragment.findViewById(R.id.list1Fragment);
		// ardap = new FeedListViewAdapter(getActivity(), Info.newsList);
		// lv.setAdapter(ardap);
		Log.d(tag, "onCreateView");
		lv.setOnItemClickListener(this);

		return newsFragment;
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

	}

	private class RequestNewsContent extends AsyncTask<String, String, String> {
		private String tag = this.getClass().getSimpleName();
		private News n;
		private HttpClient httpclient;
		private HttpResponse response;
		private String responseString = null;
		private StatusLine statusLine;
		private ByteArrayOutputStream out;
		private Calendar c;
		private String extract[];
		private String newsContent;
		private String title;
		private String time;
		private String imgUrl;
		private int newsTime;
		private int currentTime;

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
			//prevent error when internet connection down
			try{
				extract = result.split("<div id=\"content\">");
			}
			catch(NullPointerException e){
				e.printStackTrace();
				Log.e(tag,"null response");
				Info.newsList.remove(n);
				return;
			}

			newsContent = extract[2].split("</div>")[0];
			// Info.longInfo(tag, newsContent);

			title = result.split("<title>")[1].split(" - ")[0];
			time = result.split("<p class=\"time\">")[1].split("</p>")[0];

			imgUrl = newsContent.split("<img class=\"heading center\" src=\"")[1]
					.split("/>")[0].replaceAll("\"", "").replaceAll(" ", "");

			c = Calendar.getInstance();
			currentTime = c.get(Calendar.MINUTE);
			currentTime = c.get(Calendar.HOUR);

			time = time.split(",")[1];

			// for smooth parsing
			String hour = time.split(":")[0].trim();
			String minute = time.split(":")[1].trim().split(" ")[0];
			System.out.println(hour);

			/*
			 * if( (hour.charAt(0)+"").equals("0") ){ hour = hour.charAt(1)+"";
			 * }
			 * 
			 * if( (minute.charAt(0)+"").equals("0") ){ minute =
			 * hour.charAt(1)+""; }
			 */

			if (c.get(Calendar.HOUR) > Integer.parseInt(hour)) {
				time = (c.get(Calendar.HOUR) - Integer.parseInt(hour)) + " "
						+ getString(R.string.pass_hour_text);
			} else if (c.get(Calendar.MINUTE) > Integer.parseInt(minute)) {
				time = (c.get(Calendar.MINUTE) - Integer.parseInt(minute))
						+ " " + getString(R.string.pass_minute_text);
			} else {
				time = getString(R.string.pass_second_text);
			}

			System.out.println(c.get(Calendar.MINUTE));
			System.out.println(c.get(Calendar.HOUR));

			this.n.title = title;
			this.n.imgUrl = imgUrl;
			this.n.description = newsContent;
			this.n.time = time;

			// remove yesterday news
			if (c.get(Calendar.HOUR) < Integer.parseInt(hour)) {
				time = (c.get(Calendar.HOUR) - Integer.parseInt(hour)) + ""
						+ getString(R.string.pass_hour_text);
				Info.newsList.remove(n);
			}
			// update only when complete
			reloadViewAfterRequestTaskComplete();

		}

	}

	private class RequestTask extends AsyncTask<String, String, String> {
		private String tag = getClass().getSimpleName();
		private String providerUrl;

		@Override
		protected String doInBackground(String... uri) {
			this.providerUrl = uri[0];
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
			String a[] = result.split("<a href=\"/content/");
			// ignore html header
			for (int i = 1; i < a.length; i++) {
				String link = a[i].split("\">")[0];
				link = "/content/" + link;
				News n = new News(this.providerUrl.split("[.]")[1], link, link,
						this.providerUrl + link, "loading...");
				Info.uniqueAdd(n);
			}
			// on post exucute
			for (int i = 0; i < Info.newsList.size(); i++) {
				new RequestNewsContent(Info.newsList.get(i))
						.execute(Info.newsList.get(i).urlContent);
			}

		}
	}

}
