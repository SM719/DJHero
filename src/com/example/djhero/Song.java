package com.example.djhero;

public class Song {
	public String Title;
	public String Artist;
	public int Length;
	
	public Song(){
		Title = "Untitled";
		Artist = "Unknown Artist";
		Length = 0;
	}
	
	public Song(String songstring){
		String[] parsedstring = songstring.split(":");
		Title = parsedstring[0];
		Artist = parsedstring[1];
		Length = Integer.parseInt(parsedstring[2]);
	}
}
