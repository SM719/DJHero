package com.example.djhero;

import java.util.ListIterator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {
	public final static String ITEM = "com.example.MainActivity.ITEM";
	private ListView m_listview;
	private songList songlist;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		// Create new songlist element
		songlist = new songList("Shake it:Moe:20|Why can't you love me:Gursimran:30|I ate a shit ton of Reese's cups:Sukhi Mann:40");
		
		m_listview = (ListView) findViewById(R.id.id_list_view);
		SongListAdapter<songList> adapter = new SongListAdapter<songList>(this, android.R.layout.simple_list_item_1, songlist);

		m_listview.setAdapter(adapter);
		m_listview.setClickable(true);
		m_listview.setOnItemClickListener(this);
		m_listview.setItemsCanFocus(true);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void onRefreshClick(View view) {
		StringBuilder strb = new StringBuilder("");

		// Iterate though the list and display the information
		ListIterator<Song> it = songlist.Songs.listIterator();
 		while(it.hasNext()){
 			Song nextsong = it.next();
 			strb.append("Title:  " + nextsong.Title + "\n");
 			strb.append("Artist:  " + nextsong.Artist + "\n");
 			strb.append("Length:  " + nextsong.Length + "\n");
 			strb.append("\n");
 		}
 
 		// Rather than writing to a pre-allocated field on the
 		// screen, create a new view, and write to it.
 		LinearLayout top_linear_layout = 
 				(LinearLayout) findViewById(R.id.LinearLayoutMain);

 		TextView report_line = new TextView(this);
 		report_line.setText(strb);
 		top_linear_layout.addView(report_line);
	}
	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		String str = adapter.getItemAtPosition(position).toString();
		Log.i("TEST",str);
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, SongDetailActivity.class);
		intent.putExtra(ITEM, str);
		startActivity(intent);
	}
	public void onClickTextView(View view) {
		Log.i("TEST","TEST");
	}
}
