package com.angogasapps.myfamily.ui.screens.findorcreatefamily;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.firebase.FindFamilyFunks;
import com.angogasapps.myfamily.firebase.interfaces.IOnFindFamily;
import com.angogasapps.myfamily.firebase.interfaces.IOnJoinToFamily;
import com.angogasapps.myfamily.ui.screens.main.DeprecatedMainActivity;

import com.angogasapps.myfamily.ui.screens.main.MainActivity;
import com.angogasapps.myfamily.utils.FamilyManager;

import es.dmoral.toasty.Toasty;

public class FindOrCreateFamilyActivity extends AppCompatActivity {
    String familyIdParam;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_or_create_family);
    }

    @Override
    protected void onStart() {
        super.onStart();

        analysisIntent();

    }

    private void startFragments(){
        getSupportFragmentManager().beginTransaction()
                .add(R.id.findOrCreateFamilyDataContainer, new FindOrCreateFamilyFragment()).commit();
    }

    private void analysisIntent(){
        Intent intent = getIntent();
        familyIdParam = intent.getStringExtra(FamilyManager.PARAM_FAMILY_ID);

        if (familyIdParam == null){
            startFragments();
            return;
        }
        if (familyIdParam.equals("")) {
            startFragments();
            return;
        }

        showInviteDialog();
    }
    private void showInviteDialog() {

        AlertDialog alertDialog = new Builder(this).setTitle(R.string.you_was_invite_to_family)
                .setMessage(R.string.do_you_want_join_to_invite_family)
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> { joinToInviteFamily();})
                .setNegativeButton(getString(R.string.no), (dialog, which) -> {dialog.dismiss();})
                .setOnDismissListener(dialog -> startFragments())
                .create();
        alertDialog.show();
    }

    private void joinToInviteFamily(){
        FindFamilyFunks.tryFindFamilyById(familyIdParam, new IOnFindFamily() {
            @Override
            public void onSuccess() {
                FindFamilyFunks.joinUserToFamily(familyIdParam, new IOnJoinToFamily() {
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(FindOrCreateFamilyActivity.this, MainActivity.class));
                        FindOrCreateFamilyActivity.this.finish();
                    }
                    @Override
                    public void onFailure() {
                        startFragments();
                    }
                });
            }
            @Override
            public void onFailure() {
                familyNotFound();
            }
            @Override
            public void onCancelled() {
                familyNotFound();
            }
        });
    }

    private void familyNotFound(){
        Toasty.error(this, R.string.link_is_not_correct).show();
    }

}