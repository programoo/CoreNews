package com.programoo.corenews;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.programoo.snews.R;

public class NewsListViewAdapter extends BaseAdapter {
	private String tag = getClass().getSimpleName();
	private Context context;
	private ArrayList<News> newsList;
	public NewsListViewAdapter(Context context,ArrayList<News> newsList) {
		super();
		// TODO Auto-generated constructor stub
		this.context = context;		
		this.newsList = newsList;
		Log.d(tag,"NewsListViewAdapter");

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		convertView = inflater.inflate(R.layout.news_fragment_listview, parent,
				false);
		/*
		ImageView imageView = (ImageView) convertView
				.findViewById(R.id.newsLogo);
		TextView description = (TextView) convertView
				.findViewById(R.id.newsText);
		TextView reporter = (TextView) convertView.findViewById(R.id.reporter);

		TextView endTime = (TextView) convertView.findViewById(R.id.newsTime);

		imageView.setImageResource(R.drawable.ic_launcher);
		*/


		return convertView;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return newsList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}