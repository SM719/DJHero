package com.example.djhero;

import java.util.ListIterator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnItemClickListener {
	public final static String ITEM = "com.example.MainActivity.ITEM";
	private ListView m_listview;
	
	 @Override
	  public void onCreate(Bundle savedInstanceState)
	  {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.activity_main);

	    m_listview = (ListView) findViewById(R.id.id_list_view);

	    String[] items = new String[] {"Sukhi 1", "Gursimran 2", "Donald 3", "Nabeel 4", "Nikola 5", "Moe 6", "Nigel 7", "Ali 8", "Mike 9", "Paul 10"};
	    ArrayAdapter<String> adapter =
	      new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);

	    m_listview.setAdapter(adapter);
	    
	    m_listview.setOnItemClickListener(this);
	    

	  }


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	public void onRefreshClick(View view) {
		 StringBuilder strb = new StringBuilder("");
		 
		 // Create new songlist element
         songList songlist = new songList("Shake it:Moe:20|Why can't you love me:Gursimran:30|I ate a shit ton of Reese's cups:Sukhi Mann:40");
         
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
         RelativeLayout top_linear_layout =
        		 (RelativeLayout) findViewById(R.id.RelativeLayoutMain);
         
         TextView report_line = new TextView(this);
         report_line.setText(strb);
         top_linear_layout.addView(report_line);

	}


	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		String str = adapter.getItemAtPosition(position).toString();
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(this, SongDetailActivity.class);
		intent.putExtra(ITEM, str);
		startActivity(intent);
		
	}
}
