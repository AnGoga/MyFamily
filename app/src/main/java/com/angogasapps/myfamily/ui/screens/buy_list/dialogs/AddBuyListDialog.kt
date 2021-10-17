package com.angogasapps.myfamily.ui.screens.buy_list.dialogs

import android.content.Context
import android.content.DialogInterface
import com.angogasapps.myfamily.models.buy_list.BuyList
import com.angogasapps.myfamily.R
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.angogasapps.myfamily.firebase.BuyListFunks
import com.angogasapps.myfamily.firebase.interfaces.IOnEndCommunicationWithFirebase
import es.dmoral.toasty.Toasty

class AddBuyListDialog(private val context: Context) {
    private var buyList: BuyList? = null

    constructor(context: Context, buyList: BuyList?) : this(context) {
        this.buyList = buyList
    }

    fun show() {
        val alertDialog = AlertDialog.Builder(
            context
        )
        alertDialog.setTitle(context.getString(R.string.enter_buy_list_name))
        val input = EditText(context)
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.MATCH_PARENT
        )
        input.layoutParams = lp
        if (buyList != null) {
            input.setText(buyList!!.name)
        }
        alertDialog.setView(input)
        alertDialog.setPositiveButton(
            context.getString(R.string.cancel)
        ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
        alertDialog.setNegativeButton(
            context.getString(if (buyList == null) R.string.add else R.string.change)
        ) { dialog: DialogInterface?, which: Int ->
            val str = input.text.toString().trim { it <= ' ' }
            if (str != "") {
                val inputBuyList: BuyList
                if (buyList == null) {
                    inputBuyList = BuyList(str)
                    BuyListFunks.addNewBuyList(
                        inputBuyList,
                        object : IOnEndCommunicationWithFirebase {
                            override fun onSuccess() {}
                            override fun onFailure() {}
                        })
                } else {
                    inputBuyList = BuyList(buyList!!)
                    inputBuyList.name = str
                    BuyListFunks.updateBuyListName(
                        inputBuyList,
                        object : IOnEndCommunicationWithFirebase {
                            override fun onSuccess() {}
                            override fun onFailure() {}
                        })
                }
            } else {
                Toasty.error(context, R.string.enter_buy_list_name).show()
            }
        }
        alertDialog.show()
    }
}