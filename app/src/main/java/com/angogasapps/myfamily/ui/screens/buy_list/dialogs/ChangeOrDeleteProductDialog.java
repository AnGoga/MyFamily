package com.angogasapps.myfamily.ui.screens.buy_list.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.databinding.ChangeOrDeleteProductDialogBinding;
import com.angogasapps.myfamily.firebase.BuyListFunks;
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase;
import com.angogasapps.myfamily.models.BuyList;

public class ChangeOrDeleteProductDialog extends AlertDialog {
    private ChangeOrDeleteProductDialogBinding binding;
    private BuyList.Product product;
    private String buyListId;

    public ChangeOrDeleteProductDialog(@NonNull Context context, String buyListId, BuyList.Product product) {
        super(context);
        this.buyListId = buyListId;
        this.product = product;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ChangeOrDeleteProductDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.editBtn.setOnClickListener(this::onClickEditBtn);
        binding.removeBtn.setOnClickListener(this::onClickRemoveBtn);
    }

    public void onClickRemoveBtn(View view){
        AlertDialog dialog = new Builder(getContext())
                .setTitle(R.string.remove_product)
                .setMessage(getContext().getString(R.string.change_or_delete_product_dialog_text1) + "\"" + product.getName() + "\"" + "?" +
                        getContext().getString(R.string.change_or_delete_product_dialog_text2))
                .setPositiveButton(R.string.remove, (dialog1, which) -> {
                    if (which != BUTTON_POSITIVE) return;

                    getButton(BUTTON_POSITIVE).setClickable(false);
                    BuyListFunks.deleteProduct(buyListId, product, new IOnEndCommunicationWithFirebase() {
                        @Override
                        public void onSuccess() {
                            getButton(BUTTON_POSITIVE).setClickable(true);
                        }

                        @Override
                        public void onFailure() {
                            getButton(BUTTON_POSITIVE).setClickable(true);
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

    public void onClickEditBtn(View view){

    }
}
