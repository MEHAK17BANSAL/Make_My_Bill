package com.mehak.make_my_bill.adapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mehak.make_my_bill.R;
import com.mehak.make_my_bill.holder.StockHolder;
import com.mehak.make_my_bill.model.Stock;

public class StockAdapter extends FirestoreRecyclerAdapter<Stock,StockHolder>  {

    Context context;
    FirebaseFirestore firestore;


    public StockAdapter(FirestoreRecyclerOptions<Stock> options, Context context) {
        super(options);
        this.context  = context;
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onBindViewHolder(StockHolder holder, int position, Stock model) {
        holder.products_name.setText(model.name);
        holder.products_price.setText(String.valueOf(model.price));
        holder.products_date.setText(model.date);
    }

    @NonNull
    @Override
    public StockHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);

        StockHolder stockHolder = new StockHolder(view);
        // noticeHolder.setOnRecyclerItemClickListener(this);

        return stockHolder;
    }

    // If any data shall be changed on server, this method will be executed
    @Override
    public void onDataChanged() {
        super.onDataChanged();

    }


}

