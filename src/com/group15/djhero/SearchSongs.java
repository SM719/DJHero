package com.group15.djhero;

import android.os.Bundle;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SearchSongs extends Activity implements OnItemClickListener{

	songList searchedSongs;
	private ListView m_listview;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search_songs);
		m_listview = (ListView) findViewById(R.id.search_list_view);
		m_listview.setOnItemClickListener(this);
		Intent intent = getIntent();
		if(Intent.ACTION_SEARCH.equals(intent.getAction())){
			String query = intent.getStringExtra(SearchManager.QUERY);
			doMySearch(query);
		}
	}

	private void doMySearch(String query) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.search_songs, menu);
		return true;
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int position,
			long arg3) {
		int absolutePosition = 0;
		Boolean found = false;
		MyApplication myApp = (MyApplication) SearchSongs.this.getApplication();
		if (myApp.sock != null) {
				Song thisSong = searchedSongs.Songs.get(position);
				myApp.songlist = myApp.mainSongList;
				for (int i=0; i<myApp.mainSongList.Songs.size(); i++){
					for(int j=0; j<searchedSongs.Songs.size(); j++){
						if (myApp.mainSongList.Songs.get(i).id == searchedSongs.Songs.get(j).id){
							absolutePosition = i;
							found = true;
							break;
						}
					}
					if(found == true){
						break;
					}
				}
				Intent intent = new Intent(this, PlaySongPage.class);
				intent.putExtra("songName", thisSong.Title);
				intent.putExtra("position", absolutePosition);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				startActivity(intent);
			}
		 else {
			// Take the user to the settings view if the socket is not open
			Intent intent = new Intent(this, AutoDetect.class);
			startActivity(intent);

		}
	}

}
