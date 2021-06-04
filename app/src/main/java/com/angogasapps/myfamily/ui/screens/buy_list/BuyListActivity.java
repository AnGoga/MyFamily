package com.angogasapps.myfamily.ui.screens.buy_list;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.databinding.ActivityByuListBinding;
import com.angogasapps.myfamily.models.buy_list.BuyList;

import es.dmoral.toasty.Toasty;

public class BuyListActivity extends AppCompatActivity {
    private ActivityByuListBinding binding;
    private ListOfBuyListsFragment fragment1;
    private BuyListFragment fragment2;
    private BuyListManager buyListManager;

    private static IGoToBuyListFragment iGoToBuyListFragment;
    private static IGOToListOfBuyListsFragment igoToListOfBuyListsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityByuListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        buyListManager = BuyListManager.getInstance();

        fragment1 = new ListOfBuyListsFragment();
        fragment2 = new BuyListFragment();

        iGoToBuyListFragment = (buyList) -> {
            fragment2.setBuyList(buyList);
            getSupportFragmentManager()
                    .beginTransaction().replace(R.id.container, fragment2).addToBackStack("").commit();
        };

        igoToListOfBuyListsFragment = () -> {
            getSupportFragmentManager().beginTransaction().remove(fragment2).commit();
            Toasty.error(this, R.string.this_buy_list_was_remove).show();
            getSupportFragmentManager().beginTransaction().add(R.id.container, fragment1).commit();
        };


        getSupportFragmentManager()
                .beginTransaction().add(R.id.container, fragment1).commit();

    }

    public static IGoToBuyListFragment getIGoToBuyListFragment(){
        return iGoToBuyListFragment;
    }
    public static IGOToListOfBuyListsFragment getIgoToListOfBuyListsFragment(){
        return igoToListOfBuyListsFragment;
    }

    public interface IGoToBuyListFragment{
        void go(BuyList data);
    }
    public interface IGOToListOfBuyListsFragment{
        void go();
    }
}