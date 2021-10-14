package com.angogasapps.myfamily.ui.screens.findorcreatefamily;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.databinding.ActivityFindOrCreateFamilyBinding;
import com.angogasapps.myfamily.firebase.FindFamilyFunks;
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts;
import com.angogasapps.myfamily.firebase.RegisterFamilyFunks;
import com.angogasapps.myfamily.firebase.interfaces.IOnFindFamily;
import com.angogasapps.myfamily.firebase.interfaces.IOnJoinToFamily;

import com.angogasapps.myfamily.ui.screens.main.MainActivity;
import com.angogasapps.myfamily.ui.screens.splash.SplashActivity;
import com.angogasapps.myfamily.utils.FamilyManager;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import es.dmoral.toasty.Toasty;

public class FindOrCreateFamilyActivity extends AppCompatActivity {
    private String familyIdParam;
    private ActivityFindOrCreateFamilyBinding binding;
    private Uri mFamilyEmblemUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFindOrCreateFamilyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    @Override
    protected void onStart() {
        super.onStart();

        analyzeIntent();
        setJoinBtnOnClick();
        addCreateFamilyOnClick();
    }


    private void analyzeIntent(){
        Intent intent = getIntent();
        familyIdParam = intent.getStringExtra(FamilyManager.PARAM_FAMILY_ID);

        if (familyIdParam != null && !familyIdParam.equals("")){
            showInviteDialog();
        }


    }
    private void showInviteDialog() {

        AlertDialog alertDialog = new Builder(this).setTitle(R.string.you_was_invite_to_family)
                .setMessage(R.string.do_you_want_join_to_invite_family)
                .setPositiveButton(getString(R.string.yes), (dialog, which) -> { joinToInviteFamily();})
                .setNegativeButton(getString(R.string.no), (dialog, which) -> {dialog.dismiss();})
//                .setOnDismissListener(dialog -> startFragments())
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

    public void setJoinBtnOnClick() {
        binding.joinBtn.setOnClickListener(v -> {
            String text = binding.editText.getText().toString();
            if (text.isEmpty()) {
                Toasty.warning(FindOrCreateFamilyActivity.this, R.string.enter_identification).show();
            } else {
                FindFamilyFunks.tryFindFamilyById(text, new IOnFindFamily() {
                    @Override
                    public void onSuccess() {
                        FindFamilyFunks.joinUserToFamily(text, new IOnJoinToFamily() {
                            @Override
                            public void onSuccess() {
                                Toasty.success(FindOrCreateFamilyActivity.this,
                                        R.string.you_success_join_to_you_family).show();

                                FindOrCreateFamilyActivity.this.startActivity(
                                        new Intent(FindOrCreateFamilyActivity.this.getApplicationContext(),
                                        MainActivity.class));
                                FindOrCreateFamilyActivity.this.finish();
                            }

                            @Override
                            public void onFailure() {
                                Toasty.error(FindOrCreateFamilyActivity.this,
                                        R.string.error).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure() {
                        Toasty.error(FindOrCreateFamilyActivity.this, R.string.family_not_found).show();
                    }

                    @Override
                    public void onCancelled() {
                        Toasty.error(FindOrCreateFamilyActivity.this,
                                R.string.you_canceled_searches).show();
                    }
                });
            }
        });
    }

    public void addCreateFamilyOnClick(){
        binding.addEmblemBtn.setOnClickListener(v -> {
            addFamilyEmblem();
        });

        binding.createFamilyButton.setOnClickListener(v -> {
            String familyName = binding.familyNameEditText.getText().toString();
            if (familyName.isEmpty()){
                Toasty.info(this, R.string.enter_family_last_name).show();
                return;
            }
            if(mFamilyEmblemUri == null){
                mFamilyEmblemUri = Uri.EMPTY;
            }
            RegisterFamilyFunks.createNewFamily(this,
                    familyName, mFamilyEmblemUri, () -> {
                        //когда регистрация новой семьи в базе данных прошла успешно
                        Toasty.success(this.getApplicationContext(),
                                R.string.everything_went_well).show();
                        startActivity(new Intent(this, MainActivity.class));
                        this.finish();
                    });
        });
    }

    private void addFamilyEmblem() {
        CropImage.activity().setAspectRatio(1, 1)
                .setRequestedSize(300, 300)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .start(FindOrCreateFamilyActivity.this);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            mFamilyEmblemUri = CropImage.getActivityResult(data).getUri();
            binding.familyEmblemImage.setBackground(null);
            binding.familyEmblemImage.setImageURI(mFamilyEmblemUri);
        }catch (Exception e){
            e.printStackTrace();
            Toasty.error(this, "неизвестная ошибка").show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_item_signout){
            FirebaseVarsAndConsts.AUTH.signOut();
            startActivity(new Intent(this, SplashActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}