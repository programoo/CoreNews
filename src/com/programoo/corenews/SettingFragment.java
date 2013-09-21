package com.programoo.corenews;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.programoo.snews.R;

public class SettingFragment extends Fragment {
	private String tag = this.getClass().getSimpleName();
	private View newsFragment;
	FeedListViewAdapter ardap;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.newsFragment = inflater.inflate(R.layout.settings_fragment,
				container, false);
		Log.d(tag, "onCreateView");

		return newsFragment;
	}

}