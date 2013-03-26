package com.example.djhero;

import java.net.Socket;

import android.app.Application;

public class MyApplication extends Application {
	Socket sock = null;
	songList songlist = new songList();
	String listFromDE2 = " ";
}
