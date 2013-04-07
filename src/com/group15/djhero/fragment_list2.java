package com.group15.djhero;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/*
 * Class for fragment list to display list of songs that the user selects in dj mode
 */
public class fragment_list2 extends Fragment implements OnItemClickListener {
	private ListView m_listview;
	MyApplication myApp;

	// initialize list view
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View V = inflater.inflate(R.layout.fragment_list, container, false);

		m_listview = (ListView) V.findViewById(R.id.new_playlist_list_view2);
		m_listview.setOnItemClickListener(this);
		myApp = (MyApplication) getActivity().getApplication();
		LazyAdapter adapter = new LazyAdapter(getActivity(), myApp.mainSongList);
		m_listview.setAdapter(adapter);
		return V;

	}

	// when an item is selected, close the fragment and set one of the songs of
	// DJ to the selected one
	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int position,
			long arg3) {

		myApp.songSelectedLeft = myApp.mainSongList.Songs.get(position);
		myApp.songSelectedLeftBitmap = myApp.images[position];

		getActivity().getFragmentManager().beginTransaction().remove(this)
				.commit();
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragment2 fragment2 = new fragment2();
		fragmentTransaction.add(R.id.fragment_container, fragment2);
		fragmentTransaction.commit();

	}
}