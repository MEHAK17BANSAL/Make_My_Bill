package com.mehak.make_my_bill.model;

public class User {


    public String name;
    public String email;
    public  String password;
    public String repass;

    public User()
    {

    }
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }
}