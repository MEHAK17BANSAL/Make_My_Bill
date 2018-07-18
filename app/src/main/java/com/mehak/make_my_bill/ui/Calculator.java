package com.mehak.make_my_bill.ui;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mehak.make_my_bill.R;

import java.io.File;


public class Calculator extends AppCompatActivity {

    private Button zero,one,two,three,four,five,six,seven,eight,nine,add,sub,mul,div,equal,clear;
    private TextView info,result;
    private final char ADDITION = '+';
    private final char SUBTRATION='-';
    private final char MULTIPLICATION='*';
    private final char DIVISION='/';
    private final char EQU=0;
    private double val1 = Double.NaN;
    private double val2;
    private char ACTION;

    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String uid;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calculator);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        setupUIViews();




        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setText(info.getText().toString()+"0");
            }
        });
        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setText(info.getText().toString()+"1");
            }
        });
        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setText(info.getText().toString()+"2");
            }
        });
        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setText(info.getText().toString()+"3");
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setText(info.getText().toString()+"4");
            }
        });
        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setText(info.getText().toString()+"5");
            }
        });
        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setText(info.getText().toString()+"6");
            }
        });
        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setText(info.getText().toString()+"7");
            }
        });
        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setText(info.getText().toString()+"8");
            }
        });
        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                info.setText(info.getText().toString()+"9");
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(info.getText().toString().isEmpty()){

                }else {
                    compute();
                    ACTION = ADDITION;
                    result.setText(String.valueOf(val1)+"+");
                    info.setText(null);
                }}
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(info.getText().toString().isEmpty()){

                }else {
                    compute();
                    ACTION = SUBTRATION;
                    result.setText(String.valueOf(val1)+"-");
                    info.setText(null);
                }}
        });
        mul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(info.getText().toString().isEmpty()){

                }else {
                    compute();
                    ACTION = MULTIPLICATION;
                    result.setText(String.valueOf(val1)+"*");
                    info.setText(null);
                }}
        });
        div.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(info.getText().toString().isEmpty()){

                }else {
                    compute();
                    ACTION = DIVISION;
                    result.setText(String.valueOf(val1)+"/");
                    info.setText(null);
                }}
        });

        equal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(info.getText().toString().isEmpty() || result.getText().toString().isEmpty()) {

                }else {
                    compute();
                    ACTION = EQU;
                    result.setText(result.getText().toString()+ String.valueOf(val2)+"="+String.valueOf(val1));
                    //5+4=9;
                    info.setText(null);
                }}
        });
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info.getText().length()>0){
                    CharSequence name = info.getText().toString();
                    info.setText(name.subSequence(0,name.length()-1));
                }
                else {
                    val1 = Double.NaN;
                    val2 = Double.NaN;
                    info.setText(null);
                    result.setText(null);
                }
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_in_cal, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.id_logout:

                auth.signOut();

                Intent intent = new Intent(Calculator.this,LogInActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.id_contact:

                Intent intent1 = new Intent(Calculator.this,Profile.class);
                startActivity(intent1);
              //  finish();
                break;


        }
        return true;
    }


    private void setupUIViews(){
        one = (Button)findViewById(R.id.btn1);
        zero = (Button)findViewById(R.id.btn0);
        two = (Button)findViewById(R.id.btn2);
        three = (Button)findViewById(R.id.btn3);
        four = (Button)findViewById(R.id.btn4);
        five = (Button)findViewById(R.id.btn5);
        six = (Button)findViewById(R.id.btn6);
        seven = (Button)findViewById(R.id.btn7);
        eight = (Button)findViewById(R.id.btn8);
        nine = (Button)findViewById(R.id.btn9);
        add = (Button)findViewById(R.id.btnadd);
        sub = (Button)findViewById(R.id.btnsub);
        mul = (Button)findViewById(R.id.btnmul);
        div = (Button)findViewById(R.id.btndivide);
        info = (TextView)findViewById(R.id.tvControl);
        result = (TextView)findViewById(R.id.tvResult);
        equal = (Button)findViewById(R.id.btnequal);
        clear = (Button)findViewById(R.id.btnclear);
    }
    private void compute(){
        if(!Double.isNaN(val1)){
            val2 = Double.parseDouble(info.getText().toString());

            switch (ACTION){
                case ADDITION:
                    val1=val1+val2;
                    break;
                case SUBTRATION:
                    val1=val1-val2;
                    break;
                case MULTIPLICATION:
                    val1=val1*val2;
                    break;
                case DIVISION:
                    val1=val1/val2;
                    break;
                case EQU:
                    break;
            }

        }
        else {
            val1 = Double.parseDouble(info.getText().toString());
        }
    }
}
