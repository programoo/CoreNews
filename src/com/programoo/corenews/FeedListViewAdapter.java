package com.programoo.corenews;

import java.util.ArrayList;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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
	private ArrayList<News> newsList;
	private LayoutInflater inflater = null;
	private TextView description = null;
	private ImageView providerIcon = null;
	private TextView reporterText = null;
	private AQuery aq = null;
	private Typeface tf = null;
	
	public FeedListViewAdapter(Context context, ArrayList<News> newsList,AQuery aq)
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
		
		ImageButton iv = (ImageButton) convertView.findViewById(R.id.newsIcon);
		aq.id(iv)
				.image(this.newsList.get(position).imgUrl, true, true, 200, 0);
		iv.setOnClickListener(this);
		iv.setTag(position+"");
		
		ImageView providerIcon = (ImageView) convertView
				.findViewById(R.id.providerIcon);
		providerIcon.setImageResource(R.drawable.reporter_icon);
		/*
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
		*/
		TextView description = (TextView) convertView
				.findViewById(R.id.newsText);
		description.setText(this.newsList.get(position).title);
		description.setTypeface(tf);

		
		TextView reporterText = (TextView) convertView
				.findViewById(R.id.fromText);
		String providerText = this.newsList.get(position).provider.split("[.]")[1]+" "+this.newsList.get(position).pubDate;
		reporterText.setText(providerText);
		
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
		return this.newsList.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public void onClick(View v)
	{
		//v.getTag()
		int position = Integer.parseInt(v.getTag()+"");
		Log.i(TAG,"onClick: "+v.getTag());
		final Dialog dialog = new Dialog(this.context);
		dialog.getWindow();
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.fullscreen_img_dialog);
		dialog.setCancelable(true);
		dialog.show();
		
		ImageView iv = (ImageView) dialog.findViewById(R.id.fullScreenImgDialog);
		aq.id(iv).image(this.newsList.get(position).imgUrl, true, true, 200, 0);
		
	}
	
}