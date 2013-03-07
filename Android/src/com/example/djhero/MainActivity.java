package com.example.djhero;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

import android.view.View;
import android.widget.EditText; 
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		songList songlist = new songList("Shake it:Moe:20|Why can't you love me:Gursimran:40");
		songlist.getListNames();
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
         strb.append( "Whats up my people?\n");
         strb.append( "We taking over!");
         
         // Rather than writing to a pre-allocated field on the
         // screen, create a new view, and write to it.
         
         RelativeLayout top_linear_layout =
                    (RelativeLayout) findViewById(R.id.RelativeLayoutMain);
         
         TextView report_line = new TextView(this);
         report_line.setText(strb);
         top_linear_layout.addView(report_line);

	}
	
	public void getListOfSongs(){
		
	}
	

}
