package com.group15.djhero;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * Custom list view adapter to display list of songs that are available to add to playlists
 */
public class PlayListSelectAdapter extends BaseAdapter {

	private Activity activity;
	private songList data;
	private static LayoutInflater inflater = null;
	List<View> vi_ = new ArrayList<View>();
	int count;
	MyApplication myApp;

	// initialize list view
	public PlayListSelectAdapter(Activity a, songList d, MyApplication myApp) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		count = 0;
		this.myApp = myApp;
	}

	// return view at position
	public View getViewAt(int position) {
		return vi_.get(position);
	}

	// get size of list
	@Override
	public int getCount() {
		if (data.Songs == null)
			return 0;
		return data.Songs.size();
	}

	// get item at position
	@Override
	public Object getItem(int position) {
		return vi_.get(position);
	}

	// get item id of item at position
	@Override
	public long getItemId(int position) {
		return vi_.get(position).getId();
	}

	// get view at position with data in textview, checkbox and image
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.itemplaylist, null);

		vi_.add(position, vi);

		TextView text = (TextView) vi.findViewById(R.id.textViewPlayListSelect);
		ImageView image = (ImageView) vi.findViewById(R.id.imagePlayListSelect);
		CheckBox checkBox = (CheckBox) vi
				.findViewById(R.id.checkBoxPlayListSelect);
		checkBox.setChecked(myApp.selectedSongsForPlayList.get(position));
		Song song = data.Songs.get(position);

		text.setText(song.Title);
		image.setImageBitmap(myApp.images[position]);
		return vi;
	}

}