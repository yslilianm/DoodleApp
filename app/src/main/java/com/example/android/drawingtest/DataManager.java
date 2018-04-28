package com.example.android.drawingtest;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by yslilianm on 2018/4/20.
 */

/**
 * Note: This class currently is NOT used in the app
 */

public class DataManager {
//   public static ArrayList<ArrayList<String>> drawingInfos = new ArrayList<ArrayList<String>>();
public static ArrayList<String> drawingInfo_arrayL = new ArrayList<String>();

//   public static List<Drawing> drawingList;
    /**
     * Get data(bitmap, time, and the whole branch) from firebase
     */
    ////!!!Important: Think about the data structure and the efficiency of reading data

/*
    public static ArrayList<String> readFromCloud(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference parentNode = database.getReference(MainActivity.drawingTitle);
        Log.i("Reading drawingTitle" ,MainActivity.drawingTitle);

        parentNode.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    Map<String, Object> branch = (Map<String, Object>) postSnapshot.getValue();
                    String title;
                    String bitmap;
                    String timestamp;
                    if (branch.get(Key.CHILDNODE_TITLE)!= null){
                        title = (String) branch.get(Key.CHILDNODE_TITLE);
                        Log.i("Reading drawing title" ,title);

                    }

                    else{
                        title = "untitled";
                        Log.i("drawing title" ,"is null or undefined");
                    }
                    drawingInfo_arrayL.add(title);

                    if (branch.get(Key.CHILDNODE_BMP)!= null){
                        bitmap = (String) branch.get(Key.CHILDNODE_BMP);
                        Log.i("Reading drawing title" ,bitmap);
                    }
                    else{
                        bitmap = "noBitmap";
                        Log.i("drawing bitmap" ,"is null or undefined");
                    }
                    drawingInfo_arrayL.add(bitmap);


                    if (branch.get(Key.CHILDNODE_TIME)!= null){
                        timestamp = (String) branch.get(Key.CHILDNODE_TIME);
                        Log.i("Reading drawing title" ,timestamp);

                    }
                    else{
                        timestamp = "impossible to not have a time...?";
                        Log.i("drawing timestamp" ,"is null or undefined");
                    }
                    drawingInfo_arrayL.add(timestamp);
                    Log.i("Reading drawing bitmap" ,Integer.toString(drawingInfo_arrayL.size()));

                }
            }

//                collectDrawingInfo((Map<String, Object>) dataSnapshot.getValue());

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("The read failed: " ,databaseError.getMessage());
            }
        });
        return drawingInfo_arrayL;
    }
*/

    /**
     * Get data(bitmap, time, and the whole branch) from firebase
     */
    public static ArrayList<String> readFromCloud(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference parentNode = database.getReference(MainActivity.drawingTitle);
        parentNode.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                collectDrawingInfo((Map<String, Object>) dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }

        });
        return drawingInfo_arrayL;
    }

    private static void collectDrawingInfo(Map<String, Object> drawingInfo) {
        //iterate through each child
        for (Map.Entry<String, Object> entry : drawingInfo.entrySet()) {
            //Get string from child
            String info = (String) entry.getValue();
            //Append each child to list
            drawingInfo_arrayL.add(info);
            Log.i("Each Drawing Info:", info + Integer.toString(drawingInfo_arrayL.size()));

        }
    }
//    private static void collectDrawingInfo(Map<String, Object> drawingInfo) {
//        //iterate through each child
//        for (Map.Entry<String, Object> entry : drawingInfo.entrySet()) {
//            //Get string from child
//            String info = (String) entry.getValue();
//            //Append each child to list
//            drawingInfo_arrayL.add(info);
//            Log.i("collectDrawingInf Check", info);
//        }
//        Log.i("drawingInfos ckeck", Integer.toString(drawingInfo_arrayL.size()));
//
//    }
}
