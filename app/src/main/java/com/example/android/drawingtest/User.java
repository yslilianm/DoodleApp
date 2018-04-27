package com.example.android.drawingtest;

/**
 * Created by yslilianm on 2018/4/26.
 */

public class User {
    public String email;
    public String pass;
    public String name;
    public String gender;


    public User(String email, String pass, String name, String gender){
        this.email = email;
        this.pass = pass;
        this.name = name;
        this.gender = gender;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
