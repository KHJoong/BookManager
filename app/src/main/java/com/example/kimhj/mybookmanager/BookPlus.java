package com.example.kimhj.mybookmanager;

import android.Manifest;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by kimhj on 2017-04-30.
 */

public class BookPlus extends AppCompatActivity {

    int REQUEST_IMAGE_CAPTURE = 1;      // 카메라로 사진 찍기
    int REQUEST_IMAGE_LOAD = 2;         // 갤러리에서 사진 가져오기

    int PICK_FROM_GALLERY = 11;          // 갤러리에서 사진 가져올 수 잇는 권한 얻기
    int PICK_FROM_CAMERA = 12;           // 카메라로 사진 가져올 수 있는 권한 얻기

    String thisTitle;
    String thisAuthor;
    String compareTitle;
    String compareAuthor;
    int countTitle;
    int countAuthor;

    private final String Tag = getClass().getName();
    String imagePath;
    Bitmap mBitmap;
    int orientation;

    String bookObject;
    JSONObject bookJson;
    SharedPreferences bookPref;
    SharedPreferences.Editor bookEditor;
    SharedPreferences bookCount;
    SharedPreferences.Editor bookCountEditor;
    int countSaveAmount;

    byte[] posterByteArray;

    ImageView ivPoster;
    Button gallery_btn;
    Button camera_btn;
    Button poster_cancel_btn;
    Button poster_search;
    String search;

    TextView fixTitle;
    EditText etTitle;
    TextView fixAuthor;
    EditText etAuthor;
    EditText etPublisher;
    EditText etPubDate;

    Button save_btn;
    Button cancel_btn;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_item_plus);

        // JSON & SharedPreferences 선언
        bookJson = new JSONObject();
        bookPref = getSharedPreferences("BookItem", MODE_PRIVATE);
        // Shared 편집할 것이므로 Editor 역시 함께 선언
        bookEditor = bookPref.edit();
        bookCount = getSharedPreferences("BookCount", MODE_PRIVATE);
        bookCountEditor = bookCount.edit();

        ivPoster = (ImageView)findViewById(R.id.poster);

        gallery_btn = (Button)findViewById(R.id.poster_gallery);
        gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(BookPlus.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(BookPlus.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY);
                    } else {
                        takeImageIntent();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        camera_btn = (Button)findViewById(R.id.poster_camera);
        camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(BookPlus.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(BookPlus.this, new String[] {Manifest.permission.CAMERA}, PICK_FROM_CAMERA);
                    } else {
                        takePictureIntent();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        poster_cancel_btn = (Button)findViewById(R.id.poster_cancel);
        poster_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivPoster.setImageDrawable(null);
            }
        });
        poster_search = (Button)findViewById(R.id.poster_search);
        poster_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search = etTitle.getText().toString() + " " + etAuthor.getText().toString() + " book";
                Uri uri = Uri.parse("http://images.google.com/search?num=10&hl=en&site=&tbm=isch&source=hp‌​&biw=1366&bih=667&q="+search);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.putExtra(SearchManager.QUERY, search);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
            }
        });

        fixTitle = (TextView)findViewById(R.id.fix_title);
        etTitle = (EditText)findViewById(R.id.title);
        fixAuthor = (TextView)findViewById(R.id.fix_author);
        etAuthor = (EditText)findViewById(R.id.author);
        etPublisher = (EditText)findViewById(R.id.publisher);
        etPubDate = (EditText)findViewById(R.id.pub_date);
        etPubDate.addTextChangedListener(mDateEntryWatcher);

        save_btn = (Button)findViewById(R.id.save_ok);
        cancel_btn = (Button)findViewById(R.id.save_cancel);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTitle = 0;
                countAuthor = 0;
                thisTitle = etTitle.getText().toString();
                thisAuthor = etAuthor.getText().toString();
                for(int i=0; i<MainActivity.adapter.getCount(); i++){
                    compareTitle = MainActivity.adapter.mItem.get(i).getTitle();
                    compareAuthor = MainActivity.adapter.mItem.get(i).getAuthor();
                    if(thisTitle.equals(compareTitle) && thisAuthor.equals(compareAuthor)){
                        fixTitle.setTextColor(Color.RED);
                        etTitle.setTextColor(Color.RED);
                        fixAuthor.setTextColor(Color.RED);
                        etAuthor.setTextColor(Color.RED);
                        countTitle = 1;
                        countAuthor = 1;
                        break;
                    }
                    countTitle = 0;
                    countAuthor = 0;
                }

                if(countTitle==1 && countAuthor==1){
                    Toast.makeText(getApplicationContext(), "이미 저장된 책입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        if(ivPoster.getDrawable()!=null) {
                            Drawable drawable = ivPoster.getDrawable();
                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            posterByteArray = stream.toByteArray();
                            bookJson.put("Poster", Arrays.toString(posterByteArray));
                        } else {
                            bookJson.put("Poster", "");
                        }
                        // JSON에 객체 담기
                        bookJson.put("Title", etTitle.getText().toString());
                        bookJson.put("Author", etAuthor.getText().toString());
                        bookJson.put("Publisher", etPublisher.getText().toString());
                        bookJson.put("PubDate", etPubDate.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // JSON 객체를 String으로 변환하기
                    bookObject = bookJson.toString();

                    int c = MainActivity.adapter.getCount();
                    // 변환된 String을 Shared에 담기
                    bookEditor.putString(String.valueOf(c), bookObject);
                    bookEditor.commit();

                    Book book = new Book(posterByteArray, etTitle.getText().toString(), etAuthor.getText().toString(), etPublisher.getText().toString(), etPubDate.getText().toString());
                    MainActivity.adapter.addItem(book);

                    countSaveAmount = MainActivity.adapter.getCount();
                    bookCountEditor.putInt("Count", countSaveAmount);
                    bookCountEditor.commit();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    setResult(RESULT_OK, intent);
                    finish();
                    overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
                }

            }
        });     // item 추가 확인 버튼

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
                overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
            }
        });     // item 추가 취소 버튼
    }

    // Take a Picture from camera
    public void takePictureIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
        }

    }

    // Take a Picture from gallery
    public void takeImageIntent(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_IMAGE_LOAD);
        overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_IMAGE_CAPTURE){

                // 원래있던거
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap)extras.get("data");
                ivPoster.setImageBitmap(bitmap);
            }
            if(requestCode == REQUEST_IMAGE_LOAD){
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    ivPoster.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(BookPlus.this, "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 11:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                    startActivityForResult(galleryIntent, REQUEST_IMAGE_LOAD);
                    takeImageIntent();
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
            case 12:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    takePictureIntent();
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
    }


    // 출판일 YYYY-MM-DD 형식으로 입력 포맷 및 경고문
    TextWatcher mDateEntryWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String working = s.toString();
            boolean isValid = true;
            if(working.length()==4 && before==0){
                String enteredYear = etPubDate.getText().toString();
                int currentYear = Calendar.getInstance().get(Calendar.YEAR);
                if(Integer.parseInt(enteredYear)>currentYear){
                    isValid = false;
                }
                working+="-";
                etPubDate.setText(working);
                etPubDate.setSelection(working.length());
            }
            else if(working.length()==7 && before==0){
                String enteredMonth = working.substring(5);
                if(Integer.parseInt(enteredMonth)<1 || Integer.parseInt(enteredMonth)>12){
                    isValid = false;
                }
                working+="-";
                etPubDate.setText(working);
                etPubDate.setSelection(working.length());
            }
            else if(working.length()==10 && before==0){
                String enteredDate = working.substring(8);
                if(Integer.parseInt(enteredDate)<1 || Integer.parseInt(enteredDate)>31){
                    isValid = false;
                }
            }
            else if(working.length()>10 && before==0){
                isValid=false;
            }

            if(!isValid){
                etPubDate.setError("YYYY-MM-DD");
            } else {
                etPubDate.setError(null);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };



}
