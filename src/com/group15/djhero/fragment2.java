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

public class fragment2 extends Fragment implements OnClickListener {

	MyApplication myApp;
	ImageButton imageButton_add;
	ImageButton imageButton_ff;
	ImageButton	imageButton_rew;
	ImageButton imageButton_forward;
	ImageButton imageButton_rewind;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View V = inflater.inflate(R.layout.fragment_test, container, false);
		
		imageButton_add = (ImageButton) V.findViewById(R.id.imageButton_add);	
		imageButton_ff = (ImageButton) V.findViewById(R.id.imageButton_ff);
		imageButton_rew = (ImageButton) V.findViewById(R.id.imageButton_rew);
		imageButton_forward = (ImageButton) V.findViewById(R.id.imageButton_forward);
		imageButton_rewind = (ImageButton) V.findViewById(R.id.imageButton_rewind);
		
		imageButton_add.setOnClickListener(this);
		imageButton_ff.setOnClickListener(this);
		imageButton_rew.setOnClickListener(this);
		imageButton_forward.setOnClickListener(this);
		imageButton_rewind.setOnClickListener(this);
		return V;
	}

	@Override
	public void onResume() {
		super.onResume();
		myApp = (MyApplication) getActivity().getApplication();
		TextView textViewforSongPosition = (TextView) getView().findViewById(R.id.songNameFrag);
		textViewforSongPosition.setText(myApp.songSelectedRight.Title);
	}

	@Override
	public void onClick(View V) {
		switch (V.getId()) {
			case R.id.imageButton_add:
				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
				fragment_list2 fl = new fragment_list2();
				fragmentTransaction.replace(R.id.fragment_container2, fl);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
				break;
			
			case R.id.imageButton_ff:
				myApp.rightSpeed = (myApp.rightSpeed +1) % 3;
				SendMessage.sendMessage("t "+ String.valueOf(myApp.leftSpeed)+" "+String.valueOf(myApp.rightSpeed), myApp.sock);
				break;
				
			case R.id.imageButton_rew:
				if(myApp.rightSpeed > 0){
					myApp.rightSpeed = myApp.rightSpeed -1;
					}
					SendMessage.sendMessage("t "+ String.valueOf(myApp.leftSpeed)+" "+String.valueOf(myApp.rightSpeed), myApp.sock);
					break;
			
			case R.id.imageButton_forward:
				SendMessage.sendMessage("y 0 1", myApp.sock);
				break;
				
			case R.id.imageButton_rewind:
				SendMessage.sendMessage("w 0 1", myApp.sock);
				break;

		}

	}

}