package com.angogasapps.myfamily.ui.screens.buy_list.dialogs

import android.content.Context
import android.content.DialogInterface
import com.angogasapps.myfamily.models.buy_list.BuyList
import com.angogasapps.myfamily.R
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog
import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.network.repositories.BuyListRepository
import es.dmoral.toasty.Toasty
import javax.inject.Inject

class AddBuyListDialog(private val context: Context) {
    private var buyList: BuyList? = null
    @Inject
    lateinit var buyListRepository: BuyListRepository

    init {
        appComponent.inject(this)
    }

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
            input.setText(buyList!!.title)
        }
        alertDialog.setView(input)
        alertDialog.setPositiveButton(
            context.getString(R.string.cancel)
        ) { dialog: DialogInterface, which: Int -> dialog.cancel() }
        alertDialog.setNegativeButton(
            context.getString(if (buyList == null) R.string.add else R.string.change)
        ) { dialog: DialogInterface?, which: Int ->
            val name = input.text.toString().trim { it <= ' ' }
            if (name != "") {
                val inputBuyList: BuyList
                if (buyList == null) {
                    inputBuyList = BuyList(title = name)
                    buyListRepository.createNewBuyList(inputBuyList)
                } else {
                    inputBuyList = BuyList(buyList!!)
                    inputBuyList.title = name
                    buyListRepository.updateBuyListName(inputBuyList)
                }
            } else {
                Toasty.error(context, R.string.enter_buy_list_name).show()
            }
        }
        alertDialog.show()
    }
}