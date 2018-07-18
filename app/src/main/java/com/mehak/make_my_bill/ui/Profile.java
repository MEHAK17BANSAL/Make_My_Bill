package com.mehak.make_my_bill.ui;

import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mehak.make_my_bill.Manifest;
import com.mehak.make_my_bill.R;

public class Profile extends AppCompatActivity {
    LinearLayout call;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String uid;
    void initViews() {
        call = findViewById(R.id.callpad);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             /*   Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:+919803462459"));
                if (ActivityCompat.checkSelfPermission(Profile.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                startActivity(intent);*/
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:+919803462459"));
                if(ActivityCompat.checkSelfPermission(Profile.this, android.Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(Profile.this, "Please on the phone permission", Toast.LENGTH_LONG).show();

                }
                else {
                    startActivity(intent);
                }

            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);
        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(101);
       // firestore = FirebaseFirestore.getInstance();
       // auth = FirebaseAuth.getInstance();
       // uid = auth.getCurrentUser().getUid();
        initViews();
    }

    public void about(View view) {
        Intent intent = new Intent(Profile.this,AboutAppActivity.class);
        startActivity(intent);
    }
}
