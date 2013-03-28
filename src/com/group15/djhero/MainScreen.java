package com.group15.djhero;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class MainScreen extends Activity implements OnItemClickListener  {

	ImageView[] image_view_location = new ImageView[10];
	public final static String ITEM = "com.example.MainActivity.ITEM";
	private ListView m_listview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication myApp = (MyApplication) MainScreen.this.getApplication();
		setContentView(R.layout.activity_main_scrren);
		m_listview = (ListView) findViewById(R.id.ip_list_view);
		m_listview.setOnItemClickListener(this);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_screen, menu);
		return true;
	}


		
		// This is the asynchronous task. It is extended from AsyncTask
		class NewAsyncTask extends AsyncTask<String, Void, Bitmap> {
			// This is the "guts" of the asynchronus task. The code
			// in doInBackground will be executed in a separate thread
			@Override
			protected Bitmap doInBackground(String... url_array) {
				URL url;
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
					return bitmap;
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
			}

			// This routine is called at the end of the task. This
			// routine is run as part of the main thread, so it can
			// update the GUI. The input parameter is automatically
			// set by the output parameter of doInBackground()
			@Override
			protected void onPostExecute(Bitmap result) {
				image_view_location[0].setImageBitmap(result);
			}
		}

		@Override
		public void onItemClick(AdapterView<?> adapter, View arg1, int position,
				long arg3) {

			MyApplication myApp = (MyApplication) MainScreen.this.getApplication();
			if(myApp.sock != null){
				
				Song thisSong = myApp.songlist.Songs.get(position);
				Intent intent = new Intent(this, PlaySongPage.class);
				intent.putExtra("songName", thisSong.Title);
				intent.putExtra("position", position);
				
				SendMessage.sendMessage("play "+thisSong.id, myApp.sock);
				startActivity(intent);
			}
			else{
				//Take the user to the settings view if the socket is not open
				Intent intent = new Intent(this, ConnectToDE2.class);
				startActivity(intent);
				
			}
		}
		
		
		public void onRefreshClick(View view) throws InterruptedException {
			
			
			MyApplication myApp = (MyApplication) MainScreen.this.getApplication();
			
			if(myApp.sock == null ){
				//Take the user to the settings view if the socket is not open
				Intent intent = new Intent(this, ConnectToDE2.class);
				startActivity(intent);
			}
			
			myApp.listFromDE2 = " ";
			SendMessage.sendMessage("l", myApp.sock);
			while (myApp.listFromDE2.equals(" "));
			
			Log.i("tag", myApp.listFromDE2);
			
			Log.i("before", myApp.listFromDE2);
			
			myApp.listFromDE2 = myApp.listFromDE2.replaceAll(".WAV", "");
			Log.i("after", myApp.listFromDE2);
			
			myApp.songlist = new songList(myApp.listFromDE2);
			myApp.images.clear();
			LazyAdapter adapter = new LazyAdapter(this, myApp.songlist);
			m_listview.setAdapter(adapter);	
		}
		

		public void sendSignal(View view){
			MyApplication myApp = (MyApplication) MainScreen.this.getApplication();
			
			if(myApp.sock != null){
				myApp.songlist.clearList();
				SendMessage.sendMessage("l", myApp.sock);
			}
			else{
				Intent intent = new Intent(this, ConnectToDE2.class);
				startActivity(intent);
					
			}
		}
		
		public void ipSettings(View view){
			
			Intent intent = new Intent(this, AutoDetect.class);
			intent .setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(intent);
			
		}

		
}
