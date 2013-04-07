package com.group15.djhero;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

/*
 * Class to detect available de2s on the network
 */
public class AutoDetect extends Activity implements OnItemClickListener {

	Button button;
	private ListView m_listview;
	ListIpAddresses adapter;
	TextView textView;
	public static final double SCAN_NETWORK = 100.0;

	/*
	 * Check if the app is connected top a de2, if it is then display the option
	 * to disconnect or connect to another DE2. If the app is not connected then
	 * search for DE2s.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		MyApplication myApp = (MyApplication) getApplication();
		super.onCreate(savedInstanceState);

		// Initialize text views and list adapters
		setContentView(R.layout.activity_auto_detect);
		m_listview = (ListView) findViewById(R.id.ip_list_view);
		m_listview.setOnItemClickListener(this);
		textView = (TextView) findViewById(R.id.connectedIPDisplay);
		setTitle("Connect to DE2");

		// if socket is not connected, display a message and start searching for
		// de2s
		if (myApp.sock == null) {
			textView.setText("Not Connected");
			new FindDE2sOnNetwork().execute();
		} else {
			adapter = new ListIpAddresses(AutoDetect.this, myApp.availableDE2s);
			m_listview.setAdapter(adapter);
			textView.setText("Connected to: " + myApp.connectedTo);
		}

		// set the action bar
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.auto_detect, menu);
		return true;
	}

	/*
	 * Go back when the back button in the action bar is pressed
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			super.onBackPressed();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// Called when the user closes a socket
	public void closeSocket() {
		MyApplication app = (MyApplication) getApplication();
		Socket s = app.sock;
		try {
			s.getOutputStream().close();
			s.close();
			app.sock = null;
			textView.setText("Not Connected");
		} catch (IOException e) {
			textView.setText("Error: Could not disconnect!");
			e.printStackTrace();
		}
	}

	/*
	 * Get the local IP address of the phone, i.e the wifi address of the phone
	 */
	public String getLocalIpAddress() {
		try {
			String Ip = null;
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();

				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						Ip = inetAddress.getHostAddress().toString();
					}
				}
			}
			return Ip;
		} catch (SocketException obj) {
			Log.e("Error occurred during IP fetching: ", obj.toString());
		}
		return null;
	}

	// This is the Socket Connect asynchronous thread. Opening a socket
	// has to be done in an Asynchronous thread in Android. Be sure you
	// have done the Asynchronous Tread tutorial before trying to understand
	// this code.

	public class SocketConnect extends AsyncTask<String, Void, Socket> {

		// The main parcel of work for this thread. Opens a socket
		// to connect to the specified IP.

		protected Socket doInBackground(String... url) {
			Socket s = null;
			String ip = url[0];
			Integer port = 50002;

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
		// the socket in this app's persistent storage and update the
		// view to display the de2 connected to or error message

		protected void onPostExecute(Socket s) {
			MyApplication myApp = (MyApplication) AutoDetect.this
					.getApplication();
			myApp.sock = s;
			if (myApp.sock != null) {
				textView.setText("Connected to: " + myApp.connectedTo);
			} else {
				textView.setText("Error: Could not connect");
			}

		}
	}

	// This is a timer Task to read data from the middleman every 500 ms
	// once a connection has been established to the DE2

	public class TCPReadTimerTask extends TimerTask {
		public void run() {
			MyApplication app = (MyApplication) AutoDetect.this
					.getApplication();
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

						final String s = new String(buf, 0, bytes_avail,
								"US-ASCII");

						MyApplication myApp = (MyApplication) AutoDetect.this
								.getApplication();

						// check if the message contains "djdj". This is to
						// identify when the two songs chosen
						// for DJ ahve been brought in to memory on the de2, so
						// the loading bar on the android
						// can be dismissed

						if (s.contains("djdj")) {
							myApp.djDoneLoad = true;
						}

						// this is for receiveing song list from the de2. Once
						// the message has been recieved, the app
						// sends "a" to the DE2 acknowledging the part of the
						// list has been transferred
						else {
							myApp.listComplete = myApp.mainSongList.addSongs(s);
							Log.i("DE2list", s);
							SendMessage.sendMessage("a", myApp.sock);
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// This is an asynchronous task that goes thru the network to search for
	// available de2s
	class FindDE2sOnNetwork extends AsyncTask<Void, Integer, List<String>> {

		ProgressDialog progress;
		MyApplication app = (MyApplication) AutoDetect.this.getApplication();

		// Display a progress bar
		@Override
		protected void onPreExecute() {
			progress = new ProgressDialog(AutoDetect.this);
			progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progress.setMessage("Discovering DE2s");
			progress.setIndeterminate(false);
			progress.setMax(100);
			progress.setCancelable(false);
			progress.setProgressNumberFormat(null);
			progress.show();
		}

		// build a new ip to check, check if port 50002 is open then add it to
		// the available de2 list else timeout in 500ms
		// and check for the next one
		@Override
		protected List<String> doInBackground(Void... arg0) {
			int port = 50002;
			List<String> result = new ArrayList<String>();
			int count = 0;
			String ip = getLocalIpAddress();
			try {
				for (int i = 0; i < SCAN_NETWORK; i++) {
					// build the next IP address
					String addr = ip.substring(0, ip.lastIndexOf('.') + 1)
							+ (i);
					InetAddress pingAddr = InetAddress.getByName(addr);
					publishProgress(i, count);
					SocketAddress socks = new InetSocketAddress(pingAddr, port);
					Socket s = new Socket();
					try {
						s.connect(socks, 500);
						s.close();
						result.add(pingAddr.getHostAddress());
						count = count + 1;
					} catch (IOException e) {
					} catch (IllegalArgumentException e) {
					}
				}
			} catch (UnknownHostException ex) {
			}
			publishProgress(100, count);
			return result;
		}

		// After every search of the computer on the network update the progress
		// bar
		// if a de2 is found then update the count of de2 found till now
		@Override
		protected void onProgressUpdate(Integer... is) {
			progress.setProgress((int) ((is[0].intValue() / SCAN_NETWORK) * 100));
			if (is[1].intValue() > 0) {
				progress.setMessage("Discovering DE2s...\nFound "
						+ is[1].toString());
			}
		}

		// Display the list of all DE2s found and start the timer threads to get
		// data from midlleman
		@Override
		protected void onPostExecute(List<String> result) {
			MyApplication myApp = (MyApplication) AutoDetect.this
					.getApplication();
			progress.dismiss();
			myApp.availableDE2s.clear();
			myApp.availableDE2s.addAll(result);
			adapter = new ListIpAddresses(AutoDetect.this, myApp.availableDE2s);
			m_listview.setAdapter(adapter);
			TCPReadTimerTask tcp_task = new TCPReadTimerTask();
			Timer tcp_timer = new Timer();
			tcp_timer.schedule(tcp_task, 3000, 500);
		}
	}

	/*
	 * When the user clicks on an IP from the available list, if the app is not
	 * connected to the IP then connect to it else disconnect from that DE2. If
	 * the user is connected to one DE2 and clicks on another one in the list
	 * then disconnect from the current one and connect to the new one selected
	 */
	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int position,
			long arg3) {
		MyApplication myApp = (MyApplication) AutoDetect.this.getApplication();
		if (myApp.sock != null) {
			if (myApp.connectedTo.equals(myApp.availableDE2s.get(position))) {
				closeSocket();
			} else {
				closeSocket();
				new SocketConnect().execute(myApp.availableDE2s.get(position));
				this.adapter = new ListIpAddresses(this, myApp.availableDE2s);
				myApp.connectedTo = myApp.availableDE2s.get(position);
			}
		} else {
			new SocketConnect().execute(myApp.availableDE2s.get(position));
			this.adapter = new ListIpAddresses(this, myApp.availableDE2s);
			myApp.connectedTo = myApp.availableDE2s.get(position);
		}

	}
}
