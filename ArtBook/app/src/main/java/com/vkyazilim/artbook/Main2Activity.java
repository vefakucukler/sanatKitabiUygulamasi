package com.vkyazilim.artbook;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Main2Activity extends AppCompatActivity {

    ImageView ımageView;
    EditText editText;
    static SQLiteDatabase database;
    Bitmap selectedImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        ımageView=(ImageView) findViewById(R.id.imageView);
        editText=(EditText) findViewById(R.id.editText);
        Button button = (Button) findViewById(R.id.button1);
        Intent ıntent =getIntent();
        String bilgi = ıntent.getStringExtra("Bilgi");
        if(bilgi.equalsIgnoreCase("new")){
            Bitmap background = BitmapFactory.decodeResource(getApplicationContext().getResources(),R.drawable.ic_launcher_background);
            ımageView.setImageBitmap(background);
            button.setVisibility(View.VISIBLE);
            editText.setText("");
        }
        else {
            String name=ıntent.getStringExtra("name");
            editText.setText(name);
            int position = ıntent.getIntExtra("position",0);
            ımageView.setImageBitmap(MainActivity.artImage.get(position));
            button.setVisibility(View.INVISIBLE);
        }
        }
        public void save(View view){
            String isim = editText.getText().toString();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            selectedImage.compress(Bitmap.CompressFormat.PNG,50,outputStream);
            byte[] bytes=outputStream.toByteArray();
            try{
                database=this.openOrCreateDatabase("Arts",MODE_PRIVATE,null);
                database.execSQL("CREATE TABLE IF NOT EXISTS arts (name VARCHAR,image BLOB)");
                String sqlString = "INSERT INTO arts (name,image) VALUES (?,?)";
                SQLiteStatement statement = database.compileStatement(sqlString);
                statement.bindString(1,isim);
                statement.bindBlob(2,bytes);
                statement.execute();
            }catch (Exception e){
                e.printStackTrace();
            }
            Intent ıntent= new Intent(getApplicationContext(),MainActivity.class);
            startActivity(ıntent);
        }
    @TargetApi(Build.VERSION_CODES.M)
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void select (View view){

        if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
        }
        else{
            Intent ıntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(ıntent,1);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==2){
            if(grantResults.length>0&& grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent ıntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(ıntent,1);
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            Uri image=data.getData();
            try {
                selectedImage=MediaStore.Images.Media.getBitmap(this.getContentResolver(),image);
                ımageView.setImageBitmap(selectedImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
