package com.example.kimhj.mybookmanager;

import android.Manifest;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Created by kimhj on 2017-05-01.
 */

public class BookModify extends AppCompatActivity {

    int REQUEST_MODI_IMAGE_CAPTURE = 1;     // 카메라로 사진 찍기
    int REQUEST_MODI_IMAGE_LOAD = 2;        // 갤러리에서 사진 가져오기

    int PICK_FROM_GALLERY_MODI = 11;        // 갤러리에서 사진 가져올 수 있는 권한 얻기
    int PICK_FROM_CAMERA_MODI = 12;           // 카메라로 사진 가져올 수 있는 권한 얻기

    ImageView ivPoster;
    Button modi_gallery_btn;
    Button modi_camera_btn;
    Button modi_poster_cancel_btn;
    Button modi_poster_search;
    String search;

    TextView modiFixTitle;
    EditText etTitle;
    TextView modiFixAuthor;
    EditText etAuthor;
    EditText etPublisher;
    EditText etPubDate;

    Button btn_modify;

    int position;
    int reverse_position;
    String bookObject;
    String titleBeforeChangeComment;
    String authorBeforeChangeComment;

    String thisTitle;
    String thisAuthor;
    String compareTitle;
    String compareAuthor;
    int countTitle;
    int countAuthor;

    byte[] posterByteArray;
    SharedPreferences bookPref;
    SharedPreferences.Editor bookEditor;
    JSONObject bookJson;
    SharedPreferences bookCom;
    SharedPreferences.Editor bookComEditor;
    JSONObject bookComJson;

    Handler handler;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_item_modify);

        handler = new Handler();

        bookCom = getSharedPreferences("BookComment", MODE_PRIVATE);
        bookComEditor = bookCom.edit();

        bookPref = getSharedPreferences("BookItem", MODE_PRIVATE);
        bookEditor = bookPref.edit();

        ivPoster = (ImageView)findViewById(R.id.modi_poster);
        modi_gallery_btn = (Button)findViewById(R.id.modi_poster_gallery);
        modi_gallery_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(BookModify.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(BookModify.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PICK_FROM_GALLERY_MODI);
                    } else {
                        takeImageIntent();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        modi_camera_btn = (Button)findViewById(R.id.modi_poster_camera);
        modi_camera_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (ActivityCompat.checkSelfPermission(BookModify.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(BookModify.this, new String[] {Manifest.permission.CAMERA}, PICK_FROM_CAMERA_MODI);
                    } else {
                        takeModiPictureIntent();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        modi_poster_cancel_btn = (Button)findViewById(R.id.modi_poster_cancel);
        modi_poster_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivPoster.setImageDrawable(null);
            }
        });

        modiFixTitle = (TextView)findViewById(R.id.modi_fix_title);
        etTitle = (EditText)findViewById(R.id.modi_title);
        modiFixAuthor = (TextView)findViewById(R.id.modi_fix_author);
        etAuthor = (EditText)findViewById(R.id.modi_author);
        etPublisher = (EditText)findViewById(R.id.modi_publisher);
        etPubDate = (EditText)findViewById(R.id.modi_pub_date);
        etPubDate.addTextChangedListener(mDateEntryWatcher);


        Intent intent = getIntent();
        position = intent.getIntExtra("Position", 0);
        reverse_position = MainActivity.adapter.getCount()-position-1;
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

            titleBeforeChangeComment = bookJson.getString("Title");
            authorBeforeChangeComment = bookJson.getString("Author");
            etTitle.setText(bookJson.getString("Title"));
            etAuthor.setText(bookJson.getString("Author"));
            etPublisher.setText(bookJson.getString("Publisher"));
            etPubDate.setText(bookJson.getString("PubDate"));

            thisTitle = etTitle.getText().toString();
            thisAuthor = etAuthor.getText().toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }



        modi_poster_search = (Button)findViewById(R.id.modi_poster_search);
        modi_poster_search.setOnClickListener(new View.OnClickListener() {
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

        btn_modify = (Button)findViewById(R.id.modi_confirm);
        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countTitle = 0;
                countAuthor = 0;
                compareTitle = etTitle.getText().toString();
                compareAuthor = etAuthor.getText().toString();
                if(!(thisTitle.equals(compareTitle) && thisAuthor.equals(compareAuthor))) {
                    thisTitle = etTitle.getText().toString();
                    thisAuthor = etAuthor.getText().toString();
                    for (int i = 0; i < MainActivity.adapter.getCount(); i++) {
                        compareTitle = MainActivity.adapter.mItem.get(i).getTitle();
                        compareAuthor = MainActivity.adapter.mItem.get(i).getAuthor();
                        if (thisTitle.equals(compareTitle) && thisAuthor.equals(compareAuthor)) {
                            modiFixTitle.setTextColor(Color.RED);
                            etTitle.setTextColor(Color.RED);
                            modiFixAuthor.setTextColor(Color.RED);
                            etAuthor.setTextColor(Color.RED);
                            countTitle = 1;
                            countAuthor = 1;
                            break;
                        }
                        countTitle = 0;
                        countAuthor = 0;
                    }
                }

                if(countTitle==1 && countAuthor==1){
                    Toast.makeText(getApplicationContext(), "이미 저장된 책입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        bookComJson = new JSONObject(bookCom.getString(titleBeforeChangeComment+authorBeforeChangeComment, ""));
                        bookComEditor.putString(etTitle.getText().toString()+etAuthor.getText().toString(), bookComJson.toString());
                        bookComEditor.remove(titleBeforeChangeComment+authorBeforeChangeComment);
                        bookComEditor.commit();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        if(ivPoster.getDrawable()!=null) {
                            Drawable drawable = ivPoster.getDrawable();
                            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            posterByteArray = stream.toByteArray();
                            bookJson.put("Poster", Arrays.toString(posterByteArray));
                        } else {
                            posterByteArray = null;
                            bookJson.put("Poster", "");
                        }
                        bookJson.put("Title", etTitle.getText().toString());
                        bookJson.put("Author", etAuthor.getText().toString());
                        bookJson.put("Publisher", etPublisher.getText().toString());
                        bookJson.put("PubDate", etPubDate.getText().toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    bookObject = bookJson.toString();

                    bookEditor.putString(String.valueOf(reverse_position), bookObject);
                    bookEditor.commit();

                    Book book = new Book(posterByteArray, etTitle.getText().toString(), etAuthor.getText().toString(), etPublisher.getText().toString(), etPubDate.getText().toString());
                    MainActivity.adapter.setmItem(position, book);

                    Intent intent = new Intent(getApplicationContext(), BookView.class);
                    setResult(RESULT_OK, intent);
                    finish();
                    overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
                }




//                Intent intent = new Intent(getApplicationContext(), BookView.class);
//                intent.putExtra("Position", position);
//                intent.putExtra("Title", etTitle.getText().toString());
//                intent.putExtra("Author", etAuthor.getText().toString());
//                intent.putExtra("Publisher", etPublisher.getText().toString());
//                intent.putExtra("PubDate", etPubDate.getText().toString());
//
//                if(ivPoster.getDrawable()!=null) {
//                    Drawable drawable = ivPoster.getDrawable();
//                    Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
//                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                    byte[] byteArray = stream.toByteArray();
//                    intent.putExtra("Poster", byteArray);
//                }
//
//                setResult(RESULT_OK, intent);
//                finish();
            }
        });
    }

    // Take a Picture
    public void takeModiPictureIntent(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivityForResult(intent, REQUEST_MODI_IMAGE_CAPTURE);
            overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
        }
    }

    // Take a Picture from gallery
    public void takeImageIntent(){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, REQUEST_MODI_IMAGE_LOAD);
        overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
    }

    public void onActivityResult(int requestCode, int resultCode,Intent data){
        if(resultCode == RESULT_OK){
            if(requestCode == REQUEST_MODI_IMAGE_CAPTURE){
                Bundle extras = data.getExtras();
                Bitmap bitmap = (Bitmap)extras.get("data");
                ivPoster.setImageBitmap(bitmap);
            }
            if(requestCode == REQUEST_MODI_IMAGE_LOAD){
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    ivPoster.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(BookModify.this, "Something went wrong", Toast.LENGTH_LONG).show();
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
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(galleryIntent, REQUEST_MODI_IMAGE_LOAD);
                    overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
                } else {
                    //do something like displaying a message that he didn`t allow the app to access gallery and you wont be able to let him select from gallery
                }
                break;
            case 12:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, REQUEST_MODI_IMAGE_CAPTURE);
                    overridePendingTransition(R.anim.anim_open_activity, R.anim.anim_close_activity);
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
