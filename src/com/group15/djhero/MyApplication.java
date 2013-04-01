package com.group15.djhero;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;


public class MyApplication extends Application {
	Socket sock = null;
	songList songlist = new songList();
	List<songList> allSongList = new ArrayList<songList>();
	Bitmap[] images = new Bitmap[10];
	String connectedTo = null;
	List<String> availableDE2s = new ArrayList<String>();
	Boolean listComplete = false;
	List<String> playLists = new ArrayList<String>();
	List<Integer> selectedSongsForPlayList = new ArrayList<Integer>();
	
	
	int lengthOfCurrentSong;
	int progressTracker = 0;
	int positionOfSong;
	boolean playButton = false;
	boolean timeFlag = false;
	ProgressBar songProgressBar;
	TextView timeLeft;
	ImageView imageView;
	TextView textViewforSongPosition;
	int Global_progress = 40;
	
}
