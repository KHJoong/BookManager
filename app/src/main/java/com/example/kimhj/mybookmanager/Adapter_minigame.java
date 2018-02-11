package com.example.kimhj.mybookmanager;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kimhj on 2017-05-16.
 */

public class Adapter_minigame extends BaseAdapter {

    Context mContext;
    ArrayList<Point> pItem;

    TextView order;
    TextView point;

    Adapter_minigame(Context context){
        mContext = context;
        pItem = new ArrayList<Point>();
    }

    @Override
    public int getCount() {
        return pItem.size();
    }

    @Override
    public Point getItem(int position) {
        return pItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.card_item, null);

            order = (TextView)view.findViewById(R.id.confirm_point_order);
            point = (TextView)view.findViewById(R.id.confirm_point);
        }
        int intOrder = position + 1;
        order.setText(String.valueOf(intOrder));
        point.setText(String.valueOf(pItem.get(position).getPoint()));

        return view;
    }

    public void addItem(int order, Point item){
        pItem.add(order, item);
    }
}
