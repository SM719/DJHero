package com.group15.djhero;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {
    
    private Activity activity;
    private songList data;
    private static LayoutInflater inflater=null;
    List<View> vi_ = new ArrayList<View>();
    int count;
    

     public LazyAdapter(Activity a, songList d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        count = 0;
    }

    public int getCount() {
    	if(data.Songs == null )
    		return 0;
        return data.Songs.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.item, null);
        
        vi_.add(position, vi);

        TextView text=(TextView)vi.findViewById(R.id.text);
        ImageView image=(ImageView)vi.findViewById(R.id.image);
    
        Song song =data.Songs.get(position);
        
        
        text.setText(song.Title);
        
     
		new NewAsyncTask().execute ("http://server.gursimran.net/test2.php?track="+song.Title);
		
        return vi;
    }
    
	// This is the asynchronous task. It is extended from AsyncTask
	class NewAsyncTask extends AsyncTask<String, Void, Bitmap> {
		// This is the "guts" of the asynchronus task. The code
		// in doInBackground will be executed in a separate thread
		@Override
		protected Bitmap doInBackground(String... url_array) {
			URL url;
			Log.i("MainActivity", "Inside the asynchronous task");
			try {
				url = new URL(url_array[0]);
				HttpURLConnection connection = (HttpURLConnection) url
						.openConnection();
				connection.setDoInput(true);
				connection.connect();
				Log.i("MainActivity", "Successfully opened the web page");
				InputStream input = connection.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(input);
				input.close();
				return bitmap;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}

		// This routine is called at the end of the task. This
		// routine is run as part of the main thread, so it can
		// update the GUI. The input parameter is automatically
		// set by the output parameter of doInBackground()
		@Override
		protected void onPostExecute(Bitmap result) {
			MyApplication myApp = (MyApplication) activity.getApplication();
			myApp.images.add(result);
			ImageView image=(ImageView)vi_.get(count).findViewById(R.id.image);
			image.setImageBitmap(result);
			count++;
		}
	}
}