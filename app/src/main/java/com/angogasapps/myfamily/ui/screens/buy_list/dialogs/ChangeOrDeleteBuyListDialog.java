package com.angogasapps.myfamily.ui.screens.buy_list.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.databinding.ChangeOrDeleteDialogBinding;
import com.angogasapps.myfamily.firebase.BuyListFunks;
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase;
import com.angogasapps.myfamily.models.buy_list.BuyList;

import es.dmoral.toasty.Toasty;

public class ChangeOrDeleteBuyListDialog {
    private BuyList buyList;
    private Context context;
    private AlertDialog dialog;

    public ChangeOrDeleteBuyListDialog(@NonNull Context context, BuyList buyList) {
        this.context = context;
        this.buyList = buyList;
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

    public void onClickEditButton(View view){
        (new AddBuyListDialog(context, buyList)).show();
        this.dialog.dismiss();
    }

    public void onClickRemoveButton(View view){
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.remove_buy_list)
                .setMessage(context.getString(R.string.change_or_delet_buy_list_dialog_text1)+ "\"" + buyList.getName() + "\" ?"
                    + "\n" + context.getString(R.string.change_or_delete_product_dialog_text2))
                .setPositiveButton(R.string.remove, (dialog1, which) -> {
                    if (which != AlertDialog.BUTTON_POSITIVE) return;
                    BuyListFunks.deleteBuyList(buyList, new IOnEndCommunicationWithFirebase() {
                        @Override
                        public void onSuccess() { /*TODO: . . .*/}

                        @Override
                        public void onFailure() {
                            Toasty.error(context, R.string.something_went_wrong).show();
                        }
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
}
