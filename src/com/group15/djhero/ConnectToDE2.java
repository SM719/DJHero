package com.group15.djhero;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ConnectToDE2 extends Activity {

	Button button;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		// This call will result in better error messages if you
		// try to do things in the wrong thread.


		MyApplication myApp = (MyApplication) ConnectToDE2.this.getApplication();
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_connect_to_de2);

		button = (Button) findViewById(R.id.connect);
		if (myApp.sock != null){
			button.setText("Disconnect");
		}
		else {
			button.setText("Connect");
		}
		
		// Set up a timer task.  We will use the timer to check the
		// input queue every 500 ms
		TCPReadTimerTask tcp_task = new TCPReadTimerTask();
		Timer tcp_timer = new Timer();
		tcp_timer.schedule(tcp_task, 3000, 500);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.connect_to_de2, menu);
		return true;
	}

	// Route called when the user presses "connect"
	
	public void openSocket(View view) {
		MyApplication app = (MyApplication) getApplication();
		

		if (app.sock != null){
			button.setText("Connect");
			closeSocket(view);
		}
		else {
			button.setText("Disconnect");
			new SocketConnect().execute((Void) null);
			
		}
	}


	// Called when the user closes a socket
	
	public void closeSocket(View view) {
		MyApplication app = (MyApplication) getApplication();
		Socket s = app.sock;
		try {
			s.getOutputStream().close();
			s.close();
			app.sock = null;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Construct an IP address from the four boxes
	
	public String getConnectToIP() {
		String addr = "";
		EditText text_ip;
		text_ip = (EditText) findViewById(R.id.ip1);
		addr += text_ip.getText().toString();
		text_ip = (EditText) findViewById(R.id.ip2);
		addr += "." + text_ip.getText().toString();
		text_ip = (EditText) findViewById(R.id.ip3);
		addr += "." + text_ip.getText().toString();
		text_ip = (EditText) findViewById(R.id.ip4);
		addr += "." + text_ip.getText().toString();
		return addr;
	}

	// Gets the Port from the appropriate field.
	
	public Integer getConnectToPort() {
		Integer port;
		EditText text_port;

		text_port = (EditText) findViewById(R.id.port);
		port = Integer.parseInt(text_port.getText().toString());

		return port;
	}

	
    // This is the Socket Connect asynchronous thread.  Opening a socket
	// has to be done in an Asynchronous thread in Android.  Be sure you
	// have done the Asynchronous Tread tutorial before trying to understand
	// this code.
	
	public class SocketConnect extends AsyncTask<Void, Void, Socket> {

		// The main parcel of work for this thread.  Opens a socket
		// to connect to the specified IP.
		
		protected Socket doInBackground(Void... voids) {
			Socket s = null;
			String ip = getConnectToIP();
			Integer port = getConnectToPort();

			try {
				s = new Socket(ip, port);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return s;
		}

		// After executing the doInBackground method, this is 
		// automatically called, in the UI (main) thread to store
		// the socket in this app's persistent storage
		
		protected void onPostExecute(Socket s) {
			MyApplication myApp = (MyApplication) ConnectToDE2.this
					.getApplication();
			myApp.sock = s;
		}
	}

	// This is a timer Task.  Be sure to work through the tutorials
	// on Timer Tasks before trying to understand this code.
	
	public class TCPReadTimerTask extends TimerTask {
		public void run() {
			MyApplication app = (MyApplication) ConnectToDE2.this.getApplication();
			if (app.sock != null && app.sock.isConnected()
					&& !app.sock.isClosed()) {
				
				try {
					InputStream in = app.sock.getInputStream();

					// See if any bytes are available from the Middleman
					
					int bytes_avail = in.available();
					if (bytes_avail > 0) {
						
						// If so, read them in and create a sring
						
						byte buf[] = new byte[bytes_avail];
						in.read(buf);

						final String s = new String(buf, 0, bytes_avail, "US-ASCII");
		
						MyApplication myApp = (MyApplication) ConnectToDE2.this.getApplication();
						myApp.listFromDE2 = s;
						Log.i("DE2list", myApp.listFromDE2 );
						// As explained in the tutorials, the GUI can not be
						// updated in an asyncrhonous task.  So, update the GUI
						// using the UI thread.
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
