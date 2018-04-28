package com.example.android.drawingtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by yslilianm on 2018/4/19.
 */

public class GalleryActivity extends AppCompatActivity {

    public String drawingTitle;
    public String bitmap_str;
    public String time_str;
    public Drawing drawing;
    public ImageView drawingResult;
    public DatabaseReference parentNode;
    public DatabaseReference userRef;
    public FirebaseDatabase database;
    public FirebaseAuth mAuth;
    public FirebaseAuth.AuthStateListener authListener;
    public TextView tv_title;
    public TextView tv_timestamp;
    public Button bt_love;
    //Add drawing to the arraylist "drawinglist" will be update to recyclerView
    public ArrayList<Drawing> drawingList = new ArrayList<>();
    private GalleryAdapter galleryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
//        Intent intent = getIntent();
        //DataManager.readFromCloud();


        /** Get drawing info string and object from MainAcitivity
         */
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            setRecyclerView();
            return;
        }
        drawingTitle = extras.getString(Key.DRAWING_TITLE);
        Log.i("extras getString title", drawingTitle);

        bitmap_str = extras.getString(Key.DRAWING_CODE);
        Log.i("extras getString bmp", bitmap_str);

        time_str = extras.getString(Key.DRAWING_TIME);
        Log.i("extras getString time", time_str);

        drawing = (Drawing) extras.getSerializable(Key.DRAWING);
        Log.i("extras drawing check", drawing.getTitle());


        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(Key.USERTEST);


        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_timestamp = (TextView) findViewById(R.id.tv_timestamp);
        drawingResult = (ImageView) findViewById(R.id.iv_drawingResult);
        bt_love = (Button) findViewById(R.id.bt_love);

//        initialData();
//        addDrawing();
        setRecyclerView();


    }
    /**
     * Assign arrayList from MainActivity, creaye recycler view and send it to recyclerView
     */
    public void setRecyclerView(){
        RecyclerView rv_gallery = (RecyclerView) findViewById(R.id.rv_gallery);
        rv_gallery.setHasFixedSize(true);
        rv_gallery.setLayoutManager(new LinearLayoutManager(this));
        galleryAdapter = new GalleryAdapter(MainActivity.arrayL_drawing, this);
        Log.i("Gallery_drawingL", Integer.toString(MainActivity.arrayL_drawing.size()));

        rv_gallery.setAdapter(galleryAdapter);
    }

    /**
     * Initialize data for cardView
     */
//    public void initialData() {
//        //drawingInfo_arrayL = DataManager.drawingInfo_arrayL;
////        Log.i("Before drawingInfo_aray" ,Integer.toString(drawingInfo_arrayL.size()));
////        Log.i("After drawingInfo_aray" ,Integer.toString(drawingInfo_arrayL.size()));
//        //drawingList.add(new Drawing(drawingInfo_arrayL.get(Key.title), drawingInfo_arrayL.get(Key.bmp), drawingInfo_arrayL.get(Key.time)));
////        drawingList.add(new Drawing(drawingTitle,bitmap_str,time_str));
////        Log.i("1 means strings to lst" ,Integer.toString(drawingList.size()));
//        this.drawingList.add(drawing);
//        Log.i("1 means drawing to lst", Integer.toString(this.drawingList.size()));
//    }

    /**
     * Link menu directory to java
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    /**
     * Add functions to the menu options
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logout:
                logOut();
                // GalleryAdapter.notifyDataSetChanged();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Logout User account
     */
    public void logOut() {
        MainActivity.arrayL_drawing.clear();
        MainActivity.mAuth.signOut();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        Toast.makeText(GalleryActivity.this, getString(R.string.tst_success_logout), Toast.LENGTH_SHORT).show();
    }

//    public Drawing addDrawing() {
//        Drawing newDraw = new Drawing(drawingTitle, bitmap_str, time_str);
//        drawingList.add(newDraw);
//        Log.i(">1 means store to lst", Integer.toString(drawingList.size()));
//        return newDraw;
//    }
}


//    public Bitmap getScreenshotBmp() {
//
//
//        FileOutputStream fileOutputStream = null;
//
//        File path = Environment
//                .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//
//        String uniqueID = UUID.randomUUID().toString();
//
//        File file = new File(path, uniqueID + ".jpg");
//        try {
//            fileOutputStream = new FileOutputStream(file);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        drawingBmp.compress(Bitmap.CompressFormat.JPEG, 30, fileOutputStream);
//
//        try {
//            fileOutputStream.flush();
//            fileOutputStream.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return drawing_jpg;
//    }
//
//}
