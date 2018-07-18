package com.mehak.make_my_bill.model;

public class inventorymanager {

    public String name;
    public int price;
    public  String date;
    public int serial;


    public inventorymanager()
    {

    }
    public inventorymanager(int serial, String name, int price, String date) {
        this.serial=serial;
        this.name = name;
        this.price = price;
        this.date = date;


    }

    @Override
    public String toString() {
        return "inventorymanager{" +
                "name='" + name + '\'' +
                ", price=" + price +
                ", date='" + date + '\'' +
                ", serial=" + serial +
                '}';
    }
}
