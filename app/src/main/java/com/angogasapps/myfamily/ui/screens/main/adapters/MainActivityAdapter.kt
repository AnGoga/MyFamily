package com.angogasapps.myfamily.ui.screens.main.adapters

import android.content.Context
import com.angogasapps.myfamily.models.ActionCardState
import androidx.recyclerview.widget.RecyclerView
import com.angogasapps.myfamily.ui.screens.main.adapters.MainActivityAdapter.ActionCardHolder
import com.angogasapps.myfamily.ui.screens.main.adapters.ItemTouchHelperAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.databinding.ActionCardViewHolderBinding
import java.util.*

class MainActivityAdapter(private val context: Context, var list: ArrayList<ActionCardState>
    ) : RecyclerView.Adapter<ActionCardHolder>(), ItemTouchHelperAdapter {

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActionCardHolder {
        return ActionCardHolder(inflater.inflate(R.layout.action_card_view_holder, parent, false))
    }

    override fun onBindViewHolder(holder: ActionCardHolder, position: Int) {
        val stats = list[position]
        holder.update(stats)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onItemDismiss(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int): Boolean {
        swapList(fromPosition, toPosition)
        list.size
        notifyItemMoved(fromPosition, toPosition)
        return true
    }

    private fun swapList(from: Int, to: Int) {
        if (from > to) { // справа налево
            for (i in from downTo to + 1) {
                Collections.swap(list, i, i - 1)
            }
        } else { //слева направо
            for (i in from until to) {
                Collections.swap(list, i, i + 1)
            }
        }
    }

    class ActionCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val binding: ActionCardViewHolderBinding = ActionCardViewHolderBinding.bind(itemView)
        fun update(state: ActionCardState) {
            binding.card.update(state)
        }
    }
}