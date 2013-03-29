package com.group15.djhero;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class MainScreen extends Activity implements OnItemClickListener {

	ImageView image_view_location;
	public final static String ITEM = "com.example.MainActivity.ITEM";
	private ListView m_listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_scrren);
		m_listview = (ListView) findViewById(R.id.main_list_view);
		m_listview.setOnItemClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_screen, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int position,
			long arg3) {

		MyApplication myApp = (MyApplication) MainScreen.this.getApplication();
		if (myApp.sock != null) {

			Song thisSong = myApp.songlist.Songs.get(position);
			Intent intent = new Intent(this, PlaySongPage.class);
			intent.putExtra("songName", thisSong.Title);
			intent.putExtra("position", position);

			SendMessage.sendMessage("play " + thisSong.id, myApp.sock);
			startActivity(intent);
		} else {
			// Take the user to the settings view if the socket is not open
			Intent intent = new Intent(this, ConnectToDE2.class);
			startActivity(intent);

		}
	}

	public void onRefreshClick(View view) throws InterruptedException {
		// Get the global variables from myapp
		MyApplication myApp = (MyApplication) MainScreen.this.getApplication();
		if (myApp.sock == null) {
			// Take the user to the settings view if the socket is not open
			Intent intent = new Intent(this, ConnectToDE2.class);
			startActivity(intent);
		}

		// Clear the songlist and prepare to get the updated song list
		myApp.songlist.clearList();

		// Send a request for the song list string
		SendMessage.sendMessage("l", myApp.sock);

		// call aysnc
		new RefreshProgressDialog().execute();
	}

	public void sendSignal(View view) {
		MyApplication myApp = (MyApplication) MainScreen.this.getApplication();

		if (myApp.sock != null) {
			myApp.songlist.clearList();
			SendMessage.sendMessage("l", myApp.sock);
		} else {
			Intent intent = new Intent(this, ConnectToDE2.class);
			startActivity(intent);

		}
	}

	public void ipSettings(View view) {

		Intent intent = new Intent(this, AutoDetect.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
		startActivity(intent);

	}

	class RefreshProgressDialog extends AsyncTask<Void, Void, Integer> {

		ProgressDialog progress;
		MyApplication myApp = (MyApplication) MainScreen.this.getApplication();

		@Override
		protected void onPreExecute() {
			progress = new ProgressDialog(MainScreen.this);
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
			myApp.images = new ArrayList<Bitmap>(myApp.songlist.Songs.size());

			LazyAdapter adapter = new LazyAdapter(MainScreen.this,
					myApp.songlist);
			m_listview.setAdapter(adapter);

		}
	}

}
