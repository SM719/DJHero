package com.group15.djhero;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class fragment1 extends Fragment {

	ImageButton imageButton1;
	boolean playButton = false;
	MyApplication myApp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_test, container, false);
	}

	@Override
	public void onResume() {
		super.onResume();
		myApp = (MyApplication) getActivity().getApplication();
		TextView textViewforSongPosition = (TextView) getView().findViewById(R.id.songNameFrag);
		textViewforSongPosition.setText(myApp.songSelectedLeft.Title);
	}
}