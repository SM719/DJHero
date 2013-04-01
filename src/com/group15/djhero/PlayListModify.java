package com.group15.djhero;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;

public class PlayListModify extends Activity implements OnItemClickListener{

	private ListView p_listview;
	PlayListSelectAdapter adapter;
	MyApplication myApp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_list_modify);
		 myApp = (MyApplication) PlayListModify.this.getApplication();
		p_listview = (ListView) findViewById(R.id.new_playlist_list_view);

		p_listview.setOnItemClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play_list_modify, menu);
		return true;
	}
	

	
	@Override
	protected void onResume(){
		//call list adapter
		super.onResume();
		
		adapter = new PlayListSelectAdapter(PlayListModify.this, myApp.songlist);
		p_listview.setAdapter(adapter);
	}
	


	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		
		case R.id.action_delete:
			finish();
//			Intent newPlaylist = new Intent(this, PlayListModify.class);
//			newPlaylist.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//			this.startActivity(newPlaylist);
			return true;
	
		case R.id.action_save:
			CheckBox checkBox = (CheckBox) findViewById(R.id.checkBoxPlayListSelect);
			View eachSong;
			for (int i=0; i < myApp.songlist.Songs.size(); i++){
				eachSong = adapter.getViewAt(i);
				checkBox = (CheckBox)eachSong.findViewById(R.id.checkBoxPlayListSelect);
				if(checkBox.isChecked()){
					System.out.println(i);
				}
			}
		default:
			return super.onOptionsItemSelected(item);
		}
	}


	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int position,
			long arg3) {
		
		myApp.selectedSongsForPlayList.add(myApp.songlist.Songs.get(position).id);
	}
	
	

}
