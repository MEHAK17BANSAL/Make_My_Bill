package com.mehak.make_my_bill.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mehak.make_my_bill.R;
import com.mehak.make_my_bill.model.User;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{
    RelativeLayout layout1,layout2,layout3,layout4;
    EditText username,email,password,repassword;
    Button signup,login;
    boolean checked;
    FirebaseAuth auth;
    User user;
int i=0;
    FirebaseFirestore firestore;
    CollectionReference userCollection;

    ProgressDialog progressDialog;

    String uid;
    public int counter=5;

    void initviews()
    {
        layout1=findViewById(R.id.relativeLayout1);
        username=findViewById(R.id.imgView_username);
        layout2=findViewById(R.id.relativeLayout2);
        email=findViewById(R.id.imgView_email);
        layout3=findViewById(R.id.relativeLayout3);
        password=findViewById(R.id.imgView_password);
        layout4=findViewById(R.id.relativeLayout4);
        repassword=findViewById(R.id.imgView_repassword);
        signup=findViewById(R.id.btn_signUp);
        login=findViewById(R.id.alreadylogin);

        //Toast.makeText(SignUpActivity.this,username.getText().toString(),Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
       initviews();
        signup.setOnClickListener(this);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userCollection = firestore.collection("users");
        user = new User();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);


    }

    void registerUser(){

        progressDialog.show();

        auth.createUserWithEmailAndPassword(user.email,user.password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            uid = task.getResult().getUser().getUid();
                            // uid can be saved in the Firebase Database along with other details of user

                            Toast.makeText(SignUpActivity.this,user.name+" Registered Successfully !!",Toast.LENGTH_LONG).show();
                            Log.i("User","User Registered: "+uid);

                            saveUser();
                        }

                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(this,new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("User","User Registration Failed: "+e.getMessage());
                e.printStackTrace();
                progressDialog.dismiss();
            }
        });

    }

    void saveUser() {

        userCollection.document(uid).set(user).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(SignUpActivity.this, "User Saved in DB", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
                progressDialog.dismiss();
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUpActivity.this, "Error while saving User", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        String e = username.getText().toString();
        String n = username.getText().toString();
        String pass = password.getText().toString();
        String repass = repassword.getText().toString();
        user.name = username.getText().toString().trim();
        user.email = email.getText().toString().trim();
        user.password = password.getText().toString().trim();
        user.repass = repassword.getText().toString().trim();
        if (validateFields()) {
            if(user.password.length()>=6 && user.repass.length()>=6){
            registerUser();

            }
            else {
                Toast.makeText(this, "password length more than 6" +
                        "", Toast.LENGTH_LONG).show();
            }
        }
        else {

            Toast.makeText(this, "Please Fill All Details & On Permissions ", Toast.LENGTH_LONG).show();
        }

    }

    public void loginhere(View view) {

                Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
                startActivity(intent);
            }


    public void checkbox(View view) {
         checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.chkBox1:
                if (checked) {

                   Toast.makeText(SignUpActivity.this,"storage permission on",Toast.LENGTH_LONG).show();
                   i++;
                }
            else
                    Toast.makeText(SignUpActivity.this,"please on the storage permission",Toast.LENGTH_LONG).show();
                break;


        }
    }
    boolean validateFields(){
        boolean flag = true;

        if(user.email.isEmpty()){
            flag = false;
        }
        if(user.password.isEmpty() && user.password.length()>=6){
            flag = false;

        }
        if(user.repass.isEmpty() && user.repass.length()>=6){
            flag = false;

        }
        if(user.password.equals(user.repass)){
            flag = true;
        }
        else {
            flag=false;
        }
        if(user.name.isEmpty()){
            flag = false;
        }
        if(checked==false){
            flag = false;
        }
        return flag;
    }
}
