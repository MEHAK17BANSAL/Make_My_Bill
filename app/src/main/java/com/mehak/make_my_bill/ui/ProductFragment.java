package com.mehak.make_my_bill.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mehak.make_my_bill.R;
import com.mehak.make_my_bill.model.Util;
import com.mehak.make_my_bill.model.product;


public class ProductFragment extends Fragment {
    product pro = new product();
    EditText productname,productprice,productquantity;
    Util U = new Util();
    Editable n;
    int total=0;
    public ProductFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_product, container, false);

        // Inflate the layout for this fragment
        Button save=rootView.findViewById(R.id.button_save);
     final EditText productname=rootView.findViewById(R.id.editTextProductName);
       final EditText productprice=rootView.findViewById(R.id.editTextPrice);
      final EditText productquantity=rootView.findViewById(R.id.editTextQuantity);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pro.name=productname.getText().toString();
              //  pro.name=n;
                String N=productname.getText().toString();
                String P=productprice.getText().toString();
                String q=productquantity.getText().toString();
                pro.price= Integer.parseInt(productprice.getText().toString());
                pro.quantity= Integer.parseInt(productquantity.getText().toString());
                pro.total=pro.price*pro.quantity;
               if(N.isEmpty()&&P.isEmpty()&&q.isEmpty()){
                   Toast.makeText(getActivity(), "Fill Details!", Toast.LENGTH_SHORT).show();
                }

    else {
                   Util.productslist.add(pro);
               }



            }
        });



        return rootView;
    }
    public void clear(){
      // if(Util)
    }
}
