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
import android.widget.TextView;

/*
 * Custom list adapter for displaying lists to be selected by user to delete.
 */
public class DeletePlayListSelectAdapter extends BaseAdapter {

	private Activity activity;
	List<String> data = new ArrayList<String>();
	private static LayoutInflater inflater = null;
	List<View> vi_ = new ArrayList<View>();
	int count;
	MyApplication myApp;

	public DeletePlayListSelectAdapter(Activity a, List<String> d,
			MyApplication myApp) {
		// Initialize private variables
		this.myApp = myApp;
		activity = a;
		data.addAll(d);
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		count = 0;
	}

	// return size of list
	public int getCount() {
		if (data == null)
			return 0;
		return data.size();
	}

	// return item at position
	public Object getItem(int position) {
		return position;
	}

	// return ID of item
	public long getItemId(int position) {
		return position;
	}

	// Return the view at position with data set in the list view
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null) {
			vi = inflater.inflate(R.layout.removeplaylist, null);
		}
		vi_.add(position, vi);
		TextView text = (TextView) vi.findViewById(R.id.text_delete_playlist);
		text.setText(data.get(position));
		CheckBox checkBox = (CheckBox) vi
				.findViewById(R.id.checkBox_delete_playlist);
		checkBox.setChecked(myApp.selectedPlayListsToDelete.get(position));
		return vi;
	}
}