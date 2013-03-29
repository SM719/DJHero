package com.group15.djhero;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.util.Log;

@SuppressLint("UseValueOf")
public class songList {
	public List<Song> Songs;
	public static String TERMINATING_STRING = "xx";

	public songList() {
		Songs = new ArrayList<Song>();
		// Songs.add(new Song());
	}

	public songList(String songliststring) {
		Songs = new ArrayList<Song>();
		addSongs(songliststring);
	}

	public boolean addSongs(String songliststring) {

		if (songliststring.isEmpty()) {
			Log.i("SongList", "Skipped - Songlist string is empty");
			return false;
		}
		String[] parsed = songliststring.split("\\|");
		String temp;
		for (int i = 0; i < parsed.length; i++) {
			temp = parsed[i];
			if (temp.equals(TERMINATING_STRING)) {
				Log.i("SongList", "Finished adding songs - " + Songs.size()
						+ " songs in songlist");
				return true;
			} else {
				try {
					Songs.add(new Song(parsed[i]));
				} catch (ArrayIndexOutOfBoundsException e) {
					Log.i("SongList",
							"Could not add song - Missing information");
				} catch (NumberFormatException e) {
					Log.i("SongList", "String might be empty");
				}
			}
		}
		return false;
	}

	public void getListTitles() {
		Iterator<Song> it = Songs.iterator();
		while (it.hasNext()) {
			Log.i("songs", it.next().Title);
		}
	}

	public void addSong(Song song) {
		Songs.add(song);
	}

	public void clearList() {
		Songs.clear();
	}
}