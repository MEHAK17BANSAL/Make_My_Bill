package com.mehak.make_my_bill.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mehak.make_my_bill.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PasswordActivity extends AppCompatActivity {

    @BindView(R.id.etPasswordEmail)
    EditText passwordEmail;

    @BindView(R.id.btnPasswordReset)
    Button resetPassword;

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;

   // Intent rcv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        ButterKnife.bind(this);
     // String email=  rcv.getStringExtra("key");
     // Log.i("passemail","  "+email);
     // passwordEmail.setText(email);
        firebaseAuth=FirebaseAuth.getInstance();
        resetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String useremail = passwordEmail.getText().toString().trim();

                if(useremail.equals("")){
                    Toast.makeText(PasswordActivity.this,"please enter your registered email id",Toast.LENGTH_LONG).show();

                }else {
                   firebaseAuth.sendPasswordResetEmail(useremail).addOnCompleteListener(new OnCompleteListener<Void>() {
                       @Override
                       public void onComplete(@NonNull Task<Void> task) {
                           if(task.isSuccessful()){
                               Toast.makeText(PasswordActivity.this,"password reset email sent!",Toast.LENGTH_LONG).show();
                               finish();
                               startActivity(new Intent(PasswordActivity.this,LogInActivity.class));
                           }
                           else {
                               Toast.makeText(PasswordActivity.this,"error in sending password reset email",Toast.LENGTH_LONG).show();
                           }
                       }
                   }) ;
                }
            }
        });
    }
}
