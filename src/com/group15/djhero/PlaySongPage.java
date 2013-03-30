package com.group15.djhero;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class PlaySongPage extends Activity implements OnSeekBarChangeListener {
	boolean state = false;
	ImageButton imageButton;
	private SeekBar bar; // declare seekbar object variable
	boolean fun = false;
	int Global_progress;
	ImageView imageView;
	int progressTracker = 0;
	int lengthOfCurrentSong;

	int positionOfSong;
	ProgressBar songProgressBar;
	Timer pb_timer = new Timer();
	UpdateSongProgress pb_task;

	TextView timeLeft;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyApplication myApp = (MyApplication) PlaySongPage.this
				.getApplication();
		setContentView(R.layout.activity_play_song_page);

		String songName = getIntent().getStringExtra("songName");
		TextView textViewforSongPosition = (TextView) findViewById(R.id.connectedIPDisplay);
		textViewforSongPosition.setText(songName);

		positionOfSong = getIntent().getIntExtra("position", 0);

		imageView = (ImageView) findViewById(R.id.imageView1);
		if (myApp.images[positionOfSong] != null) {
			imageView.setImageBitmap(myApp.images[positionOfSong]);
		}
		imageButton = (ImageButton) findViewById(R.id.imageButton1);
		imageButton.setImageResource((R.drawable.pause));

		bar = (SeekBar) findViewById(R.id.seekBar0); // make seekbar object
		bar.setOnSeekBarChangeListener(this); // set seekbar listener.
		bar.setProgress(40); // default value for volume seekbar

		lengthOfCurrentSong = (myApp.songlist.Songs.get(positionOfSong).Length) / 1000;
		songProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		songProgressBar.setProgress(progressTracker);
		songProgressBar.setMax(lengthOfCurrentSong);

		timeLeft = (TextView) findViewById(R.id.textView1);
		timeLeft.setTextColor(Color.BLACK);
		timeLeft.setText("0:00");

		// Set up a timer task. We will use the timer to check the
		// to update the song progress bar
		pb_task = new UpdateSongProgress();
		pb_timer.schedule(pb_task, 1750, 1000);

	}
	
	@Override
	public void onRestart(){
		super.onRestart();
		if (state == false){
			imageButton.setImageResource((R.drawable.play));
		}
		else {
			imageButton.setImageResource((R.drawable.pause));
		}
		songProgressBar.setProgress(progressTracker);
		timeLeft.setText(String.valueOf((lengthOfCurrentSong - progressTracker+1)/60)+":"+String.format("%02d", Integer.valueOf(((lengthOfCurrentSong - progressTracker+1)%60))));
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play_song_page, menu);
		return true;
	}

	public void PausePlay(View view) {
		MyApplication myApp = (MyApplication) PlaySongPage.this
				.getApplication();
		imageButton = (ImageButton) findViewById(R.id.imageButton1);

		if (state == false) {
			SendMessage.sendMessage("x ", myApp.sock);
			imageButton.setImageResource((R.drawable.play));
			state = true;
		} else {
			SendMessage.sendMessage("r ", myApp.sock);
			imageButton.setImageResource((R.drawable.pause));
			state = false;
		}
	}

	public void songNext(View view) {
		MyApplication myApp = (MyApplication) PlaySongPage.this
				.getApplication();
		positionOfSong = (++positionOfSong) % myApp.songlist.Songs.size();
		Log.i("position", Integer.toString(positionOfSong));
		Song thisSong = myApp.songlist.Songs.get(positionOfSong);

		TextView textViewforSongPosition = (TextView) findViewById(R.id.connectedIPDisplay);
		textViewforSongPosition.setText(thisSong.Title);

		progressTracker = 0;
		lengthOfCurrentSong = (myApp.songlist.Songs.get(positionOfSong).Length) / 1000;

		try {
			imageView.setImageBitmap(myApp.images[positionOfSong]);
		} catch (NullPointerException e) {
		} catch (IndexOutOfBoundsException e) {
		}
		SendMessage.sendMessage(
				"p "
						+ String.valueOf((myApp.songlist.Songs
								.get(positionOfSong).id)), myApp.sock);

	}

	public void songPrevious(View view) {
		MyApplication myApp = (MyApplication) PlaySongPage.this
				.getApplication();

		Log.i("positionBefore", Integer.toString(positionOfSong));
		if (positionOfSong > 0)
			positionOfSong = positionOfSong - 1;
		Log.i("positionAfter", Integer.toString(positionOfSong));

		Song thisSong = myApp.songlist.Songs.get(positionOfSong);

		TextView textViewforSongPosition = (TextView) findViewById(R.id.connectedIPDisplay);
		textViewforSongPosition.setText(thisSong.Title);

		progressTracker = 0;
		lengthOfCurrentSong = (myApp.songlist.Songs.get(positionOfSong).Length) / 1000;

		try {
			imageView.setImageBitmap(myApp.images[positionOfSong]);
		} catch (NullPointerException e) {
		} catch (IndexOutOfBoundsException e) {
		}
		SendMessage.sendMessage("p "+ String.valueOf((myApp.songlist.Songs.get(positionOfSong).id)), myApp.sock);
	}

	@Override
	// Update the volume progress as the user changes it
	public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
		if ((progress % 10) < 5) {
			progress = (progress / 10) * 10;
		} else {
			progress = ((progress / 10) * 10) + 10;
		}
		Global_progress = progress;
	}

	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		MyApplication myApp = (MyApplication) PlaySongPage.this
				.getApplication();

		int volume = (Global_progress / 10) * 10;

		// Send desired volume to the DE2
		SendMessage.sendMessage("volume " + Integer.toString(volume),
				myApp.sock);
		Log.i("VolumeTag", Integer.toString(volume));
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}

	public class UpdateSongProgress extends TimerTask {
		public void run() {
			MyApplication myApp = (MyApplication) PlaySongPage.this
					.getApplication();
			if (lengthOfCurrentSong +1 >= progressTracker) {
				if (!state)
					runOnUiThread(new Runnable() {
						@Override
						public void run() {	
							timeLeft.setText(String.valueOf((lengthOfCurrentSong - progressTracker+1)/60)+":"+String.format("%02d", Integer.valueOf(((lengthOfCurrentSong - progressTracker+1)%60))));
							
						}
					});
					songProgressBar.setProgress(progressTracker++);
				
				
				
			} else {
				positionOfSong = (++positionOfSong)% myApp.songlist.Songs.size();
				SendMessage.sendMessage("p "+ String.valueOf((myApp.songlist.Songs.get(positionOfSong).id)), myApp.sock);
				
				try {
					Thread.sleep(1750);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				progressTracker = 0;
				songProgressBar.setProgress(progressTracker);
				lengthOfCurrentSong = (myApp.songlist.Songs.get(positionOfSong).Length) / 1000;
				songProgressBar.setMax(lengthOfCurrentSong);
				
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						MyApplication myApp = (MyApplication) PlaySongPage.this
								.getApplication();
						TextView textViewforSongPosition = (TextView) findViewById(R.id.connectedIPDisplay);
						Song thisSong = myApp.songlist.Songs
								.get(positionOfSong);
						textViewforSongPosition.setText(thisSong.Title);
						imageView.setImageBitmap(myApp.images[positionOfSong]);
					}
				});

			}

		}
	}	
}
