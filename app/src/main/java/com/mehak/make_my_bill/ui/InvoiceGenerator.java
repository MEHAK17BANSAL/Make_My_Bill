package com.mehak.make_my_bill.ui;

import android.app.FragmentTransaction;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mehak.make_my_bill.R;

import com.mehak.make_my_bill.model.DateDialog;
import com.mehak.make_my_bill.model.Util;
import com.mehak.make_my_bill.model.product;

public class InvoiceGenerator extends AppCompatActivity implements View.OnClickListener {
    ProductFragment productFragment;
    EditText invoice_no, txtOrderDate, txtDeliveryDate, companyname, companyphone, companyaddress, productname, productprice, productquantity;
    Spinner DATE,gst;
    String PROname;
    int tg;
    boolean checked;
    boolean gstflag=false;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    String uid;
    int r1=0,r2=0,p1=0;
    String dateo;
    String dated;;
     int count=0;;
     LinearLayout layout;
    Date date = new Date();
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);
    File myFile;
    File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOCUMENTS), "mehakpdf");
    EditText recipientname, recipientphone, recipientaddress, description;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adaptergst;

    Intent rcvintent;
    RadioButton withoutTax, withTax;
    android.support.v4.app.FragmentManager fragmentManager;
    Button addproduct, createinvoice;
    int total = 0;
    int tot=0;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int countI;
    int t;
    int gst1=0;
    int gt=0;
    int gstt=0;
    int gst2=0;
    int gt2=0;
    int gstt2=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_generator);
        initviews();

        productFragment = new ProductFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragmentcontainer, productFragment).commit();

       firestore = FirebaseFirestore.getInstance();
       auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();

        addproduct.setOnClickListener(this);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        sharedPreferences = getSharedPreferences("QuranVerse",0);
        t= sharedPreferences.getInt("id",countI);
        invoice_no.setText(""+t);
        invoice_no.setEnabled(false);

    }


    private void initviews() {
        layout=findViewById(R.id.gstcontainer);
        companyname = findViewById(R.id.editTextCompanyName);
        companyphone = findViewById(R.id.editTextcompanyphoneno);
        companyaddress = findViewById(R.id.editTextcompanyaddress);
        productname = findViewById(R.id.editTextProductName);
        productprice = findViewById(R.id.editTextPrice);
        productquantity = findViewById(R.id.editTextQuantity);
        recipientname = findViewById(R.id.editTextrecipientNAME);
        recipientaddress = findViewById(R.id.editTextrecipientAddress);
        recipientphone = findViewById(R.id.editTextrecipientPhone);
        description = findViewById(R.id.editTextDescripition);
        txtOrderDate = findViewById(R.id.editTextOrderDate);
        txtDeliveryDate = findViewById(R.id.editTextDeliveryDate);
        withoutTax = findViewById(R.id.radioButtonsimple);
        withTax = findViewById(R.id.radioButtongst);
        invoice_no = findViewById(R.id.editTextInvoiceNo);
        addproduct = findViewById(R.id.buttonproduct);
        gst = findViewById(R.id.spinnergst);
        createinvoice = findViewById(R.id.buttoncreateinvoice);
        DATE = findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        adapter.add("--Select Date Format--"); //0
        adapter.add("dd/MM/yyyy");
        adapter.add("MM/dd/yyyy");
        adapter.add("yyyy/dd/MM");
        // n-1
          DATE.setAdapter(adapter);
          DATE.setVisibility(View.VISIBLE);
        adaptergst = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);

        adaptergst.add("--Select GST--Percentage 5%"); //0
        adaptergst.add("10%");
        adaptergst.add("15%");
       // adaptergst.add("20%");
        // n-1
        gst.setAdapter(adaptergst);
        if (withoutTax.isChecked()) {
            String without = "no tax";
           // Toast.makeText(InvoiceGenerator.this, "simple", Toast.LENGTH_LONG).show();
        } else {
            String with = "gst";
           // Toast.makeText(InvoiceGenerator.this, "tax", Toast.LENGTH_LONG).show();

        }


          DATE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
              @Override
              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  if (position!=0){
                      dateo = txtOrderDate.getText().toString();
                      dated = txtDeliveryDate.getText().toString();
                      switch (position) {
                          case 1:
                              dateo = txtOrderDate.getText().toString();
                              dated = txtDeliveryDate.getText().toString();
                              SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd");
                              //  Toast.makeText(InvoiceGenerator.this, "You Selected " + position, Toast.LENGTH_LONG).show();

                              Date   date = new Date();

                              String dat=   fmt.format(date);
                              txtOrderDate.setText(""+dat);
                              txtDeliveryDate.setText(""+dat);
                              Log.i("show", " date -" +txtOrderDate+"     " +txtDeliveryDate);

                                break;


                          case 2:
                              dateo = txtOrderDate.getText().toString();
                              dated = txtDeliveryDate.getText().toString();
                              SimpleDateFormat fmt1 = new SimpleDateFormat("MM/dd/yyyy");


                              Date   date1 = new Date();

                              String dat1= fmt1.format(date1);
                              txtOrderDate.setText(""+dat1);
                              txtDeliveryDate.setText(""+dat1);
                              Log.i("show", " date -" +txtOrderDate+"     " +txtDeliveryDate);
                                break;

                          case 3:
                             dateo = txtOrderDate.getText().toString();
                             dated = txtDeliveryDate.getText().toString();
                              SimpleDateFormat fmt2 = new SimpleDateFormat("yyyy/dd/MM");


                              Date   date3 = new Date();

                              String dat3=   fmt2.format(date3);
                              txtOrderDate.setText(""+dat3);
                              txtDeliveryDate.setText(""+dat3);
                              Log.i("show", " date -" +txtOrderDate+"     " +txtDeliveryDate);
                              break;
                      }
                  }
              }

              @Override
              public void onNothingSelected(AdapterView<?> parent) {

              }
          });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_in_app, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.id_share:
                Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                File fileWithinMyDir = new File(pdfFolder + "/" + timeStamp + ".pdf");

                if (fileWithinMyDir.exists()) {
                    intentShareFile.setType("application/pdf");
                    intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + pdfFolder + "/" + timeStamp + ".pdf"));

                    intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                            "Sharing File...");
                    intentShareFile.putExtra(Intent.EXTRA_TEXT, "Sharing File...");

                    startActivity(Intent.createChooser(intentShareFile, "Share File"));
                } else {

                }


                break;
            case R.id.id_clear:

                companyname.setText("");
                companyphone.setText("");
                companyaddress.setText("");
                recipientname.setText("");
                recipientaddress.setText("");
                recipientphone.setText("");
                description.setText("");
                Util.productslist.clear();
                txtOrderDate.setText("");
                txtDeliveryDate.setText("");
                Util.clear=1;
      // Intent intent1 = new Intent(InvoiceGenerator.this,ProductFragment.class);
//intent1.putExtra("key","hy");
              //  android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
             //   ft.remove(ProductFragment.class).replace(R.id.fragmentcontainer, ProductFragment.newInstance());;
              //  ft.commit();

                ProductFragment PR = new ProductFragment();
                fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().remove(PR).replace(R.id.fragmentcontainer,PR).commit();
               // fragmentManager.beginTransaction().add(R.id.fragmentcontainer, PR).commit();
Util.productslist.clear();
addproduct.setText("Add Products");
                break;
            case R.id.id_logout:

                auth.signOut();

                Intent intent = new Intent(InvoiceGenerator.this,LogInActivity.class);
                startActivity(intent);
               finish();
                break;
            case R.id.id_cal:



                Intent intent5 = new Intent(InvoiceGenerator.this,Calculator.class);
                startActivity(intent5);
              //  finish();
                break;
            case R.id.id_pdf:



                Intent intent2 = new Intent(InvoiceGenerator.this,ShowInvoice.class);
                startActivity(intent2);
               // finish();
                break;
            case R.id.id_contact:



                Intent intent3 = new Intent(InvoiceGenerator.this,Profile.class);
                startActivity(intent3);
               // finish();
                break;
            case R.id.id_pro:



                Intent intent6 = new Intent(InvoiceGenerator.this,inventory.class);
                startActivity(intent6);
                // finish();
                break;

        }
        return true;
    }

    public void onStart() {
        super.onStart();

        txtOrderDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    DateDialog dialog = new DateDialog(v);
                    FragmentTransaction FT = getFragmentManager().beginTransaction();
                    dialog.show(FT, "Date Picker");
                }
            }
        });
        txtDeliveryDate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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


    @Override
    public void onClick(View v) {
         ProductFragment PR = new ProductFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragmentcontainer, PR).commit();

    }

    public void CreateInvoice(View view) throws FileNotFoundException, DocumentException {
        if (validateFields()) {

            if (!pdfFolder.exists()) {
                pdfFolder.mkdir();
                // Log.i(LOG, "");
                Log.i("LogTag", "Pdf Directory created: ");
                Toast.makeText(InvoiceGenerator.this, "pdf FOLDER created", Toast.LENGTH_LONG).show();

            }

            //Create time stamp
            final String BASE_FONT_BOLD = "Trebuchet MS B";
            final Font titlefontSmall = FontFactory.getFont(
                    BASE_FONT_BOLD, 12, Font.UNDERLINE);
            // final String ALIGN ="TabStop.Alignment.CENTER";

            //  File myFile = new File(pdfFolder + timeStamp + ".pdf");
            myFile = new File(pdfFolder + "/" + timeStamp + ".pdf");

            OutputStream output = new FileOutputStream(myFile);

            //Step 1
            Document document = new Document();

            //Step 2
            PdfWriter writer = PdfWriter.getInstance(document, output);

            //Step 3
            document.open();


            countI = sharedPreferences.getInt("id", 100);
            countI++;
            editor = sharedPreferences.edit();
            editor.putInt("id", countI);
            editor.apply();
            t = sharedPreferences.getInt("id", countI);
            invoice_no.setEnabled(true);
            invoice_no.setText("" + t);

            //Step 4 Add content
            document.add(new Paragraph("                                             " + "                Invoice", FontFactory.getFont(FontFactory.COURIER_BOLD, 20, Font.BOLD, BaseColor.DARK_GRAY)));
            // document.add(new Paragraph("                                                    Invoice",FontFactory.getFont(FontFactory.COURIER_BOLD,BaseColor.BLUE)));
            //  document.add(new Paragraph("                                                                     __________"));
            document.add(new Paragraph("               "));
            document.add(new Paragraph(companyname.getText().toString()));
            document.add(new Paragraph(companyphone.getText().toString()));
            document.add(new Paragraph(companyaddress.getText().toString()));
            document.add(new Paragraph("______________________________________________________________________________"));
            document.add(new Paragraph("               "));
            document.add(new Paragraph("                Invoice No                             Order Date                              Delivery Dte                         ", titlefontSmall));
            // document.add(new Paragraph("______________________________________________________________________________"));
            document.add(new Paragraph("                  " + invoice_no.getText().toString() + "                                       " + txtOrderDate.getText().toString() + "                                " + txtDeliveryDate.getText().toString()));
            //  document.add(new Paragraph("               "));
            // document.add(new Paragraph("______________________________________________________________________________"));
            document.add(Chunk.NEWLINE);
            // document.add(Chunk.NEWLINE);
           // if (gstflag==true) {
                if(withTax.isChecked()){

                //  document.add(new Paragraph("                Name                  Price                  Quantity                  GST                      Total"));

                // document.add(new Paragraph("               "));
                // document.add(Chunk.NEWLINE);
                PdfPTable table = new PdfPTable(new float[]{3, 2, 2, 2, 2});
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell cel1 = new PdfPCell((new Paragraph("Name", FontFactory.getFont(FontFactory.COURIER_BOLD, 10, Font.BOLD, BaseColor.PINK))));
                PdfPCell cel2 = new PdfPCell((new Paragraph("Price", FontFactory.getFont(FontFactory.COURIER_BOLD, 10, Font.BOLD, BaseColor.GREEN))));
                PdfPCell cel3 = new PdfPCell((new Paragraph("Quantity", FontFactory.getFont(FontFactory.COURIER_BOLD, 10, Font.BOLD, BaseColor.BLUE))));
                PdfPCell cel4 = new PdfPCell((new Paragraph("GST", FontFactory.getFont(FontFactory.COURIER_BOLD, 10, Font.BOLD, BaseColor.ORANGE))));
                PdfPCell cel5 = new PdfPCell((new Paragraph("Toatal", FontFactory.getFont(FontFactory.COURIER_BOLD, 10, Font.BOLD, BaseColor.RED))));
                cel1.setBackgroundColor(BaseColor.GRAY);
                cel2.setBackgroundColor(BaseColor.GRAY);
                cel3.setBackgroundColor(BaseColor.GRAY);
                cel4.setBackgroundColor(BaseColor.GRAY);
                cel5.setBackgroundColor(BaseColor.GRAY);
                table.addCell(cel1);
                table.addCell(cel2);
                table.addCell(cel3);
                table.addCell(cel4);
                table.addCell(cel5);
                table.setHeaderRows(1);

                for (product pr : Util.productslist) {
                    gst1 = pr.total * Util.gstPercent;
                     gt = gst1 / 100;
                     gstt = gt + pr.total;

                    table.addCell("" + pr.name);
                    table.addCell("" + pr.price);
                    table.addCell("" + pr.quantity);
                    table.addCell("" + Util.gstPercent);
                    table.addCell("" + gstt);
                    //  document.add(new Paragraph("                 "+pr.name+"                      "+pr.price+"                          "+pr.quantity+"                          "+Util.gstPercent+"%"+"                          "+gstt));

                }
                document.add(table);
                for (product pr : Util.productslist) {
                    int gst2 = pr.total* Util.gstPercent;
                    int gt2 = gst2 / 100;
                    int gstt2 = gt2 + pr.total;
                    total = gstt2 + total;
                }
                document.add(new Paragraph("______________________________________________________________________________"));
                document.add(new Paragraph("                                                                                         Grand Total" + "               " + total));
            } else {

                PdfPTable table = new PdfPTable(new float[]{3, 2, 2, 2});
                // table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
                table.setHorizontalAlignment(Element.ALIGN_CENTER);
                PdfPCell cel1 = new PdfPCell((new Paragraph("Name", FontFactory.getFont(FontFactory.COURIER_BOLD, 10, Font.BOLD, BaseColor.PINK))));
                PdfPCell cel2 = new PdfPCell((new Paragraph("Price", FontFactory.getFont(FontFactory.COURIER_BOLD, 10, Font.BOLD, BaseColor.GREEN))));
                PdfPCell cel3 = new PdfPCell((new Paragraph("Quantity", FontFactory.getFont(FontFactory.COURIER_BOLD, 10, Font.BOLD, BaseColor.BLUE))));
                PdfPCell cel4 = new PdfPCell((new Paragraph("Toatal", FontFactory.getFont(FontFactory.COURIER_BOLD, 10, Font.BOLD, BaseColor.RED))));
                cel1.setBackgroundColor(BaseColor.GRAY);
                cel2.setBackgroundColor(BaseColor.GRAY);
                cel3.setBackgroundColor(BaseColor.GRAY);
                cel4.setBackgroundColor(BaseColor.GRAY);


                table.addCell(cel1);
                table.addCell(cel2);
                table.addCell(cel3);
                table.addCell(cel4);
                table.setHeaderRows(1);


                //  document.add(new Paragraph("                 Name                        Price                              Quantity                             Total"));
                // document.add(new Paragraph("               "));
                for (product pr : Util.productslist) {
                    int tot = pr.total;
                    table.addCell("" + pr.name);
                    table.addCell("" + pr.price);
                    table.addCell("" + pr.quantity);
                    table.addCell("" + tot);


                    // document.add(new Paragraph("                   " + pr.name + "                             " + pr.price + "                                     " + pr.quantity  + "                                    " +tot));
                    // document.add(new Paragraph("               "));

                }
                document.add(table);
                for (product pr : Util.productslist) {
                    tot = pr.total;
                    total = tot + total;
                }
                document.add(new Paragraph("______________________________________________________________________________"));
                document.add(new Paragraph("                                                                                      Grand Total" + "                           " + total));
            }


            // document.add(new Paragraph("               "));
            // document.add(new Paragraph("               "));
            //  document.add(new Paragraph("               "));
            document.add(new Paragraph("               "));
            document.add(new Paragraph("   Description:- " + description.getText().toString()));
            document.add(new Paragraph("______________________________________________________________________________"));
            document.add(new Paragraph("               "));
            document.add(new Paragraph(recipientname.getText().toString()));
            document.add(new Paragraph(recipientphone.getText().toString()));
            document.add(new Paragraph(recipientaddress.getText().toString()));
            //Step 5: Close the document
            document.close();
            Toast.makeText(InvoiceGenerator.this, "pdf created", Toast.LENGTH_LONG).show();
            Util.productslist.clear();;
                tot=0;
                total=0;
                    gst1=0;
                    gt=0;
                gstt=0;
            gst2=0;
            gt2=0;
            gstt2=0;
            // File file = new File()
            myFile = new File(pdfFolder + "/" + timeStamp + ".pdf");
            Intent target = new Intent(Intent.ACTION_VIEW);
            target.setDataAndType(Uri.fromFile(myFile), "application/pdf");
            target.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            Intent intent = Intent.createChooser(target, "Open File");
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException e) {

            }


        }
        else {
            Toast.makeText(this,"Please Enter Details",Toast.LENGTH_LONG).show();
        }
    }
    public void f1(int tg) {

    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioButtongst:
                if (checked)
                    // Pirates are the best
//Util.gstPercent=5;
                    gstflag=true;
                      layout.setVisibility(view.VISIBLE);
                r1++;
                r2++;
                      //  Toast.makeText(InvoiceGenerator.this, "tax", Toast.LENGTH_LONG).show();

                gst.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                        String PERCENTAGE;
                        p1++;
                        switch (i) {
                            case 0:
                                PERCENTAGE = adaptergst.getItem(i);
                                Util.gstPercent = 5;
                                count++;
                                Toast.makeText(InvoiceGenerator.this, "You Selected " + PERCENTAGE, Toast.LENGTH_LONG).show();
                                break;
                            case 1:
                                PERCENTAGE = adaptergst.getItem(i);
                                Util.gstPercent = 10;
                                count++;
                                Toast.makeText(InvoiceGenerator.this, "You Selected " + PERCENTAGE, Toast.LENGTH_LONG).show();
                                break;
                            case 2:
                                PERCENTAGE = adaptergst.getItem(i);
                                Util.gstPercent = 15;
                                count++;
                                Toast.makeText(InvoiceGenerator.this, "You Selected " + PERCENTAGE, Toast.LENGTH_LONG).show();

                                break;
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });



                    break;
            case R.id.radioButtonsimple:
                if (checked){
                      layout.setVisibility(view.GONE);
                      r1++;

                    //Toast.makeText(InvoiceGenerator.this, "simple", Toast.LENGTH_LONG).show();
                }
                    break;
        }
    }
    boolean validateFields(){
        boolean flag = true;


        String odate=txtOrderDate.getText().toString();
        String ddate=txtOrderDate.getText().toString();
        String cn=companyname.getText().toString();
        String cp=companyphone.getText().toString();
        String ca=companyaddress.getText().toString();
        String rn=recipientname.getText().toString();
        String rp=recipientphone.getText().toString();
        String ra=recipientaddress.getText().toString();
        String des=description.getText().toString();

        if(odate.isEmpty()){
            flag = false;
        }
        if(ddate.isEmpty()){
            flag = false;
        }

        if(cn.isEmpty()){
                flag = false;
        }
        if(cp.isEmpty()){
            flag = false;
        }
        if(ca.isEmpty()){
            flag = false;
        }
        if (r1==0){
            flag = false;
        }
        if(Util.productslist.isEmpty()){
            flag = false;
        }
        if(rn.isEmpty()){
            flag = false;
        }
        if(rp.isEmpty()){
            flag = false;
        }
        if(ra.isEmpty()){
            flag = false;
        }
        if(des.isEmpty()){
            flag = false;
        }
        return flag;
    }

}