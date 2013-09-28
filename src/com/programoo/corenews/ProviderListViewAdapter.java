package com.programoo.corenews;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.androidquery.AQuery;
import com.programoo.snews.R;

public class ProviderListViewAdapter extends BaseAdapter implements OnClickListener
{
	private String TAG = getClass().getSimpleName();
	private Context context;
	private ArrayList<Provider> pList;
	private LayoutInflater inflater = null;
	private AQuery aq = null;
	private Typeface tf = null;
	
	public ProviderListViewAdapter(Context context, ArrayList<Provider> pList)
	{
		super();
		this.context = context;
		this.pList = pList;
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
			
			convertView = inflater.inflate(R.layout.provider_listview,
					parent, false);
			
		}
		
		CheckBox cb = (CheckBox) convertView.findViewById(R.id.providerCb);
		cb.setText(this.pList.get(position).name);
		
		return convertView;
		
	}
	
	@Override
	public int getCount()
	{
		return this.pList.size();
	}
	
	@Override
	public Provider getItem(int position)
	{
		return this.pList.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return 0;
	}

	@Override
	public void onClick(View v)
	{
	}
	
}