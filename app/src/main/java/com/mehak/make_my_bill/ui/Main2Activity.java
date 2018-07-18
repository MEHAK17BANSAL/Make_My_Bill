package com.mehak.make_my_bill.ui;

import android.app.ProgressDialog;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mehak.make_my_bill.R;

import java.io.File;
import java.io.IOException;

public class Main2Activity extends AppCompatActivity {
private Button mRecirdBtn;
private TextView mRecordLable;
private MediaRecorder mRecorder;
private String mFileName=null;
    private ProgressDialog mProgress;
private  StorageReference mStorage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mStorage= FirebaseStorage.getInstance().getReference();
        mProgress=new ProgressDialog(this);
        mRecirdBtn=findViewById(R.id.recordBtn);
        mRecordLable=findViewById(R.id.recordLable);
mFileName= Environment.getExternalStorageDirectory().getAbsolutePath();
mFileName += "/recorded_audio.3gp";

mRecirdBtn.setOnTouchListener(new View.OnTouchListener() {
    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        if(motionEvent.getAction()== MotionEvent.ACTION_DOWN){
            startRecording();
            mRecordLable.setText("recording start");
        }
        else if(motionEvent.getAction()==MotionEvent.ACTION_UP);{
           stopRecording();
           mRecordLable.setText("recording stop");
        }
        return false;
    }

});

    }
    private void startRecording() {
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(mFileName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e("record_log", "prepare() failed");
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
         upload();
    }
    private void upload() {
        mProgress.setMessage("UPLOAD");
        mProgress.show();
        StorageReference filepath=mStorage.child("Audio").child("new_audio.3gp");
        Uri uri= Uri.fromFile(new File(mFileName));
        filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                mProgress.dismiss();
                mRecordLable.setText("uploading finish");
            }
        });
    }

}
