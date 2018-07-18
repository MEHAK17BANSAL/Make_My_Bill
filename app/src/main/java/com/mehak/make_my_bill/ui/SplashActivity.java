package com.mehak.make_my_bill.ui;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mehak.make_my_bill.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth auth;
String  date= new SimpleDateFormat("dd-mm-yyyy hh:mm:ss").format(Calendar.getInstance().getTime());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        auth = FirebaseAuth.getInstance();

        FirebaseUser firebaseUser = auth.getCurrentUser();
        //firebaseUser.getUid();

        if(firebaseUser != null){
            //Toast.makeText(this,"User Exists "+firebaseUser.getUid(),Toast.LENGTH_LONG).show();
            handler.sendEmptyMessageDelayed(201,1500);
        }else{
            //Toast.makeText(this,"User Does Not Exists ",Toast.LENGTH_LONG).show();
            handler.sendEmptyMessageDelayed(101,1500);
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {

            Intent intent = null;

            if(msg.what == 101){
                showNotification();
                intent = new Intent(SplashActivity.this,LogInActivity.class);
            }else{
                intent = new Intent(SplashActivity.this,MainActivity.class);
            }

            startActivity(intent);
            finish();
        }
    };

    void showNotification(){

        NotificationManager notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        String chId = "myChannelId";

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(chId,"MyChannel",NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,chId);
        builder.setSmallIcon(android.R.drawable.ic_menu_add);
        builder.setContentTitle(" Welcome In Make My Bill");
        builder.setContentText("  "+date);

       // Intent intent = new Intent(SplashActivity.this,AboutAppActivity.class);
        Intent intent1 = new Intent(SplashActivity.this,Profile.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,101,intent1,PendingIntent.FLAG_UPDATE_CURRENT);

        // Style Should be a BigTextStyle if you wish to add buttons
        builder.setStyle(new NotificationCompat.BigTextStyle().bigText("Please On The Storage & Internet Permissions"));
       // builder.addAction(android.R.drawable.ic_menu_add,"Add",pendingIntent);
        builder.addAction(android.R.drawable.ic_menu_delete,"Delete",pendingIntent);


        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        notificationManager.notify(101,notification);

    }
}
