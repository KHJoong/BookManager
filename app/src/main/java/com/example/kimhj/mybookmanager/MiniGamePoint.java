package com.example.kimhj.mybookmanager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Created by kimhj on 2017-05-16.
 */

public class MiniGamePoint extends AppCompatActivity {

    ListView lvOrder;
    Button order_init_btn;

    Adapter_minigame miniAdapter;

    SharedPreferences pointShared;
    SharedPreferences.Editor pointSharedEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_minigame_point);

        miniAdapter = new Adapter_minigame(getApplicationContext());

        order_init_btn = (Button)findViewById(R.id.order_init_btn);
        lvOrder = (ListView)findViewById(R.id.point_order);
        lvOrder.setAdapter(miniAdapter);


        pointShared = getSharedPreferences("PointOrder", MODE_PRIVATE);
        pointSharedEditor = pointShared.edit();

        for(int i=0;;i++){
            if(pointShared.contains(String.valueOf(i))){
                System.out.println( (i+1)+ "번째 저장된 아이템은"+pointShared.getInt(String.valueOf(i),0));
            } else {
                break;
            }
        }
        while(true){
            for(int i=0;;i++){
                System.out.println("shared의 " + i + "번째에 값이 들어있나요?"+pointShared.contains(String.valueOf(i)));
                if(pointShared.contains(String.valueOf(i))){
                    System.out.println(" i = " + i);
                    System.out.println("shared의 "+i+"번째 값은 " + pointShared.getInt(String.valueOf(i), 0));
                    if(i==0){
                        Point point = new Point(pointShared.getInt(String.valueOf(i),0));
                        miniAdapter.addItem(0, point);
                    } else {
                        for(int j=0;;j++) {
                            if(j==10){
                                break;
                            }
                            if(i!=j) {
                                System.out.println(" j = " + j);
                                System.out.println("listview의 "+j+"번째 값은" + miniAdapter.getItem(j).getPoint());
                                if (pointShared.getInt(String.valueOf(i), 0) >= miniAdapter.getItem(j).getPoint()) {
                                    Point point = new Point(pointShared.getInt(String.valueOf(i), 0));
                                    miniAdapter.addItem(j, point);
                                    break;
                                } else {

                                }
                            } else {
                                Point point = new Point(pointShared.getInt(String.valueOf(i), 0));
                                miniAdapter.addItem(j, point);
                                break;
                            }
                        }
                    }
                } else {
                    break;
                }
            }
            break;
        }

        // 기존 10위권 사이에 점수를 획득하여 10등이 11등으로 밀려났을 경우 지워주는 부분
        for(int i=10; i<miniAdapter.getCount();){
            miniAdapter.pItem.remove(i);
        }

        // 점수 초기화 ( 모두 삭제 )
        order_init_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MiniGamePoint.this);
                builder.setMessage("정말 모든 기록을 삭제하시겠습니까?");
                builder.setCancelable(true);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pointSharedEditor.clear();
                        pointSharedEditor.commit();
                        miniAdapter.pItem.clear();
                        miniAdapter.notifyDataSetChanged();

                        Toast.makeText(getApplicationContext(), "기록을 초기화했습니다.", Toast.LENGTH_SHORT).show();
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

    }       // onCreate

    @Override
    protected void onDestroy() {
        super.onDestroy();
        miniAdapter.pItem.clear();
    }       // onDestroy

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
    }
}
