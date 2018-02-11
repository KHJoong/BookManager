package com.example.kimhj.mybookmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

/**
 * Created by kimhj on 2017-05-21.
 */

public class SplashScreen extends AppCompatActivity {

    ImageView imageView;
    AnimationDrawable splashAnim;

    Thread splashThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);


        imageView = (ImageView)findViewById(R.id.splash_image);
        imageView.setBackgroundResource(R.drawable.splash_anim);
        splashAnim = (AnimationDrawable)imageView.getBackground();

        splashAnim.start();

        splashThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Random random = new Random();
                    int sleep = random.nextInt(1000)+2000;
                    Thread.sleep(sleep);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
            }
        });
        splashThread.start();

    }

}
