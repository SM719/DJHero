package com.group15.djhero;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class OnePlayList extends Activity implements OnItemClickListener{

	private ListView m_listview;
	private songList playList;
	MyApplication myApp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		myApp = (MyApplication) OnePlayList.this.getApplication();
		setContentView(R.layout.activity_one_play_list);
		m_listview = (ListView) findViewById(R.id.one_lists_list_view);
		m_listview.setOnItemClickListener(this);
		playList = myApp.allSongList.get(getIntent().getIntExtra("playlistSelected",0));
		setTitle(myApp.playLists.get(getIntent().getIntExtra("playlistSelected",0)));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.one_play_list, menu);
		return true;
	}

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
			}
		 else {
			// Take the user to the settings view if the socket is not open
			Intent intent = new Intent(this, AutoDetect.class);
			startActivity(intent);

		}
	}

	public void onResume(){
		super.onResume();
		LazyAdapter adapter = new LazyAdapter(OnePlayList.this,
				playList);
		m_listview.setAdapter(adapter);
	}
}
