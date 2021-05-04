package com.angogasapps.myfamily.ui.screens.buy_list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.angogasapps.myfamily.databinding.ProductInBuyListHolderBinding;
import com.angogasapps.myfamily.models.BuyList;
import com.angogasapps.myfamily.models.Family;
import com.angogasapps.myfamily.utils.BuyListUtils;

public class ProductsAdapter extends RecyclerView.Adapter<ProductsAdapter.ProductHolder>{
    private Context context;
    private BuyList buyList;
    private LayoutInflater inflater;

    public ProductsAdapter(Context context, BuyList buyList) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.buyList = buyList;
    }

    @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductHolder(ProductInBuyListHolderBinding.inflate(inflater));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
        BuyList.Product product = buyList.getProducts().get(position);

        holder.binding.textName.setText(product.getName());
        holder.binding.commentText.setText(BuyListUtils.getCorrectComment(product));

        holder.binding.getRoot().setOnLongClickListener(v -> {
            new ChangeOrDeleteProductDialog(context, buyList.getId(), product).show();
            return true;
        });
    }

    @Override
    public int getItemCount() {
        return buyList.getProducts().size();
    }


    public void update(BuyList newBuyList){

        if (newBuyList.getProducts().size() > buyList.getProducts().size()){
            // Добавлен новый продукт
            buyList = newBuyList;
//            BuyListManager.getInstance().addProductToBuyList(buyList, buyList.getProducts().get(buyList.getProducts().size() - 1));
            this.notifyItemChanged(buyList.getProducts().size() - 1);
        }else if(newBuyList.getProducts().size() < buyList.getProducts().size()){
            // Один продукт удалён
            int index = BuyListUtils.getIndexOfRemoveProduct(buyList.getProducts(), newBuyList.getProducts());
            buyList = newBuyList;
//            BuyListManager.getInstance().removeProductInBuyList(buyList, index);
            this.notifyItemRemoved(index);

        }else if(newBuyList.getProducts().size() == buyList.getProducts().size()){
            //Один продукт изменился

        }
    }

    public static class ProductHolder extends RecyclerView.ViewHolder{
        ProductInBuyListHolderBinding binding;

        public ProductHolder(@NonNull ProductInBuyListHolderBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
