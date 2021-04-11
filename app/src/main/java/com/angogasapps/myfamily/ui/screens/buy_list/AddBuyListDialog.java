package com.angogasapps.myfamily.ui.screens.buy_list;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.firebase.BuyListFunks;
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase;
import com.angogasapps.myfamily.objects.BuyList;

import io.reactivex.subjects.PublishSubject;

public class AddBuyListDialog {
    private Context context;
    private PublishSubject<BuyList> subject;


    protected AddBuyListDialog(@NonNull Context context, PublishSubject<BuyList> subject) {
        this.context = context;
        this.subject = subject;
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
                            subject.onNext(buyList);
                        }
                        @Override
                        public void onFailure() {}
                    });
                });


        alertDialog.show();
    }
}
