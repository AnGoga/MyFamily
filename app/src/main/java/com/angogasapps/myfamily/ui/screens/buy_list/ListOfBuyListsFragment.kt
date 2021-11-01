package com.angogasapps.myfamily.ui.screens.buy_list

import androidx.recyclerview.widget.GridLayoutManager
import com.angogasapps.myfamily.ui.screens.buy_list.adapters.BuyListAdapter
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.databinding.FragmentListOfBuyListsBinding
import com.angogasapps.myfamily.ui.screens.buy_list.dialogs.AddBuyListDialog
import com.angogasapps.myfamily.ui.screens.buy_list.BuyListManager
import com.angogasapps.myfamily.models.buy_list.BuyListEvent
import com.angogasapps.myfamily.network.repositories.BuyListRepository
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class ListOfBuyListsFragment : Fragment() {
    private lateinit var binding: FragmentListOfBuyListsBinding
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var adapter: BuyListAdapter

    @Inject
    lateinit var buyListRepository: BuyListRepository
    @Inject
    lateinit var buyListManager: BuyListManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        appComponent.inject(this)
        binding = FragmentListOfBuyListsBinding.inflate(inflater, container, false)
        initOnClickListeners()
        initObserver()
        initRecyclerView()
        return binding.root
    }

    private fun initOnClickListeners() {
        binding.floatingBtn.setOnClickListener {
            AddBuyListDialog(requireContext()).show()
        }
    }

    private fun initRecyclerView() {
        adapter = BuyListAdapter(requireContext(), buyListRepository.buyLists)
        layoutManager = GridLayoutManager(context, 2)
        binding.recycleView.layoutManager = layoutManager
        binding.recycleView.adapter = adapter
    }

    private fun initObserver() {
        lifecycleScope.launch {
            buyListRepository.listener.collect {
                adapter.update(it)
            }
        }
    }
}