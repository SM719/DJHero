package com.group15.djhero;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.util.Log;

/*
 * Class songlist to store list of songs
 */
@SuppressLint("UseValueOf")
public class songList {
	public List<Song> Songs;
	public static String TERMINATING_STRING = "xx";

	// initialize empty song list
	public songList() {
		Songs = new ArrayList<Song>();
		// Songs.add(new Song());
	}

	// initialize song list using a string (parse string into song objects and
	// add to list)
	public songList(String songliststring) {
		Songs = new ArrayList<Song>();
		addSongs(songliststring);
	}

	// Add songs to song list from string
	public boolean addSongs(String songliststring) {

		// if string is empty then return
		if (songliststring.isEmpty()) {
			return false;
		}
		String[] parsed = songliststring.split("\\|");
		String temp;

		// if string contaings 'xx' then return true, indicating no more songs
		// expected from de2
		for (int i = 0; i < parsed.length; i++) {
			temp = parsed[i];
			if (temp.equals(TERMINATING_STRING)) {
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

	// log all artist names of all songs
	public void getListTitles() {
		Iterator<Song> it = Songs.iterator();
		while (it.hasNext()) {
			Log.i("songs", it.next().Title);
		}
	}

	// add one song
	public void addSong(Song song) {
		Songs.add(song);
	}

	// clear all songs
	public void clearList() {
		Songs.clear();
	}
}