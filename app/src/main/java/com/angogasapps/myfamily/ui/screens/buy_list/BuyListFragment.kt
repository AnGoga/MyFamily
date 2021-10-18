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
import com.angogasapps.myfamily.databinding.FragmentBuyListBinding
import com.angogasapps.myfamily.ui.screens.buy_list.dialogs.BuyListProductCreatorDialog
import com.angogasapps.myfamily.ui.screens.buy_list.BuyListManager
import com.angogasapps.myfamily.models.buy_list.BuyListEvent
import com.angogasapps.myfamily.ui.screens.buy_list.BuyListActivity
import io.reactivex.disposables.Disposable

class BuyListFragment : Fragment() {
    private lateinit var binding: FragmentBuyListBinding
    lateinit var buyList: BuyList
    private lateinit var adapter: ProductsAdapter
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var observer: Disposable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBuyListBinding.inflate(inflater, container, false)
        initOnClickListeners()
        initObserver()
        initRecyclerView()
        requireActivity().title = buyList.name
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if (!observer.isDisposed) observer.dispose()
    }

    private fun initOnClickListeners() {
        binding.floatingBtn.setOnClickListener { v: View? ->
            BuyListProductCreatorDialog(
                requireContext(), buyList.id
            ).show()
        }
    }

    private fun initObserver() {
        observer = BuyListManager.subject.subscribe { event: BuyListEvent ->
            if (event.event == BuyListEvent.EBuyListEvents.buyListRemoved) {
                if (event.buyListId == buyList.id) {
                    onRemoveThisBuyList()
                    return@subscribe
                }
            }
            if (event.buyListId != buyList.id) {
                return@subscribe
            }
            adapter.update(event)
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