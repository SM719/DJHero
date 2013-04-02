package com.group15.djhero;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class fragment_list extends Fragment implements OnItemClickListener {
	private ListView m_listview;
	MyApplication myApp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	        Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View V = inflater.inflate(R.layout.fragment_list, container, false);

		m_listview = (ListView) V.findViewById(R.id.new_playlist_list_view2);
		m_listview.setOnItemClickListener(this);
		myApp = (MyApplication) getActivity().getApplication();
		myApp.mainSongList.addSong(new Song("1:Test:4000:moe|"));
		LazyAdapter adapter = new LazyAdapter(getActivity(), myApp.mainSongList);
		m_listview.setAdapter(adapter);

		return V;
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int position,
	        long arg3) { 
			myApp.songSelectedLeft = myApp.mainSongList.Songs.get(position);
			getActivity().getFragmentManager().popBackStack();
		}
}