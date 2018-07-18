package com.mehak.make_my_bill.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mehak.make_my_bill.R;

import java.io.File;
import java.util.Locale;

public class ShowInvoice extends AppCompatActivity implements AdapterView.OnItemClickListener{

    ListView listView;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String uid;
    ArrayAdapter<String> adapter;
    TextToSpeech tts;
    File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOCUMENTS), "mehakpdf").getAbsoluteFile();
    void initviews()
    {
        listView=findViewById(R.id.listView);

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "mehakpdf").getAbsoluteFile();
        String[] fileNames = pdfFolder.list();
        for(String name : fileNames){
            if (name.endsWith(".pdf"))
                adapter.add(name);
        }
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_invoice);
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
     //   android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.app_bar);
      //  setSupportActionBar(toolbar);

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (TextToSpeech.SUCCESS==i){
                    tts.setLanguage(Locale.US);
                }
                else {

                    Toast.makeText(ShowInvoice.this,"TTS not avaliable.please check settings!!",Toast.LENGTH_LONG).show();
                }

            }
        });

        initviews();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        String fileName = adapter.getItem(position);
        Toast.makeText(this,"you selected"+fileName,Toast.LENGTH_LONG).show();
       String message = "do you want to open";
        tts.speak(message,TextToSpeech.QUEUE_FLUSH,null);
        File myFile = new File(pdfFolder + "/" + fileName );//+ ".pdf");
        Intent target=new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(myFile),"application/pdf");
        target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        Intent intent = Intent.createChooser(target,"Open File");
        try {
            startActivity(intent);
        }catch (ActivityNotFoundException e){

        }

    }
}
