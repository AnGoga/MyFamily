package com.angogasapps.myfamily.ui.screens.buy_list.adapters

import android.content.Context
import com.angogasapps.myfamily.ui.screens.buy_list.BuyListActivity.Companion.iGoToBuyListFragment
import androidx.recyclerview.widget.RecyclerView
import com.angogasapps.myfamily.ui.screens.buy_list.adapters.BuyListAdapter.BuyListHolder
import android.view.LayoutInflater
import android.view.View
import com.angogasapps.myfamily.models.buy_list.BuyList
import android.view.ViewGroup
import com.angogasapps.myfamily.R
import android.view.View.OnLongClickListener
import com.angogasapps.myfamily.databinding.BuyListHolderBinding
import com.angogasapps.myfamily.ui.screens.buy_list.dialogs.ChangeOrDeleteBuyListDialog
import com.angogasapps.myfamily.models.buy_list.BuyListEvent
import com.angogasapps.myfamily.ui.screens.buy_list.BuyListActivity
import com.angogasapps.myfamily.ui.screens.buy_list.BuyListManager
import java.util.ArrayList

class BuyListAdapter(private var context: Context) : RecyclerView.Adapter<BuyListHolder>() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    private var buyListsArray: ArrayList<BuyList> = BuyListManager.buyLists
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyListHolder {
        return BuyListHolder(inflater.inflate(R.layout.buy_list_holder, parent, false))
    }

    override fun onBindViewHolder(holder: BuyListHolder, position: Int) {
        synchronized(this) {
            holder.setTextName(buyListsArray[position].name)
            holder.binding.root.setOnLongClickListener {
                ChangeOrDeleteBuyListDialog(context, buyListsArray[holder.position]).show()
                true
            }
        }
    }

    override fun getItemCount(): Int {
        return buyListsArray.size
    }

    fun updateEnd() {
        notifyItemInserted(buyListsArray.size - 1)
    }

    fun update(event: BuyListEvent) {
        when (event.event) {
            BuyListEvent.EBuyListEvents.buyListRemoved -> {
                notifyItemRemoved(event.index)
            }
            BuyListEvent.EBuyListEvents.buyListAdded -> {
                this.notifyItemChanged(event.index)
            }
            BuyListEvent.EBuyListEvents.buyListChanged -> {
                this.notifyItemChanged(event.index)
            }
        }
    }

    inner class BuyListHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding: BuyListHolderBinding = BuyListHolderBinding.bind(itemView)

        fun setTextName(textName: String) {
            binding.textName.text = textName
        }

        init {
            binding.root.setOnClickListener {
                iGoToBuyListFragment.invoke(buyListsArray[layoutPosition])
            }
        }
    }
}