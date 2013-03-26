package com.example.djhero;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import android.annotation.SuppressLint;
import android.util.Log;

@SuppressLint("UseValueOf")
public class songList{
	public List<Song> Songs = new ArrayList<Song>();
	
	public songList(){
		Songs.add(new Song());
	}
	
	public songList(String songliststring){
		String[] parsed = songliststring.split("\\|");
		for (int i=0; i<parsed.length; i++){
			Songs.add(new Song(parsed[i]));
		}
	}
	public void getListTitles(){
		ListIterator<Song> it = Songs.listIterator();
		while(it.hasNext()){
			Log.i("songs",it.next().Title);
		}
	}
	public void addSong(Song song){
		Songs.add(song);
	}
	public void clearList(){
		Songs.clear();
	}
}