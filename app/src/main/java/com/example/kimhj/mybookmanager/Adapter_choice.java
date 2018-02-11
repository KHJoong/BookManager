package com.example.kimhj.mybookmanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kimhj on 2017-05-04.
 */

public class Adapter_choice extends BaseAdapter {

    Context cmContext;

    ArrayList<Book> cmItem;

    ViewHolder holder;

    Adapter_choice(Context context, ArrayList<Book> Item){
        cmContext = context;
        cmItem = Item;
    }

    @Override
    public int getCount() {
        return cmItem.size();
    }

    @Override
    public Object getItem(int position) {
        return cmItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = ((LayoutInflater)cmContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.book_item, null);
            holder = new ViewHolder();

            holder.poster = (ImageView)view.findViewById(R.id.item_poster);
            holder.title = (TextView)view.findViewById(R.id.item_title);
            holder.author = (TextView)view.findViewById(R.id.item_author);
            holder.checkBox = (CheckBox) view.findViewById(R.id.item_cb);
            holder.checkBox.setVisibility(View.VISIBLE);
            view.setTag(holder);
        } else {
            holder = (ViewHolder)view.getTag();
        }


        if(cmItem.get(position).getPoster()!=null){
            byte[] posterByteArray = cmItem.get(position).getPoster();
            Bitmap bitmap = BitmapFactory.decodeByteArray(posterByteArray, 0, posterByteArray.length);
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            Bitmap resized = null;
            while(height>72){
                resized = Bitmap.createScaledBitmap(bitmap, (width*72)/height, 72, true);
                height = resized.getHeight();
                width = resized.getWidth();
            }
            holder.poster.setImageBitmap(resized);
        } else {
            Bitmap bitmap;
            if(Build.VERSION.SDK_INT>=21) {
                bitmap = ((BitmapDrawable) cmContext.getDrawable(R.drawable.emptybook)).getBitmap();
            } else {
                bitmap = ((BitmapDrawable) cmContext.getResources().getDrawable(R.drawable.emptybook)).getBitmap();
            }
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            Bitmap resized ;
            while(height>72){
                resized = Bitmap.createScaledBitmap(bitmap, (width*72)/height, 72, true);
                height = resized.getHeight();
                width = resized.getWidth();
            }
            if(Build.VERSION.SDK_INT>=21) {
                holder.poster.setImageDrawable(cmContext.getDrawable(R.drawable.emptybook));
            } else {
                holder.poster.setImageDrawable(cmContext.getResources().getDrawable(R.drawable.emptybook));
            }

        }


        holder.title.setText(cmItem.get(position).getTitle());
        holder.author.setText(cmItem.get(position).getAuthor());
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    cmItem.get(position).setCheckedItem(true);
                } else {
                    cmItem.get(position).setCheckedItem(false);
                }
            }
        });
        holder.checkBox.setChecked(cmItem.get(position).getCheckedItem());

        return view;
    }
}

