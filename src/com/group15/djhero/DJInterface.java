package com.group15.djhero;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.group15.djhero.MainScreen.DownloadImages;
import com.group15.djhero.MainScreen.RefreshProgressDialog;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class DJInterface extends FragmentActivity implements OnSeekBarChangeListener {
	
	MyApplication myApp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_djinterface);
		myApp = (MyApplication) DJInterface.this.getApplication();
		
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction1 = fragmentManager.beginTransaction();
		FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction();

		fragment1 fragment = new fragment1();
		fragmentTransaction1.add(R.id.fragment_container2, fragment);
		fragmentTransaction1.commit();

		fragment2 fragment2 = new fragment2();
		fragmentTransaction2.add(R.id.fragment_container, fragment2);
		fragmentTransaction2.commit();
		
		SeekBar bar = (SeekBar) findViewById(R.id.seekBar1);
		bar.setOnSeekBarChangeListener(this); // set Seekbar listener.
		bar.setProgress(myApp.djVolumeBar);
		
		SwipeDetector gesture = new SwipeDetector(this);
		LinearLayout currentLayout = (LinearLayout)this.findViewById(R.id.dj_interface_layout);
		currentLayout.setOnTouchListener(gesture);
		

	}

	//Send message to start playing songs specified  by id
	//Message sent to DE2 is format of d id1 id2
	public void PlayPause(View view) {
		SendMessage.sendMessage("d "+String.valueOf(myApp.songSelectedLeft.id)+" "+String.valueOf(myApp.songSelectedRight.id), myApp.sock);
		for(int i =0; i<20000; i++);
		SendMessage.sendMessage("d "+String.valueOf(myApp.songSelectedLeft.id)+" "+String.valueOf(myApp.songSelectedRight.id), myApp.sock);
		
		// Display a progress dialog while the DE2 loads the songs to mix into memory
		new DjProgressDialog().execute();
	}
	
	public void cowBell(View view){
		SendMessage.sendMessage("1", myApp.sock);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.djinterface, menu);
		return true;
	}
	
	class DjProgressDialog extends AsyncTask<Void, Void, Integer> {

		ProgressDialog progress;

		@Override
		protected void onPreExecute() {
			progress = new ProgressDialog(DJInterface.this);
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.setMessage("Loading...");
			progress.setMax(100);
			progress.setProgressNumberFormat(null);
			progress.show();
		}

		@Override
		protected Integer doInBackground(Void... arg0) {

			while (!myApp.djDoneLoad);
			myApp.djDoneLoad = false;
			return 0;

		}

		@Override
		protected void onPostExecute(Integer result) {
			progress.dismiss();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent djIntent = new Intent(this, DJInterface.class);
		djIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);

		switch (item.getItemId()) {
		
			case R.id.action_share:
				Intent intent = new Intent(this, Share.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				this.startActivity(intent);
				return true;
			
			case R.id.action_update:
				try {
					onRefreshClick();
				} catch (InterruptedException e) {
				}

				return true;

			case R.id.action_settings:
				Intent intentSettings = new Intent(this, AutoDetect.class);
				intentSettings.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				this.startActivity(intentSettings);
				return true;

			case R.id.action_dj:
				return true;

			case R.id.action_music:
				finish();
				return true;

			default:
				return super.onOptionsItemSelected(item);
		}
	}

	public void onRefreshClick() throws InterruptedException {
		// Get the global variables from myApp
		MyApplication myApp = (MyApplication) DJInterface.this.getApplication();
		if (myApp.sock == null) {
			// Take the user to the settings view if the socket is not open
			Intent intent = new Intent(this, AutoDetect.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
		} else {
			// Clear the songlist and prepare to get the updated song list
			myApp.songlist.clearList();

			// Send a request for the song list string
			// SendMessage.sendMessage("l", myApp.sock); UNCOMMENT LATER
			SendMessage.sendMessage("l", myApp.sock);

			// Display a progress dialog while we get the list from the DE2
			new RefreshProgressDialog().execute();
		}
	}

	class RefreshProgressDialog extends AsyncTask<Void, Void, Integer> {

		ProgressDialog progress;
		MyApplication myApp = (MyApplication) DJInterface.this.getApplication();

		@Override
		protected void onPreExecute() {
			progress = new ProgressDialog(DJInterface.this);
			progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progress.setMessage("Updating song list");
			progress.setMax(100);
			progress.setProgressNumberFormat(null);
			progress.show();
		}

		@Override
		protected Integer doInBackground(Void... arg0) {

			while (!myApp.listComplete)
				;
			return 0;

		}

		@Override
		protected void onPostExecute(Integer result) {
			progress.dismiss();
			myApp.listComplete = false;
			myApp.images = new Bitmap[myApp.songlist.Songs.size()];

			for (int i = 0; i < myApp.songlist.Songs.size(); i++) {
				new DownloadImages().execute(
				        "http://server.gursimran.net/test2.php?track="
				                + myApp.songlist.Songs.get(i).Title,
				        String.valueOf(i));
			}

		}
	}

	// This is the asynchronous task. It is extended from AsyncTask
	class DownloadImages extends
	        com.group15.djhero.AsyncTask<String, Void, Integer> {
		// This is the "guts" of the asynchronus task. The code
		// in doInBackground will be executed in a separate thread
		@Override
		protected Integer doInBackground(String... url_array) {
			URL url;
			MyApplication myApp = (MyApplication) DJInterface.this
			        .getApplication();
			Log.i("MainActivity", "Inside the asynchronous task");
			try {
				url = new URL(url_array[0]);
				HttpURLConnection connection = (HttpURLConnection) url
				        .openConnection();
				connection.setDoInput(true);
				connection.connect();
				Log.i("MainActivity", "Successfully opened the web page");
				InputStream input = connection.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(input);
				input.close();
				myApp.images[Integer.parseInt(url_array[1])] = bitmap;
				return Integer.parseInt(url_array[1]);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

	public void recordMix(View view){
		SendMessage.sendMessage("h", myApp.sock);
	}
	
	@Override
	// Update the volume progress as the user changes it
	public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
//		if ((progress % 10) < 5) {
//			progress = (progress / 10) * 10;
//		} else {
//			progress = ((progress / 10) * 10) + 10;
//		}
		myApp.djVolumeBar = progress;
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		MyApplication myApp = (MyApplication) DJInterface.this.getApplication();
		int volume1 = 70;
		int volume2 =70;
		
		if(myApp.djVolumeBar >70){
			volume2 = 70;
			volume1 = 140 - myApp.djVolumeBar; 
		}
		else if(myApp.djVolumeBar < 70){
			volume1 =70;
			volume2 = myApp.djVolumeBar;
		}
		
		// Send desired volume to the DE2
		SendMessage.sendMessage("i " + Integer.toString((volume1 / 10) * 10)+" "+Integer.toString((volume2 / 10) * 10) ,myApp.sock);
		Log.i("VolumeTag", Integer.toString((myApp.Global_progress / 10) * 10));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}

	public void goToMusic() {
		// TODO Auto-generated method stub
		finish();
		
	}
}
