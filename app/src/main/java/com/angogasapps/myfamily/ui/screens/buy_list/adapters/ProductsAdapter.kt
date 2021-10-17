package com.angogasapps.myfamily.ui.screens.buy_list.adapters

import android.content.Context
import com.angogasapps.myfamily.models.buy_list.BuyList
import androidx.recyclerview.widget.RecyclerView
import com.angogasapps.myfamily.ui.screens.buy_list.adapters.ProductsAdapter.ProductHolder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.models.buy_list.BuyList.Product
import com.angogasapps.myfamily.utils.BuyListUtils
import android.view.View.OnLongClickListener
import com.angogasapps.myfamily.databinding.ProductInBuyListHolderBinding
import com.angogasapps.myfamily.ui.screens.buy_list.dialogs.ChangeOrDeleteProductDialog
import com.angogasapps.myfamily.models.buy_list.BuyListEvent

class ProductsAdapter(private val context: Context, private val buyList: BuyList) : RecyclerView.Adapter<ProductHolder>() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductHolder {
        return ProductHolder(
            ProductInBuyListHolderBinding.bind(
                LayoutInflater.from(parent.context)
                    .inflate(R.layout.product_in_buy_list_holder, parent, false)
            )
        )
    }

    override fun onBindViewHolder(holder: ProductHolder, position: Int) {
        val product = buyList.products[position]
        holder.binding.textName.text = product.name
        holder.binding.commentText.text = BuyListUtils.getCorrectComment(product)
        holder.binding.root.setOnLongClickListener {
            ChangeOrDeleteProductDialog(context, buyList.id, product).show()
            true
        }
        holder.binding.commentText.visibility =
            if (product.comment == "") View.GONE else View.VISIBLE
    }

    override fun getItemCount(): Int {
        return buyList.products.size
    }

    fun update(event: BuyListEvent) {
        when (event.event) {
            BuyListEvent.EBuyListEvents.productAdded -> {
                this.notifyItemChanged(event.index)
            }
            BuyListEvent.EBuyListEvents.productRemoved -> {
                notifyItemRemoved(event.index)
            }
            BuyListEvent.EBuyListEvents.productChanged -> {
                this.notifyItemChanged(event.index)
            }
        }
    }

    class ProductHolder(var binding: ProductInBuyListHolderBinding)
        : RecyclerView.ViewHolder(binding.root)

}