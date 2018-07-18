package com.mehak.make_my_bill.model;



public class Stock {

    public String name,date;
public int price;
    public Stock(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "price=" + price +
                '}';
    }
}
