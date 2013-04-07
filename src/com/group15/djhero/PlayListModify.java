package com.group15.djhero;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

/*
 * Class to select songs to be added to playlist
 */
public class PlayListModify extends Activity implements OnItemClickListener {

	private ListView p_listview;
	PlayListSelectAdapter adapter;
	MyApplication myApp;

	// initialize list to display all songs on the sd card on de2
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_list_modify);
		myApp = (MyApplication) PlayListModify.this.getApplication();
		p_listview = (ListView) findViewById(R.id.new_playlist_list_view);
		setTitle("Create playlist");
		p_listview.setOnItemClickListener(this);

		// mark all songs as false(not selected) by default
		myApp.selectedSongsForPlayList = new ArrayList<Boolean>();
		for (int i = 0; i < myApp.mainSongList.Songs.size(); i++) {
			myApp.selectedSongsForPlayList.add(i, false);
		}

		// set action bar
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play_list_modify, menu);
		return true;
	}

	// dsiplay list of songs
	@Override
	protected void onResume() {
		// call list adapter
		super.onResume();

		adapter = new PlayListSelectAdapter(PlayListModify.this,
				myApp.mainSongList, myApp);
		p_listview.setAdapter(adapter);
	}

	// menu bar
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		// go back to previous page
		case android.R.id.home:
			super.onBackPressed();
			return true;

			// cancel current modification to playlist
		case R.id.action_delete:
			finish();
			return true;

			// add selected songs to playlist
		case R.id.action_save:

			EditText editText = (EditText) findViewById(R.id.edittext_done);
			String playListName = editText.getText().toString();
			myApp.playLists.add(playListName);
			songList newPLaylistsSongs = new songList();
			for (int i = 0; i < myApp.selectedSongsForPlayList.size(); i++) {
				if (myApp.selectedSongsForPlayList.get(i) == true) {
					newPLaylistsSongs.addSong(myApp.mainSongList.Songs.get(i));
				}

			}
			myApp.allSongList.add(newPLaylistsSongs);
			finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// when a song is selected mark as true to be added to playlist and update
	// the checkbox
	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int position,
			long arg3) {
		CheckBox checkBox = (CheckBox) arg1
				.findViewById(R.id.checkBoxPlayListSelect);
		System.out.println(position
				+ myApp.selectedSongsForPlayList.get(position).toString());
		if (checkBox.isChecked()) {
			myApp.selectedSongsForPlayList.set(position, false);
		} else {
			myApp.selectedSongsForPlayList.set(position, true);
		}

		checkBox.setChecked(myApp.selectedSongsForPlayList.get(position));

	}

}
