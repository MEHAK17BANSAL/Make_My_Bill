package com.mehak.make_my_bill.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mehak.make_my_bill.R;

public class Button extends AppCompatActivity {
FloatingActionButton fabRecord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.button);
        fabRecord=findViewById(R.id.fabrecordbutton);
    }

    public void fab(View view) {
        startActivity(new Intent(Button.this,RecordAudioActivity.class));
    }
}
