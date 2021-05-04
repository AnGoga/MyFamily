package com.angogasapps.myfamily.ui.screens.buy_list;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.databinding.ActivityByuListBinding;
import com.angogasapps.myfamily.models.BuyList;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import es.dmoral.toasty.Toasty;

import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.DATABASE_ROOT;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.NODE_BUY_LIST;
import static com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER;

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