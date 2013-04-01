package com.group15.djhero;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.group15.djhero.MainScreen.DownloadImages;
import com.group15.djhero.MainScreen.RefreshProgressDialog;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class DJInterface extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_djinterface);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_screen, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		Intent djIntent = new Intent(this, DJInterface.class);
		djIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		
		switch(item.getItemId()){
		
		case R.id.action_update:
			try {
				onRefreshClick();
			} catch (InterruptedException e) {}
			
			return true;
		
		case R.id.action_settings:
			Intent intent = new Intent(this, AutoDetect.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			this.startActivity(intent);
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
			//SendMessage.sendMessage("l", myApp.sock); UNCOMMENT LATER
			SendMessage.sendMessage("l ", myApp.sock);

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

			while (!myApp.listComplete);
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

}