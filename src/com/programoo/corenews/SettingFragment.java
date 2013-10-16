package com.programoo.corenews;

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
		mCtx = (MainActivity) getActivity();
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		
		this.layout = inflater.inflate(R.layout.settings_fragment, container,
				false);
		providerLv = (ListView) this.layout.findViewById(R.id.providerLv);
		typeLv = (ListView) this.layout.findViewById(R.id.typeLv);
		
		
		TypeListViewAdapter p2Adapter = new TypeListViewAdapter(getActivity(),
				mCtx.typeList);
		typeLv.setAdapter(p2Adapter);
		
		FeederListViewAdapter pAdapter = new FeederListViewAdapter(
				getActivity(), mCtx.uFeederList);
		providerLv.setAdapter(pAdapter);
		
		return layout;
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
	}
	
	@Override
	public void onPause()
	{
		super.onPause();
		
	}
	
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id)
	{
		String itemSelect = (String) parent.getItemAtPosition(pos);
	}
	
	public void onNothingSelected(AdapterView<?> parent)
	{
		// Another interface callback
	}
}
