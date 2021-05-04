package com.angogasapps.myfamily.utils;

import com.angogasapps.myfamily.models.BuyList;
import com.angogasapps.myfamily.models.Family;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_COMMENT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_FROM;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_NAME;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.CHILD_PRODUCTS;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

public class BuyListUtils {
    /*
  DataSnapshot {
    key = -MY0xumFvGFkkxbaYiLo,
    value = {
        name=#1,
        products = {
            -MZO2_VpJkWN3pY3fdyE = {
                annotation=,
                count=-1,
                name=продукт 1,
                from=-MZO2_VpJkWN3pY3fdyE,
                id=-MZO2_VpJkWN3pY3fdyE
                }
             }
         }
     }

 */
    public static BuyList parseBuyList(DataSnapshot snapshot){
        BuyList buyList = new BuyList();

        buyList.setId(snapshot.getKey());
        buyList.setName(snapshot.child(CHILD_NAME).getValue(String.class));
        buyList.setProducts(getProductsList(snapshot));

        return buyList;
    }

    private static ArrayList<BuyList.Product> getProductsList(DataSnapshot snapshot){
        ArrayList<BuyList.Product> list = new ArrayList<>();
        for (DataSnapshot productSnapshot : snapshot.child(CHILD_PRODUCTS).getChildren()){
            list.add(getProduct(productSnapshot));
        }

        return list;
    }

    private static BuyList.Product getProduct(DataSnapshot snapshot){
        BuyList.Product product = snapshot.getValue(BuyList.Product.class);
        product.setId(snapshot.getKey());
        return product;
    }

    public static HashMap<String, Object> getHashMap(BuyList.Product product){
        HashMap<String, Object> map = new HashMap<>();
        map.put(CHILD_NAME, product.getName());
        map.put(CHILD_FROM, product.getFrom());
        map.put(CHILD_COMMENT, product.getComment());

        return map;
    }

    public static String getCorrectComment(BuyList.Product product){
        String comment = product.getComment();
        if (product.getFrom().equals(USER.getPhone())){
            if (comment != null){
                return comment;
            }else{
                return "";
            }
        }else {
            if (!comment.equals("")){
                return comment + " : " + Family.getInstance().getNameByPhone(product.getFrom());
            }else{
               return Family.getInstance().getNameByPhone(product.getFrom());
            }
        }
    }

    public static int getIndexOfRemoveProduct(ArrayList<BuyList.Product> oldBuyList, ArrayList<BuyList.Product> newBuyList){
        for (int i = 0; i < newBuyList.size(); i++) {
            if (!oldBuyList.get(i).getId().equals(newBuyList.get(i).getId())){
                return i;
            }
        }
        return newBuyList.size();
    }
}
