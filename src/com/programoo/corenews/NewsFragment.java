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

public class NewsFragment extends SherlockFragment implements
		OnItemClickListener {
	private String tag = this.getClass().getSimpleName();
	private View viewMainFragment;
	private ListView lv;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		this.viewMainFragment = inflater.inflate(R.layout.news_fragment,
				container, false);

		lv = (ListView) viewMainFragment.findViewById(R.id.list1Fragment);

		NewsListViewAdapter ardap = new NewsListViewAdapter(getActivity(),Info.newsList);

		lv.setAdapter(ardap);
	
		Log.d(tag, "onCreateView");

		lv.setOnItemClickListener(this);

		return viewMainFragment;
	}

	public void onViewCreated(View view, Bundle savedInstanceState) {
		Log.d(tag, "onViewCreated");
		super.onViewCreated(view, savedInstanceState);
	}

	public void onActivityCreated(final Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		Log.d(tag, "onActivityCreated");

	}

	public void reloadViewAfterRequestTaskComplete() {
		Log.d(tag, "reloadViewAfterRequestTaskComplete");

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	public void onStart() {
		super.onStart();
		Log.d(tag, "onStart");
		new RequestTask().execute("http://m.thairath.co.th");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	
	private class RequestTask extends AsyncTask<String, String, String> {
		private String tag = getClass().getSimpleName();
		private String url = "";

		@Override
		protected String doInBackground(String... uri) {
			this.url =uri[0];
			
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
			Log.d(tag, "response: ["+this.url+"]"+result);
			responseStringParser(result);
		}

	}
	
	public void responseStringParser(String result){
		String a = result.split("<ul>")[1];
		String imgSrcUrl = a.split("<img src=")[1].split("/>")[0];
		String newsDetail = a.split("a href=")[1].split(">")[0];

		Log.i(tag,a);
		Log.i(tag,"img url: "+imgSrcUrl);
	}
	
	

}
