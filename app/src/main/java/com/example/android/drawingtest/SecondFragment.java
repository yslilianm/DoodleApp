package com.example.android.drawingtest;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by yslilianm on 2018/4/25.
 */

public class SecondFragment extends Fragment {
    private static final String TAG = "FirstFragment";
    private EditText et_email;
    private EditText et_pass;
    private Button bt_login;
    private FirebaseAuth mAuth;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.first_login_activity, container, false);

        mAuth = FirebaseAuth.getInstance();

        et_email = (EditText) view.findViewById(R.id.et_email);
        et_pass = (EditText) view.findViewById(R.id.et_password);

        bt_login = (Button) view.findViewById(R.id.bt_login);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
            }
        });

        return view;
    }

    /**
     * Check if email and password is empty, if something: Login with User's input
     *
     */
    public void logIn() {
        String email = et_email.getText().toString();
        Log.d(TAG, email);
        String password = et_pass.getText().toString();
        Log.d(TAG, password);
        //Check if email field is empty
        if (email.isEmpty()) {
            Toast.makeText(getActivity(), getString(R.string.tst_empty_email), Toast.LENGTH_SHORT).show();
        } else {
            //Check if email field has "@"
            if (!email.contains("@")) {
                Toast.makeText(getActivity(), getString(R.string.tst_bad_email), Toast.LENGTH_SHORT).show();
            } else {
                //Check if password field is empty
                if (password.isEmpty()) {
                    Toast.makeText(getActivity(), getString(R.string.tst_empty_password), Toast.LENGTH_SHORT).show();
                } else {
                    //sign in with the given email and password
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(getActivity(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getActivity(), task.getResult().getUser().getEmail() + getString(R.string.tst_success_login),
                                                Toast.LENGTH_SHORT).show();
                                        getActivity().finish();
                                    }
                                }
                            });
                }

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

