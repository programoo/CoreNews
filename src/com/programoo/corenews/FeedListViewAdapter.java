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

public class FeedListViewAdapter extends BaseAdapter
{
	private String tag = getClass().getSimpleName();
	private Context context;
	private ArrayList<News> newsList;
	private LayoutInflater inflater = null;
	private TextView description = null;
	private ImageView providerIcon = null;
	private TextView reporterText = null;
	private AQuery aq = null;
	private Typeface tf = null;
	
	public FeedListViewAdapter(Context context, ArrayList<News> newsList)
	{
		super();
		this.context = context;
		this.newsList = newsList;
		
		aq = new AQuery(this.context);
		
		Log.d(tag, "FeedListViewAdapter");
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			this.inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			this.tf = Typeface.createFromAsset(this.context.getAssets(),
					"fonts/MicrosoftSansSerif.ttf");
			
			convertView = inflater.inflate(R.layout.news_fragment_listview,
					parent, false);
			
		}
		
		ImageView iv = (ImageView) convertView.findViewById(R.id.newsIcon);
		aq.id(iv)
				.image(this.newsList.get(position).imgUrl, true, false, 200, 0);
		// aq.id(iv).image(this.newsList.get(position).imgUrl, true, false,200,
		// 0);
		
		// encode url for thai language
		/*
		 * String uri =
		 * Uri.parse("http://...").buildUpon().appendQueryParameter("key",
		 * "val").build().toString(); aq.id(iv).image(uri);
		 */
		
		// aq.id(iv).image(this.newsList.get(position).imgUrl);
		// aq.id(iv).image
		ImageView providerIcon = (ImageView) convertView
				.findViewById(R.id.providerIcon);
		if (this.newsList.get(position).provider.indexOf("thairath") != -1)
		{
			providerIcon.setImageResource(R.drawable.thairath);
		} else if (this.newsList.get(position).provider.indexOf("dailynews") != -1)
		{
			providerIcon.setImageResource(R.drawable.daily);
		} else if (this.newsList.get(position).provider.indexOf("blognone") != -1)
		{
			providerIcon.setImageResource(R.drawable.blognone);
		} else if (this.newsList.get(position).provider.indexOf("posttoday") != -1)
		{
			providerIcon.setImageResource(R.drawable.posttoday);
		}
		
		TextView description = (TextView) convertView
				.findViewById(R.id.newsText);
		description.setText(this.newsList.get(position).title);
		description.setTypeface(tf);

		
		TextView reporterText = (TextView) convertView
				.findViewById(R.id.fromText);
		reporterText.setText(this.newsList.get(position).showtime);
		
		return convertView;
		
	}
	
	@Override
	public int getCount()
	{
		return this.newsList.size();
	}
	
	@Override
	public Object getItem(int position)
	{
		return null;
	}
	
	@Override
	public long getItemId(int position)
	{
		return 0;
	}
	
}