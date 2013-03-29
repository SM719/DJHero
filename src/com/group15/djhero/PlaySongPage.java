package com.group15.djhero;

import android.app.Activity;
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

	int positionOfSong;

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
		ProgressBar pb = (ProgressBar) findViewById(R.id.progressBar1);

		imageView = (ImageView) findViewById(R.id.imageView1);
		if (myApp.images.get(positionOfSong) != null) {
			imageView.setImageBitmap(myApp.images.get(positionOfSong));
		}
		imageButton = (ImageButton) findViewById(R.id.imageButton1);
		imageButton.setImageResource((R.drawable.pause));

		bar = (SeekBar) findViewById(R.id.seekBar0); // make seekbar object
		bar.setOnSeekBarChangeListener(this); // set seekbar listener.
		bar.setProgress(40);

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
		if (myApp.images.get(positionOfSong) != null) {
			imageView.setImageBitmap(myApp.images.get(positionOfSong));
		}
		SendMessage.sendMessage("n ", myApp.sock);

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
		if (myApp.images.get(positionOfSong) != null) {
			imageView.setImageBitmap(myApp.images.get(positionOfSong));
		}
		SendMessage.sendMessage("b ", myApp.sock);
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
}
