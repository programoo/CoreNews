package com.programoo.corenews;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;

import com.programoo.snews.R;

public class SettingFragment extends Fragment implements OnItemSelectedListener
{
	private String TAG = this.getClass().getSimpleName();
	private View layout;
	FeedListViewAdapter ardap;
	private ListView providerLv;
	private ListView typeLv;
	private MainActivity mCtx;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		// read settings
		Log.i(TAG, "onCreate");
		mCtx = (MainActivity) getActivity();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		
		Log.i(TAG, "onCreateView");
		
		this.layout = inflater.inflate(R.layout.settings_fragment, container,
				false);
		providerLv = (ListView) this.layout.findViewById(R.id.providerLv);
		typeLv = (ListView) this.layout.findViewById(R.id.typeLv);
		
		ProviderListViewAdapter pAdapter = new ProviderListViewAdapter(
				getActivity(), mCtx.fList);
		providerLv.setAdapter(pAdapter);
		
		TypeListViewAdapter p2Adapter = new TypeListViewAdapter(getActivity(),
				Info.getInstance().typeList);
		typeLv.setAdapter(p2Adapter);
		
		return layout;
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		Log.d(TAG, "on stop");
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
		Log.d(TAG, "on start");
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		Log.d(TAG, "on pause");
		
	}
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id)
	{
		String itemSelect = (String) parent.getItemAtPosition(pos);
		Log.i(TAG, "You select: " + itemSelect);
	}
	
	public void onNothingSelected(AdapterView<?> parent)
	{
		// Another interface callback
	}
}
