package com.group15.djhero;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {

	private Activity activity;
	private songList data;
	private static LayoutInflater inflater = null;
	List<View> vi_ = new ArrayList<View>();
	int count;

	public LazyAdapter(Activity a, songList d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		count = 0;
	}

	@Override
	public int getCount() {
		if (data.Songs == null)
			return 0;
		return data.Songs.size();
	}

	@Override
	public Object getItem(int position) {
		return vi_.get(position);
	}

	@Override
	public long getItemId(int position) {
		return vi_.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.item, null);

		vi_.add(position, vi);

		TextView text = (TextView) vi.findViewById(R.id.text);
		ImageView image = (ImageView) vi.findViewById(R.id.image);

		Song song = data.Songs.get(position);

		text.setText(song.Title);
		MyApplication myApp = (MyApplication) activity.getApplication();
		image.setImageBitmap(myApp.images[position]);

		return vi;
	}

}