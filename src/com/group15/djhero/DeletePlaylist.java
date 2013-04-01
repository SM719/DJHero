package com.group15.djhero;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class DeletePlaylist extends Activity implements OnItemClickListener{
	private ListView p_listview;
	DeletePlayListSelectAdapter adapter;
	MyApplication myApp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_delete_playlist);
		 myApp = (MyApplication) DeletePlaylist.this.getApplication();
			p_listview = (ListView) findViewById(R.id.delete_playlist_list_view);

			p_listview.setOnItemClickListener(this);
			
			myApp.selectedPlayListsToDelete = new ArrayList<Boolean>();
			for(int i=0; i<myApp.playLists.size(); i++){
				myApp.selectedPlayListsToDelete.add(i, false);
			}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.delete_playlist, menu);
		return true;
	}
	
	@Override
	protected void onResume(){
		//call list adapter
		super.onResume();
		
		adapter = new DeletePlayListSelectAdapter(DeletePlaylist.this, myApp.playLists, myApp);
		p_listview.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int position,
			long arg3) {
		CheckBox checkBox = (CheckBox)arg1.findViewById(R.id.checkBox_delete_playlist);
		System.out.println(position + myApp.selectedPlayListsToDelete.get(position).toString());
		if(checkBox.isChecked())
        {
        	//checkBox.setChecked(false);                       
            myApp.selectedPlayListsToDelete.set(position, false);                      
        }
        else
        {   
        	//checkBox.setChecked(true);
            myApp.selectedPlayListsToDelete.set(position, true);           
        }
		
		checkBox.setChecked(myApp.selectedPlayListsToDelete.get(position));
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		
		case R.id.action_delete:
			finish();
			return true;
	
		case R.id.action_save:
			
			for(int i =0;i<myApp.playLists.size();i++){
				if(myApp.selectedPlayListsToDelete.get(i) == true){
					myApp.playLists.remove(i);
					myApp.allSongList.remove(i);
				}
				
			}   
			finish();
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
