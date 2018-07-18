package com.mehak.make_my_bill.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.mehak.make_my_bill.R;

public class StockHolder extends RecyclerView.ViewHolder{
   public TextView products_name;
  public TextView products_price;
    public TextView products_date;

    public StockHolder(View itemView) {
        super(itemView);
      products_name   =  itemView.findViewById(R.id.name_text);
   products_price=  itemView.findViewById(R.id.status_text);
    products_date  =  itemView.findViewById(R.id.status_text1);


    }
}
