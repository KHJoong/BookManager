package com.example.kimhj.mybookmanager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Locale;

/**
 * Created by kimhj on 2017-05-13.
 */

public class BookSearch extends AppCompatActivity {

    Adapter_search sAdapter;

    int beforeDel;
    String keepShared;

    Spinner spSearch;
    EditText etSearch;
    Button btnSearch;
    ListView lvSearch;

    String searchType;
    String searchName;

    int clickNumber;
    String searchTitle;
    String searchAuthor;
    String searchPublisher;
    String searchPubDate;
    String compareItem;
    String compareTitle;
    String compareAuthor;
    String comparePublisher;
    String comparePubDate;
    JSONObject compareJson;
    JSONObject bookJson;
    SharedPreferences bookPref;
    SharedPreferences.Editor bookEditor;
    SharedPreferences bookCount;
    SharedPreferences.Editor bookCountEditor;
    SharedPreferences bookCom;
    SharedPreferences.Editor bookComEditor;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_item_search);

        bookCom = getSharedPreferences("BookComment", MODE_PRIVATE);
        bookComEditor = bookCom.edit();

        sAdapter = new Adapter_search(getApplicationContext());

        spSearch = (Spinner) findViewById(R.id.search_type);
        etSearch = (EditText) findViewById(R.id.search_name);
        btnSearch = (Button) findViewById(R.id.search_button);

        lvSearch = (ListView) findViewById(R.id.search_listview);
        lvSearch.setAdapter(sAdapter);

        String[] search_option = getResources().getStringArray(R.array.searchArray);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, search_option);
        spSearch.setAdapter(adapter);
        spSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                searchType = (String) spSearch.getSelectedItem();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // EditText 변화에 따라 바로바로 검색 결과 알려주기
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(searchType.equals("")){
                    Toast toast = Toast.makeText(getApplicationContext(), "<- 검색 유형을 골라주세요", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 120);
                    toast.show();
                } else {
                    String text = etSearch.getText().toString().toLowerCase(Locale.getDefault());
                    String type = searchType;
                    sAdapter.watcherFilter(type, text);
                }
            }
        });

        // 엔터키 입력 시 검색 실행
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) || (actionId == EditorInfo.IME_ACTION_DONE)){
                    searchItem();
                }
                return false;
            }
        });

        // 검색 버튼 크릭 시 검색 실행
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchItem();
            }
        });

        // 리스트뷰 클릭 시 아이템 확인 및 수정 가능
        lvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                bookPref = getSharedPreferences("BookItem", MODE_PRIVATE);
                try {
                    searchTitle = sAdapter.sItem.get(position).getTitle();
                    searchAuthor = sAdapter.sItem.get(position).getAuthor();
                    searchPublisher = sAdapter.sItem.get(position).getPublisher();
                    searchPubDate = sAdapter.sItem.get(position).getPublish_date();
                    for(int i=0; i<MainActivity.adapter.getCount(); i++){
                        compareItem = bookPref.getString(String.valueOf(i),"");
                        compareJson = new JSONObject(compareItem);
                        compareTitle = compareJson.getString("Title");
                        compareAuthor = compareJson.getString("Author");
                        comparePublisher = compareJson.getString("Publisher");
                        comparePubDate = compareJson.getString("PubDate");
                        System.out.println("BookSearch Class CompareItem : "+ compareItem);
                        if(searchTitle.equals(compareTitle) && searchAuthor.equals(compareAuthor) && searchPublisher.equals(comparePublisher) && searchPubDate.equals(comparePubDate)){
                            clickNumber = i;
                            System.out.println("BookSearch Class ClickNumber : "+ clickNumber);
                            break;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(), BookView.class);
                intent.putExtra("Position", (MainActivity.adapter.getCount()-clickNumber-1));
                System.out.println("BookSearch Class ClickNumber : "+ clickNumber);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
            }
        });

        // 리스트뷰 롱클릭 삭제 기능
        lvSearch.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final String selectedItem = sAdapter.sItem.get(position).getTitle();
                AlertDialog.Builder builder = new AlertDialog.Builder(BookSearch.this);
                builder.setMessage("정말 책 "+selectedItem+"을(를) 삭제하시겠습니까?");
                builder.setCancelable(true);

                // 확인버튼 눌렀을 경우우
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.out.println("Long Click delete position : " + position);
                        beforeDel = MainActivity.adapter.getCount();

                        bookPref = getSharedPreferences("BookItem", MODE_PRIVATE);
                        try {
                            searchTitle = sAdapter.sItem.get(position).getTitle();
                            searchAuthor = sAdapter.sItem.get(position).getAuthor();
                            searchPublisher = sAdapter.sItem.get(position).getPublisher();
                            searchPubDate = sAdapter.sItem.get(position).getPublish_date();
                            for(int i=0; i<MainActivity.adapter.getCount(); i++){
                                compareItem = bookPref.getString(String.valueOf(i),"");
                                compareJson = new JSONObject(compareItem);
                                compareTitle = compareJson.getString("Title");
                                compareAuthor = compareJson.getString("Author");
                                comparePublisher = compareJson.getString("Publisher");
                                comparePubDate = compareJson.getString("PubDate");
                                System.out.println("BookSearch Class CompareItem : "+ compareItem);
                                if(searchTitle.equals(compareTitle) && searchAuthor.equals(compareAuthor) && searchPublisher.equals(comparePublisher) && searchPubDate.equals(comparePubDate)){
                                    clickNumber = i;
                                    System.out.println("BookSearch Class ClickNumber : "+ clickNumber);
                                    break;
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // Comment 삭제
                        try {
                            bookJson = new JSONObject(bookPref.getString(String.valueOf(clickNumber), ""));
                            bookComEditor.remove(bookJson.getString("Title")+bookJson.getString("Author"));
                            bookComEditor.commit();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        // shared에 저장된 book item 삭제
                        bookEditor = bookPref.edit();
                        bookEditor.remove(String.valueOf(clickNumber));
                        bookEditor.commit();

                        // shared의 중간에 삭제된 공간을 땡기는 부분
                        for(int i=(clickNumber+1); i<beforeDel; i++){
                            keepShared = bookPref.getString(String.valueOf(i),"");
                            System.out.println("롱클릭 shread index " + i + "의 " + keepShared +"를 " + (i-1) + "로 옮기겠다.");
                            bookEditor.remove(String.valueOf(i));
                            bookEditor.putString(String.valueOf(i-1), keepShared);
                            bookEditor.commit();
                        }

                        bookCount = getSharedPreferences("Count", MODE_PRIVATE);
                        bookCountEditor = bookCount.edit();

                        MainActivity.adapter.mItem.remove(MainActivity.adapter.getCount()-clickNumber-1);
                        bookCountEditor.putInt("Count", MainActivity.adapter.getCount());
                        bookCountEditor.commit();

                        sAdapter.sItem.remove(position);

                        Toast.makeText(getApplicationContext(), "책 "+selectedItem+"이(가) 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                        searchItem();
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

   }        // onCreate end

    @Override
    public void onResume(){
        super.onResume();
        searchName = etSearch.getText().toString();
        sAdapter.filter(searchType, searchName);
    }

    // 검색 메소드
    public void searchItem(){
        searchName = etSearch.getText().toString();
        sAdapter.filter(searchType, searchName);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
    }
}           // BookSearch.java end
