package com.angogasapps.myfamily.ui.screens.buy_list;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.app.AppApplication;
import com.angogasapps.myfamily.databinding.DialogNewProductBinding;
import com.angogasapps.myfamily.firebase.BuyListFunks;
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase;
import com.angogasapps.myfamily.models.BuyList;

import es.dmoral.toasty.Toasty;

public class BuyListProductCreatorDialog extends AlertDialog {
    private DialogNewProductBinding binding;
    private String buyListId;

    protected BuyListProductCreatorDialog(Context context, String buyListId) {
        super(context);
        this.buyListId = buyListId;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DialogNewProductBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setCancelable(true);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        initOnClickListeners();

    }

    private void initOnClickListeners() {
        binding.switcher.setOnCheckedChangeListener((buttonView, isChecked) -> {
            binding.layoutExtraOptions.setVisibility(isChecked ? View.VISIBLE : View.INVISIBLE);
        });

        ArrayAdapter<String> aa = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                AppApplication.getInstance().getResources().getStringArray(R.array.EProductAnnotations));
        binding.spinner.setAdapter(aa);

        binding.buttonAddProduct.setOnClickListener(v -> {
            addNewProduct();
        });
    }

    private void addNewProduct() {
        if (binding.productName.getText().toString().equals("")) {
            Toasty.error(getContext(), R.string.enter_product_name).show();
            return;
        }

        BuyList.Product product = new BuyList.Product();

        product.setName(binding.productName.getText().toString());

        if (binding.switcher.isChecked()){
            String num = binding.productCount.getText().toString();
            if (num.equals("")){
                Toasty.error(getContext(), R.string.enter_count).show();
                return;
            }
            product.setComment(Integer.parseInt(num) + " " + binding.spinner.getSelectedItem().toString());
        }

        BuyListFunks.addNewProductToBuyList(buyListId, product, new IOnEndCommunicationWithFirebase() {
            @Override
            public void onSuccess() {
                BuyListProductCreatorDialog.this.dismiss();
            }

            @Override
            public void onFailure() {
                Toasty.error(getContext(), R.string.something_went_wrong).show();
            }
        });
    }
}
