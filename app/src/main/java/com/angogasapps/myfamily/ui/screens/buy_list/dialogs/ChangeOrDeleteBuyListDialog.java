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

public class ChangeOrDeleteBuyListDialog extends AlertDialog {
    private ChangeOrDeleteDialogBinding binding;
    private BuyList buyList;

    public ChangeOrDeleteBuyListDialog(@NonNull Context context, BuyList buyList) {
        super(context);
        this.buyList = buyList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ChangeOrDeleteDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.editBtn.setOnClickListener(this::onClickEditButton);
        binding.removeBtn.setOnClickListener(this::onClickRemoveButton);
    }

    public void onClickEditButton(View view){
        (new AddBuyListDialog(getContext(), buyList)).show();
        this.dismiss();
    }

    public void onClickRemoveButton(View view){
        AlertDialog dialog = new Builder(getContext())
                .setTitle(R.string.remove_buy_list)
                .setMessage(getContext().getString(R.string.change_or_delet_buy_list_dialog_text1)+ "\"" + buyList.getName() + "\" ?"
                    + "\n" + getContext().getString(R.string.change_or_delete_product_dialog_text2))
                .setPositiveButton(R.string.remove, (dialog1, which) -> {
                    if (which != BUTTON_POSITIVE) return;
                    BuyListFunks.deleteBuyList(buyList, new IOnEndCommunicationWithFirebase() {
                        @Override
                        public void onSuccess() { /*TODO: . . .*/}

                        @Override
                        public void onFailure() {
                            Toasty.error(getContext(), R.string.something_went_wrong).show();
                        }
                    });
                })
                .setNegativeButton(R.string.cancel, (dialog1, which) -> {
                    if (which != BUTTON_NEGATIVE) return;
                    dialog1.dismiss();
                })
                .create();
        dialog.show();
        this.dismiss();
    }
}
