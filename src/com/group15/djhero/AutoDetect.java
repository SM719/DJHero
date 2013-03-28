package com.group15.djhero;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

public class AutoDetect extends Activity implements OnItemClickListener{

	Button button;
	private ListView m_listview;	
	List<String> currentIPAddress = new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		// This call will result in better error messages if you
		// try to do things in the wrong thread.
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auto_detect);
		m_listview = (ListView) findViewById(R.id.ip_list_view);
		m_listview.setOnItemClickListener(this);	
		new FindDE2sOnNetwork().execute();
		
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.connect_to_de2, menu);
		return true;
	}

	// Called when the user closes a socket
	
	public void closeSocket() {
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

	public String getLocalIpAddress() {
		try {
			String Ip = null;   
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) 
	               {
	                    NetworkInterface intf = en.nextElement();       
	                    
	                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();enumIpAddr.hasMoreElements();) 
	                      {
	                        InetAddress inetAddress = enumIpAddr.nextElement();
	                        if (!inetAddress.isLoopbackAddress())
	                         { 
	                               Ip= inetAddress.getHostAddress().toString();
	                         }   
	                       }
	                  }
	           		return Ip;
	            }
	     catch (SocketException obj) 
	     { 
	       Log.e("Error occurred during IP fetching: ", obj.toString());
	      }
		return null;
	}
	
	
    // This is the Socket Connect asynchronous thread.  Opening a socket
	// has to be done in an Asynchronous thread in Android.  Be sure you
	// have done the Asynchronous Tread tutorial before trying to understand
	// this code.
	
	public class SocketConnect extends AsyncTask<String, Void, Socket> {

		// The main parcel of work for this thread.  Opens a socket
		// to connect to the specified IP.
		
		protected Socket doInBackground(String...url) {
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
		// the socket in this app's persistent storage
		
		protected void onPostExecute(Socket s) {
			MyApplication myApp = (MyApplication) AutoDetect.this
					.getApplication();
			myApp.sock = s;
		}
	}

	// This is a timer Task.  Be sure to work through the tutorials
	// on Timer Tasks before trying to understand this code.
	
	public class TCPReadTimerTask extends TimerTask {
		public void run() {
			MyApplication app = (MyApplication) AutoDetect.this.getApplication();
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
		
						MyApplication myApp = (MyApplication) AutoDetect.this.getApplication();
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
	
	class FindDE2sOnNetwork extends AsyncTask< Void, Integer, List<String>>{

		ProgressDialog progress;
		@Override
		protected void onPreExecute(){
			progress = new ProgressDialog(AutoDetect.this);
			progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progress.setMessage("Discovering DE2s");
			progress.setIndeterminate(false);
			progress.setMax(100);
			progress.setProgressNumberFormat(null);
			progress.show();
		}
		@Override
		protected List<String> doInBackground(Void... arg0) {
			int port = 50002;
			List<String> result = new ArrayList<String>();
			int count = 0;
			try {
				String ip = getLocalIpAddress();
		    NetworkInterface iFace = NetworkInterface
		            .getByInetAddress(InetAddress.getByName(ip));

		    for (int i = 0; i <= 255; i++) {
		        // build the next IP address
		        String addr = ip;
		        addr = addr.substring(0, addr.lastIndexOf('.') + 1) + i;
		        InetAddress pingAddr = InetAddress.getByName(addr);
		        // 50ms Timeout for the "ping"
		        if (pingAddr.isReachable(iFace, 200, 50)) {
		            Log.i("PING", pingAddr.getHostAddress());
		            result.add(pingAddr.getHostAddress());
		            count = count+1;
		            System.out.println("--------------Testing port " + port);
		            Socket s = null;
		            try {
		                s = new Socket(pingAddr, port);
		                s.close();
		            } catch (IOException e) {
		            	
		            } finally {
		                if( s != null){
		                    try {
		                        s.close();
		                    } catch (IOException e) {
		                        
		                    }
		                }
		            }
		        }
		        publishProgress(i,count);
		    }
		} catch (UnknownHostException ex) {
		} catch (IOException ex) {
		}
			
			return result;
		}
		
		@Override
		protected void onProgressUpdate(Integer...is){
			progress.setProgress((int) ((is[0].intValue()/255.0)*100));
			if (is[1].intValue() > 0){
				progress.setMessage("Discovering DE2s...\nFound " + is[1].toString() );
			}
		}
		@Override
		protected void onPostExecute(List<String> result){
			progress.dismiss();
			ListIpAddresses adapter = new ListIpAddresses(AutoDetect.this, result);
			m_listview.setAdapter(adapter);	
			currentIPAddress.addAll(result);
			TCPReadTimerTask tcp_task = new TCPReadTimerTask();
			Timer tcp_timer = new Timer();
			tcp_timer.schedule(tcp_task, 3000, 500);
		}	
	}

	@Override
	public void onItemClick(AdapterView<?> adapter, View arg1, int position,
			long arg3) {
		MyApplication myApp = (MyApplication) AutoDetect.this.getApplication();
		if(myApp.sock != null){
			closeSocket();
		}
		else{
			new SocketConnect().execute(currentIPAddress.get(position));
			
		}
		
	}

}
