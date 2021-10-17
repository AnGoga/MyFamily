package com.angogasapps.myfamily.ui.screens.buy_list.dialogs;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.firebase.BuyListFunks;
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase;
import com.angogasapps.myfamily.models.buy_list.BuyList;

public class ChangeOrDeleteProductDialog {
    private BuyList.Product product;
    private String buyListId;
    private Context context;
    private AlertDialog dialog;

    public ChangeOrDeleteProductDialog(@NonNull Context context, String buyListId, BuyList.Product product) {
        this.context = context;
        this.buyListId = buyListId;
        this.product = product;
    }


    public void show() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        String[] list = {context.getString(R.string.rename), context.getString(R.string.remove)};
        builder.setItems(list, (dialog, which) -> {
            if (which == 0){
                onClickEditButton(null);
            } else if(which == 1){
                onClickRemoveButton(null);
            }
        });
        this.dialog = builder.create();
        dialog.show();

    }

    public void onClickRemoveButton(View view){
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.remove_product)
                .setMessage(context.getString(R.string.change_or_delete_product_dialog_text1) + "\"" + product.getName() + "\"" + "?" +
                        context.getString(R.string.change_or_delete_product_dialog_text2))
                .setPositiveButton(R.string.remove, (dialog1, which) -> {
                    if (which != AlertDialog.BUTTON_POSITIVE) return;

                    BuyListFunks.deleteProduct(buyListId, product, new IOnEndCommunicationWithFirebase() {
                        @Override public void onSuccess() {}
                        @Override public void onFailure() {}
                    });

                })
                .setNegativeButton(R.string.cancel, (dialog1, which) -> {
                    if (which != AlertDialog.BUTTON_NEGATIVE) return;
                    dialog1.dismiss();
                })
                .create();

        dialog.show();
        this.dialog.dismiss();
    }

    public void onClickEditButton(View view){
        (new BuyListProductCreatorDialog(context, buyListId, product)).show();
        this.dialog.dismiss();
    }
}
