package com.programoo.corenews;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.programoo.snews.R;

public class FeedListViewAdapter extends BaseAdapter {
	private String tag = getClass().getSimpleName();
	private Context context;
	private ArrayList<News> newsList;
	private LayoutInflater inflater = null;
	private TextView description = null;
	private ImageView iv = null;
	private TextView reporterText = null;
	private AQuery aq = null;
	private Typeface tf = null;

	public FeedListViewAdapter(Context context, ArrayList<News> newsList) {
		super();
		this.context = context;
		this.newsList = newsList;
		this.inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.tf = Typeface.createFromAsset(this.context.getAssets(),
				"fonts/MicrosoftSansSerif.ttf");
		aq = new AQuery(this.context);

		Log.d(tag, "NewsListViewAdapter");

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// for better performance
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.news_fragment_listview,
					parent, false);

			//description.setTypeface(tf);
			//reporterText.setTypeface(tf);
		}
		// else{
		iv = (ImageView) convertView.findViewById(R.id.newsIcon);
		aq.id(iv).image(this.newsList.get(position).imgUrl, true, true, 200, 0);
		
		description = (TextView) convertView.findViewById(R.id.newsText);
		description.setText(this.newsList.get(position).title);

		reporterText = (TextView) convertView.findViewById(R.id.fromText);
		reporterText.setText(this.newsList.get(position).time + " " + this.context.getString(R.string.by_text)+" "
				+ this.newsList.get(position).provider);

		return convertView;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.newsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}