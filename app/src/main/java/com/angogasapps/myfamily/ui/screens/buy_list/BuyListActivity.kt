package com.angogasapps.myfamily.ui.screens.buy_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.databinding.ActivityByuListBinding
import com.angogasapps.myfamily.models.buy_list.BuyList
import es.dmoral.toasty.Toasty

class BuyListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityByuListBinding
    private lateinit var fragment1: ListOfBuyListsFragment
    private lateinit var fragment2: BuyListFragment
    private lateinit var buyListManager: BuyListManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityByuListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        buyListManager = BuyListManager
        fragment1 = ListOfBuyListsFragment()
        fragment2 = BuyListFragment()
        initInterfaces()
        setUpToolBar()
        supportFragmentManager.beginTransaction().add(R.id.container, fragment1).commit()
    }

    private fun setUpToolBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    private fun initInterfaces() {
        iGoToBuyListFragment = {
            fragment2.buyList = it
            supportFragmentManager
                .beginTransaction().replace(R.id.container, fragment2).addToBackStack("").commit()
        }
        igoToListOfBuyListsFragment = {
            supportFragmentManager.beginTransaction().remove(fragment2).commit()
            Toasty.error(this, R.string.this_buy_list_was_remove).show()
            supportFragmentManager.beginTransaction().add(R.id.container, fragment1).commit()
            setTitle(R.string.app_name)
        }
    }

    override fun onBackPressed() {
        setTitle(R.string.app_name)
        super.onBackPressed()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    companion object {
        lateinit var iGoToBuyListFragment: (buyList: BuyList) -> Unit
        lateinit var igoToListOfBuyListsFragment: () -> Unit
    }
}