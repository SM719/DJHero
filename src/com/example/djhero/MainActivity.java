package com.example.djhero;

import java.util.ListIterator;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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
         songList songlist = new songList("Shake it:Moe:20|Why can't you love me:Gursimran:40|I ate a shit ton of Reese's cups:Sukhi Man:30");
         
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
}
