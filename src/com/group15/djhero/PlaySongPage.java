package com.group15.djhero;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

/*
 * Class to display song being played
 */
public class PlaySongPage extends Activity implements OnSeekBarChangeListener {
	ImageButton imageButton;
	private SeekBar bar; // declare seekbar object variable
	Timer pb_timer = new Timer();
	UpdateSongProgress pb_task;
	MyApplication myApp;
	private ShakeListener mShaker;
	final Context context = this;
	private SensorManager mySensorManager;
	private static final int VOICE_RECOGNITION_REQUEST_CODE = 1001;

	// Initialize list view and images and menu bar
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_song_page);
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);

		// set view attributes to song data
		myApp = (MyApplication) PlaySongPage.this.getApplication();
		myApp.imageView = (ImageView) findViewById(R.id.imageView1);
		myApp.textViewforSongPosition = (TextView) findViewById(R.id.songName);
		myApp.textViewforSongArtist = (TextView) findViewById(R.id.artistName);
		imageButton = (ImageButton) findViewById(R.id.imageButton1);
		bar = (SeekBar) findViewById(R.id.seekBar0); // make seekbar object
		myApp.songProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		myApp.timeLeft = (TextView) findViewById(R.id.textView1);
		String songName = getIntent().getStringExtra("songName");
		myApp.textViewforSongPosition.setText(songName);

		// if song is not being played then send message to de2 to start playing
		// song
		if (!myApp.timeFlag) {
			myApp.positionOfSong = getIntent().getIntExtra("position", 0);
			SendMessage.sendMessage(
					"p " + myApp.songlist.Songs.get(myApp.positionOfSong).id,
					myApp.sock);
		}
		// iF song is being played then set button to pause else play
		if (!myApp.playButton)
			imageButton.setImageResource((R.drawable.pause));
		else
			imageButton.setImageResource((R.drawable.play));

		// if song selected is not already being played then send message to de2
		// to start the new song selected
		if (myApp.positionOfSong != getIntent().getIntExtra("position", 0)) {
			myApp.positionOfSong = getIntent().getIntExtra("position", 0);
			SendMessage.sendMessage(
					"p " + myApp.songlist.Songs.get(myApp.positionOfSong).id,
					myApp.sock);
			imageButton.setImageResource((R.drawable.pause));
			myApp.playButton = false;
			myApp.progressTracker = 0;
		}

		// set global variables to song length of current song being played
		myApp.lengthOfCurrentSong = (myApp.songlist.Songs
				.get(myApp.positionOfSong).Length) / 1000;
		myApp.textViewforSongArtist.setText(myApp.songlist.Songs
				.get(myApp.positionOfSong).artist);

		// set image to artwaork of song
		if (myApp.images[myApp.positionOfSong] != null) {
			myApp.imageView.setImageBitmap(myApp.images[myApp.positionOfSong]);
		}

		// initialize volume bar
		bar.setOnSeekBarChangeListener(this); // set Seekbar listener.
		bar.setProgress(myApp.Global_progress); // default value for volume
												// Seekbar

		// initialize progress bar of song being played
		myApp.songProgressBar.setProgress(myApp.progressTracker);
		myApp.songProgressBar.setMax(myApp.lengthOfCurrentSong);

		// set color of text of song time remian to white
		myApp.timeLeft.setTextColor(Color.WHITE);

		// change time from milliseconds to mm:ss
		myApp.timeLeft.setText(String.valueOf((myApp.lengthOfCurrentSong
				- myApp.progressTracker + 1) / 60)
				+ ":"
				+ String.format(
						"%02d",
						Integer.valueOf(((myApp.lengthOfCurrentSong
								- myApp.progressTracker + 1) % 60))));

		setTitle(myApp.mainSongList.Songs.get(myApp.positionOfSong).Title);

		// Set up a timer task. We will use the timer to check the
		// to update the song progress bar
		if (!myApp.timeFlag) {
			pb_task = new UpdateSongProgress();
			pb_timer.schedule(pb_task, 1750, 1000);
			myApp.timeFlag = true;
		}

		// initialize accelerometer to work for shake to next
		final Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		mShaker = new ShakeListener(this);
		mShaker.setOnShakeListener(new ShakeListener.OnShakeListener() {

			@Override
			public void onShake() {
				songNext(null);
				Log.i("shake:", "sending next\n");
				vibe.vibrate(100);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play_song_page, menu);
		return true;
	}

	// menu bar item
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		// go to previous page
		case android.R.id.home:
			super.onBackPressed();
			return true;

			// start voice to text
		case R.id.action_speaker:
			speak(null);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// stop currently playing song
	public void StopMusic(View view) {
		// re initialize all song variables
		MyApplication myApp = (MyApplication) PlaySongPage.this
				.getApplication();
		SendMessage.sendMessage("s", myApp.sock);
		myApp.playButton = true;
		imageButton.setImageResource((R.drawable.play));
		myApp.stopSignal = true;
		myApp.progressTracker = 0;
		myApp.songProgressBar.setProgress(myApp.progressTracker);
		myApp.timeLeft.setText(String.valueOf((myApp.lengthOfCurrentSong
				- myApp.progressTracker + 1) / 60)
				+ ":"
				+ String.format(
						"%02d",
						Integer.valueOf(((myApp.lengthOfCurrentSong
								- myApp.progressTracker + 2) % 60))));

	}

	// if song has started then pause else play song
	public void PausePlay(View view) {
		MyApplication myApp = (MyApplication) PlaySongPage.this
				.getApplication();
		imageButton = (ImageButton) findViewById(R.id.imageButton1);

		if (myApp.playButton == false) {
			SendMessage.sendMessage("x ", myApp.sock);
			imageButton.setImageResource((R.drawable.play));
			myApp.playButton = true;
		} else {

			if (myApp.stopSignal == false) {
				SendMessage.sendMessage("r ", myApp.sock);
			} else {
				SendMessage.sendMessage(
						"p "
								+ myApp.mainSongList.Songs
										.get(myApp.positionOfSong).id,
						myApp.sock);
				myApp.stopSignal = false;
			}

			imageButton.setImageResource((R.drawable.pause));
			myApp.playButton = false;
		}
	}

	// Start Voice to text
	public void speak(View view) {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
		// Specify the calling package to identify your application
		intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getClass()
				.getPackage().getName());

		int noOfMatches = 1;

		// Set how many results to receive
		intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, noOfMatches);
		startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
	}

	// When returned from speech to text, check result if it matched any of the
	// actions we need to perform
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == VOICE_RECOGNITION_REQUEST_CODE)

			// If Voice recognition is successful then it returns RESULT_OK
			if (resultCode == RESULT_OK) {

				ArrayList<String> textMatchList = data
						.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

				if (!textMatchList.isEmpty()) {
					// If first Match contains the 'search' word
					// Then start web search.
					if (textMatchList.get(0).contains("search")) {

						String searchQuery = textMatchList.get(0).replace(
								"search", " ");
						Intent search = new Intent(Intent.ACTION_WEB_SEARCH);
						search.putExtra(SearchManager.QUERY, searchQuery);
						startActivity(search);
					} else {

						// If result contains play then play/pause song
						if (textMatchList.get(0).contains("play")
								|| textMatchList.get(0).contains("pause")) {
							PausePlay(null);

						}
						// If result contains next then play next song
						else if (textMatchList.get(0).contains("next")) {
							songNext(null);
						}
						// If result contains previous then play previous song
						else if (textMatchList.get(0).contains("previous")) {
							songPrevious(null);
						}
						// If result contains stop then stop song
						else if (textMatchList.get(0).contains("stop")) {
							StopMusic(null);
						}
						// If result contains volume up then increase volume by
						// 10 points
						else if (textMatchList.get(0).contains("volume up")) {
							if (myApp.Global_progress <= 70) {
								myApp.Global_progress += 10;
							} else {
								myApp.Global_progress = 80;
							}
							bar.setProgress(myApp.Global_progress);
							SendMessage
									.sendMessage(
											"v "
													+ Integer
															.toString((myApp.Global_progress / 10) * 10),
											myApp.sock);
						}
						// If result contains volume down then decrease volume
						// by 10 points
						else if (textMatchList.get(0).contains("volume down")) {
							if (myApp.Global_progress >= 10) {
								myApp.Global_progress -= 10;
							} else {
								myApp.Global_progress = 0;
							}
							bar.setProgress(myApp.Global_progress);
							SendMessage
									.sendMessage(
											"v "
													+ Integer
															.toString((myApp.Global_progress / 10) * 10),
											myApp.sock);
						}
					}

				}
				// Result code for various error.
			}
		super.onActivityResult(requestCode, resultCode, data);
	}

	// Next song method
	public void songNext(View view) {
		MyApplication myApp = (MyApplication) PlaySongPage.this
				.getApplication();
		// set play image button to puase
		imageButton.setImageResource((R.drawable.pause));
		myApp.positionOfSong = (++myApp.positionOfSong)
				% myApp.songlist.Songs.size();

		// set title
		myApp.textViewforSongPosition.setText(myApp.songlist.Songs
				.get(myApp.positionOfSong).Title);

		// set artist
		myApp.textViewforSongArtist.setText(myApp.songlist.Songs
				.get(myApp.positionOfSong).artist);

		// set progress bar to 0 and get length of new song
		myApp.progressTracker = 0;
		myApp.songProgressBar.setProgress(myApp.progressTracker);
		myApp.lengthOfCurrentSong = (myApp.songlist.Songs
				.get(myApp.positionOfSong).Length) / 1000;
		myApp.songProgressBar.setMax(myApp.lengthOfCurrentSong);
		setTitle(myApp.mainSongList.Songs.get(myApp.positionOfSong).Title);

		// set image of new song
		try {
			myApp.imageView.setImageBitmap(myApp.images[myApp.positionOfSong]);
		} catch (NullPointerException e) {
			myApp.imageView.setImageResource(R.drawable.defaultsong);
		} catch (IndexOutOfBoundsException e) {
		}

		// send message to de2 to play song
		SendMessage.sendMessage(
				"p "
						+ String.valueOf((myApp.songlist.Songs
								.get(myApp.positionOfSong).id)), myApp.sock);

	}

	// previous song method
	public void songPrevious(View view) {
		MyApplication myApp = (MyApplication) PlaySongPage.this
				.getApplication();

		// set play image button to puase
		imageButton.setImageResource((R.drawable.pause));

		if (myApp.positionOfSong > 0)
			myApp.positionOfSong = myApp.positionOfSong - 1;

		// set artist
		Song thisSong = myApp.songlist.Songs.get(myApp.positionOfSong);
		myApp.textViewforSongArtist.setText(myApp.songlist.Songs
				.get(myApp.positionOfSong).artist);

		// set song name
		TextView textViewforSongPosition = (TextView) findViewById(R.id.songName);
		textViewforSongPosition.setText(thisSong.Title);

		// reset progress bar of song
		myApp.progressTracker = 0;
		myApp.songProgressBar.setProgress(myApp.progressTracker);
		myApp.lengthOfCurrentSong = (myApp.songlist.Songs
				.get(myApp.positionOfSong).Length) / 1000;
		myApp.songProgressBar.setMax(myApp.lengthOfCurrentSong);

		setTitle(myApp.mainSongList.Songs.get(myApp.positionOfSong).Title);

		// set image of new song
		try {
			myApp.imageView.setImageBitmap(myApp.images[myApp.positionOfSong]);
		} catch (NullPointerException e) {
			myApp.imageView.setImageResource(R.drawable.defaultsong);
		} catch (IndexOutOfBoundsException e) {
		}

		// send message to de2 to play previous song
		SendMessage.sendMessage(
				"p "
						+ String.valueOf((myApp.songlist.Songs
								.get(myApp.positionOfSong).id)), myApp.sock);
	}

	@Override
	// Update the volume progress as the user changes it
	public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
		if ((progress % 10) < 5) {
			progress = (progress / 10) * 10;
		} else {
			progress = ((progress / 10) * 10) + 10;
		}
		myApp.Global_progress = progress;
	}

	// send new volume to de2
	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		MyApplication myApp = (MyApplication) PlaySongPage.this
				.getApplication();

		// Send desired volume to the DE2
		SendMessage
				.sendMessage(
						"volume "
								+ Integer
										.toString((myApp.Global_progress / 10) * 10),
						myApp.sock);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}

	// Timer task to update song progress every minute
	public class UpdateSongProgress extends TimerTask {
		public void run() {
			// check if song has not finished and is not paused then increment
			// progress
			if (myApp.lengthOfCurrentSong + 1 >= myApp.progressTracker) {
				if (!myApp.playButton) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							myApp.timeLeft
									.setText(String
											.valueOf((myApp.lengthOfCurrentSong
													- myApp.progressTracker + 1) / 60)
											+ ":"
											+ String.format(
													"%02d",
													Integer.valueOf(((myApp.lengthOfCurrentSong
															- myApp.progressTracker + 2) % 60))));

						}
					});
					myApp.songProgressBar.setProgress(myApp.progressTracker++);
				}

			}
			// if song has finished move to next song
			else {
				myApp.positionOfSong = (++myApp.positionOfSong)
						% myApp.songlist.Songs.size();
				SendMessage.sendMessage(
						"p "
								+ String.valueOf((myApp.songlist.Songs
										.get(myApp.positionOfSong).id)),
						myApp.sock);

				try {
					Thread.sleep(1750);
				} catch (InterruptedException e) {
				}

				myApp.progressTracker = 0;
				myApp.songProgressBar.setProgress(myApp.progressTracker);
				myApp.lengthOfCurrentSong = (myApp.songlist.Songs
						.get(myApp.positionOfSong).Length) / 1000;
				myApp.songProgressBar.setMax(myApp.lengthOfCurrentSong);

				// update progress bar on ui thread
				runOnUiThread(new Runnable() {
					@Override
					public void run() {

						myApp.textViewforSongPosition
								.setText(myApp.songlist.Songs
										.get(myApp.positionOfSong).Title);
						myApp.imageView
								.setImageBitmap(myApp.images[myApp.positionOfSong]);
						myApp.textViewforSongArtist
								.setText(myApp.songlist.Songs
										.get(myApp.positionOfSong).artist);
					}
				});

			}

		}
	}

}
