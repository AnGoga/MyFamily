package com.angogasapps.myfamily.models;

import android.util.Log;

import com.angogasapps.myfamily.utils.WithUsers;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class BuyList {
    private String id = "";
    private String name;
    private ArrayList<Product> products = new ArrayList<>();

    public BuyList(){}

    public BuyList(String name){
        this.name = name;
    }
    public <T extends List<Product>> BuyList(String name, T products){
        this(name);
        this.products.addAll(products);
    }

    public static BuyList from(DataSnapshot snapshot){
        Log.i("tag", snapshot.toString());
        BuyList buyList = snapshot.getValue(BuyList.class);
        buyList.id = snapshot.getKey();
        return buyList;
    }

    public String getName(){
        return name;
    }
    public void addProduct(Product product){
        this.products.add(product);
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BuyList buyList = (BuyList) o;
        return Objects.equals(name, buyList.name) &&
                Objects.equals(products, buyList.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, products);
    }

    public static class Product{
        private String id = "";
        private String name = "";
        private String from = "";
        private String comment = "";

        public Product(){}

//
        public Product(String name, String comment, String from){
            this.name = name;
            this.comment = comment;
            this.from = from;
        }

        public String getId(){
            return id;
        }
        public void setId(String id){
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getComment(){
            return comment;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public String getFrom(){
//            return WithUsers.getMemberNameById(id);
            return this.from;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Product product = (Product) o;
            return Objects.equals(id, product.id) &&
                    Objects.equals(name, product.name) &&
                    Objects.equals(from, product.from) &&
                    Objects.equals(comment, product.comment);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, from, comment);
        }
    }
}