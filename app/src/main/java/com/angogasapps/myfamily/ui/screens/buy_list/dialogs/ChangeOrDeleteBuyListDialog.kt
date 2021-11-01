package com.angogasapps.myfamily.ui.screens.buy_list.dialogs

import android.content.Context
import android.content.DialogInterface
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.angogasapps.myfamily.models.buy_list.BuyList
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.ui.screens.buy_list.dialogs.AddBuyListDialog
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase
import com.angogasapps.myfamily.network.repositories.BuyListRepository
import es.dmoral.toasty.Toasty
import javax.inject.Inject

class ChangeOrDeleteBuyListDialog(private val context: Context, private val buyList: BuyList) {
    private lateinit var dialog: AlertDialog
    @Inject
    lateinit var buyListRepository: BuyListRepository

    init {
        appComponent.inject(this)
    }

    fun show() {
        val builder = AlertDialog.Builder(
            context
        )
        val list = arrayOf(context.getString(R.string.rename), context.getString(R.string.remove))
        builder.setItems(list) { dialog: DialogInterface?, which: Int ->
            if (which == 0) {
                onClickEditButton()
            } else if (which == 1) {
                onClickRemoveButton()
            }
        }
        dialog = builder.create()
        dialog.show()
    }

    private fun onClickEditButton() {
        AddBuyListDialog(context, buyList).show()
        dialog.dismiss()
    }

    private fun onClickRemoveButton() {
        val dialog =
            AlertDialog.Builder(context)
            .setTitle(R.string.remove_buy_list)
            .setMessage(
                """${context.getString(R.string.change_or_delet_buy_list_dialog_text1)}"${buyList.name}" ?
${context.getString(R.string.change_or_delete_product_dialog_text2)}"""
            )
            .setPositiveButton(R.string.remove) { dialog1: DialogInterface?, which: Int ->
                if (which != AlertDialog.BUTTON_POSITIVE) return@setPositiveButton
                buyListRepository.deleteBuyList(buyList)
            }
            .setNegativeButton(R.string.cancel) { dialog1: DialogInterface, which: Int ->
                if (which != AlertDialog.BUTTON_NEGATIVE) return@setNegativeButton
                dialog1.dismiss()
            }
            .create()
        dialog.show()
        this.dialog.dismiss()
    }
}