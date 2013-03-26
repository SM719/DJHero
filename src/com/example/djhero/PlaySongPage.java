package com.example.djhero;

import java.io.IOException;
import java.io.OutputStream;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import java.lang.Math;


public class PlaySongPage extends Activity implements OnSeekBarChangeListener {
	int state = 0;
	ImageButton imageButton;
	private SeekBar bar; // declare seekbar object variable
	boolean fun=false;
	int Global_progress;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_song_page);
		
		String songName = getIntent().getStringExtra("songName");
		TextView textViewforSongPosition = (TextView) findViewById(R.id.textView1);
		textViewforSongPosition.setText(songName);
		
		imageButton = (ImageButton) findViewById(R.id.imageButton1);
		imageButton.setImageResource((R.drawable.pause));
		
		bar = (SeekBar)findViewById(R.id.seekBar0); // make seekbar object
        bar.setOnSeekBarChangeListener(this); // set seekbar listener.
        bar.setProgress(100);
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play_song_page, menu);
		return true;
	}
	
	public void PausePlay (View view){
		
		imageButton = (ImageButton) findViewById(R.id.imageButton1);
		if(fun)
		{
			imageButton.setImageResource((R.drawable.pause));
			fun=false;
		}
		else
		{
			
			imageButton.setImageResource((R.drawable.play));
			fun=true;
		}
		
		if(state == 0){
			sendMessage("pause ");
			state++;
		}
		else{
			sendMessage("resume ");
			state = 0;
		}
	}

	public void songNext (View view){
		sendMessage("next ");
	}
	
	public void songPrevious (View view){
		sendMessage("previous ");
	}
	
	//  Called when the user wants to send a message
	public void sendMessage(String str) {
			MyApplication app = (MyApplication) getApplication();
			
			// Get the message from the box
			
			String msg = str.toLowerCase();
			
			// Create an array of bytes.  First byte will be the
			// message length, and the next ones will be the message
			
			byte buf[] = new byte[msg.length() + 1];
			buf[0] = (byte) msg.length(); 
			System.arraycopy(msg.getBytes(), 0, buf, 1, msg.length());

			// Now send through the output stream of the socket
			
			OutputStream out;
			try {
				out = app.sock.getOutputStream();
				try {
					out.write(buf, 0, msg.length() + 1);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	@Override
	public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
	
		Global_progress=progress;
	}

	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
	
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) 
	
	{
		int volume=Math.abs(100-(Global_progress/10)*10);
		
		Integer.toString(volume);
		//sendMessage("volume "+Integer.toString(Global_progress).format("%03d", volume));
		sendMessage("volume "+Integer.toString(volume));
		Log.i("VolumeTag", Integer.toString(volume));
	}
}

