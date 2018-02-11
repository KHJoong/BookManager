package com.example.kimhj.mybookmanager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

/**
 * Created by kimhj on 2017-05-13.
 */

public class MiniGameMain extends AppCompatActivity {



    long FINISH_INTERVAL_TIME = 1500;
    long backPressedTime = 0;

    GameLoad gameLoad;

    Button in_game_btn;
    ProgressBar progressBar;
    Button confirm_point_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_minigame);

        gameLoad = new GameLoad();

        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);


        in_game_btn = (Button)findViewById(R.id.in_game);
        in_game_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                in_game_btn.setVisibility(View.GONE);
                confirm_point_btn.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                gameLoad = new GameLoad();
                gameLoad.execute(100);
            }
        });

        confirm_point_btn = (Button)findViewById(R.id.comfirm_point);
        confirm_point_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent game_point_intent = new Intent(getApplicationContext(), MiniGamePoint.class);
                startActivity(game_point_intent);
                overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        progressBar.setVisibility(View.GONE);
        in_game_btn.setVisibility(View.VISIBLE);
        confirm_point_btn.setVisibility(View.VISIBLE);
    }



    @Override
    public void onBackPressed() {
        long current_time = System.currentTimeMillis();
        long time_interval = current_time - backPressedTime;

        System.out.println(time_interval);

        if(0 <= time_interval && time_interval <= FINISH_INTERVAL_TIME) {
            super.onBackPressed();
            overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
        }
        else {
            if(gameLoad.getStatus()== AsyncTask.Status.RUNNING || gameLoad.getStatus()==AsyncTask.Status.PENDING) {
                gameLoad.cancel(true);
            }
            if(backPressedTime<3000) {
                backPressedTime = time_interval;
            }
            else {
                backPressedTime =0;
            }
            progressBar.setVisibility(View.GONE);
            in_game_btn.setVisibility(View.VISIBLE);
            confirm_point_btn.setVisibility(View.VISIBLE);
        }
    }

    // Game Loading Progress Bar
    class GameLoad extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            final int loadingTime = params[0];
            int i = 0;
            while(!isCancelled()){
                i++;
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                publishProgress(i);
                if(i==loadingTime){
                    break;
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer ... progress) {
            super.onProgressUpdate(progress[0]);
            progressBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            Intent intent = new Intent(getApplicationContext(), MiniGameInPlay.class);
            startActivity(intent);
            overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
        }
    }



}
