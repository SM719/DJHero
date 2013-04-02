package com.group15.djhero;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

public class fragment1 extends Fragment implements OnClickListener {

	ImageButton imageButton1;
	ImageButton imageButton_add;
	boolean playButton = false;
	MyApplication myApp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View V = inflater.inflate(R.layout.fragment_test, container, false);
		imageButton_add = (ImageButton) V.findViewById(R.id.imageButton_add);
		imageButton_add.setOnClickListener(this);
		return V;

	}

	@Override
	public void onResume() {
		super.onResume();
		myApp = (MyApplication) getActivity().getApplication();
		TextView textViewforSongPosition = (TextView) getView().findViewById(R.id.songNameFrag);
		textViewforSongPosition.setText(myApp.songSelectedLeft.Title);
	}

	@Override
	public void onClick(View V) {

		switch (V.getId()) {
			case R.id.imageButton_add:
				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();

				fragment_list fl = new fragment_list();

				fragmentTransaction.replace(R.id.fragment_container, fl);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();

		}

	}
}