package com.group15.djhero;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/*
 * Class to display songs in a playlist
 */
public class OnePlayList extends Activity implements OnItemClickListener {

	private ListView m_listview;
	private songList playList;
	MyApplication myApp;

	// initialize list view of songs and action bar
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myApp = (MyApplication) OnePlayList.this.getApplication();
		setContentView(R.layout.activity_one_play_list);
		m_listview = (ListView) findViewById(R.id.one_lists_list_view);
		m_listview.setOnItemClickListener(this);
		playList = myApp.allSongList.get(getIntent().getIntExtra(
				"playlistSelected", 0));
		setTitle(myApp.playLists.get(getIntent().getIntExtra(
				"playlistSelected", 0)));
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.one_play_list, menu);
		return true;
	}

	// Menu bar item clicked
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// go back to the previous page
		case android.R.id.home:
			super.onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// When song is selected , open the song select page
	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int position,
			long arg3) {

		if (myApp.sock != null) {
			Song thisSong = playList.Songs.get(position);
			myApp.songlist = playList;
			Intent intent = new Intent(this, PlaySongPage.class);
			intent.putExtra("songName", thisSong.Title);
			intent.putExtra("position", position);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
		} else {
			// Take the user to the settings view if the socket is not open
			Intent intent = new Intent(this, AutoDetect.class);
			startActivity(intent);

		}
	}

	// display the list of songs
	public void onResume() {
		super.onResume();
		LazyAdapter adapter = new LazyAdapter(OnePlayList.this, playList);
		m_listview.setAdapter(adapter);
	}
}
