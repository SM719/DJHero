package com.example.djhero;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

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

public class MainActivity extends Activity implements OnItemClickListener {
	public final static String ITEM = "com.example.MainActivity.ITEM";
	private ListView m_listview;
	ImageView[] image_view_location = new ImageView[10];
	songList songlist2 = null;
	songList songlist = null;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		m_listview = (ListView) findViewById(R.id.id_list_view);

		songlist = new songList("1:believe:20|2:In+da+club:30");
		
		LazyAdapter adapter = new LazyAdapter(this, songlist, "test");
		Log.i("testCreate", "onCreate");

		m_listview.setAdapter(adapter);
		m_listview.setOnItemClickListener(this);
		
		image_view_location[0] =  (ImageView) findViewById(R.id.imageView1);
		
		// Set up a timer task.  We will use the timer to check the
		// input queue every 500 ms
		TCPReadTimerTask tcp_task = new TCPReadTimerTask();
		Timer tcp_timer = new Timer();
		tcp_timer.schedule(tcp_task, 3000, 500);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	public void sendSignal(View view){
		MyApplication myApp = (MyApplication) MainActivity.this.getApplication();
		
		if(myApp.sock != null){
			//myApp.songlist.clearList();
			sendMessage("list");
		}
		else{
			Intent intent = new Intent(this, ThirdActivity.class);
			startActivity(intent);
				
		}
	}

	public void onRefreshClick(View view) throws InterruptedException {
		
		
		MyApplication myApp = (MyApplication) MainActivity.this.getApplication();
		
		if(myApp.sock != null ){
			//Thread.currentThread();
			//Thread.sleep(5000);
			Log.i("tag", myApp.listFromDE2);
			
			Log.i("before", myApp.listFromDE2);
			//myApp.listFromDE2.substring(2);
			
			myApp.listFromDE2 = myApp.listFromDE2.replaceAll(".WAV", "");
			Log.i("after", myApp.listFromDE2);
			
			songlist = new songList(myApp.listFromDE2);
				LazyAdapter adapter = new LazyAdapter(this, songlist , "test");
				m_listview.setAdapter(adapter);
		}
		else {
			//Take the user to the settings view if the socket is not open
			Intent intent = new Intent(this, ThirdActivity.class);
			startActivity(intent);
		}
		
	}
	


	
	public void ipSettings(View view){
		
		Intent intent = new Intent(this, ThirdActivity.class);
		startActivity(intent);
		
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int position,
			long arg3) {

		MyApplication myApp = (MyApplication) MainActivity.this.getApplication();
		
		if(myApp.sock != null){
			Song thisSong = songlist.Songs.get(position);
			Intent intent = new Intent(this, PlaySongPage.class);
			intent.putExtra("songName", thisSong.Title);
			
			sendMessage("play"+thisSong.id);
			startActivity(intent);
		}
		else{
			//Take the user to the settings view if the socket is not open
			Intent intent = new Intent(this, ThirdActivity.class);
			startActivity(intent);
			
		}
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
	
	// This is a timer Task.  Be sure to work through the tutorials
	// on Timer Tasks before trying to understand this code.
		public class TCPReadTimerTask extends TimerTask {
		public void run() {
			MyApplication app = (MyApplication) getApplication();
			if (app.sock != null && app.sock.isConnected()
					&& !app.sock.isClosed()) {
				
				try {
					InputStream in = app.sock.getInputStream();

					// See if any bytes are available from the Middleman
					
					int bytes_avail = in.available();
					if (bytes_avail > 0) {
						
						// If so, read them in and create a sring
						
						byte buf[] = new byte[bytes_avail];
						in.read(buf);

						
						final String s = new String(buf, 0, bytes_avail, "US-ASCII");
						MyApplication myApp = (MyApplication) MainActivity.this.getApplication();
						
						Log.i("tag2", "tag " + s);
						myApp.listFromDE2 = s;
						
		
						// As explained in the tutorials, the GUI can not be
						// updated in an asyncrhonous task.  So, update the GUI
						// using the UI thread.
						
						runOnUiThread(new Runnable() {
							public void run() {
								
								//EditText et = (EditText) findViewById(R.id.RecvdMessage);
								//et.setText(s);
							}
						});
						
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void sendMessage(String message) {
		MyApplication app = (MyApplication) getApplication();
		
		// Get the message from the box
		
		//EditText et = (EditText) findViewById(R.id.MessageText);
		String msg = message.toLowerCase();
				//et.getText().toString();

		// Create an array of bytes.  First byte will be the
		// message length, and the next ones will be the message
		
		byte buf[] = new byte[msg.length() + 1];
		buf[0] = (byte) msg.length(); 
		System.arraycopy(msg.getBytes(), 0, buf, 1, msg.length());

		// Now send through the output stream of the socket
		
		OutputStream out;
		try {
			out = app.sock.getOutputStream();
			try {
				out.write(buf, 0, msg.length() + 1);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
