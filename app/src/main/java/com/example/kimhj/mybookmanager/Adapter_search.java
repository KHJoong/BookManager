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
import android.widget.ImageView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by kimhj on 2017-05-16.
 */

public class Adapter_search extends BaseAdapter {

    Context mContext;

    ArrayList<Book> sItem;

    ViewHolder viewHolder;

    String searchName;


    Adapter_search(Context context){
        super();
        mContext = context;
        sItem = new ArrayList<Book>();
    }

    @Override
    public int getCount() {
        return sItem.size();
    }

    @Override
    public Book getItem(int position) {
        return sItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = ((LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.book_item, null);
            viewHolder = new ViewHolder();

            viewHolder.poster = (ImageView)view.findViewById(R.id.item_poster);
            viewHolder.title = (TextView)view.findViewById(R.id.item_title);
            viewHolder.author = (TextView)view.findViewById(R.id.item_author);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder)view.getTag();
        }

        if(sItem.get(position).getPoster()!=null){
            byte[] posterByteArray = sItem.get(position).getPoster();
            Bitmap bitmap = BitmapFactory.decodeByteArray(posterByteArray, 0, posterByteArray.length);
            int height = bitmap.getHeight();
            int width = bitmap.getWidth();
            Bitmap resized = null;
            while(height>72){
                resized = Bitmap.createScaledBitmap(bitmap, (width*72)/height, 72, true);
                height = resized.getHeight();
                width = resized.getWidth();
            }
            viewHolder.poster.setImageBitmap(resized);
        } else {
            Bitmap bitmap;
            if(Build.VERSION.SDK_INT>=21) {
                bitmap = ((BitmapDrawable) mContext.getDrawable(R.drawable.emptybook)).getBitmap();
            } else {
                bitmap = ((BitmapDrawable) mContext.getResources().getDrawable(R.drawable.emptybook)).getBitmap();
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
                viewHolder.poster.setImageDrawable(mContext.getDrawable(R.drawable.emptybook));
            } else {
                viewHolder.poster.setImageDrawable(mContext.getResources().getDrawable(R.drawable.emptybook));
            }

        }

        viewHolder.title.setText(sItem.get(position).getTitle());
        viewHolder.author.setText(sItem.get(position).getAuthor());

        return view;
    }


    // BookSearch.class 텍스트에디터에서 검색 버틀 클릭 시 검색할 대상 찾는 메소트
    public void filter(String searchType, String searchText){
        searchName = searchText.toLowerCase(Locale.getDefault());
        sItem.clear();

        if(searchText.length() == 0){
            for(int i=0; i<MainActivity.adapter.mItem.size(); i++) {
                sItem.add(i, MainActivity.adapter.mItem.get(i));
            }
        }
        else{
            int count = MainActivity.adapter.mItem.size();
            for(int i=0; i<count; i++) {
                if (searchType.equals("제목") && MainActivity.adapter.mItem.get(i).getTitle().toLowerCase(Locale.getDefault()).contains(searchName)) {
                    sItem.add(0, MainActivity.adapter.mItem.get(i));
                } else if (searchType.equals("작가") && MainActivity.adapter.mItem.get(i).getAuthor().toLowerCase(Locale.getDefault()).contains(searchName)) {
                    sItem.add(0, MainActivity.adapter.mItem.get(i));
                } else if (searchType.equals("출판사") && MainActivity.adapter.mItem.get(i).getPublisher().toLowerCase(Locale.getDefault()).contains(searchName)) {
                    sItem.add(0, MainActivity.adapter.mItem.get(i));
                }
            }
        }
        notifyDataSetChanged();
    }

    // BookSearch.class 텍스트에디터에서 TextWatcher 이용하여 EditText 변화에 따라 검색할 대상 찾는 메소트
    public void watcherFilter(String searchType, String text){
        text = text.toLowerCase(Locale.getDefault());
        sItem.clear();

        if(text.length()==0){
            for(int i = 0; i<MainActivity.adapter.getCount(); i++){
                sItem.add(0, MainActivity.adapter.mItem.get(i));
            }
        } else {
            for(int j=0; j<MainActivity.adapter.getCount(); j++){
                if (searchType.equals("제목") && MainActivity.adapter.mItem.get(j).getTitle().toLowerCase(Locale.getDefault()).contains(text)) {
                    sItem.add(0, MainActivity.adapter.mItem.get(j));
                } else if (searchType.equals("작가") && MainActivity.adapter.mItem.get(j).getAuthor().toLowerCase(Locale.getDefault()).contains(text)) {
                    sItem.add(0, MainActivity.adapter.mItem.get(j));
                } else if (searchType.equals("출판사") && MainActivity.adapter.mItem.get(j).getPublisher().toLowerCase(Locale.getDefault()).contains(text)) {
                    sItem.add(0, MainActivity.adapter.mItem.get(j));
                }
            }
        }
        notifyDataSetChanged();
    }



}
