package com.example.djhero;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class SongListAdapter<T> extends ArrayAdapter<songList> implements OnClickListener {
	private songList songlist;
	private Context context;
	
	public SongListAdapter(Context context, int textViewResourceId, songList songlist){
		super(context, textViewResourceId, (ArrayList)songlist.Songs);
		this.context = context;
        this.songlist = songlist;
	}
	public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
        	// Inflate the view that the songs are going to be in
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.song_row, null);
        }

        Song song = songlist.Songs.get(position);
        if (song!= null) {
        	// This should likely be moved somewhere else so that
        	// it is created once.  Less for garbage collector to do...
        	TextView tv = (TextView)view.findViewById(R.id.list_item_song_title);
            if (tv != null) {
                tv.setText(String.format("%s", song.Title));
            }
            tv = (TextView)view.findViewById(R.id.list_item_song_artist);
            if (tv != null) {
                tv.setText(String.format("%s", song.Artist));
            }
         }
        return view;
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}
