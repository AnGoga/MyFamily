package com.angogasapps.myfamily.ui.screens.buy_list.dialogs

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.angogasapps.myfamily.models.buy_list.BuyList.Product
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase
import com.angogasapps.myfamily.network.repositories.BuyListRepository
import com.angogasapps.myfamily.ui.screens.buy_list.dialogs.BuyListProductCreatorDialog
import javax.inject.Inject

class ChangeOrDeleteProductDialog(
    private val context: Context,
    private val buyListId: String,
    private val product: Product
) {
    private lateinit var dialog: AlertDialog
    @Inject
    lateinit var buyListRepository: BuyListRepository

    init {
        appComponent.inject(this)
    }

    fun show() {
        val list = arrayOf(context.getString(R.string.rename), context.getString(R.string.remove))
        val builder =
            AlertDialog.Builder(context)
                .setItems(list) { _: DialogInterface?, which: Int ->
                    if (which == 0) {
                        onClickEditButton()
                    } else if (which == 1) {
                        onClickRemoveButton()
                    }
                }
        dialog = builder.create()
        dialog.show()
    }

    private fun onClickRemoveButton() {
        val dialog = AlertDialog.Builder(
            context
        )
            .setTitle(R.string.remove_product)
            .setMessage(
                context.getString(R.string.change_or_delete_product_dialog_text1) + "\"" + product.name + "\"" + "?" +
                        context.getString(R.string.change_or_delete_product_dialog_text2)
            )
            .setPositiveButton(R.string.remove) { dialog1: DialogInterface?, which: Int ->
                if (which != AlertDialog.BUTTON_POSITIVE) return@setPositiveButton
                buyListRepository.deleteProduct(buyListId, product)
            }
            .setNegativeButton(R.string.cancel) { dialog1: DialogInterface, which: Int ->
                if (which != AlertDialog.BUTTON_NEGATIVE) return@setNegativeButton
                dialog1.dismiss()
            }
            .create()
        dialog.show()
        this.dialog.dismiss()
    }

    private fun onClickEditButton() {
        BuyListProductCreatorDialog(context, buyListId, product).show()
        dialog.dismiss()
    }
}