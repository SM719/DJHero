package com.group15.djhero;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.graphics.Bitmap;

public class MyApplication extends Application {
	Socket sock = null;
	songList songlist = new songList("1:believe:20|2:In+da+club:30");
	String listFromDE2 = "";
	ArrayList<Bitmap> images = new ArrayList<Bitmap>();
	String connectedTo = null;
	List<String> availableDE2s = new ArrayList<String>();
	Boolean listComplete = false;
	int test = 0;
}
