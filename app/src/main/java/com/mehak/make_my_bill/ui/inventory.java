package com.mehak.make_my_bill.ui;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mehak.make_my_bill.R;
import com.mehak.make_my_bill.model.DateDialog;
import com.mehak.make_my_bill.model.Util;
import com.mehak.make_my_bill.model.inventorymanager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

//import jxl.Workbook;

public class inventory extends AppCompatActivity {

    EditText proname;
    EditText proprice;
    EditText prodate,serialno;
    Button add,excel,clear;
    int count=1;
    Date date = new Date();
    inventorymanager stock=new inventorymanager();
    FirebaseAuth auth;
    inventorymanager invent;
    FirebaseFirestore firestore;
    CollectionReference usercollection;
    ProgressDialog progressDialog;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        initviews();
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
       // setupUIViews();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_in_product, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {


            case R.id.id_logout:

                auth.signOut();

                Intent intent = new Intent(inventory.this,LogInActivity.class);
                startActivity(intent);
                finish();
                break;

            case R.id.id_search:
                Intent intent1 = new Intent(inventory.this,search.class);
                startActivity(intent1);
                //finish();
                break;
            case R.id.id_cal:
                Intent intent3 = new Intent(inventory.this,Calculator.class);
                startActivity(intent3);
               // finish();
                break;
            case R.id.id_contact:
                Intent intent2 = new Intent(inventory.this,Profile.class);
                startActivity(intent2);
               // finish();
                break;
            case R.id.id_in:
                Intent intent5 = new Intent(inventory.this,InvoiceGenerator.class);
                startActivity(intent5);
                // finish();
                break;
        }
        return true;
    }

    public void initviews(){
        serialno=findViewById(R.id.editTexts);
        prodate=findViewById(R.id.editTextd);
        proname=findViewById(R.id.editTextn);
        proprice=findViewById(R.id.editTextp);
        add=findViewById(R.id.buttona);
        excel=findViewById(R.id.buttone);
        clear=findViewById(R.id.buttonc);
        serialno.setText(""+count);
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        usercollection=firestore.collection("stock");
        invent=new inventorymanager();
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

    }
    void saveNotice(){

        usercollection.document(stock.name).set(stock).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(inventory.this,"notice saved in DB",Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(inventory.this,"notice not saved in DB",Toast.LENGTH_LONG).show();
            }
        });
    }
    public void onStart() {
        super.onStart();

        prodate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DateDialog dialog = new DateDialog(v);
                    FragmentTransaction FT = getFragmentManager().beginTransaction();
                    dialog.show(FT, "Date Picker");
                }
            }
        });

    }

    public void productadd(View view) {

        stock = new inventorymanager();

        stock.serial = Integer.parseInt(serialno.getText().toString());
        stock.name = proname.getText().toString();
        stock.price = Integer.parseInt(proprice.getText().toString());
        stock.date = prodate.getText().toString();
        Log.i("show", stock.toString());
        if (validateFields()) {
            Util.inventories.add(stock);
            count++;
            usercollection.document(proname.getText().toString()).set(stock).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(inventory.this, "saved in DB", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(inventory.this, "not saved in DB", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
    public void CreateSheet(View view) {

        Log.i("show",Util.inventories.toString());


        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "mehakpdf");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
       File  myFile = new File(pdfFolder + "/" + timeStamp + ".xls");

        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();

            Log.i("LogTag", "Excel Directory created: ");
            Toast.makeText(inventory.this, "pdf FOLDER created", Toast.LENGTH_LONG).show();

        }


        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook;
        try {

            workbook = jxl.Workbook.createWorkbook(myFile, wbSettings);
            WritableSheet sheet = workbook.createSheet("First Sheet", 0);

            int a=0;int b=1;int c=1;int d=1;int f=1;
            sheet.setColumnView(0,12);
            sheet.setColumnView(1,12);
            sheet.setColumnView(2,12);
            sheet.setColumnView(3,12);
           // Label label,label1,label2,label3,label0,label4,label5,label6,label7,label8
            Label label0 = new Label(0,0,"Serial No");


            for (inventorymanager in : Util.inventories) {


                Label label1 = new Label(0,b,""+in.serial);
                b++;
                try{
                    sheet.addCell(label1);

                }  catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }

            }
            Label label3 = new Label(1,0,"Product Name");
            for (inventorymanager in : Util.inventories) {


                Label label4 = new Label(1,c,""+in.name);

                c++;
                try{
                    sheet.addCell(label4);

                }  catch (RowsExceededException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }

            }
            Label label5 = new Label(2,0,"Product Price");
            for (inventorymanager in : Util.inventories) {


                Label label6 = new Label(2,d,""+in.price);

                d++;
                try{
                    sheet.addCell(label6);

                }  catch (RowsExceededException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }

            }
            Label label7 = new Label(3,0,"Date");
            for (inventorymanager in : Util.inventories) {

                Label label8 = new Label(3,f,""+in.date);
                f++;
                try{
                    sheet.addCell(label8);

                }  catch (RowsExceededException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }

            }


            try {

                sheet.addCell(label0);

                sheet.addCell(label3);

                sheet.addCell(label5);

                sheet.addCell(label7);

            } catch (RowsExceededException e) {
                e.printStackTrace();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            workbook.write();

            try {
                workbook.close();
                Toast.makeText(inventory.this,"done",Toast.LENGTH_LONG).show();
            } catch (WriteException e) {

                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        myFile = new File(pdfFolder + "/" + timeStamp + ".xls");
        Intent target=new Intent(Intent.ACTION_VIEW);
        target.setDataAndType(Uri.fromFile(myFile),"application/vnd.ms-excel");
        //target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        target.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Intent intent = Intent.createChooser(target,"Open File");
        try {
            startActivity(intent);
        }catch (ActivityNotFoundException e){

        }
    }




    public void clear(View view) {
        serialno.setText(""+count);
        proname.setText("");
        proprice.setText("");
        prodate.setText("");
    }
    boolean validateFields(){
        boolean flag = true;
String p1=String.valueOf(stock.price);
String s1=String.valueOf(stock.serial);
        if(stock.name.isEmpty()){
            flag = false;
        }
        if(stock.date.isEmpty()){
            flag = false;
        }
        if(p1.isEmpty()){
                flag=false;

        }
        if(s1.isEmpty()){
            flag=false;

        }
        return flag;
    }

}
