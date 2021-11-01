package com.angogasapps.myfamily.ui.screens.buy_list


import com.angogasapps.myfamily.ui.screens.buy_list.BuyListActivity.Companion.igoToListOfBuyListsFragment
import com.angogasapps.myfamily.models.buy_list.BuyList
import com.angogasapps.myfamily.ui.screens.buy_list.adapters.ProductsAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.databinding.FragmentBuyListBinding
import com.angogasapps.myfamily.ui.screens.buy_list.dialogs.BuyListProductCreatorDialog
import com.angogasapps.myfamily.ui.screens.buy_list.BuyListManager
import com.angogasapps.myfamily.models.buy_list.BuyListEvent
import com.angogasapps.myfamily.network.repositories.BuyListRepository
import com.angogasapps.myfamily.ui.screens.buy_list.BuyListActivity
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class BuyListFragment : Fragment() {
    private lateinit var binding: FragmentBuyListBinding
    lateinit var buyList: BuyList
    private lateinit var adapter: ProductsAdapter
    private lateinit var layoutManager: LinearLayoutManager

    @Inject
    lateinit var buyListRepository: BuyListRepository

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        appComponent.inject(this)
        binding = FragmentBuyListBinding.inflate(inflater, container, false)
        initOnClickListeners()
        initObserver()
        initRecyclerView()
        requireActivity().title = buyList.name
        return binding.root
    }


    private fun initOnClickListeners() {
        binding.floatingBtn.setOnClickListener { v: View? ->
            BuyListProductCreatorDialog(
                requireContext(), buyList.id
            ).show()
        }
    }

    private fun initObserver() {
        lifecycleScope.launch {
            buyListRepository.listener.collect { event: BuyListEvent ->
                if (event.event == BuyListEvent.EBuyListEvents.buyListRemoved) {
                    if (event.buyListId == buyList.id) {
                        onRemoveThisBuyList()
                        return@collect
                    }
                }
                if (event.buyListId != buyList.id) {
                    return@collect
                }
                adapter.update(event)
            }
        }
    }

    private fun initRecyclerView() {
        adapter = ProductsAdapter(requireContext(), buyList)
        layoutManager = LinearLayoutManager(context)
        binding.recycleView.layoutManager = layoutManager
        binding.recycleView.adapter = adapter
    }

    private fun onRemoveThisBuyList() {
        igoToListOfBuyListsFragment.invoke()
    }
}