package com.mehak.make_my_bill.model;

public class product {


    public String name;
    public int price;
    public  int quantity;
    public int total;


    public product()
    {

    }
    public product(String name, int price, int quantity, int total) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.total=total;

    }


}
