package com.example.djhero;

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
    private String[] data;
    private static LayoutInflater inflater=null;
    //public ImageLoader imageLoader; 
    private String title;
    ImageView[] image_view_location = new ImageView[10];
    Bitmap bitmap;
   // public ImageLoader imageLoader; 
    List<View> vi_ = new ArrayList<View>();
    int count;
    

     public LazyAdapter(Activity a, String[] d, String t) {
        activity = a;
        data=d;
        title = t;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        count = 0;
       // imageLoader=new ImageLoader(activity.getApplicationContext());
       // imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.length;
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

        TextView text=(TextView)vi.findViewById(R.id.text);;
        ImageView image=(ImageView)vi.findViewById(R.id.image);
        text.setText(data[position]);
       // imageLoader.DisplayImage(title[1], image);
        
		//songList songlist = new songList("believe:cher:20|In+da+club:50+cent:30");
		//ListIterator<Song> it = songlist.Songs.listIterator();
		
		//while(it.hasNext()){
			//Song nextsong = it.next();
		// Create a new Asynchronous Task and execute it.
		new NewAsyncTask().execute ("http://server.gursimran.net/test.php?track=believe&artist=cher");
		//}
		//image.setImageBitmap(bitmap); Log.i("testing setBiMap", "BITMAP");
	
        //image.setImageResource(R.drawable.ic_launcher);
        //imageLoader.DisplayImage(data[position], image);
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
			//image_view_location[0].setImageBitmap(result);
			bitmap = result;
			ImageView image=(ImageView)vi_.get(count).findViewById(R.id.image);
			image.setImageBitmap(result);
			count++;
	        //View vi = inflater.inflate(R.layout.item, null);
			//ImageView image=(ImageView)vi.findViewById(R.id.image);
			//image.setImageBitmap(result);
		}
	}
}