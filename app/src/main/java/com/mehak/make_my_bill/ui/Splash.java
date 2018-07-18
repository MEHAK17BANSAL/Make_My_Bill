package com.mehak.make_my_bill.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mehak.make_my_bill.R;

public class Splash extends AppCompatActivity {
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if (firebaseUser != null) {
            handler.sendEmptyMessageDelayed(201, 2500);
        } else {
            handler.sendEmptyMessageDelayed(101, 2500);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Intent intent = null;
            if (msg.what == 101) {
                intent = new Intent(Splash.this, LogInActivity.class);
            } else {
                intent = new Intent(Splash.this, MainActivity.class);
            }

            startActivity(intent);
            finish();

        }
    };
}
