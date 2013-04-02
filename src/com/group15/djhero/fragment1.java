package com.group15.djhero;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

public class fragment1 extends Fragment {

	ImageButton imageButton1;
	boolean playButton = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_test, container, false);
	}

	public void AddSong(View view) {

		FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

		fragment_list fl = new fragment_list();

		fragmentTransaction.replace(R.id.fragment_container, fl);

	}
}