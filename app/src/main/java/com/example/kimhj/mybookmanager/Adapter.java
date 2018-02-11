package com.example.kimhj.mybookmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by kimhj on 2017-05-01.
 */

public class Adapter extends BaseAdapter {

    Context mContext;

    ArrayList<Book> mItem;

    ViewHolder viewHolder;

    // 선언할 때 선언한 곳의 Context를 받아오는 것(그 Context에 리스트뷰를 뿌릴 것이니까)
    Adapter(Context context){
        super();
        mContext = context;
        mItem = new ArrayList<Book>();
    }

    // 어레이리스트의 크기 출력
    @Override
    public int getCount() {
        return mItem.size();
    }

    // 어레이리스트의 각 포지션을 받아 그곳의 아이템을 출력
    @Override
    public Book getItem(int position) {
        return mItem.get(position);
    }

    // 포지션을 받아 포지션을 출력....(?)
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 실질적으로 데이터를 리스트뷰에 뿌려주는 부분
    // ViewHolder는 textview, imageview, button같은 레이아웃의 아이디를 찾아 반복해서 생성하는 것을 막고, 저장해두는 역할
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


        if(mItem.get(position).getPoster()!=null){
            byte[] posterByteArray = mItem.get(position).getPoster();
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

        viewHolder.title.setText(mItem.get(position).getTitle());
        viewHolder.author.setText(mItem.get(position).getAuthor());

        return view;
    }

    // 새로운 아이템을 추가해주는 부분
    public void addItem(Book item){
        mItem.add(0, item);
    }

    // 여기서부터는 필수 x
    // 어떤 포지션의 아이템을 어떤 아이템으로 바꾸겟다는 부분
    public void setmItem(int position, Book item){
        mItem.set(position, item);
    }

    // Adapter_chocie에서 사용한 mitem을 변경 후 다시 mitem에 갱신해주는 부분
    public void renewalItem(ArrayList<Book> item){
        mItem = item;
    }


}