package com.group15.djhero;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MyApplication extends Application {
	Socket sock = null;
	songList songlist = new songList();
	songList mainSongList = new songList();
	List<songList> allSongList = new ArrayList<songList>();
	Bitmap[] images = new Bitmap[10];
	String connectedTo = null;
	List<String> availableDE2s = new ArrayList<String>();
	Boolean listComplete = false;
	List<String> playLists = new ArrayList<String>();
	List<Boolean> selectedSongsForPlayList = null;
	List<Boolean> selectedPlayListsToDelete = null;
	Song songSelectedLeft = new Song();
	Song songSelectedRight = new Song();
	Bitmap songSelectedLeftBitmap = Bitmap.createBitmap(280, 280, Bitmap.Config.ARGB_8888);
	Bitmap songSelectedRightBitmap = Bitmap.createBitmap(280, 280, Bitmap.Config.ARGB_8888);
	Boolean receivingFile = false;
	Boolean djDoneLoad = false;

	int lengthOfCurrentSong;
	int progressTracker = 0;
	int positionOfSong;
	boolean playButton = false;
	boolean timeFlag = false;
	ProgressBar songProgressBar;
	TextView timeLeft;
	ImageView imageView;
	TextView textViewforSongPosition;
	TextView textViewforSongArtist;
	int Global_progress = 40;

	int leftSpeed = 1;
	int rightSpeed = 1;
	int djVolumeBar = 70;
	boolean stopSignal = false;
	String voiceCommand = null;
}
