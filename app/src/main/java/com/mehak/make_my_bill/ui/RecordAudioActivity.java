package com.mehak.make_my_bill.ui;

import android.content.Intent;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mehak.make_my_bill.R;

import java.io.File;

public class RecordAudioActivity extends AppCompatActivity {
ImageButton ibRecord;
TextView textView;
String Audiofilepath;
Boolean Recording=false;
MediaRecorder mediaRecorder;
    StorageReference srAudio;
    DatabaseReference drAudio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_audio);
        ibRecord=findViewById(R.id.ibRecordBtn);
        textView=findViewById(R.id.tvRecordAudioView);
        srAudio= FirebaseStorage.getInstance().getReference();
       // drAudio= FirebaseDatabase.getInstance().getReference().child("Upload Audio");
        Audiofilepath= Environment.getExternalStorageDirectory().getAbsolutePath()+"/MEHAKaudio.mp3";
       ibRecord.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction()==event.ACTION_DOWN){
                    RecordAudio();
                    textView.setText("record start");
                }
                else {
                    if(event.getAction()==event.ACTION_UP){
                        stopRecord();
                        textView.setText("record stop");
                    }
                }



                return false;
            }
        });
       /* ibRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordAudio();
                textView.setText("record start");
            }
        });*/
    }

    private void RecordAudio() {
        Recording=true;
        try {
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);  //ok so I say audio source is the microphone, is it windows/linux microphone on the emulator?
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mediaRecorder.setOutputFile(Audiofilepath);
            mediaRecorder.prepare();

        }catch (Exception e){
            e.printStackTrace();
        }
        mediaRecorder.start();
    }

    private void stopRecord(){
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder=null;
        uploadaudio();
    }

    private void uploadaudio() {
        Uri uri=Uri.fromFile(new File(Audiofilepath));
        StorageReference filepath=srAudio.child("Upload Audio").child(uri.getLastPathSegment());
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                startActivity(new Intent(RecordAudioActivity.this,Log.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(RecordAudioActivity.this, " oops !! ", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void stop(View view) {

    }
}
