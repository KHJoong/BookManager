package com.example.kimhj.mybookmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kimhj on 2017-05-04.
 */

public class BookMultiChoiceDel extends AppCompatActivity {

    int beforeDel ;
    String beforeString;
    String delTitle;
    String delAuthor;

    ListView multi_choice_delete_listview;
    Button multi_chocie_delete_btn;

    Adapter_choice cAdapter;

    JSONObject bookJson;
    SharedPreferences bookPref;
    SharedPreferences.Editor bookEditor;
    SharedPreferences bookCount;
    SharedPreferences.Editor bookCountEditor;
    SharedPreferences bookCom;
    SharedPreferences.Editor bookComEditor;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_item_multi_choice);

        bookJson = new JSONObject();
        bookPref = getSharedPreferences("BookItem", MODE_PRIVATE);
        bookEditor = bookPref.edit();
        bookCount = getSharedPreferences("BookCount", MODE_PRIVATE);
        bookCountEditor = bookCount.edit();
        bookCom = getSharedPreferences("BookComment", MODE_PRIVATE);
        bookComEditor = bookCom.edit();

        cAdapter = new Adapter_choice(getApplicationContext(), MainActivity.adapter.mItem);

        // 리스트뷰 선택 초기화
        for(int i=0; i<cAdapter.cmItem.size(); i++){
            cAdapter.cmItem.get(i).setCheckedItem(false);
        }

        multi_choice_delete_listview = (ListView)findViewById(R.id.book_multi_choice_list);
        multi_choice_delete_listview.setAdapter(cAdapter);

        multi_chocie_delete_btn = (Button)findViewById(R.id.book_multi_choice_btn);
        multi_chocie_delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(BookMultiChoiceDel.this);
                builder.setMessage("선택한 항목을 정말 지우시겠습니까?");
                builder.setCancelable(true);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int beforeCount = cAdapter.getCount();

                        for(int i=beforeCount-1; i>=0; i--){
                            if(cAdapter.cmItem.get(i).getCheckedItem()==true){
                                try {
                                    bookJson = new JSONObject(bookPref.getString(String.valueOf(beforeCount-i-1), ""));
                                    delTitle = bookJson.getString("Title");
                                    delAuthor = bookJson.getString("Auhtor");
                                    bookComEditor.remove(delTitle+delAuthor);
                                    bookComEditor.commit();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        for(int i=0; i<cAdapter.getCount(); i++){
                            beforeDel = cAdapter.getCount();
//                            System.out.println("지우기 전 총 수량은 : "+ beforeDel);
                            if(cAdapter.cmItem.get(i).getCheckedItem()==true){
//                                System.out.println("지울 Listview 인덱스는 : "+i);
                                cAdapter.cmItem.remove(i);
//                                System.out.println("지운 bookEditor 인덱스는 : "+ (beforeDel-i-1) + " : "+bookPref.getString(String.valueOf(beforeDel-i-1),""));
                                bookEditor.remove(String.valueOf(beforeDel-i-1));
                                bookEditor.commit();
                                for(int j=(beforeDel-i); j<beforeDel; j++){
//                                    System.out.println("바꿀 Shared 인덱스는 : "+j);
                                    if(!bookPref.getString(String.valueOf(j), "").equals("")) {
//                                        System.out.println(bookPref.getString(String.valueOf(j),"") + "를 " + bookPref.getString(String.valueOf(j-1),"")+"자리에 넣자");
                                        beforeString = bookPref.getString(String.valueOf(j), "");
                                        bookEditor.remove(String.valueOf(j));
                                        bookEditor.putString(String.valueOf(j - 1), beforeString);
                                        bookEditor.commit();
//                                        System.out.println(j+"를 "+(j-1)+"로 바꾸었다.");
                                    }
                                }
//                                System.out.println("============================");
                                i=-1;
                            }
                        }

                        MainActivity.adapter.renewalItem(cAdapter.cmItem);

                        bookCountEditor.putInt("Count", MainActivity.adapter.getCount());
                        bookCountEditor.commit();

                        int dif = beforeCount - MainActivity.adapter.getCount();
                        Toast.makeText(getApplicationContext(), "선택한 책 " + dif + "권을 삭제했습니다.", Toast.LENGTH_SHORT).show();
                        finish();
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




    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
    }
}
