package com.angogasapps.myfamily.objects;

import androidx.annotation.StringRes;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.app.AppApplication;
import com.angogasapps.myfamily.utils.WithUsers;

import java.util.ArrayList;
import java.util.List;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class BuyList {
    private String name;
    private ArrayList<Product> products = new ArrayList<>();


    public BuyList(String name){
        this.name = name;
    }
    public <T extends List<Product>> BuyList(String name, T products){
        this(name);
        this.products.addAll(products);
    }

    public String getName(){
        return name;
    }
    public void addProduct(Product product){
        this.products.add(product);
    }


    public static class Product{
        private String id = "";
        private String name;
        private String from;
        private int count;
        private EProductAnnotations annotation;


        public Product(String name, int count, EProductAnnotations annotation){
            this.name = name;
            this.count = count;
            this.annotation = annotation;
            this.from = USER.getId();
        }

        public String getId(){
            return id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getCount(){
            return count;
        }

        public EProductAnnotations getAnnotation(){
            return annotation;
        }

        public String getFrom(){
            return WithUsers.getMemberNameById(id);
        }

        public enum EProductAnnotations{
            empty, liter, thing, kilogram, pack;
            @Override
            public String toString(){
                @StringRes int id = R.string.empty;
                switch (this){
                    case empty:
                        return "";
                    case liter:
                        id = R.string.liter;
                        break;
                    case thing:
                        id = R.string.thing;
                        break;
                    case kilogram:
                        id = R.string.kilogram;
                        break;
                    case pack:
                        id = R.string.pack;
                        break;
                }
                return AppApplication.getInstance().getApplicationContext().getString(id);
            }
        }
    }
}