package com.angogasapps.myfamily.ui.screens.buy_list.dialogs

import android.app.AlertDialog
import android.content.Context
import com.angogasapps.myfamily.models.buy_list.BuyList.Product
import android.os.Bundle
import android.view.WindowManager
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.databinding.DialogNewProductBinding
import es.dmoral.toasty.Toasty
import com.angogasapps.myfamily.network.repositories.BuyListRepository
import javax.inject.Inject

class BuyListProductCreatorDialog(context: Context, private val buyListId: String) :
    AlertDialog(context) {
    private lateinit var binding: DialogNewProductBinding
    private var product: Product? = null
    @Inject
    lateinit var buyListRepository: BuyListRepository

    init {
        appComponent.inject(this)
    }

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
        if (this.product == null) {
            buyListRepository.createProduct(buyListId, product, onSuccess = {dismiss()})
        } else {
            product.id = this.product!!.id
            buyListRepository.updateProduct(buyListId, product, onSuccess = {dismiss()})
        }
    }
}