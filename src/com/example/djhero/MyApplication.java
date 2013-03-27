package com.example.djhero;

import java.net.Socket;

import android.app.Application;

public class MyApplication extends Application {
	Socket sock = null;
	songList songlist = new songList("1:believe:20|2:In+da+club:30");
	String listFromDE2 = " ";
}
