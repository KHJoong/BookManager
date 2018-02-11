package com.example.kimhj.mybookmanager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.Random;

/**
 * Created by kimhj on 2017-05-13.
 */

public class MiniGameInPlay extends AppCompatActivity {



    Handler handler;

    static int game_point;
    static int game_life;
    static byte game_level;

    boolean isPlay;

    TextView point;
    TextView level;

    Button start_game;

    ImageView life1;
    ImageView life2;
    ImageView life3;
    ImageView life4;
    ImageView life5;
    TextView tvlife;


    // 카운트다운
    TextView countdown3;
    TextView countdown2;
    TextView countdown1;
    TextView countdownGo;
    CountTask countTask;

    // 피버타임
    int count_conti_hit;
    boolean fever_time;
    Thread threadFever;
    ImageView play_viewGroup;

    Button mole00;
    int i_mole00;
    Button mole01;
    int i_mole01;
    Button mole02;
    int i_mole02;
    Button mole03;
    int i_mole03;
    Button mole10;
    int i_mole10;
    Button mole11;
    int i_mole11;
    Button mole12;
    int i_mole12;
    Button mole13;
    int i_mole13;
    Button mole20;
    int i_mole20;
    Button mole21;
    int i_mole21;
    Button mole22;
    int i_mole22;
    Button mole23;
    int i_mole23;
    Button mole30;
    int i_mole30;
    Button mole31;
    int i_mole31;
    Button mole32;
    int i_mole32;
    Button mole33;
    int i_mole33;

    Thread thread00;
    Thread thread01;
    Thread thread02;
    Thread thread03;
    Thread thread10;
    Thread thread11;
    Thread thread12;
    Thread thread13;
    Thread thread20;
    Thread thread21;
    Thread thread22;
    Thread thread23;
    Thread thread30;
    Thread thread31;
    Thread thread32;
    Thread thread33;

    SharedPreferences pointShared;
    SharedPreferences.Editor pointSharedEditor;

    Animation correctClick;
    Animation wrongClick;
    Animation animFever;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_minigame_inplay);
        play_viewGroup = (ImageView) findViewById(R.id.in_play_viewgroup);

        handler = new Handler();

        isPlay = false;
        count_conti_hit = 0;
        fever_time = false;
        threadFever = new Thread();

        correctClick = AnimationUtils.loadAnimation(this, R.anim.anim_correct_click);
        wrongClick = AnimationUtils.loadAnimation(this, R.anim.anim_wrong_click);
        animFever = AnimationUtils.loadAnimation(this, R.anim.anim_fever);

        i_mole00 = 0;
        i_mole01 = 0;
        i_mole02 = 0;
        i_mole03 = 0;
        i_mole10 = 0;
        i_mole11 = 0;
        i_mole12 = 0;
        i_mole13 = 0;
        i_mole20 = 0;
        i_mole21 = 0;
        i_mole22 = 0;
        i_mole23 = 0;
        i_mole30 = 0;
        i_mole31 = 0;
        i_mole32 = 0;
        i_mole33 = 0;

        game_point = 0;
        game_life = 5;
        game_level = 1;

        point = (TextView) findViewById(R.id.game_point);
        level = (TextView) findViewById(R.id.game_level);

        start_game = (Button) findViewById(R.id.game_start);

        life1 = (ImageView) findViewById(R.id.life1);
        life2 = (ImageView) findViewById(R.id.life2);
        life3 = (ImageView) findViewById(R.id.life3);
        life4 = (ImageView) findViewById(R.id.life4);
        life5 = (ImageView) findViewById(R.id.life5);
        tvlife = (TextView) findViewById(R.id.tvlife);

        mole00 = (Button) findViewById(R.id.mole_0_0);
        mole01 = (Button) findViewById(R.id.mole_0_1);
        mole02 = (Button) findViewById(R.id.mole_0_2);
        mole03 = (Button) findViewById(R.id.mole_0_3);
        mole10 = (Button) findViewById(R.id.mole_1_0);
        mole11 = (Button) findViewById(R.id.mole_1_1);
        mole12 = (Button) findViewById(R.id.mole_1_2);
        mole13 = (Button) findViewById(R.id.mole_1_3);
        mole20 = (Button) findViewById(R.id.mole_2_0);
        mole21 = (Button) findViewById(R.id.mole_2_1);
        mole22 = (Button) findViewById(R.id.mole_2_2);
        mole23 = (Button) findViewById(R.id.mole_2_3);
        mole30 = (Button) findViewById(R.id.mole_3_0);
        mole31 = (Button) findViewById(R.id.mole_3_1);
        mole32 = (Button) findViewById(R.id.mole_3_2);
        mole33 = (Button) findViewById(R.id.mole_3_3);


        // 카운트다운
        countTask = new CountTask();
        countdown3 = (TextView)findViewById(R.id.countdown3);
        countdown3.setVisibility(View.GONE);
        countdown2 = (TextView)findViewById(R.id.countdown2);
        countdown2.setVisibility(View.GONE);
        countdown1 = (TextView)findViewById(R.id.countdown1);
        countdown1.setVisibility(View.GONE);
        countdownGo = (TextView)findViewById(R.id.countdowngo);
        countdownGo.setVisibility(View.GONE);


        thread00 = new Thread();
        thread01 = new Thread();
        thread02 = new Thread();
        thread03 = new Thread();
        thread10 = new Thread();
        thread11 = new Thread();
        thread12 = new Thread();
        thread13 = new Thread();
        thread20 = new Thread();
        thread21 = new Thread();
        thread22 = new Thread();
        thread23 = new Thread();
        thread30 = new Thread();
        thread31 = new Thread();
        thread32 = new Thread();
        thread33 = new Thread();

        // 게임 시작 버튼 클릭 이벤트트
        start_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countTask.getStatus() == AsyncTask.Status.RUNNING || countTask.getStatus() == AsyncTask.Status.PENDING){
                    countTask.cancel(true);
                }
                if(isPlay==true){
                    initThread();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        game_life = 5;
                        game_level = 1;
                        game_point = 0;

//                        isPlay = true;

                        point.setText(String.valueOf(game_point));
                        level.setText(String.valueOf(game_level));
                        tvlife.setText(String.valueOf(game_life));

                        life1.setBackgroundColor(getResources().getColor(R.color.life));
                        life2.setBackgroundColor(getResources().getColor(R.color.life));
                        life3.setBackgroundColor(getResources().getColor(R.color.life));
                        life4.setBackgroundColor(getResources().getColor(R.color.life));
                        life5.setBackgroundColor(getResources().getColor(R.color.life));

                        if(Build.VERSION.SDK_INT>=21) {
                            mole00.setBackground(getDrawable(R.drawable.down_card));
                            mole01.setBackground(getDrawable(R.drawable.down_card));
                            mole02.setBackground(getDrawable(R.drawable.down_card));
                            mole03.setBackground(getDrawable(R.drawable.down_card));
                            mole10.setBackground(getDrawable(R.drawable.down_card));
                            mole11.setBackground(getDrawable(R.drawable.down_card));
                            mole12.setBackground(getDrawable(R.drawable.down_card));
                            mole13.setBackground(getDrawable(R.drawable.down_card));
                            mole20.setBackground(getDrawable(R.drawable.down_card));
                            mole21.setBackground(getDrawable(R.drawable.down_card));
                            mole22.setBackground(getDrawable(R.drawable.down_card));
                            mole23.setBackground(getDrawable(R.drawable.down_card));
                            mole30.setBackground(getDrawable(R.drawable.down_card));
                            mole31.setBackground(getDrawable(R.drawable.down_card));
                            mole32.setBackground(getDrawable(R.drawable.down_card));
                            mole33.setBackground(getDrawable(R.drawable.down_card));
                        } else {
                            mole00.setBackground(getResources().getDrawable(R.drawable.down_card));
                            mole01.setBackground(getResources().getDrawable(R.drawable.down_card));
                            mole02.setBackground(getResources().getDrawable(R.drawable.down_card));
                            mole03.setBackground(getResources().getDrawable(R.drawable.down_card));
                            mole10.setBackground(getResources().getDrawable(R.drawable.down_card));
                            mole11.setBackground(getResources().getDrawable(R.drawable.down_card));
                            mole12.setBackground(getResources().getDrawable(R.drawable.down_card));
                            mole13.setBackground(getResources().getDrawable(R.drawable.down_card));
                            mole20.setBackground(getResources().getDrawable(R.drawable.down_card));
                            mole21.setBackground(getResources().getDrawable(R.drawable.down_card));
                            mole22.setBackground(getResources().getDrawable(R.drawable.down_card));
                            mole23.setBackground(getResources().getDrawable(R.drawable.down_card));
                            mole30.setBackground(getResources().getDrawable(R.drawable.down_card));
                            mole31.setBackground(getResources().getDrawable(R.drawable.down_card));
                            mole32.setBackground(getResources().getDrawable(R.drawable.down_card));
                            mole33.setBackground(getResources().getDrawable(R.drawable.down_card));
                        }
//                        mole00.setBackgroundColor(getResources().getColor(R.color.downMole));
//                        mole01.setBackgroundColor(getResources().getColor(R.color.downMole));
//                        mole02.setBackgroundColor(getResources().getColor(R.color.downMole));
//                        mole03.setBackgroundColor(getResources().getColor(R.color.downMole));
//                        mole10.setBackgroundColor(getResources().getColor(R.color.downMole));
//                        mole11.setBackgroundColor(getResources().getColor(R.color.downMole));
//                        mole12.setBackgroundColor(getResources().getColor(R.color.downMole));
//                        mole13.setBackgroundColor(getResources().getColor(R.color.downMole));
//                        mole20.setBackgroundColor(getResources().getColor(R.color.downMole));
//                        mole21.setBackgroundColor(getResources().getColor(R.color.downMole));
//                        mole22.setBackgroundColor(getResources().getColor(R.color.downMole));
//                        mole23.setBackgroundColor(getResources().getColor(R.color.downMole));
//                        mole30.setBackgroundColor(getResources().getColor(R.color.downMole));
//                        mole31.setBackgroundColor(getResources().getColor(R.color.downMole));
//                        mole32.setBackgroundColor(getResources().getColor(R.color.downMole));
//                        mole33.setBackgroundColor(getResources().getColor(R.color.downMole));
                    }
                });


                // 00~03
                thread00 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted()) {
                            int c00;
                            Random change = new Random();
                            if(fever_time){
                                c00 = change.nextInt(1);
                            } else {
                                c00 = change.nextInt(6 - (1/2)*game_level);
                            }
                            System.out.println("==============================================00 : "+c00);
                            if (c00 == 0) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole00.setBackground(getDrawable(R.drawable.up_card));
                                        } else {
                                            mole00.setBackground(getResources().getDrawable(R.drawable.up_card));
                                        }
//                                        mole00.setBackgroundColor(getResources().getColor(R.color.upMole));
                                        i_mole00 = 1;
                                    }
                                });
                            }
                            activeMole();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(Build.VERSION.SDK_INT>=21) {
                                        mole00.setBackground(getDrawable(R.drawable.down_card));
                                    } else {
                                        mole00.setBackground(getResources().getDrawable(R.drawable.down_card));
                                    }
//                                    mole00.setBackgroundColor(getResources().getColor(R.color.downMole));
                                    i_mole00 = 0;
                                }
                            });
                            restMole();
                        }
                    }
                });
                thread01 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted()) {
                            int c01;
                            Random change = new Random();
                            if(fever_time){
                                c01 = change.nextInt(1);
                            } else {
                                c01 = change.nextInt(6 - (1/2)*game_level);
                            }
                            System.out.println("==============================================01 : "+c01);
                            if (c01 == 0) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole01.setBackground(getDrawable(R.drawable.up_card));
                                        } else {
                                            mole01.setBackground(getResources().getDrawable(R.drawable.up_card));
                                        }
//                                        mole01.setBackgroundColor(getResources().getColor(R.color.upMole));
                                        i_mole01 = 1;
                                    }
                                });
                            }
                            activeMole();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(Build.VERSION.SDK_INT>=21) {
                                        mole01.setBackground(getDrawable(R.drawable.down_card));
                                    } else {
                                        mole01.setBackground(getResources().getDrawable(R.drawable.down_card));
                                    }
//                                    mole01.setBackgroundColor(getResources().getColor(R.color.downMole));
                                    i_mole01 = 0;
                                }
                            });
                            restMole();
                        }
                    }
                });
                thread02 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted()) {
                            int c02;
                            Random change = new Random();
                            if(fever_time){
                                c02 = change.nextInt(1);
                            } else {
                                c02 = change.nextInt(6 - (1/2)*game_level);
                            }
                            System.out.println("==============================================02 : "+c02);
                            if (c02 == 0) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole02.setBackground(getDrawable(R.drawable.up_card));
                                        } else {
                                            mole02.setBackground(getResources().getDrawable(R.drawable.up_card));
                                        }
//                                        mole02.setBackgroundColor(getResources().getColor(R.color.upMole));
                                        i_mole02 = 1;
                                    }
                                });
                            }
                            activeMole();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(Build.VERSION.SDK_INT>=21) {
                                        mole02.setBackground(getDrawable(R.drawable.down_card));
                                    } else {
                                        mole02.setBackground(getResources().getDrawable(R.drawable.down_card));
                                    }
//                                    mole02.setBackgroundColor(getResources().getColor(R.color.downMole));
                                    i_mole02 = 0;
                                }
                            });
                            restMole();
                        }
                    }
                });
                thread03 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted()) {
                            int c03;
                            Random change = new Random();
                            if(fever_time){
                                c03 = change.nextInt(1);
                            } else {
                                c03 = change.nextInt(6 - (1/2)*game_level);
                            }
                            System.out.println("==============================================03 : "+c03);
                            if (c03 == 0) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole03.setBackground(getDrawable(R.drawable.up_card));
                                        } else {
                                            mole03.setBackground(getResources().getDrawable(R.drawable.up_card));
                                        }
//                                        mole03.setBackgroundColor(getResources().getColor(R.color.upMole));
                                        i_mole03 = 1;
                                    }
                                });
                            }
                            activeMole();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(Build.VERSION.SDK_INT>=21) {
                                        mole03.setBackground(getDrawable(R.drawable.down_card));
                                    } else {
                                        mole03.setBackground(getResources().getDrawable(R.drawable.down_card));
                                    }
//                                    mole03.setBackgroundColor(getResources().getColor(R.color.downMole));
                                    i_mole03 = 0;
                                }
                            });
                            restMole();
                        }
                    }
                });

                // 10~13
                thread10 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted()) {
                            int c10;
                            Random change = new Random();
                            if(fever_time){
                                c10 = change.nextInt(1);
                            } else {
                                c10 = change.nextInt(6 - (1/2)*game_level);
                            }
                            System.out.println("===================================================10 : "+c10);
                            if (c10 == 0) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole10.setBackground(getDrawable(R.drawable.up_card));
                                        } else {
                                            mole10.setBackground(getResources().getDrawable(R.drawable.up_card));
                                        }
//                                        mole10.setBackgroundColor(getResources().getColor(R.color.upMole));
                                        i_mole10 = 1;
                                    }
                                });
                            }
                            activeMole();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(Build.VERSION.SDK_INT>=21) {
                                        mole10.setBackground(getDrawable(R.drawable.down_card));
                                    } else {
                                        mole10.setBackground(getResources().getDrawable(R.drawable.down_card));
                                    }
//                                    mole10.setBackgroundColor(getResources().getColor(R.color.downMole));
                                    i_mole10 = 0;
                                }
                            });
                            restMole();
                        }
                    }
                });
                thread11 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted()) {
                            int c11;
                            Random change = new Random();
                            if(fever_time){
                                c11 = change.nextInt(1);
                            } else {
                                c11 = change.nextInt(6 - (1/2)*game_level);
                            }
                            System.out.println("===================================================11 : "+c11);
                            if (c11 == 0) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole11.setBackground(getDrawable(R.drawable.up_card));
                                        } else {
                                            mole11.setBackground(getResources().getDrawable(R.drawable.up_card));
                                        }
//                                        mole11.setBackgroundColor(getResources().getColor(R.color.upMole));
                                        i_mole11 = 1;
                                    }
                                });
                            }
                            activeMole();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(Build.VERSION.SDK_INT>=21) {
                                        mole11.setBackground(getDrawable(R.drawable.down_card));
                                    } else {
                                        mole11.setBackground(getResources().getDrawable(R.drawable.down_card));
                                    }
//                                    mole11.setBackgroundColor(getResources().getColor(R.color.downMole));
                                    i_mole11 = 0;
                                }
                            });
                            restMole();
                        }
                    }
                });
                thread12 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted()) {
                            int c12;
                            Random change = new Random();
                            if(fever_time){
                                c12 = change.nextInt(1);
                            } else {
                                c12 = change.nextInt(6 - (1/2)*game_level);
                            }
                            System.out.println("===================================================12 : "+c12);
                            if (c12 == 0) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole12.setBackground(getDrawable(R.drawable.up_card));
                                        } else {
                                            mole12.setBackground(getResources().getDrawable(R.drawable.up_card));
                                        }
//                                        mole12.setBackgroundColor(getResources().getColor(R.color.upMole));
                                        i_mole12 = 1;
                                    }
                                });
                            }
                            activeMole();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(Build.VERSION.SDK_INT>=21) {
                                        mole12.setBackground(getDrawable(R.drawable.down_card));
                                    } else {
                                        mole12.setBackground(getResources().getDrawable(R.drawable.down_card));
                                    }
//                                    mole12.setBackgroundColor(getResources().getColor(R.color.downMole));
                                    i_mole12 = 0;
                                }
                            });
                            restMole();
                        }
                    }
                });
                thread13 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted()) {
                            int c13;
                            Random change = new Random();
                            if(fever_time){
                                c13 = change.nextInt(1);
                            } else {
                                c13 = change.nextInt(6 - (1/2)*game_level);
                            }
                            System.out.println("===================================================13 : "+c13);
                            if (c13 == 0) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole13.setBackground(getDrawable(R.drawable.up_card));
                                        } else {
                                            mole13.setBackground(getResources().getDrawable(R.drawable.up_card));
                                        }
//                                        mole13.setBackgroundColor(getResources().getColor(R.color.upMole));
                                        i_mole13 = 1;
                                    }
                                });
                            }
                            activeMole();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(Build.VERSION.SDK_INT>=21) {
                                        mole13.setBackground(getDrawable(R.drawable.down_card));
                                    } else {
                                        mole13.setBackground(getResources().getDrawable(R.drawable.down_card));
                                    }
//                                    mole13.setBackgroundColor(getResources().getColor(R.color.downMole));
                                    i_mole13 = 0;
                                }
                            });
                            restMole();
                        }
                    }
                });

                // 20~23
                thread20 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted()) {
                            int c20;
                            Random change = new Random();
                            if(fever_time){
                                c20 = change.nextInt(1);
                            } else {
                                c20 = change.nextInt(6 - (1/2)*game_level);
                            }
                            System.out.println("=============================================================20 : "+c20);
                            if (c20 == 0) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole20.setBackground(getDrawable(R.drawable.up_card));
                                        } else {
                                            mole20.setBackground(getResources().getDrawable(R.drawable.up_card));
                                        }
//                                        mole20.setBackgroundColor(getResources().getColor(R.color.upMole));
                                        i_mole20 =1;
                                    }
                                });
                            }
                            activeMole();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(Build.VERSION.SDK_INT>=21) {
                                        mole20.setBackground(getDrawable(R.drawable.down_card));
                                    } else {
                                        mole20.setBackground(getResources().getDrawable(R.drawable.down_card));
                                    }
//                                    mole20.setBackgroundColor(getResources().getColor(R.color.downMole));
                                    i_mole20 = 0;
                                }
                            });
                            restMole();
                        }
                    }
                });
                thread21 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted()) {
                            int c21;
                            Random change = new Random();
                            if(fever_time){
                                c21 = change.nextInt(1);
                            } else {
                                c21 = change.nextInt(6 - (1/2)*game_level);
                            }
                            System.out.println("=============================================================21 : "+c21);
                            if (c21 == 0) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole21.setBackground(getDrawable(R.drawable.up_card));
                                        } else {
                                            mole21.setBackground(getResources().getDrawable(R.drawable.up_card));
                                        }
//                                        mole21.setBackgroundColor(getResources().getColor(R.color.upMole));
                                        i_mole21 =1;
                                    }
                                });
                            }
                            activeMole();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(Build.VERSION.SDK_INT>=21) {
                                        mole21.setBackground(getDrawable(R.drawable.down_card));
                                    } else {
                                        mole21.setBackground(getResources().getDrawable(R.drawable.down_card));
                                    }
//                                    mole21.setBackgroundColor(getResources().getColor(R.color.downMole));
                                    i_mole21 = 0;
                                }
                            });
                            restMole();
                        }
                    }
                });
                thread22 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted()) {
                            int c22;
                            Random change = new Random();
                            if(fever_time){
                                c22 = change.nextInt(1);
                            } else {
                                c22 = change.nextInt(6 - (1/2)*game_level);
                            }
                            System.out.println("=============================================================22 : "+c22);
                            if (c22 == 0) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole22.setBackground(getDrawable(R.drawable.up_card));
                                        } else {
                                            mole22.setBackground(getResources().getDrawable(R.drawable.up_card));
                                        }
//                                        mole22.setBackgroundColor(getResources().getColor(R.color.upMole));
                                        i_mole22 = 1;
                                    }
                                });
                            }
                            activeMole();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(Build.VERSION.SDK_INT>=21) {
                                        mole22.setBackground(getDrawable(R.drawable.down_card));
                                    } else {
                                        mole22.setBackground(getResources().getDrawable(R.drawable.down_card));
                                    }
//                                    mole22.setBackgroundColor(getResources().getColor(R.color.downMole));
                                    i_mole22 = 0;
                                }
                            });
                            restMole();
                        }
                    }
                });
                thread23 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted()) {
                            int c23;
                            Random change = new Random();
                            if(fever_time){
                                c23 = change.nextInt(1);
                            } else {
                                c23 = change.nextInt(6 - (1/2)*game_level);
                            }
                            System.out.println("=============================================================23 : "+c23);
                            if (c23 == 0) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole23.setBackground(getDrawable(R.drawable.up_card));
                                        } else {
                                            mole23.setBackground(getResources().getDrawable(R.drawable.up_card));
                                        }
//                                        mole23.setBackgroundColor(getResources().getColor(R.color.upMole));
                                        i_mole23 = 1;
                                    }
                                });
                            }
                            activeMole();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(Build.VERSION.SDK_INT>=21) {
                                        mole23.setBackground(getDrawable(R.drawable.down_card));
                                    } else {
                                        mole23.setBackground(getResources().getDrawable(R.drawable.down_card));
                                    }
//                                    mole23.setBackgroundColor(getResources().getColor(R.color.downMole));
                                    i_mole23 = 0;
                                }
                            });
                            restMole();
                        }
                    }
                });

                //30~33
                thread30 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted()) {
                            int c30;
                            Random change = new Random();
                            if(fever_time){
                                c30 = change.nextInt(1);
                            } else {
                                c30 = change.nextInt(6 - (1/2)*game_level);
                            }
                            System.out.println("==================================================================30 : "+c30);
                            if (c30 == 0) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole30.setBackground(getDrawable(R.drawable.up_card));
                                        } else {
                                            mole30.setBackground(getResources().getDrawable(R.drawable.up_card));
                                        }
//                                        mole30.setBackgroundColor(getResources().getColor(R.color.upMole));
                                        i_mole30 = 1;
                                    }
                                });
                            }
                            activeMole();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(Build.VERSION.SDK_INT>=21) {
                                        mole30.setBackground(getDrawable(R.drawable.down_card));
                                    } else {
                                        mole30.setBackground(getResources().getDrawable(R.drawable.down_card));
                                    }
//                                    mole30.setBackgroundColor(getResources().getColor(R.color.downMole));
                                    i_mole30 = 0;
                                }
                            });
                            restMole();
                        }
                    }
                });
                thread31 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted()) {
                            int c31;
                            Random change = new Random();
                            if(fever_time){
                                c31 = change.nextInt(1);
                            } else {
                                c31 = change.nextInt(6 - (1/2)*game_level);
                            }
                            System.out.println("==================================================================31 : "+c31);
                            if (c31 == 0) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole31.setBackground(getDrawable(R.drawable.up_card));
                                        } else {
                                            mole31.setBackground(getResources().getDrawable(R.drawable.up_card));
                                        }
//                                        mole31.setBackgroundColor(getResources().getColor(R.color.upMole));
                                        i_mole31 = 1;
                                    }
                                });
                            }
                            activeMole();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(Build.VERSION.SDK_INT>=21) {
                                        mole31.setBackground(getDrawable(R.drawable.down_card));
                                    } else {
                                        mole31.setBackground(getResources().getDrawable(R.drawable.down_card));
                                    }
//                                    mole31.setBackgroundColor(getResources().getColor(R.color.downMole));
                                    i_mole31 = 0;
                                }
                            });
                            restMole();
                        }
                    }
                });
                thread32 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted()) {
                            int c32;
                            Random change = new Random();
                            if(fever_time){
                                c32 = change.nextInt(1);
                            } else {
                                c32 = change.nextInt(6 - (1/2)*game_level);
                            }
                            System.out.println("==================================================================32 : "+c32);
                            if (c32 == 0) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole32.setBackground(getDrawable(R.drawable.up_card));
                                        } else {
                                            mole32.setBackground(getResources().getDrawable(R.drawable.up_card));
                                        }
//                                        mole32.setBackgroundColor(getResources().getColor(R.color.upMole));
                                        i_mole32 = 1;
                                    }
                                });
                            }
                            activeMole();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(Build.VERSION.SDK_INT>=21) {
                                        mole32.setBackground(getDrawable(R.drawable.down_card));
                                    } else {
                                        mole32.setBackground(getResources().getDrawable(R.drawable.down_card));
                                    }
//                                    mole32.setBackgroundColor(getResources().getColor(R.color.downMole));
                                    i_mole32 = 0;
                                }
                            });
                            restMole();
                        }
                    }
                });
                thread33 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (!Thread.currentThread().isInterrupted()) {
                            int c33;
                            Random change = new Random();
                            if(fever_time){
                                c33 = change.nextInt(1);
                            } else {
                                c33 = change.nextInt(6 - (1/2)*game_level);
                            }
                            System.out.println("==================================================================33 : "+c33);
                            if (c33 == 0) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole33.setBackground(getDrawable(R.drawable.up_card));
                                        } else {
                                            mole33.setBackground(getResources().getDrawable(R.drawable.up_card));
                                        }
//                                        mole33.setBackgroundColor(getResources().getColor(R.color.upMole));
                                        i_mole33 =1;
                                    }
                                });
                            }
                            activeMole();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if(Build.VERSION.SDK_INT>=21) {
                                        mole33.setBackground(getDrawable(R.drawable.down_card));
                                    } else {
                                        mole33.setBackground(getResources().getDrawable(R.drawable.down_card));
                                    }
//                                    mole33.setBackgroundColor(getResources().getColor(R.color.downMole));
                                    i_mole33 = 0;
                                }
                            });
                            restMole();
                        }
                    }
                });


                // Fever 타임 쓰레드
                threadFever = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(!Thread.currentThread().isInterrupted()){
                            Random random = new Random();
                            int m = random.nextInt(20)+15;
                            if(count_conti_hit!=0 && count_conti_hit%m==0){
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Fever Time!!", Toast.LENGTH_SHORT).show();
                                        play_viewGroup.setBackgroundColor(getResources().getColor(R.color.fever));
                                        play_viewGroup.startAnimation(animFever);
                                    }
                                });
                                fever_time = true;
                                try {
                                    int s = random.nextInt(7000)+4000;
                                    Thread.sleep(s);
                                } catch (InterruptedException e) {
                                    Thread.currentThread().interrupt();
                                    e.printStackTrace();
                                }
                                fever_time = false;
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        play_viewGroup.setBackgroundColor(getResources().getColor(R.color.nofever));
                                        play_viewGroup.clearAnimation();
                                    }
                                });
                                count_conti_hit = 0;
                            }
                        }
                    }
                });


                countTask = new CountTask();
                countTask.execute(3);



//                gameOver();
//
//                thread00.start();
//                thread01.start();
//                thread02.start();
//                thread03.start();
//
//                thread10.start();
//                thread11.start();
//                thread12.start();
//                thread13.start();
//
//                thread20.start();
//                thread21.start();
//                thread22.start();
//                thread23.start();
//
//                thread30.start();
//                thread31.start();
//                thread32.start();
//                thread33.start();
//
//                threadFever.start();


            }
        });



        mole00.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i_mole00 == 1 && isPlay){
                    count_conti_hit++;
                    thread00.interrupt();
                    pointMole();
                    levelMole();
                    mole00.startAnimation(correctClick);
                    if(Build.VERSION.SDK_INT>=21) {
                        mole00.setBackground(getDrawable(R.drawable.down_card));
                    } else {
                        mole00.setBackground(getResources().getDrawable(R.drawable.down_card));
                    }
//                    mole00.setBackgroundColor(getResources().getColor(R.color.downMole));
                    if(thread00.isAlive()){
                        thread00.interrupt();
                    }
                    thread00 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!Thread.currentThread().isInterrupted()) {
                                int c00;
                                Random change = new Random();
                                if(fever_time){
                                    c00 = change.nextInt(1);
                                } else {
                                    c00 = change.nextInt(6 - (1/2)*game_level);
                                }
                                System.out.println("==============================================00 : "+c00);
                                if (c00 == 0) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(Build.VERSION.SDK_INT>=21) {
                                                mole00.setBackground(getDrawable(R.drawable.up_card));
                                            } else {
                                                mole00.setBackground(getResources().getDrawable(R.drawable.up_card));
                                            }
//                                            mole00.setBackgroundColor(getResources().getColor(R.color.upMole));
                                            i_mole00 = 1;
                                        }
                                    });
                                }
                                activeMole();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole00.setBackground(getDrawable(R.drawable.down_card));
                                        } else {
                                            mole00.setBackground(getResources().getDrawable(R.drawable.down_card));
                                        }
//                                        mole00.setBackgroundColor(getResources().getColor(R.color.downMole));
                                        i_mole00 = 0;
                                    }
                                });
                                restMole();
                            }
                        }
                    });
                    thread00.start();
                }
                else if (i_mole00 == 0 && isPlay){
                    if (game_life>0){
                        count_conti_hit = 0;
                        threadFever.interrupt();
                        game_life--;
                        lifeMole();
                        mole00.startAnimation(wrongClick);
                        threadFever = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(!Thread.currentThread().isInterrupted()){
                                    Random random = new Random();
                                    int m = random.nextInt(20)+15;
                                    if(count_conti_hit!=0 && count_conti_hit%m==0){
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Fever Time!!", Toast.LENGTH_SHORT).show();
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.fever));
                                                play_viewGroup.startAnimation(animFever);
                                            }
                                        });
                                        fever_time = true;
                                        try {
                                            int s = random.nextInt(7000)+5000;
                                            Thread.sleep(s);
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                            e.printStackTrace();
                                        }
                                        fever_time = false;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.nofever));
                                                play_viewGroup.clearAnimation();
                                            }
                                        });
                                        count_conti_hit = 0;
                                    }
                                }
                            }
                        });
                        threadFever.start();
                    }
                }
                i_mole00 = 0;
            }
        });
        mole01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i_mole01 == 1 && isPlay){
                    count_conti_hit++;
                    thread01.interrupt();
                    pointMole();
                    levelMole();
                    mole01.startAnimation(correctClick);
                    if(Build.VERSION.SDK_INT>=21) {
                        mole01.setBackground(getDrawable(R.drawable.down_card));
                    } else {
                        mole01.setBackground(getResources().getDrawable(R.drawable.down_card));
                    }
//                    mole01.setBackgroundColor(getResources().getColor(R.color.downMole));
                    if(thread01.isAlive()){
                        thread01.interrupt();
                    }
                    thread01 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!Thread.currentThread().isInterrupted()) {
                                int c01;
                                Random change = new Random();
                                if(fever_time){
                                    c01 = change.nextInt(1);
                                } else {
                                    c01 = change.nextInt(6 - (1/2)*game_level);
                                }
                                System.out.println("==============================================01 : "+c01);
                                if (c01 == 0) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(Build.VERSION.SDK_INT>=21) {
                                                mole01.setBackground(getDrawable(R.drawable.up_card));
                                            } else {
                                                mole01.setBackground(getResources().getDrawable(R.drawable.up_card));
                                            }
//                                            mole01.setBackgroundColor(getResources().getColor(R.color.upMole));
                                            i_mole01 = 1;
                                        }
                                    });
                                }
                                activeMole();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole01.setBackground(getDrawable(R.drawable.down_card));
                                        } else {
                                            mole01.setBackground(getResources().getDrawable(R.drawable.down_card));
                                        }
//                                        mole01.setBackgroundColor(getResources().getColor(R.color.downMole));
                                        i_mole01 = 0;
                                    }
                                });
                                restMole();
                            }
                        }
                    });
                    thread01.start();
                }
                else if (i_mole01 == 0 && isPlay){
                    if (game_life>0){
                        count_conti_hit = 0;
                        threadFever.interrupt();
                        game_life--;
                        lifeMole();
                        mole01.startAnimation(wrongClick);
                        threadFever = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(!Thread.currentThread().isInterrupted()){
                                    Random random = new Random();
                                    int m = random.nextInt(20)+15;
                                    if(count_conti_hit!=0 && count_conti_hit%m==0){
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Fever Time!!", Toast.LENGTH_SHORT).show();
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.fever));
                                                play_viewGroup.startAnimation(animFever);
                                            }
                                        });
                                        fever_time = true;
                                        try {
                                            int s = random.nextInt(7000)+5000;
                                            Thread.sleep(s);
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                            e.printStackTrace();
                                        }
                                        fever_time = false;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.nofever));
                                                play_viewGroup.clearAnimation();
                                            }
                                        });
                                        count_conti_hit = 0;
                                    }
                                }
                            }
                        });
                        threadFever.start();
                    }
                }
                i_mole01 = 0;
            }
        });
        mole02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i_mole02 == 1 && isPlay){
                    count_conti_hit++;
                    thread02.interrupt();
                    pointMole();
                    levelMole();
                    mole02.startAnimation(correctClick);
                    if(Build.VERSION.SDK_INT>=21) {
                        mole02.setBackground(getDrawable(R.drawable.down_card));
                    } else {
                        mole02.setBackground(getResources().getDrawable(R.drawable.down_card));
                    }
//                    mole02.setBackgroundColor(getResources().getColor(R.color.downMole));
                    if(thread02.isAlive()){
                        thread02.interrupt();
                    }
                    thread02 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!Thread.currentThread().isInterrupted()) {
                                int c02;
                                Random change = new Random();
                                if(fever_time){
                                    c02 = change.nextInt(1);
                                } else {
                                    c02 = change.nextInt(6 - (1/2)*game_level);
                                }
                                System.out.println("==============================================02 : "+c02);
                                if (c02 == 0) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(Build.VERSION.SDK_INT>=21) {
                                                mole02.setBackground(getDrawable(R.drawable.up_card));
                                            } else {
                                                mole02.setBackground(getResources().getDrawable(R.drawable.up_card));
                                            }
//                                            mole02.setBackgroundColor(getResources().getColor(R.color.upMole));
                                            i_mole02 = 1;
                                        }
                                    });
                                }
                                activeMole();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole02.setBackground(getDrawable(R.drawable.down_card));
                                        } else {
                                            mole02.setBackground(getResources().getDrawable(R.drawable.down_card));
                                        }
//                                        mole02.setBackgroundColor(getResources().getColor(R.color.downMole));
                                        i_mole02 = 0;
                                    }
                                });
                                restMole();
                            }
                        }
                    });
                    thread02.start();
                }
                else if (i_mole02 == 0 && isPlay){
                    if (game_life>0){
                        count_conti_hit = 0;
                        threadFever.interrupt();
                        game_life--;
                        lifeMole();
                        mole02.startAnimation(wrongClick);
                        threadFever = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(!Thread.currentThread().isInterrupted()){
                                    Random random = new Random();
                                    int m = random.nextInt(20)+15;
                                    if(count_conti_hit!=0 && count_conti_hit%m==0){
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Fever Time!!", Toast.LENGTH_SHORT).show();
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.fever));
                                                play_viewGroup.startAnimation(animFever);
                                            }
                                        });
                                        fever_time = true;
                                        try {
                                            int s = random.nextInt(7000)+5000;
                                            Thread.sleep(s);
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                            e.printStackTrace();
                                        }
                                        fever_time = false;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.nofever));
                                                play_viewGroup.clearAnimation();
                                            }
                                        });
                                        count_conti_hit = 0;
                                    }
                                }
                            }
                        });
                        threadFever.start();
                    }
                }
                i_mole02 = 0;
            }
        });
        mole03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i_mole03 == 1 && isPlay){
                    count_conti_hit++;
                    thread03.interrupt();
                    pointMole();
                    levelMole();
                    mole03.startAnimation(correctClick);
                    if(Build.VERSION.SDK_INT>=21) {
                        mole03.setBackground(getDrawable(R.drawable.down_card));
                    } else {
                        mole03.setBackground(getResources().getDrawable(R.drawable.down_card));
                    }
//                    mole03.setBackgroundColor(getResources().getColor(R.color.downMole));
                    if(thread03.isAlive()){
                        thread03.interrupt();
                    }
                    thread03 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!Thread.currentThread().isInterrupted()) {
                                int c03;
                                Random change = new Random();
                                if(fever_time){
                                    c03 = change.nextInt(1);
                                } else {
                                    c03 = change.nextInt(6 - (1/2)*game_level);
                                }
                                System.out.println("==============================================03 : "+c03);
                                if (c03 == 0) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(Build.VERSION.SDK_INT>=21) {
                                                mole03.setBackground(getDrawable(R.drawable.up_card));
                                            } else {
                                                mole03.setBackground(getResources().getDrawable(R.drawable.up_card));
                                            }
//                                            mole03.setBackgroundColor(getResources().getColor(R.color.upMole));
                                            i_mole03 = 1;
                                        }
                                    });
                                }
                                activeMole();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole03.setBackground(getDrawable(R.drawable.down_card));
                                        } else {
                                            mole03.setBackground(getResources().getDrawable(R.drawable.down_card));
                                        }
//                                        mole03.setBackgroundColor(getResources().getColor(R.color.downMole));
                                        i_mole03 = 0;
                                    }
                                });
                                restMole();
                            }
                        }
                    });
                    thread03.start();
                }
                else if (i_mole03 == 0 && isPlay){
                    if (game_life>0){
                        count_conti_hit = 0;
                        threadFever.interrupt();
                        game_life--;
                        lifeMole();
                        mole03.startAnimation(wrongClick);
                        threadFever = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(!Thread.currentThread().isInterrupted()){
                                    Random random = new Random();
                                    int m = random.nextInt(20)+15;
                                    if(count_conti_hit!=0 && count_conti_hit%m==0){
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Fever Time!!", Toast.LENGTH_SHORT).show();
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.fever));
                                                play_viewGroup.startAnimation(animFever);
                                            }
                                        });
                                        fever_time = true;
                                        try {
                                            int s = random.nextInt(7000)+5000;
                                            Thread.sleep(s);
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                            e.printStackTrace();
                                        }
                                        fever_time = false;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.nofever));
                                                play_viewGroup.clearAnimation();
                                            }
                                        });
                                        count_conti_hit = 0;
                                    }
                                }
                            }
                        });
                        threadFever.start();
                    }
                }
                i_mole03 = 0;
            }
        });

        mole10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i_mole10 == 1 && isPlay){
                    count_conti_hit++;
                    thread10.interrupt();
                    pointMole();
                    levelMole();
                    mole10.startAnimation(correctClick);
                    if(Build.VERSION.SDK_INT>=21) {
                        mole10.setBackground(getDrawable(R.drawable.down_card));
                    } else {
                        mole10.setBackground(getResources().getDrawable(R.drawable.down_card));
                    }
//                    mole10.setBackgroundColor(getResources().getColor(R.color.downMole));
                    if(thread10.isAlive()){
                        thread10.interrupt();
                    }
                    thread10 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!Thread.currentThread().isInterrupted()) {
                                int c10;
                                Random change = new Random();
                                if(fever_time){
                                    c10 = change.nextInt(1);
                                } else {
                                    c10 = change.nextInt(6 - (1/2)*game_level);
                                }
                                System.out.println("===================================================10 : "+c10);
                                if (c10 == 0) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(Build.VERSION.SDK_INT>=21) {
                                                mole10.setBackground(getDrawable(R.drawable.up_card));
                                            } else {
                                                mole10.setBackground(getResources().getDrawable(R.drawable.up_card));
                                            }
//                                            mole10.setBackgroundColor(getResources().getColor(R.color.upMole));
                                            i_mole10 = 1;
                                        }
                                    });
                                }
                                activeMole();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole10.setBackground(getDrawable(R.drawable.down_card));
                                        } else {
                                            mole10.setBackground(getResources().getDrawable(R.drawable.down_card));
                                        }
//                                        mole10.setBackgroundColor(getResources().getColor(R.color.downMole));
                                        i_mole10 = 0;
                                    }
                                });
                                restMole();
                            }
                        }
                    });
                    thread10.start();
                }
                else if (i_mole10 == 0 && isPlay){
                    if (game_life>0){
                        count_conti_hit = 0;
                        threadFever.interrupt();
                        game_life--;
                        lifeMole();
                        mole10.startAnimation(wrongClick);
                        threadFever = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(!Thread.currentThread().isInterrupted()){
                                    Random random = new Random();
                                    int m = random.nextInt(20)+15;
                                    if(count_conti_hit!=0 && count_conti_hit%m==0){
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Fever Time!!", Toast.LENGTH_SHORT).show();
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.fever));
                                                play_viewGroup.startAnimation(animFever);
                                            }
                                        });
                                        fever_time = true;
                                        try {
                                            int s = random.nextInt(7000)+5000;
                                            Thread.sleep(s);
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                            e.printStackTrace();
                                        }
                                        fever_time = false;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.nofever));
                                                play_viewGroup.clearAnimation();
                                            }
                                        });
                                        count_conti_hit = 0;
                                    }
                                }
                            }
                        });
                        threadFever.start();
                    }
                }
                i_mole10 = 0;
            }
        });
        mole11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i_mole11 == 1 && isPlay){
                    count_conti_hit++;
                    thread11.interrupt();
                    pointMole();
                    levelMole();
                    mole11.startAnimation(correctClick);
                    if(Build.VERSION.SDK_INT>=21) {
                        mole11.setBackground(getDrawable(R.drawable.down_card));
                    } else {
                        mole11.setBackground(getResources().getDrawable(R.drawable.down_card));
                    }
//                    mole11.setBackgroundColor(getResources().getColor(R.color.downMole));
                    if(thread11.isAlive()){
                        thread11.interrupt();
                    }
                    thread11 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!Thread.currentThread().isInterrupted()) {
                                int c11;
                                Random change = new Random();
                                if(fever_time){
                                    c11 = change.nextInt(1);
                                } else {
                                    c11 = change.nextInt(6 - (1/2)*game_level);
                                }
                                System.out.println("===================================================11 : "+c11);
                                if (c11 == 0) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(Build.VERSION.SDK_INT>=21) {
                                                mole11.setBackground(getDrawable(R.drawable.up_card));
                                            } else {
                                                mole11.setBackground(getResources().getDrawable(R.drawable.up_card));
                                            }
//                                            mole11.setBackgroundColor(getResources().getColor(R.color.upMole));
                                            i_mole11 = 1;
                                        }
                                    });
                                }
                                activeMole();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole11.setBackground(getDrawable(R.drawable.down_card));
                                        } else {
                                            mole11.setBackground(getResources().getDrawable(R.drawable.down_card));
                                        }
//                                        mole11.setBackgroundColor(getResources().getColor(R.color.downMole));
                                        i_mole11 = 0;
                                    }
                                });
                                restMole();
                            }
                        }
                    });
                    thread11.start();
                }
                else if (i_mole11 == 0 && isPlay){
                    if (game_life>0){
                        count_conti_hit = 0;
                        threadFever.interrupt();
                        game_life--;
                        lifeMole();
                        mole11.startAnimation(wrongClick);
                        threadFever = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(!Thread.currentThread().isInterrupted()){
                                    Random random = new Random();
                                    int m = random.nextInt(20)+15;
                                    if(count_conti_hit!=0 && count_conti_hit%m==0){
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Fever Time!!", Toast.LENGTH_SHORT).show();
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.fever));
                                                play_viewGroup.startAnimation(animFever);
                                            }
                                        });
                                        fever_time = true;
                                        try {
                                            int s = random.nextInt(7000)+5000;
                                            Thread.sleep(s);
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                            e.printStackTrace();
                                        }
                                        fever_time = false;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.nofever));
                                                play_viewGroup.clearAnimation();
                                            }
                                        });
                                        count_conti_hit = 0;
                                    }
                                }
                            }
                        });
                        threadFever.start();
                    }
                }
                i_mole11 = 0;
            }
        });
        mole12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i_mole12 == 1 && isPlay){
                    count_conti_hit++;
                    thread12.interrupt();
                    pointMole();
                    levelMole();
                    mole12.startAnimation(correctClick);
                    if(Build.VERSION.SDK_INT>=21) {
                        mole12.setBackground(getDrawable(R.drawable.down_card));
                    } else {
                        mole12.setBackground(getResources().getDrawable(R.drawable.down_card));
                    }
//                    mole12.setBackgroundColor(getResources().getColor(R.color.downMole));
                    if(thread12.isAlive()){
                        thread12.interrupt();
                    }
                    thread12 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!Thread.currentThread().isInterrupted()) {
                                int c12;
                                Random change = new Random();
                                if(fever_time){
                                    c12 = change.nextInt(1);
                                } else {
                                    c12 = change.nextInt(6 - (1/2)*game_level);
                                }
                                System.out.println("===================================================12 : "+c12);
                                if (c12 == 0) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(Build.VERSION.SDK_INT>=21) {
                                                mole12.setBackground(getDrawable(R.drawable.up_card));
                                            } else {
                                                mole12.setBackground(getResources().getDrawable(R.drawable.up_card));
                                            }
//                                            mole12.setBackgroundColor(getResources().getColor(R.color.upMole));
                                            i_mole12 = 1;
                                        }
                                    });
                                }
                                activeMole();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole12.setBackground(getDrawable(R.drawable.down_card));
                                        } else {
                                            mole12.setBackground(getResources().getDrawable(R.drawable.down_card));
                                        }
//                                        mole12.setBackgroundColor(getResources().getColor(R.color.downMole));
                                        i_mole12 = 0;
                                    }
                                });
                                restMole();
                            }
                        }
                    });
                    thread12.start();
                }
                else if (i_mole12 == 0 && isPlay){
                    if (game_life>0){
                        count_conti_hit = 0;
                        threadFever.interrupt();
                        game_life--;
                        lifeMole();
                        mole12.startAnimation(wrongClick);
                        threadFever = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(!Thread.currentThread().isInterrupted()){
                                    Random random = new Random();
                                    int m = random.nextInt(20)+15;
                                    if(count_conti_hit!=0 && count_conti_hit%m==0){
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Fever Time!!", Toast.LENGTH_SHORT).show();
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.fever));
                                                play_viewGroup.startAnimation(animFever);
                                            }
                                        });
                                        fever_time = true;
                                        try {
                                            int s = random.nextInt(7000)+5000;
                                            Thread.sleep(s);
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                            e.printStackTrace();
                                        }
                                        fever_time = false;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.nofever));
                                                play_viewGroup.clearAnimation();
                                            }
                                        });
                                        count_conti_hit = 0;
                                    }
                                }
                            }
                        });
                        threadFever.start();
                    }
                }
                i_mole12 = 0;
            }
        });
        mole13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i_mole13 == 1 && isPlay){
                    count_conti_hit++;
                    thread13.interrupt();
                    pointMole();
                    levelMole();
                    mole13.startAnimation(correctClick);
                    if(Build.VERSION.SDK_INT>=21) {
                        mole13.setBackground(getDrawable(R.drawable.down_card));
                    } else {
                        mole13.setBackground(getResources().getDrawable(R.drawable.down_card));
                    }
//                    mole13.setBackgroundColor(getResources().getColor(R.color.downMole));
                    if(thread13.isAlive()){
                        thread13.interrupt();
                    }
                    thread13 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!Thread.currentThread().isInterrupted()) {
                                int c13;
                                Random change = new Random();
                                if(fever_time){
                                    c13 = change.nextInt(1);
                                } else {
                                    c13 = change.nextInt(6 - (1/2)*game_level);
                                }
                                System.out.println("===================================================13 : "+c13);
                                if (c13 == 0) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(Build.VERSION.SDK_INT>=21) {
                                                mole13.setBackground(getDrawable(R.drawable.up_card));
                                            } else {
                                                mole13.setBackground(getResources().getDrawable(R.drawable.up_card));
                                            }
//                                            mole13.setBackgroundColor(getResources().getColor(R.color.upMole));
                                            i_mole13 = 1;
                                        }
                                    });
                                }
                                activeMole();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole13.setBackground(getDrawable(R.drawable.down_card));
                                        } else {
                                            mole13.setBackground(getResources().getDrawable(R.drawable.down_card));
                                        }
//                                        mole13.setBackgroundColor(getResources().getColor(R.color.downMole));
                                        i_mole13 = 0;
                                    }
                                });
                                restMole();
                            }
                        }
                    });
                    thread13.start();
                }
                else if (i_mole13 == 0 && isPlay){
                    if (game_life>0){
                        count_conti_hit = 0;
                        threadFever.interrupt();
                        game_life--;
                        lifeMole();
                        mole13.startAnimation(wrongClick);
                        threadFever = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(!Thread.currentThread().isInterrupted()){
                                    Random random = new Random();
                                    int m = random.nextInt(20)+15;
                                    if(count_conti_hit!=0 && count_conti_hit%m==0){
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Fever Time!!", Toast.LENGTH_SHORT).show();
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.fever));
                                                play_viewGroup.startAnimation(animFever);
                                            }
                                        });
                                        fever_time = true;
                                        try {
                                            int s = random.nextInt(7000)+5000;
                                            Thread.sleep(s);
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                            e.printStackTrace();
                                        }
                                        fever_time = false;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.nofever));
                                                play_viewGroup.clearAnimation();
                                            }
                                        });
                                        count_conti_hit = 0;
                                    }
                                }
                            }
                        });
                        threadFever.start();
                    }
                }
                i_mole13 = 0;
            }
        });

        mole20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i_mole20 == 1 && isPlay){
                    count_conti_hit++;
                    thread20.interrupt();
                    pointMole();
                    levelMole();
                    mole20.startAnimation(correctClick);
                    if(Build.VERSION.SDK_INT>=21) {
                        mole20.setBackground(getDrawable(R.drawable.down_card));
                    } else {
                        mole20.setBackground(getResources().getDrawable(R.drawable.down_card));
                    }
//                    mole20.setBackgroundColor(getResources().getColor(R.color.downMole));
                    if(thread20.isAlive()){
                        thread20.interrupt();
                    }
                    thread20 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!Thread.currentThread().isInterrupted()) {
                                int c20;
                                Random change = new Random();
                                if(fever_time){
                                    c20 = change.nextInt(1);
                                } else {
                                    c20 = change.nextInt(6 - (1/2)*game_level);
                                }
                                System.out.println("=============================================================20 : "+c20);
                                if (c20 == 0) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(Build.VERSION.SDK_INT>=21) {
                                                mole20.setBackground(getDrawable(R.drawable.up_card));
                                            } else {
                                                mole20.setBackground(getResources().getDrawable(R.drawable.up_card));
                                            }
//                                            mole20.setBackgroundColor(getResources().getColor(R.color.upMole));
                                            i_mole20 =1;
                                        }
                                    });
                                }
                                activeMole();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole20.setBackground(getDrawable(R.drawable.down_card));
                                        } else {
                                            mole20.setBackground(getResources().getDrawable(R.drawable.down_card));
                                        }
//                                        mole20.setBackgroundColor(getResources().getColor(R.color.downMole));
                                        i_mole20 = 0;
                                    }
                                });
                                restMole();
                            }
                        }
                    });
                    thread20.start();
                }
                else if (i_mole20 == 0 && isPlay){
                    if (game_life>0){
                        count_conti_hit = 0;
                        threadFever.interrupt();
                        game_life--;
                        lifeMole();
                        mole20.startAnimation(wrongClick);
                        threadFever = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(!Thread.currentThread().isInterrupted()){
                                    Random random = new Random();
                                    int m = random.nextInt(20)+15;
                                    if(count_conti_hit!=0 && count_conti_hit%m==0){
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Fever Time!!", Toast.LENGTH_SHORT).show();
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.fever));
                                                play_viewGroup.startAnimation(animFever);
                                            }
                                        });
                                        fever_time = true;
                                        try {
                                            int s = random.nextInt(7000)+5000;
                                            Thread.sleep(s);
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                            e.printStackTrace();
                                        }
                                        fever_time = false;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.nofever));
                                                play_viewGroup.clearAnimation();
                                            }
                                        });
                                        count_conti_hit = 0;
                                    }
                                }
                            }
                        });
                        threadFever.start();
                    }
                }
                i_mole20 = 0;
            }
        });
        mole21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i_mole21 == 1 && isPlay){
                    count_conti_hit++;
                    thread21.interrupt();
                    pointMole();
                    levelMole();
                    mole21.startAnimation(correctClick);
                    if(Build.VERSION.SDK_INT>=21) {
                        mole21.setBackground(getDrawable(R.drawable.down_card));
                    } else {
                        mole21.setBackground(getResources().getDrawable(R.drawable.down_card));
                    }
//                    mole21.setBackgroundColor(getResources().getColor(R.color.downMole));
                    if(thread21.isAlive()){
                        thread21.interrupt();
                    }
                    thread21 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!Thread.currentThread().isInterrupted()) {
                                int c21;
                                Random change = new Random();
                                if(fever_time){
                                    c21 = change.nextInt(1);
                                } else {
                                    c21 = change.nextInt(6 - (1/2)*game_level);
                                }
                                System.out.println("=============================================================21 : "+c21);
                                if (c21 == 0) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(Build.VERSION.SDK_INT>=21) {
                                                mole21.setBackground(getDrawable(R.drawable.up_card));
                                            } else {
                                                mole21.setBackground(getResources().getDrawable(R.drawable.up_card));
                                            }
//                                            mole21.setBackgroundColor(getResources().getColor(R.color.upMole));
                                            i_mole21 =1;
                                        }
                                    });
                                }
                                activeMole();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole21.setBackground(getDrawable(R.drawable.down_card));
                                        } else {
                                            mole21.setBackground(getResources().getDrawable(R.drawable.down_card));
                                        }
//                                        mole21.setBackgroundColor(getResources().getColor(R.color.downMole));
                                        i_mole21 = 0;
                                    }
                                });
                                restMole();
                            }
                        }
                    });
                    thread21.start();
                }
                else if (i_mole21 == 0 && isPlay){
                    if (game_life>0){
                        count_conti_hit = 0;
                        threadFever.interrupt();
                        game_life--;
                        lifeMole();
                        mole21.startAnimation(wrongClick);
                        threadFever = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(!Thread.currentThread().isInterrupted()){
                                    Random random = new Random();
                                    int m = random.nextInt(20)+15;
                                    if(count_conti_hit!=0 && count_conti_hit%m==0){
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Fever Time!!", Toast.LENGTH_SHORT).show();
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.fever));
                                                play_viewGroup.startAnimation(animFever);
                                            }
                                        });
                                        fever_time = true;
                                        try {
                                            int s = random.nextInt(7000)+5000;
                                            Thread.sleep(s);
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                            e.printStackTrace();
                                        }
                                        fever_time = false;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.nofever));
                                                play_viewGroup.clearAnimation();
                                            }
                                        });
                                        count_conti_hit = 0;
                                    }
                                }
                            }
                        });
                        threadFever.start();
                    }
                }
                i_mole21 = 0;
            }
        });
        mole22.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i_mole22 == 1 && isPlay){
                    count_conti_hit++;
                    thread22.interrupt();
                    pointMole();
                    levelMole();
                    mole22.startAnimation(correctClick);
                    if(Build.VERSION.SDK_INT>=21) {
                        mole22.setBackground(getDrawable(R.drawable.down_card));
                    } else {
                        mole22.setBackground(getResources().getDrawable(R.drawable.down_card));
                    }
//                    mole22.setBackgroundColor(getResources().getColor(R.color.downMole));
                    if(thread22.isAlive()){
                        thread22.interrupt();
                    }
                    thread22 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!Thread.currentThread().isInterrupted()) {
                                int c22;
                                Random change = new Random();
                                if(fever_time){
                                    c22 = change.nextInt(1);
                                } else {
                                    c22 = change.nextInt(6 - (1/2)*game_level);
                                }
                                System.out.println("=============================================================22 : "+c22);
                                if (c22 == 0) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(Build.VERSION.SDK_INT>=21) {
                                                mole22.setBackground(getDrawable(R.drawable.up_card));
                                            } else {
                                                mole22.setBackground(getResources().getDrawable(R.drawable.up_card));
                                            }
//                                            mole22.setBackgroundColor(getResources().getColor(R.color.upMole));
                                            i_mole22 = 1;
                                        }
                                    });
                                }
                                activeMole();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole22.setBackground(getDrawable(R.drawable.down_card));
                                        } else {
                                            mole22.setBackground(getResources().getDrawable(R.drawable.down_card));
                                        }
//                                        mole22.setBackgroundColor(getResources().getColor(R.color.downMole));
                                        i_mole22 = 0;
                                    }
                                });
                                restMole();
                            }
                        }
                    });
                    thread22.start();
                }
                else if (i_mole22 == 0 && isPlay){
                    if (game_life>0){
                        count_conti_hit = 0;
                        threadFever.interrupt();
                        game_life--;
                        lifeMole();
                        mole22.startAnimation(wrongClick);
                        threadFever = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(!Thread.currentThread().isInterrupted()){
                                    Random random = new Random();
                                    int m = random.nextInt(20)+15;
                                    if(count_conti_hit!=0 && count_conti_hit%m==0){
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Fever Time!!", Toast.LENGTH_SHORT).show();
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.fever));
                                                play_viewGroup.startAnimation(animFever);
                                            }
                                        });
                                        fever_time = true;
                                        try {
                                            int s = random.nextInt(7000)+5000;
                                            Thread.sleep(s);
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                            e.printStackTrace();
                                        }
                                        fever_time = false;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.nofever));
                                                play_viewGroup.clearAnimation();
                                            }
                                        });
                                        count_conti_hit = 0;
                                    }
                                }
                            }
                        });
                        threadFever.start();
                    }
                }
                i_mole22 = 0;
            }
        });
        mole23.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i_mole23 == 1 && isPlay){
                    count_conti_hit++;
                    thread23.interrupt();
                    pointMole();
                    levelMole();
                    mole23.startAnimation(correctClick);
                    if(Build.VERSION.SDK_INT>=21) {
                        mole23.setBackground(getDrawable(R.drawable.down_card));
                    } else {
                        mole23.setBackground(getResources().getDrawable(R.drawable.down_card));
                    }
//                    mole23.setBackgroundColor(getResources().getColor(R.color.downMole));
                    if(thread23.isAlive()){
                        thread23.interrupt();
                    }
                    thread23 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!Thread.currentThread().isInterrupted()) {
                                int c23;
                                Random change = new Random();
                                if(fever_time){
                                    c23 = change.nextInt(1);
                                } else {
                                    c23 = change.nextInt(6 - (1/2)*game_level);
                                }
                                System.out.println("=============================================================23 : "+c23);
                                if (c23 == 0) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(Build.VERSION.SDK_INT>=21) {
                                                mole23.setBackground(getDrawable(R.drawable.up_card));
                                            } else {
                                                mole23.setBackground(getResources().getDrawable(R.drawable.up_card));
                                            }
//                                            mole23.setBackgroundColor(getResources().getColor(R.color.upMole));
                                            i_mole23 = 1;
                                        }
                                    });
                                }
                                activeMole();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole23.setBackground(getDrawable(R.drawable.down_card));
                                        } else {
                                            mole23.setBackground(getResources().getDrawable(R.drawable.down_card));
                                        }
//                                        mole23.setBackgroundColor(getResources().getColor(R.color.downMole));
                                        i_mole23 = 0;
                                    }
                                });
                                restMole();
                            }
                        }
                    });
                    thread23.start();
                }
                else if (i_mole23 == 0 && isPlay){
                    if (game_life>0){
                        count_conti_hit = 0;
                        threadFever.interrupt();
                        game_life--;
                        lifeMole();
                        mole23.startAnimation(wrongClick);
                        threadFever = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(!Thread.currentThread().isInterrupted()){
                                    Random random = new Random();
                                    int m = random.nextInt(20)+15;
                                    if(count_conti_hit!=0 && count_conti_hit%m==0){
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Fever Time!!", Toast.LENGTH_SHORT).show();
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.fever));
                                                play_viewGroup.startAnimation(animFever);
                                            }
                                        });
                                        fever_time = true;
                                        try {
                                            int s = random.nextInt(7000)+5000;
                                            Thread.sleep(s);
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                            e.printStackTrace();
                                        }
                                        fever_time = false;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.nofever));
                                                play_viewGroup.clearAnimation();
                                            }
                                        });
                                        count_conti_hit = 0;
                                    }
                                }
                            }
                        });
                        threadFever.start();
                    }
                }
                i_mole23 = 0;
            }
        });

        mole30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i_mole30 == 1 && isPlay){
                    count_conti_hit++;
                    thread30.interrupt();
                    pointMole();
                    levelMole();
                    mole30.startAnimation(correctClick);
                    if(Build.VERSION.SDK_INT>=21) {
                        mole30.setBackground(getDrawable(R.drawable.down_card));
                    } else {
                        mole30.setBackground(getResources().getDrawable(R.drawable.down_card));
                    }
//                    mole30.setBackgroundColor(getResources().getColor(R.color.downMole));
                    if(thread30.isAlive()){
                        thread30.interrupt();
                    }
                    thread30 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!Thread.currentThread().isInterrupted()) {
                                int c30;
                                Random change = new Random();
                                if(fever_time){
                                    c30 = change.nextInt(1);
                                } else {
                                    c30 = change.nextInt(6 - (1/2)*game_level);
                                }
                                System.out.println("==================================================================30 : "+c30);
                                if (c30 == 0) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(Build.VERSION.SDK_INT>=21) {
                                                mole30.setBackground(getDrawable(R.drawable.up_card));
                                            } else {
                                                mole30.setBackground(getResources().getDrawable(R.drawable.up_card));
                                            }
//                                            mole30.setBackgroundColor(getResources().getColor(R.color.upMole));
                                            i_mole30 = 1;
                                        }
                                    });
                                }
                                activeMole();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole30.setBackground(getDrawable(R.drawable.down_card));
                                        } else {
                                            mole30.setBackground(getResources().getDrawable(R.drawable.down_card));
                                        }
//                                        mole30.setBackgroundColor(getResources().getColor(R.color.downMole));
                                        i_mole30 = 0;
                                    }
                                });
                                restMole();
                            }
                        }
                    });
                    thread30.start();
                }
                else if (i_mole30 == 0 && isPlay){
                    if (game_life>0){
                        count_conti_hit = 0;
                        threadFever.interrupt();
                        game_life--;
                        lifeMole();
                        mole30.startAnimation(wrongClick);
                        threadFever = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(!Thread.currentThread().isInterrupted()){
                                    Random random = new Random();
                                    int m = random.nextInt(20)+15;
                                    if(count_conti_hit!=0 && count_conti_hit%m==0){
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Fever Time!!", Toast.LENGTH_SHORT).show();
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.fever));
                                                play_viewGroup.startAnimation(animFever);
                                            }
                                        });
                                        fever_time = true;
                                        try {
                                            int s = random.nextInt(7000)+5000;
                                            Thread.sleep(s);
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                            e.printStackTrace();
                                        }
                                        fever_time = false;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.nofever));
                                                play_viewGroup.clearAnimation();
                                            }
                                        });
                                        count_conti_hit = 0;
                                    }
                                }
                            }
                        });
                        threadFever.start();
                    }
                }
                i_mole30 = 0;
            }
        });
        mole31.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i_mole31 == 1 && isPlay){
                    count_conti_hit++;
                    thread31.interrupt();
                    pointMole();
                    levelMole();
                    mole31.startAnimation(correctClick);
                    if(Build.VERSION.SDK_INT>=21) {
                        mole31.setBackground(getDrawable(R.drawable.down_card));
                    } else {
                        mole31.setBackground(getResources().getDrawable(R.drawable.down_card));
                    }
//                    mole31.setBackgroundColor(getResources().getColor(R.color.downMole));
                    if(thread31.isAlive()){
                        thread31.interrupt();
                    }
                    thread31 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!Thread.currentThread().isInterrupted()) {
                                int c31;
                                Random change = new Random();
                                if(fever_time){
                                    c31 = change.nextInt(1);
                                } else {
                                    c31 = change.nextInt(6 - (1/2)*game_level);
                                }
                                System.out.println("==================================================================31 : "+c31);
                                if (c31 == 0) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(Build.VERSION.SDK_INT>=21) {
                                                mole31.setBackground(getDrawable(R.drawable.up_card));
                                            } else {
                                                mole31.setBackground(getResources().getDrawable(R.drawable.up_card));
                                            }
//                                            mole31.setBackgroundColor(getResources().getColor(R.color.upMole));
                                            i_mole31 = 1;
                                        }
                                    });
                                }
                                activeMole();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole31.setBackground(getDrawable(R.drawable.down_card));
                                        } else {
                                            mole31.setBackground(getResources().getDrawable(R.drawable.down_card));
                                        }
//                                        mole31.setBackgroundColor(getResources().getColor(R.color.downMole));
                                        i_mole31 = 0;
                                    }
                                });
                                restMole();
                            }
                        }
                    });
                    thread31.start();
                }
                else if (i_mole31 == 0 && isPlay){
                    if (game_life>0){
                        count_conti_hit = 0;
                        threadFever.interrupt();
                        game_life--;
                        lifeMole();
                        mole31.startAnimation(wrongClick);
                        threadFever = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(!Thread.currentThread().isInterrupted()){
                                    Random random = new Random();
                                    int m = random.nextInt(20)+15;
                                    if(count_conti_hit!=0 && count_conti_hit%m==0){
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Fever Time!!", Toast.LENGTH_SHORT).show();
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.fever));
                                                play_viewGroup.startAnimation(animFever);
                                            }
                                        });
                                        fever_time = true;
                                        try {
                                            int s = random.nextInt(7000)+5000;
                                            Thread.sleep(s);
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                            e.printStackTrace();
                                        }
                                        fever_time = false;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.nofever));
                                                play_viewGroup.clearAnimation();
                                            }
                                        });
                                        count_conti_hit = 0;
                                    }
                                }
                            }
                        });
                        threadFever.start();
                    }
                }
                i_mole31 = 0;
            }
        });
        mole32.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i_mole32 == 1 && isPlay){
                    count_conti_hit++;
                    thread32.interrupt();
                    pointMole();
                    levelMole();
                    mole32.startAnimation(correctClick);
                    if(Build.VERSION.SDK_INT>=21) {
                        mole32.setBackground(getDrawable(R.drawable.down_card));
                    } else {
                        mole32.setBackground(getResources().getDrawable(R.drawable.down_card));
                    }
//                    mole32.setBackgroundColor(getResources().getColor(R.color.downMole));
                    if(thread32.isAlive()){
                        thread32.interrupt();
                    }
                    thread32 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!Thread.currentThread().isInterrupted()) {
                                int c32;
                                Random change = new Random();
                                if(fever_time){
                                    c32 = change.nextInt(1);
                                } else {
                                    c32 = change.nextInt(6 - (1/2)*game_level);
                                }
                                System.out.println("==================================================================32 : "+c32);
                                if (c32 == 0) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(Build.VERSION.SDK_INT>=21) {
                                                mole32.setBackground(getDrawable(R.drawable.up_card));
                                            } else {
                                                mole32.setBackground(getResources().getDrawable(R.drawable.up_card));
                                            }
//                                            mole32.setBackgroundColor(getResources().getColor(R.color.upMole));
                                            i_mole32 = 1;
                                        }
                                    });
                                }
                                activeMole();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole32.setBackground(getDrawable(R.drawable.down_card));
                                        } else {
                                            mole32.setBackground(getResources().getDrawable(R.drawable.down_card));
                                        }
//                                        mole32.setBackgroundColor(getResources().getColor(R.color.downMole));
                                        i_mole32 = 0;
                                    }
                                });
                                restMole();
                            }
                        }
                    });
                    thread32.start();
                }
                else if (i_mole32 == 0 && isPlay){
                    if (game_life>0){
                        count_conti_hit = 0;
                        threadFever.interrupt();
                        game_life--;
                        lifeMole();
                        mole32.startAnimation(wrongClick);
                        threadFever = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(!Thread.currentThread().isInterrupted()){
                                    Random random = new Random();
                                    int m = random.nextInt(20)+15;
                                    if(count_conti_hit!=0 && count_conti_hit%m==0){
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Fever Time!!", Toast.LENGTH_SHORT).show();
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.fever));
                                                play_viewGroup.startAnimation(animFever);
                                            }
                                        });
                                        fever_time = true;
                                        try {
                                            int s = random.nextInt(7000)+5000;
                                            Thread.sleep(s);
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                            e.printStackTrace();
                                        }
                                        fever_time = false;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.nofever));
                                                play_viewGroup.clearAnimation();
                                            }
                                        });
                                        count_conti_hit = 0;
                                    }
                                }
                            }
                        });
                        threadFever.start();
                    }
                }
                i_mole32 = 0;
            }
        });
        mole33.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i_mole33 == 1 && isPlay){
                    count_conti_hit++;
                    thread33.interrupt();
                    pointMole();
                    levelMole();
                    mole33.startAnimation(correctClick);
                    if(Build.VERSION.SDK_INT>=21) {
                        mole33.setBackground(getDrawable(R.drawable.down_card));
                    } else {
                        mole33.setBackground(getResources().getDrawable(R.drawable.down_card));
                    }
//                    mole33.setBackgroundColor(getResources().getColor(R.color.downMole));
                    if(thread33.isAlive()){
                        thread33.interrupt();
                    }
                    thread33 = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            while (!Thread.currentThread().isInterrupted()) {
                                int c33;
                                Random change = new Random();
                                if(fever_time){
                                    c33 = change.nextInt(1);
                                } else {
                                    c33 = change.nextInt(6 - (1/2)*game_level);
                                }
                                System.out.println("==================================================================33 : "+c33);
                                if (c33 == 0) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if(Build.VERSION.SDK_INT>=21) {
                                                mole33.setBackground(getDrawable(R.drawable.up_card));
                                            } else {
                                                mole33.setBackground(getResources().getDrawable(R.drawable.up_card));
                                            }
//                                            mole33.setBackgroundColor(getResources().getColor(R.color.upMole));
                                            i_mole33 =1;
                                        }
                                    });
                                }
                                activeMole();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(Build.VERSION.SDK_INT>=21) {
                                            mole33.setBackground(getDrawable(R.drawable.down_card));
                                        } else {
                                            mole33.setBackground(getResources().getDrawable(R.drawable.down_card));
                                        }
//                                        mole33.setBackgroundColor(getResources().getColor(R.color.downMole));
                                        i_mole33 = 0;
                                    }
                                });
                                restMole();
                            }
                        }
                    });
                    thread33.start();
                }
                else if (i_mole33 == 0 && isPlay){
                    if (game_life>0){
                        count_conti_hit = 0;
                        threadFever.interrupt();
                        game_life--;
                        lifeMole();
                        mole33.startAnimation(wrongClick);
                        threadFever = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                while(!Thread.currentThread().isInterrupted()){
                                    Random random = new Random();
                                    int m = random.nextInt(20)+15;
                                    if(count_conti_hit!=0 && count_conti_hit%m==0){
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getApplicationContext(), "Fever Time!!", Toast.LENGTH_SHORT).show();
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.fever));
                                                play_viewGroup.startAnimation(animFever);

                                            }
                                        });
                                        fever_time = true;
                                        try {
                                            int s = random.nextInt(7000)+5000;
                                            Thread.sleep(s);
                                        } catch (InterruptedException e) {
                                            Thread.currentThread().interrupt();
                                            e.printStackTrace();
                                        }
                                        fever_time = false;
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                play_viewGroup.setBackgroundColor(getResources().getColor(R.color.nofever));
                                                play_viewGroup.clearAnimation();
                                            }
                                        });
                                        count_conti_hit = 0;
                                    }
                                }
                            }
                        });
                        threadFever.start();
                    }
                }
                i_mole33 = 0;
            }
        });

    }


    // 여기도 카운트다운 하나 들어감
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        initThread();
        if(countTask.getStatus()== AsyncTask.Status.RUNNING || countTask.getStatus()== AsyncTask.Status.PENDING) {
            countTask.cancel(true);
        }
        overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
    }

    // 두더지 올라와 있는 시간
    public void activeMole(){
        if(game_level <=5) {
            try {
                Thread.sleep(2000 - 100 * game_level);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
        else{
            try {
                Thread.sleep(2000 - 180 * game_level);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }
    }

    // 두더지 내려가서 쉬는 시간
    public void restMole(){
        try {
            Thread.sleep(2000 - 200 * game_level);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }

    // 게임 라이프
    public void lifeMole(){
        if(game_life ==4){
            life5.setBackgroundColor(getResources().getColor(R.color.nolife));
            tvlife.setText(String.valueOf(game_life));
        }
        else if(game_life ==3){
            life4.setBackgroundColor(getResources().getColor(R.color.nolife));
            tvlife.setText(String.valueOf(game_life));
        }
        else if(game_life ==2){
            life3.setBackgroundColor(getResources().getColor(R.color.nolife));
            tvlife.setText(String.valueOf(game_life));
        }
        else if(game_life ==1){
            life2.setBackgroundColor(getResources().getColor(R.color.nolife));
            tvlife.setText(String.valueOf(game_life));
        }
        else if(game_life ==0){
            life1.setBackgroundColor(getResources().getColor(R.color.nolife));
            gameOver();
            tvlife.setText(String.valueOf(game_life));
            Toast.makeText(getApplicationContext(), "Game Over! ! ! !", Toast.LENGTH_SHORT).show();
        }
    }

    // 점수
    public void pointMole(){
        game_point += 100+10*game_level;
        point.setText(String.valueOf(game_point));
    }

    // 레벨
    public void levelMole(){
        if(game_point > 22000){
            if(game_level == 8) {
                game_level = 9;
                level.setText(String.valueOf(game_level));
                Toast.makeText(getApplicationContext(), "Level UP!!!!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(game_point <= 22000 && game_point > 17500){
            if(game_level == 7) {
                game_level = 8;
                level.setText(String.valueOf(game_level));
                Toast.makeText(getApplicationContext(), "Level UP!!!!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(game_point <= 17500 && game_point > 13500){
            if(game_level == 6) {
                game_level = 7;
                level.setText(String.valueOf(game_level));
                Toast.makeText(getApplicationContext(), "Level UP!!!!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(game_point <=13500 && game_point > 10000){
            if(game_level == 5) {
                game_level = 6;
                level.setText(String.valueOf(game_level));
                Toast.makeText(getApplicationContext(), "Level UP!!!!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(game_point <= 10000 && game_point > 7000){
            if(game_level == 4) {
                game_level = 5;
                level.setText(String.valueOf(game_level));
                Toast.makeText(getApplicationContext(), "Level UP!!!!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(game_point <= 7000 && game_point > 4500){
            if(game_level == 3) {
                game_level = 4;
                level.setText(String.valueOf(game_level));
                Toast.makeText(getApplicationContext(), "Level UP!!!!", Toast.LENGTH_SHORT).show();
            }
        }
        else if(game_point <= 4500 && game_point > 2500){
            if(game_level == 2) {
                game_level = 3;
                level.setText(String.valueOf(game_level));
                Toast.makeText(getApplicationContext(), "Level UP!!!!", Toast.LENGTH_SHORT).show();
            }
        }
        else if (game_point <= 2500 && game_point > 1000){
            if(game_level == 1) {
                game_level = 2;
                level.setText(String.valueOf(game_level));
                Toast.makeText(getApplicationContext(), "Level UP!!!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 게임오버 & 점수 저장
    public void gameOver(){
        initThread();

        pointShared = getSharedPreferences("PointOrder", MODE_PRIVATE);
        pointSharedEditor = pointShared.edit();

        for(int i=0;;i++){
            System.out.println("Gameover point Save"+i+" 현재 i 입니다.");
            if(!pointShared.contains(String.valueOf(i))){
                System.out.println("Gameover point Save"+ i+" 안에 들어왔습니다.");
                pointSharedEditor.putInt(String.valueOf(i), game_point);
                pointSharedEditor.commit();
                break;
            }
        }

        Intent view_point_intent = new Intent(getApplicationContext(), MiniGamePoint.class);
        startActivity(view_point_intent);
        overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
    }

    // 쓰레드 초기화
    public void initThread() {
        int[] alive = new int[17];
        int count = 0;
        while(count!=17) {
            count =0;
            for(int i=0; i<17; i++){
                count = count +alive[i];
            }
            System.out.println("ccccccccccccccccccccccccccccccccccccccccccccccc : "+ count);
            if (thread00.isAlive()) {
                thread00.interrupt();
            }
            else if( !thread00.isAlive()){
                alive[0]=1;
                System.out.println("alive alive alive alive alive alive alive0 : " + alive[0]);
            }
            if (thread01.isAlive()) {
                thread01.interrupt();
            }
            else if( !thread01.isAlive()){
                alive[1]=1;
                System.out.println("alive alive alive alive alive alive alive1 : " + alive[1]);
            }
            if (thread02.isAlive()) {
                thread02.interrupt();
            }
            else if( !thread02.isAlive()){
                alive[2]=1;
                System.out.println("alive alive alive alive alive alive alive2 : " + alive[2]);
            }
            if (thread03.isAlive()) {
                thread03.interrupt();
            }
            else if( !thread03.isAlive()){
                alive[3]=1;
                System.out.println("alive alive alive alive alive alive alive3 : " + alive[3]);
            }

            if (thread10.isAlive()) {
                thread10.interrupt();
            }
            else if( !thread10.isAlive()){
                alive[4]=1;
                System.out.println("alive alive alive alive alive alive alive4 : " + alive[4]);
            }
            if (thread11.isAlive()) {
                thread11.interrupt();
            }
            else if( !thread11.isAlive()){
                alive[5]=1;
                System.out.println("alive alive alive alive alive alive alive5 : " + alive[5]);
            }
            if (thread12.isAlive()) {
                thread12.interrupt();
            }
            else if( !thread12.isAlive()){
                alive[6]=1;
                System.out.println("alive alive alive alive alive alive alive6 : " + alive[6]);
            }
            if (thread13.isAlive()) {
                thread13.interrupt();
            }
            else if( !thread13.isAlive()){
                alive[7]=1;
                System.out.println("alive alive alive alive alive alive alive7 : " + alive[7]);
            }

            if (thread20.isAlive()) {
                thread20.interrupt();
            }
            else if( !thread20.isAlive()){
                alive[8]=1;
                System.out.println("alive alive alive alive alive alive alive8 : " + alive[8]);
            }
            if (thread21.isAlive()) {
                thread21.interrupt();
            }
            else if( !thread21.isAlive()){
                alive[9]=1;
                System.out.println("alive alive alive alive alive alive alive9 : " + alive[9]);
            }
            if (thread22.isAlive()) {
                thread22.interrupt();
            }
            else if( !thread22.isAlive()){
                alive[10]=1;
                System.out.println("alive alive alive alive alive alive alive10 : " + alive[10]);
            }
            if (thread23.isAlive()) {
                thread23.interrupt();
            }
            else if( !thread23.isAlive()){
                alive[11]=1;
                System.out.println("alive alive alive alive alive alive alive11 : " + alive[11]);
            }

            if (thread30.isAlive()) {
                thread30.interrupt();
            }
            else if( !thread30.isAlive()){
                alive[12]=1;
                System.out.println("alive alive alive alive alive alive alive12 : " + alive[12]);
            }
            if (thread31.isAlive()) {
                thread31.interrupt();
            }
            else if( !thread31.isAlive()){
                alive[13]=1;
                System.out.println("alive alive alive alive alive alive alive13 : " + alive[13]);
            }
            if (thread32.isAlive()) {
                thread32.interrupt();
            }
            else if( !thread32.isAlive()){
                alive[14]=1;
                System.out.println("alive alive alive alive alive alive alive14 : " + alive[14]);
            }
            if (thread33.isAlive()) {
                thread33.interrupt();
            }
            else if( !thread33.isAlive()){
                alive[15]=1;
                System.out.println("alive alive alive alive alive alive alive15 : " + alive[15]);
            }
            if (threadFever.isAlive()) {
                threadFever.interrupt();
            }
            else if( !threadFever.isAlive()){
                alive[16]=1;
                System.out.println("alive alive alive alive alive alive alive15 : " + alive[16]);
            }
        }
        isPlay = false;
    }

    // 카운트다운 토스트
    class CountTask extends AsyncTask<Integer, Integer, Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Integer... params) {
            for(int i= params[0]; i>=0; i--){
                publishProgress(i);     // onProgressUpdate 호출해서 i값을 넘겨주는 메소드
                SystemClock.sleep(1000);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(values[0]==3){
                countdown3.setVisibility(View.VISIBLE);
            }
            else if(values[0]==2){
                countdown3.setVisibility(View.GONE);
                countdown2.setVisibility(View.VISIBLE);
            }
            else if(values[0]==1){
                countdown2.setVisibility(View.GONE);
                countdown1.setVisibility(View.VISIBLE);
            }
            else if(values[0]==0){
                countdown1.setVisibility(View.GONE);
                countdownGo.setVisibility(View.VISIBLE);
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            countdownGo.setVisibility(View.GONE);

            initThread();

            isPlay = true;

            thread00.start();
            thread01.start();
            thread02.start();
            thread03.start();

            thread10.start();
            thread11.start();
            thread12.start();
            thread13.start();

            thread20.start();
            thread21.start();
            thread22.start();
            thread23.start();

            thread30.start();
            thread31.start();
            thread32.start();
            thread33.start();

            threadFever.start();

        }
    }



}
