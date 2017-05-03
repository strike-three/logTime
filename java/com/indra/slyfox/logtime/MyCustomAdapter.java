package com.indra.slyfox.logtime;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by slyfox on 02/04/17.
 */

public class MyCustomAdapter extends BaseAdapter {

    Context context;
    ArrayList al = null;
    int count = 0;
    int limit;
    private LayoutInflater inflater = null;

    public MyCustomAdapter(Context context, ArrayList al, int size)
    {
        this.context = context;
        this.al = al;
        this.limit = size;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount(){
        return limit;
    }

    @Override
    public Object getItem(int position){
        return this.al.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        View rowView = inflater.inflate(R.layout.display_item, parent, false);
        TextView text1 = (TextView) rowView.findViewById(R.id.event_name);
        TextView text2 = (TextView) rowView.findViewById(R.id.event_details);
        String current_str = (String) getItem(position);
        String[] parts = current_str.split(";");
        //System.out.println("Text1  " + parts[0]);
        //System.out.println("Text2  " + parts[1]);
        text1.setText(parts[0]);
        text2.setText(parts[1]);
        return rowView;
    }

}
