package com.example.theon.expensetracker;

/**
 * Created by subra on 3/15/2018.
 */


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter<String> {

    private final Activity context;
    private final ArrayList<String> itemname;
    private final Integer[] imgid;

    public CustomListAdapter(Activity context, ArrayList<String> itemname, Integer[] imgid) {
        super(context, R.layout.listlayputexpenses, itemname);
        // TODO Auto-generated constructor stub

        this.context=context;
        this.itemname=itemname;
        this.imgid=imgid;
    }

    public View getView(int position,View view,ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View rowView=inflater.inflate(R.layout.listlayputexpenses, null,true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.Itemname);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.icon);

        txtTitle.setText(itemname.get(position));
        if(itemname.get(position).contains("Shopping")) {
            imageView.setImageResource(imgid[0]);
        } else if(itemname.get(position).contains("Dining")) {
            imageView.setImageResource(imgid[1]);
        } else if(itemname.get(position).contains("Entertainment")) {
            imageView.setImageResource(imgid[2]);
        } else if(itemname.get(position).contains("Gas")) {
            imageView.setImageResource(imgid[3]);
        } else {
            imageView.setImageResource(imgid[0]);
        }
        return rowView;

    };
}