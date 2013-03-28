package com.group15.djhero;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ListIpAddresses extends BaseAdapter {
    
    private Activity activity;
    List<String> data = new ArrayList<String>();
    private static LayoutInflater inflater=null;
    List<View> vi_ = new ArrayList<View>();
    int count;
    

     public ListIpAddresses(Activity a, List<String> d) {
        activity = a;
        data.addAll(d);
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        count = 0;
    }

    
	public int getCount() {
    	if(data == null )
    		return 0;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null){
            vi = inflater.inflate(R.layout.ipitem, null);
        }
        vi_.add(position, vi);
        TextView text=(TextView)vi.findViewById(R.id.iptext);
        text.setText(data.get(position));
        return vi;
    }
}