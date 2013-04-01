package com.group15.djhero;

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_lists);
		MyApplication myApp = (MyApplication) PlayLists.this.getApplication();
		p_listview = (ListView) findViewById(R.id.lists_list_view);
		myApp.playLists.add("test");
		p_listview.setOnItemClickListener(this);
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
		
		case R.id.action_add_new_playlist:
			Intent newPlaylist = new Intent(this, PlayListModify.class);
			newPlaylist.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			this.startActivity(newPlaylist);
			return true;
	
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int position,
			long arg3) {
		
				Intent intent = new Intent(this, OnePlayList.class);
				startActivity(intent);
	}
}
