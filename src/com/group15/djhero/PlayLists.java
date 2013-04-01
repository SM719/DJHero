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

public class PlayLists extends Activity implements OnItemClickListener {

	private ListView p_listview;
	ListIpAddresses adapter;
	int REQUEST_CODE;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_lists);
		p_listview = (ListView) findViewById(R.id.lists_list_view);
		p_listview.setOnItemClickListener(this);
		setTitle("My Playlists");
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	
	@Override
	protected void onResume(){
		//call list adapter
		super.onResume();
		MyApplication myApp = (MyApplication) PlayLists.this.getApplication();
		adapter = new ListIpAddresses(PlayLists.this, myApp.playLists);
		p_listview.setAdapter(adapter);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play_lists, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			super.onBackPressed();
			return true;
		
		case R.id.action_add_new_playlist:
			Intent newPlaylist = new Intent(this, PlayListModify.class);
			newPlaylist.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			this.startActivity(newPlaylist);
			return true;
	
		case R.id.action_delete_playlist:
			Intent deletePlaylist = new Intent(this, DeletePlaylist.class);
			deletePlaylist.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			this.startActivity(deletePlaylist);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int position,
			long arg3) {
				Intent intent = new Intent(this, OnePlayList.class);
				intent.putExtra("playlistSelected", position);
				startActivity(intent);
	}
	

}
