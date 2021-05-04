package com.angogasapps.myfamily.ui.screens.buy_list.dialogs;

import android.content.Context;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.firebase.BuyListFunks;
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase;
import com.angogasapps.myfamily.models.BuyList;

public class AddBuyListDialog {
    private Context context;


    public AddBuyListDialog(@NonNull Context context) {
        this.context = context;
    }

    public void show(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);


        alertDialog.setTitle(context.getString(R.string.enter_buy_list_name));


        final EditText input = new EditText(context);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);

        alertDialog.setPositiveButton(context.getString(R.string.cancel),
                (dialog, which) -> {
                    dialog.cancel();
                });

        alertDialog.setNegativeButton(context.getString(R.string.add),
                (dialog, which) -> {
                    BuyList buyList = new BuyList(input.getText().toString());
                    BuyListFunks.addNewBuyList(buyList, new IOnEndCommunicationWithFirebase() {
                        @Override
                        public void onSuccess() {

                        }
                        @Override
                        public void onFailure() {}
                    });
                });


        alertDialog.show();
    }
}
