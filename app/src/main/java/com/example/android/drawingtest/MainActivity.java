package com.example.android.drawingtest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static Context contextOfApplication;
    static String drawingTitle;
    static String currentDrawingCode;
    static String drawingTime;
    static boolean isSkipLogin = false;
    static FirebaseAuth mAuth;
    static ArrayList<Drawing> arrayL_drawing = new ArrayList<Drawing>();
    public EditText et_drawTitle;
    public boolean isClicked = false;
    public DatabaseReference userRef;
    public FirebaseDatabase database;
    public FirebaseUser user;
    public FirebaseAuth.AuthStateListener authListener;
    public Drawing drawing;
    public FirebaseFirestore db;
    private DrawingView customCanvas;

    public static Context getContextOfApplication() {
        return contextOfApplication;
    }

    /**
     * Here is a bug to be solved related to orientation data lost (Bitmap)
     */
//    @Override
//    public void onSaveInstanceState(Bundle toSave) {
//        super.onSaveInstanceState(toSave);
//        toSave.putParcelable("bitmap", customCanvas.mBitmap);
//        Log.i("onSaveInstanceState", customCanvas.saveBitmapToString(customCanvas.mBitmap));
//    }
///**
// * Save it onSaveInstanceState
// */


//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        outState.putParcelable("obj", Drawing);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        MyParcelable = savedInstanceState.getParcelable("obj");
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        db = FirebaseFirestore.getInstance();
        database = FirebaseDatabase.getInstance();
        userRef = database.getReference(Key.USERTEST);
//        int size = writeToCloud().size();
//        Log.i("wrt to cld 1 is sucess", Integer.toString(size));

/**
 * Check if logged in
 */
        mAuth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    userRef = database.getReference(user.getUid());
                }
                //If the User hasn't login, direct the page to login.
                else {
                    startActivity(new Intent(MainActivity.this, TestLoginActivity.class));
                }
            }
        };


        super.onCreate(savedInstanceState);
//        /**
//         * Retrieve state onCreate
//         */
//        if (savedInstanceState != null) customCanvas.mBitmap = savedInstanceState.getParcelable("bitmap");

        //dv = new DrawingView(this);
        setContentView(R.layout.activity_main);
        //Disable the focus
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        customCanvas = findViewById(R.id.canvas);

        Button bt_clearAll = (Button) findViewById(R.id.bt_clearAll);
        bt_clearAll.setOnClickListener(new ButtonClick());

/*        At this point, we didn't have functions to read global database
        therefore, the button publish serves as reading local database*/

//        Button bt_save = (Button) findViewById(R.id.bt_save);
//        bt_save.setOnClickListener(new ButtonClick());

        Button bt_share = (Button) findViewById(R.id.bt_publish);
        bt_share.setOnClickListener(new ButtonClick());

        et_drawTitle = (EditText) findViewById(R.id.et_drawTitle);

        ImageButton ib_greenPaint = (ImageButton) findViewById(R.id.ib_greenPaint);
        ib_greenPaint.setOnClickListener(new ButtonClick());
        ImageButton ib_bluePaint = (ImageButton) findViewById(R.id.ib_bluePaint);
        ib_bluePaint.setOnClickListener(new ButtonClick());
        ImageButton ib_pinkPaint = (ImageButton) findViewById(R.id.ib_pinkPaint);
        ib_pinkPaint.setOnClickListener(new ButtonClick());
        ImageButton ib_blackPaint = (ImageButton) findViewById(R.id.ib_blackPaint);
        ib_blackPaint.setOnClickListener(new ButtonClick());

        /**
         * Set stroke width by getting user's input through seekbar
         */
        SeekBar skb_stroke = (SeekBar) findViewById(R.id.skb_stroke);
        skb_stroke.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangeValue = Key.default_strokeWidth;

            //Get the value when the bar is changed
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                progressChangeValue = i;
                Log.i("MainAct_seekbar", Integer.toString(i));
            }

            //When the user start touching the screen
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            //When the user stop touching the screen
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                customCanvas.setStrokeWidth(progressChangeValue);

                Toast.makeText(MainActivity.this, getString(R.string.tst_change_stroke) + " " + progressChangeValue,
                        Toast.LENGTH_SHORT).show();
            }
        });

        contextOfApplication = getApplicationContext();

        /** Get data from LoginAcitivity
         */
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        isSkipLogin = extras.getBoolean(Key.SKIPLOGIN);
        Log.i("Does User skip login?", Boolean.toString(isSkipLogin));


    }

    /**
     * Waive prompting register page if User clicked "skip"
     */
    @Override
    public void onStart() {
        super.onStart();
        if (!isSkipLogin) {
            mAuth.addAuthStateListener(authListener);
        }

//        // Check if User is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authListener);
    }

    /**
     * Calling clearCanvas function from drawingView
     */
    public void clearCanvas() {
        customCanvas.clearCanvas();
    }

    /**
     * Calling set new color to the paint function from drawingView
     */
    public void setColor(int colorId) {
        customCanvas.setColor(colorId);
    }

    /**
     * Prevent empty title and check whether User logins before writing to the cloud
     */
    public void saveCanvas() {
        //Get data from User input
        drawingTitle = et_drawTitle.getText().toString();
        //Get timestamp
        drawingTime = customCanvas.getDate();
        //Save the string code of bitmap into an array
        currentDrawingCode = customCanvas.saveBitmapToString(customCanvas.mBitmap);


        if (drawingTitle.isEmpty()) {
            Toast.makeText(MainActivity.this, getString(R.string.tst_empty_title), Toast.LENGTH_SHORT).show();
        } else {
            if (user != null) {
                writeToCloud();
                Toast.makeText(MainActivity.this, getString(R.string.tst_success_save), Toast.LENGTH_SHORT).show();
            } else {
                mAuth.addAuthStateListener(authListener);
            }
        }

    }

    /**
     * Check empty title then User login
     * before saving and sending drawing data and object to cloud and GalleryaActivity
     */
    public void sentInfoToIntent() {
        saveCanvas();
        if (!drawingTitle.isEmpty()) {
            //If User login, sent intent to gallerytActivity
            if (user != null) {
//              Toast.makeText(MainActivity.this, getString(R.string.success_publish), Toast.LENGTH_SHORT).show();
//              ArrayList<String> drawingInfo_arrayL = customCanvas.drawingInfo_arrayL;
                Intent intent = new Intent(this, GalleryActivity.class);
                intent.putExtra(Key.DRAWING_TITLE, drawingTitle);
                Log.i("intent putExtra title", drawingTitle);
                intent.putExtra(Key.DRAWING_CODE, currentDrawingCode);
                Log.i("intent putExtra bmp", currentDrawingCode);
                intent.putExtra(Key.DRAWING_TIME, drawingTime);
                Log.i("intent putExtra time", drawingTime);
                intent.putExtra(Key.DRAWING, drawing);
//              Log.i("Intent: ", drawingTitle);
                startActivity(intent);
                //If User is not login, direct to loginActivity
            } else {
                mAuth.addAuthStateListener(authListener);
            }
        } else {
            Toast.makeText(MainActivity.this, getString(R.string.tst_empty_title), Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Writing drawing object to firebase database
     * Add drawing to ArrayList
     */
    public void writeToCloud() {
        //Create a reference, a parent node
        Log.i("writeToCloud Check",  "Title: " + drawingTitle + "Drawing Code: " + currentDrawingCode + "Drawing Time:" + drawingTime);
        drawing = new Drawing(drawingTitle, currentDrawingCode, drawingTime);
        arrayL_drawing.add(0,drawing);
        Log.i("MainActivity_Array", Integer.toString(arrayL_drawing.size()));
        userRef.push().setValue(drawing);
    }

    public void viewGallery(){
        Intent intent = new Intent(this, GalleryActivity.class);
        startActivity(intent);
    }

    /**
     * Link menu directory to java
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_gallery, menu);

        return true;
    }

    /**
     * Add view gallary function to the menu options
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.gallery:
                viewGallery();

                // GalleryAdapter.notifyDataSetChanged();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    class ButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bt_clearAll:
                    clearCanvas();
                    break;
//                case R.id.bt_save:
//                    saveCanvas();
//                    break;
                case R.id.bt_publish:
                    sentInfoToIntent();
                    break;
                case R.id.ib_greenPaint:
                    setColor(getResources().getColor(R.color.colorAccent));
                    break;
                case R.id.ib_bluePaint:
                    setColor(getResources().getColor(R.color.brush_blue));
                    break;
                case R.id.ib_pinkPaint:
                    setColor(getResources().getColor(R.color.brush_pink));
                    break;
                case R.id.ib_blackPaint:
                    setColor(getResources().getColor(R.color.secondaryTextColor));
                    break;
            }
        }
    }

}
