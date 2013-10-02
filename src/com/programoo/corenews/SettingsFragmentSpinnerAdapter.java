package com.programoo.corenews;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.programoo.snews.R;

public class SettingsFragmentSpinnerAdapter extends BaseAdapter
{
	private String TAG = this.getClass().getSimpleName();
	FeedListViewAdapter ardap;
	
	private Context context;
	ArrayList<String> data;
	
	public SettingsFragmentSpinnerAdapter(Context context,
			int textViewResourceId, ArrayList<String> data)
	{
		super();
		Log.d(TAG,"SettingsFragmentSpinnerAdapter");
		this.data = data;
		this.context = context;
		
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent)
	{
		return getCustomView(position, convertView, parent);
	}
	
	@Override
	public int getCount()
	{
		return data.size();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		return getCustomView(position, convertView, parent);
	}
	
	public View getCustomView(int position, View convertView, ViewGroup parent)
	{
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View row = inflater.inflate(R.layout.settings_spinerview_fragment,
				parent, false);
		TextView label = (TextView) row.findViewById(R.id.dataSpinnerTv);
		label.setText(this.data.get(position));
		return row;
	}

	@Override
	public String getItem(int position)
	{
		return this.data.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return (position);
	}
	
}
