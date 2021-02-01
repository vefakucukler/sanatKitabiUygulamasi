package com.vkyazilim.artbook;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    static ArrayList<Bitmap> artImage;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater =getMenuInflater();
        menuInflater.inflate(R.menu.add_art,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.add_note){
            Intent ıntent = new Intent(getApplicationContext(),Main2Activity.class);
            ıntent.putExtra("Bilgi","Yeni");
            startActivity(ıntent);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView =(ListView) findViewById(R.id.listView);
        final ArrayList<String> artName =new ArrayList<String>();
        artImage=new ArrayList<Bitmap>();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,artName);
        listView.setAdapter(arrayAdapter);
        try{
            Main2Activity.database= this.openOrCreateDatabase("Arts",MODE_PRIVATE,null);
            Main2Activity.database.execSQL("CREATE TABLE IF NOT EXISTS arts (name VARCHAR,image BLOB)");
            Cursor cursor = Main2Activity.database.rawQuery("SELECT * FROM arts",null);
            int nameIx = cursor.getColumnIndex("name");
            int imageIx = cursor.getColumnIndex("image");
            cursor.moveToFirst();
            while (cursor!=null){
                artName.add(cursor.getString(nameIx));
                byte[] bytes=cursor.getBlob(imageIx);
                Bitmap image = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
                artImage.add(image);
                cursor.moveToNext();
                arrayAdapter.notifyDataSetChanged();
            }

        }catch (Exception e ){
            e.printStackTrace();
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent ıntent =new Intent(getApplicationContext(),Main2Activity.class);
                ıntent.putExtra("Bilgi","Eski");
                ıntent.putExtra("name",artName.get(position));
                ıntent.putExtra("position",position);
                startActivity(ıntent);

            }
        });

    }
}
