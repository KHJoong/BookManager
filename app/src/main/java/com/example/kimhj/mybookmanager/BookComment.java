package com.example.kimhj.mybookmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.conn.ssl.StrictHostnameVerifier;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kimhj on 2017-05-23.
 */

public class BookComment extends AppCompatActivity {

    TextView comTitle;
    EditText comComment;
    RatingBar comStarpoint;
    Button comSaveBtn;

    int position;
    int reverse_position;

    SharedPreferences bookPref;
    SharedPreferences bookCom;
    SharedPreferences.Editor bookComEditor;
    JSONObject bookJson;
    JSONObject bookComJson;

    String stringComment;
    Float floatStarPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_comment);

        bookComJson = new JSONObject();
        bookPref = getSharedPreferences("BookItem", MODE_PRIVATE);
        bookCom = getSharedPreferences("BookComment", MODE_PRIVATE);
        bookComEditor = bookCom.edit();

        Intent intent = getIntent();
        position = intent.getIntExtra("Position", 0);
        reverse_position = MainActivity.adapter.getCount()-position-1;

        comTitle = (TextView)findViewById(R.id.comTitle);
        comComment = (EditText)findViewById(R.id.comComment);
        comStarpoint = (RatingBar)findViewById(R.id.comStarpoint);
        comSaveBtn = (Button)findViewById(R.id.comSavebtn);
        comSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(BookComment.this);
                builder.setMessage("Comment를 수정할까요?");
                builder.setCancelable(true);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        stringComment = comComment.getText().toString();
                        floatStarPoint = comStarpoint.getRating();
                        try {
                            bookComJson.put("Comment", stringComment);
                            bookComJson.put("Starpoint", String.valueOf(floatStarPoint));
                            bookComEditor.putString(String.valueOf(bookJson.getString("Title"))+String.valueOf(bookJson.getString("Author")), bookComJson.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        bookComEditor.commit();
                        finish();
                        overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();

            }
        });

        try {
            bookJson = new JSONObject(bookPref.getString(String.valueOf(reverse_position), ""));
            if(!bookJson.isNull(String.valueOf("Title"))) {
                comTitle.setText(bookJson.getString("Title"));
            }
            bookComJson = new JSONObject(bookCom.getString(String.valueOf(bookJson.getString("Title"))+String.valueOf(bookJson.getString("Author")),""));
            if(!bookComJson.isNull(String.valueOf("Comment"))){
                comComment.setText(bookComJson.getString("Comment"));
            }
            if(!bookComJson.isNull(String.valueOf("Starpoint"))){
                comStarpoint.setRating(Float.valueOf(bookComJson.getString("Starpoint")));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
    }
}
