package com.example.kimhj.mybookmanager;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    int REQUEST_CODE1 = 1;      // 아이템 추가
    int REQUEST_CODE2 = 2;      // 아이템 확인, 수정
    int REQUEST_CODE3 = 3;      // 아이템 선택 삭제

//    ImageView main_poster;
//    Handler handler;

    ListView listView;
    static Adapter adapter;

    int beforeDel;
    String keepShared;

    byte[] posterByteArray;
    String sTitle;
    String sAuthor;
    String sPublisher;
    String sPubDate;
    int init;
    int getItemCount;
    JSONObject bookJson;
    SharedPreferences bookPref;
    SharedPreferences.Editor bookEditor;
    SharedPreferences bookCount;
    SharedPreferences.Editor bookCountEditor;
    SharedPreferences bookCom;
    SharedPreferences.Editor bookComEditor;

    // poster 슬라이드
//    Thread posterThread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookJson = new JSONObject();
        bookPref = getSharedPreferences("BookItem", MODE_PRIVATE);
        bookEditor = bookPref.edit();
        bookCount = getSharedPreferences("BookCount", MODE_PRIVATE);
        bookCountEditor = bookCount.edit();
        getItemCount = bookCount.getInt("Count", 0);
        bookCom = getSharedPreferences("BookComment", MODE_PRIVATE);
        bookComEditor = bookCom.edit();


        adapter = new Adapter(getApplicationContext());
        listView = (ListView)findViewById(R.id.book_list);
        listView.setAdapter(adapter);

        // 저장된 리스트뷰 불러오기
        for(int i=0;i<getItemCount;++i){
            ListViewLoad lvLoad = new ListViewLoad();
            lvLoad.execute(i);

        } // 리스트뷰




        // 아이템 확인하기
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = (Book)parent.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), BookView.class);
                intent.putExtra("Position", position);
                startActivityForResult(intent, REQUEST_CODE2);
                overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
            }
        });

        // 아이템 하나 롱클릭 삭제 메뉴 띄우기
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                final String selectedItem = adapter.mItem.get(position).getTitle();
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(MainActivity.this);
                builder.setMessage("정말 책 "+selectedItem+"을(를) 삭제하시겠습니까?");
                builder.setCancelable(true);

                // 확인버튼 눌렀을 경우우
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        beforeDel = adapter.getCount();

                        bookComEditor.remove(adapter.mItem.get(position).getTitle()+adapter.mItem.get(position).getAuthor());
                        bookComEditor.commit();

                        adapter.mItem.remove(position);
                        System.out.println("롱클릭 삭제 position : " + position);
                        bookEditor.remove(String.valueOf(beforeDel-position-1));
                        System.out.println("롱클릭 삭제 Shared Intex : "+ (adapter.getCount()-position-1));

                        for(int i=(beforeDel-position); i<beforeDel; i++){
                            keepShared = bookPref.getString(String.valueOf(i),"");
                            System.out.println("롱클릭 shread index " + i + "의 " + keepShared +"를 " + (i-1) + "로 옮기겠다.");
                            bookEditor.remove(String.valueOf(i));
                            bookEditor.putString(String.valueOf(i-1), keepShared);
                            bookEditor.commit();
                        }

                        bookCountEditor.putInt("Count", adapter.getCount());
                        bookCountEditor.commit();

                        listView.setAdapter(adapter);
                        Toast.makeText(getApplicationContext(), "책 "+selectedItem+"이(가) 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                // 취소버튼 눌렀을 경우
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                // 다이얼로그 만들기
                builder.show();
                return true;
            }
        });


    }


    @Override
    public void onResume(){
        super.onResume();
        adapter.notifyDataSetChanged();
//        if(posterThread.isInterrupted()) {
//            posterThread.start();
//        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if(!posterThread.isInterrupted()) {
//            posterThread.interrupt();
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        bookCountEditor.putInt("Count", MainActivity.adapter.getCount());
        bookCountEditor.commit();
    }

    // 액션바 메뉴 추가
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    // 액션바 메뉴 클릭
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.plus:
                Intent book_plus_intent = new Intent(getApplicationContext(), BookPlus.class);
                startActivityForResult(book_plus_intent,REQUEST_CODE1);
                overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
                break;
            case R.id.search:
                Intent book_search_intent = new Intent(getApplicationContext(), BookSearch.class);
                startActivity(book_search_intent);
                overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
                break;
            case R.id.delete_choice:
                Intent book_choice_delete_intent = new Intent(getApplicationContext(), BookMultiChoiceDel.class);
                startActivityForResult(book_choice_delete_intent, REQUEST_CODE3);
                overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
                break;
            case R.id.delete_all:
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("정말 전체 목록을 삭제하시겠습니까?");
                builder.setCancelable(true);

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        bookEditor.clear();
                        bookCountEditor.clear();
                        bookComEditor.clear();
                        bookEditor.commit();
                        bookCountEditor.commit();
                        bookComEditor.commit();

                        adapter.mItem.clear();
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getApplicationContext(), "등록했던 전체 책을 삭제하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                break;
            case R.id.gobookstore:
                callDiaLog();
                break;
            case R.id.mini_game:
                Intent mini_game_intent = new Intent(getApplicationContext(), MiniGameMain.class);
                startActivity(mini_game_intent);
                overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
            default:
                break;
        }
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_CODE1){
//                String title = data.getStringExtra("Title");
//                String author = data.getStringExtra("Author");
//                String publisher = data.getStringExtra("Publisher");
//                String pubdate = data.getStringExtra("PubDate");
//
//                byte[] bytePoster = data.getByteArrayExtra("Poster");
//
//                Book book = new Book(bytePoster, title, author, publisher, pubdate);
//                adapter.addItem(book);

                listView.setAdapter(adapter);
            }
            else if(requestCode == REQUEST_CODE2){
//                int position = data.getIntExtra("Position", 0);
//                String title = data.getStringExtra("Title");
//                String author = data.getStringExtra("Author");
//                String publisher = data.getStringExtra("Publisher");
//                String pubdate = data.getStringExtra("PubDate");
//
//                byte[] bytePoster = data.getByteArrayExtra("Poster");
//
//                Book book = new Book(bytePoster, title, author, publisher, pubdate);
//                adapter.setmItem(position, book);

                listView.setAdapter(adapter);
            }
            else if(requestCode == REQUEST_CODE3){
                listView.setAdapter(adapter);
            }
        }
    }

    // 서점 링크 띄우는 메소드
    public void callDiaLog(){
        LayoutInflater dialog = LayoutInflater.from(this);
        View dialogLayout = dialog.inflate(R.layout.go_bookstore_dialog, null);
        final Dialog myDialog = new Dialog(this);

        myDialog.setTitle(getResources().getString(R.string.whereru));
        myDialog.setContentView(dialogLayout);
        myDialog.setCancelable(true);
        myDialog.show();

        final RadioButton newbook = (RadioButton)dialogLayout.findViewById(R.id.newbook);
        final RadioButton bestbook = (RadioButton)dialogLayout.findViewById(R.id.bestbook);
        Button aladinBtn = (Button)dialogLayout.findViewById(R.id.goAladin);
        Button yes24Btn = (Button)dialogLayout.findViewById(R.id.goYes24);
        aladinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newbook.isChecked()){
                    Uri uri = Uri.parse("http://www.aladin.co.kr/shop/common/wnew.aspx?BranchType=1");
                    Intent it = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(it);
                    overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
                }
                else if (bestbook.isChecked()){
                    Uri uri = Uri.parse("http://www.aladin.co.kr/shop/common/wbest.aspx?BranchType=1");
                    Intent it = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(it);
                    overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
                }
                myDialog.cancel();
            }
        });
        yes24Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newbook.isChecked()){
                    Uri uri = Uri.parse("http://m.yes24.com/Home/index");
                    Intent it = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(it);
                    overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
                }
                else if (bestbook.isChecked()){
                    Uri uri = Uri.parse("http://m.yes24.com/Home/best");
                    Intent it = new Intent(Intent.ACTION_VIEW,uri);
                    startActivity(it);
                    overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
                }
                myDialog.cancel();
            }
        });
    }

    // String > byte array 오래걸려서 쓰레드로 빼봄....
    class ListViewLoad extends AsyncTask<Integer, Void, Void>{

        @Override
        protected Void doInBackground(Integer... params) {
            byte[] poster;
            String title;
            String author;
            String publisher;
            String pubdate;

            try {
                bookJson = new JSONObject(bookPref.getString(String.valueOf(params[0]),""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if(!bookJson.getString("Poster").equals("")){
                    String stringArray = bookJson.getString("Poster");
                    String[] split = stringArray.substring(1, stringArray.length()-1).split(", ");
                    poster = new byte[split.length];
                    for(int j=0; j<split.length; j++){
                        poster[j] = Byte.parseByte(split[j]);
                    }
                } else {
                    poster = null;
                }
                if(!bookJson.isNull(String.valueOf("Title"))){
                    title = bookJson.getString("Title");
                } else {
                    title = null;
                }
                if(!bookJson.isNull(String.valueOf("Author"))) {
                    author = bookJson.getString("Author");
                } else {
                    author = null;
                }
                if(!bookJson.isNull(String.valueOf("Publisher"))) {
                    publisher = bookJson.getString("Publisher");
                } else {
                    publisher = null;
                }
                if(!bookJson.isNull(String.valueOf("PubDate"))) {
                    pubdate = bookJson.getString("PubDate");
                } else {
                    pubdate = null;
                }
                Book book = new Book(poster, title, author, publisher, pubdate);
                MainActivity.adapter.addItem(book);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void Void) {
            super.onPostExecute(Void);
            listView.setAdapter(adapter);
        }
    }


}

