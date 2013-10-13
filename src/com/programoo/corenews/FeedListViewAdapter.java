package com.programoo.corenews;

import object.News;
import object.SArrayList;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.programoo.snews.R;

public class FeedListViewAdapter extends BaseAdapter implements OnClickListener
{
	private String TAG = getClass().getSimpleName();
	private Context context;
	private SArrayList newsList;
	private LayoutInflater inflater = null;
	private AQuery aq = null;
	private Typeface tf = null;
	
	public FeedListViewAdapter(Context context, SArrayList newsList,
			AQuery aq)
	{
		super();
		this.context = context;
		this.newsList = newsList;
		this.aq = aq;
		Log.d(TAG, "FeedListViewAdapter");
		
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null)
		{
			this.inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			this.tf = Typeface.createFromAsset(this.context.getAssets(),
					"fonts/DroidSerif-Regular.ttf");
			
			convertView = inflater.inflate(R.layout.news_fragment_listview,
					parent, false);
		}
		
		News newsObj = (News) this.newsList.get(position);
		convertView.setTag(newsObj);
		
		ImageButton iv = (ImageButton) convertView.findViewById(R.id.newsIcon);
		
		if ( newsObj.imgUrl != null ){
			aq.id(iv).image(newsObj.imgUrl, true, true, 200, 0);
		}
		else{
			iv.setImageResource(R.drawable.default_img);
		}
		
		iv.setOnClickListener(this);
		iv.setTag(newsObj);
		
		TextView description = (TextView) convertView
				.findViewById(R.id.newsText);
		description.setText(newsObj.title);
		description.setTypeface(tf);
		
		TextView reporterText = (TextView) convertView
				.findViewById(R.id.fromText);
		try{
			String providerText = newsObj.provider.split("[.]")[1] + "("
					+ newsObj.kind+") "+newsObj.pubDate;
			reporterText.setText(providerText);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		ImageView isReadIv = (ImageView) convertView
				.findViewById(R.id.isReadIv);
		
		if (newsObj.isRead)
		{
			isReadIv.setVisibility(View.GONE);
		} else
		{
			isReadIv.setVisibility(View.VISIBLE);
		}
		
		return convertView;
		
	}
	
	@Override
	public int getCount()
	{
		return this.newsList.size();
	}
	
	@Override
	public News getItem(int position)
	{
		return (News) this.newsList.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return 0;
	}
	
	@Override
	public void onClick(View v)
	{
		News newsMention = (News) v.getTag();
		
		Log.i(TAG, "onClick: " + newsMention.toString());
		final Dialog dialog = new Dialog(this.context);
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.fullscreen_img_dialog);
		dialog.setCancelable(true);
		dialog.show();
		
		ImageView iv = (ImageView) dialog
				.findViewById(R.id.fullScreenImgDialog);
		aq.id(iv).image(newsMention.imgUrl, true, true);
		
	}
	
}