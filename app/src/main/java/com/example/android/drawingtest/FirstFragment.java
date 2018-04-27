package com.example.android.drawingtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.support.v4.app.Fragment;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Text;

/**
 * Created by yslilianm on 2018/4/25.
 */

public class FirstFragment extends Fragment {
    private static final String TAG = "FirstFragment";
    private EditText et_email;
    private EditText et_pass;
    private EditText et_name;
    private FirebaseAuth mAuth;
    private boolean isSkip = false;
    private Button bt_signup;
    private Button bt_skip;
    private Spinner spn_gender;
    private FirebaseFirestore db;

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.first_signup_activity,container,false);
            mAuth = FirebaseAuth.getInstance();
            db = FirebaseFirestore.getInstance();

            et_email = (EditText) view.findViewById(R.id.et_email);
            et_pass = (EditText) view.findViewById(R.id.et_password);
            et_name = (EditText) view.findViewById(R.id.et_name);

            spn_gender = (Spinner) view.findViewById(R.id.spn_gender);
            // Create an ArrayAdapter using the string array and a default spinner layout
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(), R.array.gender_array, android.R.layout.simple_spinner_item);
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            // Apply the adapter to the spinner
            spn_gender.setAdapter(adapter);

            bt_signup = (Button) view.findViewById(R.id.bt_signup);
            bt_signup.setOnClickListener(new ButtonClick());

            bt_skip = (Button) view.findViewById(R.id.bt_skip);
            bt_skip.setOnClickListener(new ButtonClick());

            /**
             * Check if new users to skip registering at the first time, if skipped: change the xml without skip button
             */
            if (MainActivity.isSkipLogin){
                bt_skip.setVisibility(View.GONE);
            }

            return view;
        }

    /**
     * Allow new users to skip the register process at the first time
     */

    public void skip(){
        isSkip = true;
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.putExtra(Key.SKIPLOGIN, isSkip);
        startActivity(intent);
    }


    /**
     * Check if email and password is empty, if something: Register a new account with User's input
     */
//Important takeaway:
//When working in fragment, replace "this" with "getActivity()"!!
    public void signUp() {
        String email = et_email.getText().toString();
        Log.d(TAG, email);
        String password = et_pass.getText().toString();
        Log.d(TAG, password);
        String name = et_name.getText().toString();
        Log.d(TAG, name);
        String gender = spn_gender.getSelectedItem().toString();

        //Check if email field is empty
        if (email.isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.tst_empty_email), Toast.LENGTH_SHORT).show();
        }
        else{
            //Check if email field has "@"
            if(!email.contains("@")){
                Toast.makeText(getActivity(), getString(R.string.tst_bad_email), Toast.LENGTH_SHORT).show();
            }
            else{
                //Check if password field is empty
                if (password.isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.tst_empty_password), Toast.LENGTH_SHORT).show();
                }
                //Check if name field is empty
                else if (name.isEmpty()){
                    Toast.makeText(getActivity(), getString(R.string.tst_empty_name), Toast.LENGTH_SHORT).show();
                }
                else{
                    //Create an account with the given email and password
                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                        Log.e("Error signup", task.getException().toString());
                                    }
                                    else {
                                        Toast.makeText(getActivity(), task.getResult().getUser().getEmail() + getString(R.string.tst_success_signup),
                                                Toast.LENGTH_SHORT).show();
                                        getActivity().finish();
                                    }
                                }
                            });
                    User user = new User(email, password, name, gender);
                    Log.i("User create check:", user.getEmail());
                    writeUserToCloud(user);
                }
            }
        }
        }

    public void writeUserToCloud(User user) {
        db.collection("user").add(user).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

    }


    class ButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.bt_signup:
                    signUp();
                    break;
                case R.id.bt_skip:
                    skip();
                    break;
            }
        }
    }
    }
//    // Store instance variables
//    private String title;
//    private int page;
//
//    // newInstance constructor for creating fragment with arguments
//    public static FirstFragment newInstance(int page, String title) {
//        FirstFragment fragmentFirst = new FirstFragment();
//        Bundle args = new Bundle();
//        args.putInt("someInt", page);
//        args.putString("someTitle", title);
//        fragmentFirst.setArguments(args);
//        return fragmentFirst;
//    }
//
//    // Store instance variables based on arguments passed
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        page = getArguments().getInt("someInt", 0);
//        title = getArguments().getString("someTitle");
//    }
//
//    // Inflate the view for the fragment based on layout XML
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_first, container, false);
//        TextView tvLabel = (TextView) view.findViewById(R.id.tvLabel);
//        tvLabel.setText(page + " -- " + title);
//        return view;
//    }

