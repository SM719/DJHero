package com.example.djhero;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ListIterator;

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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {
	public final static String ITEM = "com.example.MainActivity.ITEM";
	private ListView m_listview;
	ImageView[] image_view_location = new ImageView[10];
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		m_listview = (ListView) findViewById(R.id.id_list_view);

		String[] items = new String[] { "Sukhi 1", "Gursimran 2", "Donald 3",
				"Nabeel 4", "Nikola 5", "Moe 6", "Nigel 7", "Ali 8", "Mike 9",
				"Paul 10" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, items);

		m_listview.setAdapter(adapter);
		m_listview.setOnItemClickListener(this);
		
		image_view_location[0] =  (ImageView) findViewById(R.id.imageView1);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void onRefreshClick(View view) {
		
		songList songlist = new songList("believe:cher:20|In+da+club:50+cent:30");
		ListIterator<Song> it = songlist.Songs.listIterator();
		
		while(it.hasNext()){
			Song nextsong = it.next();
		// Create a new Asynchronous Task and execute it.
		new NewAsyncTask().execute ("http://server.gursimran.net/test.php?track="+nextsong.Title+"&artist="+nextsong.Artist);
		}
	
		/*
		 * StringBuilder strb = new StringBuilder("");

		// Create new songlist element
		songList songlist = new songList(
				"Shake it:Moe:20|Why can't you love me:Gursimran:30|I ate a shit ton of Reese's cups:Sukhi Mann:40");

		// Iterate though the list and display the information
		ListIterator<Song> it = songlist.Songs.listIterator();
		while (it.hasNext()) {
			Song nextsong = it.next();
			strb.append("Title:  " + nextsong.Title + "\n");
			strb.append("Artist:  " + nextsong.Artist + "\n");
			strb.append("Length:  " + nextsong.Length + "\n");
			strb.append("\n");
		}

		// Rather than writing to a pre-allocated field on the
		// screen, create a new view, and write to it.
		RelativeLayout top_linear_layout = (RelativeLayout) findViewById(R.id.RelativeLayoutMain);

		TextView report_line = new TextView(this);
		report_line.setText(strb);
		top_linear_layout.addView(report_line);
		*/

	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int position,
			long arg3) {
		// TODO Auto-generated method stub
		String str = adapter.getItemAtPosition(position).toString();
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, SongDetailActivity.class);
		intent.putExtra(ITEM, str);
		startActivity(intent);
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

}
