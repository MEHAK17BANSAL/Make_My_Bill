package com.mehak.make_my_bill.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mehak.make_my_bill.R;
import com.mehak.make_my_bill.model.User;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LogInActivity extends AppCompatActivity {
    @BindView(R.id.editTextuname)
    EditText eTxtName;
    @BindView(R.id.editTextpass)
    EditText eTxtPass;
    @BindView(R.id.textViewattempt)
    TextView attempts;
    @BindView(R.id.textViewphonelogin)
    TextView PhoneLogin;
    @BindView(R.id.cardView)
    CardView cview;
    FirebaseAuth auth;
    User user;
    FirebaseFirestore firestore;
    CollectionReference userCollection;
    ProgressDialog progressDialog;
    String uid;
    public int counter=5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        ButterKnife.bind(this);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userCollection = firestore.collection("users");
        user = new User();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

    }

    public void forgotpass(View view) {
        Intent intent=new Intent(LogInActivity.this,PasswordActivity.class);
       // intent.putExtra("key",eTxtName.getText().toString());
       // Log.i("intent login"," "+eTxtName.getText().toString());
        startActivity(intent);
       // startActivity(new Intent(LogInActivity.this,PasswordActivity.class));

    }
    public void PHONELOGIN(View view) {
        startActivity(new Intent(LogInActivity.this,Phone_Login.class));
    }

    public void registerhere(View view) {
        startActivity(new Intent(LogInActivity.this,SignUpActivity.class));
    }

    public void loginUser(View view) {
        String e = eTxtName.getText().toString();
        String p = eTxtPass.getText().toString();

        if (e.isEmpty() && p.isEmpty()) {
            Toast.makeText(this, "Please Fill Email & Password", Toast.LENGTH_LONG).show();
        }
        else {

            user.email = eTxtName.getText().toString().trim();
            user.password = eTxtPass.getText().toString().trim();

            progressDialog.show();
            auth.signInWithEmailAndPassword(user.email, user.password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                Toast.makeText(LogInActivity.this, user.email + " Login Success !!", Toast.LENGTH_LONG).show();
                                Log.i("User", "login success: ");
                                Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                progressDialog.dismiss();
                            }
                        }
                    })
                    .addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i("User", "User Login Failed: " + e.getMessage());
                            e.printStackTrace();
                            counter--;
                            attempts.setText("NO OF ATTEMPTS REMANING:" + String.valueOf(counter));
                            if (counter == 0) {
                                cview.setEnabled(false);
                                Toast.makeText(LogInActivity.this, " oops !! ", Toast.LENGTH_LONG).show();
                            }
                            progressDialog.dismiss();
                        }
                    });
        }
    }

    boolean validateFields(){
        boolean flag = true;

        if(user.email.isEmpty()){
            flag = false;
        }
        if(user.password.isEmpty()){
            flag = false;
        }else {
            if(user.password.length()>=6){
                flag=false;
            }
        }
        return flag;
    }

}
