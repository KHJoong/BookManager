package com.example.kimhj.mybookmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;

/**
 * Created by kimhj on 2017-05-01.
 */

public class BookView extends AppCompatActivity {

    ImageView ivPoster;

    TextView tvTitle;
    TextView tvAuthor;
    TextView tvPublisher;
    TextView tvPubDate;

    Button btn_modify;
    Button btn_comment;

    int position;
    int reverse_position;
    int REQUEST_CODE = 123;

    byte[] posterByteArray;
    SharedPreferences bookPref;
    JSONObject bookJson;

    Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceSate){
        super.onCreate(savedInstanceSate);
        setContentView(R.layout.book_item_view);

        handler = new Handler();

        bookPref = getSharedPreferences("BookItem", MODE_PRIVATE);

        Intent intent = getIntent();
        position = intent.getIntExtra("Position", 0);
        reverse_position = MainActivity.adapter.getCount()-position-1;

        ivPoster = (ImageView)findViewById(R.id.view_poster);
        tvTitle = (TextView)findViewById(R.id.view_title);
        tvAuthor = (TextView)findViewById(R.id.view_author);
        tvPublisher = (TextView)findViewById(R.id.view_publisher);
        tvPubDate = (TextView)findViewById(R.id.view_pub_date);
        btn_modify = (Button)findViewById(R.id.view_modify);
        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), BookModify.class);
                intent.putExtra("Position", position);

                startActivityForResult(intent, REQUEST_CODE);
                overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
            }
        });
        btn_comment = (Button)findViewById(R.id.view_comment);
        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent comment_intent = new Intent(getApplicationContext(), BookComment.class);
                comment_intent.putExtra("Position", position);
                startActivity(comment_intent);
                overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
            }
        });


        try {
            bookJson = new JSONObject(bookPref.getString(String.valueOf(reverse_position), ""));
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        if(!bookJson.getString("Poster").equals("")){
                            String stringArray = bookJson.getString("Poster");
                            String[] split = stringArray.substring(1, stringArray.length()-1).split(", ");
                            posterByteArray = new byte[split.length];
                            for(int j=0; j<split.length; j++){
                                posterByteArray[j] = Byte.parseByte(split[j]);
                            }
                            final Bitmap bitmap = BitmapFactory.decodeByteArray(posterByteArray, 0, posterByteArray.length);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ivPoster.setImageBitmap(bitmap);
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            tvTitle.setText(bookJson.getString("Title"));
            tvAuthor.setText(bookJson.getString("Author"));
            tvPublisher.setText(bookJson.getString("Publisher"));
            tvPubDate.setText(bookJson.getString("PubDate"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_CODE){
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
            }
        }
    }
}
