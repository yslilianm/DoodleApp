package com.example.android.drawingtest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.MotionEventCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Calendar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


/**
 * Created by yslilianm on 2018/4/10.
 */
//public class DrawingView extends View implements OnTouchListener{
public class DrawingView extends View {

    private static final String TAG = "AndroidBash";
    private DatabaseReference myRef;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    int action = 0;

    //Here
    String drawingCode;
    //Here
//    private ArrayList<Path> paths = new ArrayList<Path>();
//    private ArrayList<Path> undonePaths = new ArrayList<Path>();

    public ArrayList<String> drawingInfo_arrayL = new ArrayList<String>();


    public int width;
    public int height;
    public Bitmap mBitmap;
    private Path mPath;
    private Canvas mCanvas;
    private Paint mBitmapPaint;
    public Context context;
    private Paint mPaint;

    private Paint circlePaint;
    private Path circlePath;

    public DrawingView(Context c, AttributeSet attrs) {
        super(c, attrs);
//        setFocusable(true);
//        setFocusableInTouchMode(true);
//        this.setOnTouchListener(this);
        context = c;
        //Set a new Path
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

        //Parameters for the tracking circle
        circlePaint = new Paint();
        circlePath = new Path();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.GRAY);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeJoin(Paint.Join.MITER);
        circlePaint.setStrokeWidth(3f);

        //Parameters of the paint brush
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(8);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, mBitmapPaint);
//        for (Path p : paths){
//            canvas.drawPath(p, mPaint);
//            Log.i("path loop", Integer.toString(paths.size()));
//
//        }
        canvas.drawPath(mPath, mPaint);
        canvas.drawPath(circlePath, circlePaint);
    }

    private float mX, mY;
    private static final float TOUCH_TOLERANCE = 4;

    private void touch_start(float x, float y) {
//        undonePaths.clear();
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;

            circlePath.reset();
            circlePath.addCircle(mX, mY, 30, Path.Direction.CW);
        }
    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        circlePath.reset();
        // commit the path to our offscreen
        mCanvas.drawPath(mPath, mPaint);
        // kill this so we don't double draw
//        paths.add(mPath);
        mPath = new Path();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//  public boolean onTouch(View arg0, MotionEvent event) {


        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.i("onTouchEvent", "Action was DOWN");

                touch_start(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                Log.i("onTouchEvent", "Action was MOVE");
                touch_move(x, y);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                Log.i("onTouchEvent", "Action was UP");
                action++;
                Log.i("onTouchEvent", Integer.toString(action));
                touch_up();
                invalidate();
                break;
        }
        return true;
    }

    /**
     * Erase bitmap with a layer of transparent color
     */
    public void clearCanvas() {
        mPath.reset();
        invalidate();
        //Set a new background color as transparent
        mBitmap.eraseColor(Color.TRANSPARENT);
        //mCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        Log.v("CLEARALL!!", "YES");

    }

    /**
     * Save Bitmap into String
     *
     * @param bitmapImage
     * @return drawingCode as String
     */
    public String saveBitmapToString(Bitmap bitmapImage) {
        drawingCode = ImageUtil.bitmapToByteString(bitmapImage);
        return drawingCode;
    }

//    /**
//     * Writing data (drawing information) to firebase database
//     */
//    public ArrayList<String> writeToCloud() {
//        drawingCode = saveBitmapToString(mBitmap);
//        //Create a reference, a parent node
//        myRef = database.getReference(MainActivity.drawingTitle);
//
//        //Create a child node
//        drawingInfo_arrayL.add(MainActivity.drawingTitle);
//        myRef.child(Key.CHILDNODE_TITLE).setValue(MainActivity.drawingTitle);
//        Log.i("writeToCloud checking", "send title to firebase:" + MainActivity.drawingTitle);
//
//        myRef.child(Key.CHILDNODE_BMP).setValue(drawingCode);
//        Log.i("writeToCloud checking", "send bitmap to firebase:" + drawingCode);
//        drawingInfo_arrayL.add(drawingCode);
//
//        myRef.child(Key.CHILDNODE_TIME).setValue(getDate());
//        Log.i("writeToCloud checking", "send timestamp to firebase:" + getDate());
//        drawingInfo_arrayL.add(getDate());
//
//        return drawingInfo_arrayL;
//    }

    /**
     * Get the timestamp when User publish the drawing
     */
    public String getDate() {
        TimeZone.getDefault();
        //Get today's date
        Date currentTime = Calendar.getInstance().getTime();
        //Create a date "formatter"
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss");
        //create a new String using the date format
        return formatter.format(currentTime);
    }


//    public void undo () {
//        if (paths.size()>0)
//        {
//            undonePaths.add(paths.remove(paths.size()-1));
//            invalidate();
//            Log.i("undonePath.add","yes");
//        }
//
//        Toast.makeText(context, "Undo", Toast.LENGTH_SHORT).show();
//    }
//
//    public void redo (){
//        if (undonePaths.size()>0)
//        {
//            paths.add(undonePaths.remove(undonePaths.size()-1));
//            invalidate();
//            Log.i("paths.add","yes");
//
//        }
//        Toast.makeText(context, "Redo", Toast.LENGTH_SHORT).show();
////toast the User
//    }

}



