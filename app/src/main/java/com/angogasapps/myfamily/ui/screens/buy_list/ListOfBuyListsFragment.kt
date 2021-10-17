package com.angogasapps.myfamily.ui.screens.buy_list

import androidx.recyclerview.widget.GridLayoutManager
import com.angogasapps.myfamily.ui.screens.buy_list.adapters.BuyListAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.angogasapps.myfamily.databinding.FragmentListOfBuyListsBinding
import com.angogasapps.myfamily.ui.screens.buy_list.dialogs.AddBuyListDialog
import com.angogasapps.myfamily.ui.screens.buy_list.BuyListManager
import com.angogasapps.myfamily.models.buy_list.BuyListEvent
import io.reactivex.disposables.Disposable

class ListOfBuyListsFragment : Fragment() {
    private lateinit var binding: FragmentListOfBuyListsBinding
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var adapter: BuyListAdapter
    private lateinit var disposable: Disposable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentListOfBuyListsBinding.inflate(inflater, container, false)
        initOnClickListeners()
        initObserver()
        initRecyclerView()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (!disposable.isDisposed) disposable.dispose()
    }

    private fun initOnClickListeners() {
        binding.floatingBtn.setOnClickListener {
            AddBuyListDialog(requireContext()).show()
        }
    }

    private fun initRecyclerView() {
        adapter = BuyListAdapter(requireContext())
        layoutManager = GridLayoutManager(context, 2)
        binding.recycleView.layoutManager = layoutManager
        binding.recycleView.adapter = adapter
    }

    private fun initObserver() {
        disposable = BuyListManager.subject.subscribe {
            adapter.update(it)
        }
    }
}