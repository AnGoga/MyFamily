package com.angogasapps.myfamily.ui.screens.buy_list.dialogs

import android.app.AlertDialog
import android.content.Context
import com.angogasapps.myfamily.models.buy_list.BuyList.Product
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.databinding.DialogNewProductBinding
import es.dmoral.toasty.Toasty
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase
import com.angogasapps.myfamily.firebase.BuyListFunks

class BuyListProductCreatorDialog(context: Context, private val buyListId: String) :
    AlertDialog(context) {
    private lateinit var binding: DialogNewProductBinding
    private var product: Product? = null

    constructor(context: Context, buyListId: String, product: Product?) : this(
        context,
        buyListId
    ) {
        this.product = product
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DialogNewProductBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setCancelable(true)
        window!!.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        initOnClickListeners()
        initFields()
    }

    private fun initFields() {
        product?.let {
            binding.productName.setText(it.name)
            binding.commentEditText.setText(it.comment)
            binding.buttonAddProduct.text = context.getString(R.string.change)
        }
    }

    private fun initOnClickListeners() {
        binding.buttonAddProduct.setOnClickListener { addNewProduct() }
    }

    private fun addNewProduct() {
        if (binding.productName.text.toString() == "") {
            Toasty.error(context, R.string.enter_product_name).show()
            return
        }
        val product = Product()
        product.name = binding.productName.text.toString()
        product.comment = binding.commentEditText.text.toString()
        val i: IOnEndCommunicationWithFirebase = object : IOnEndCommunicationWithFirebase {
            override fun onSuccess() { dismiss() }
            override fun onFailure() { Toasty.error(context, R.string.something_went_wrong).show() }
        }
        if (this.product == null) {
            BuyListFunks.addNewProductToBuyList(buyListId, product, i)
        } else {
            product.id = this.product!!.id
            BuyListFunks.updateProduct(buyListId, product, i)
        }
    }
}