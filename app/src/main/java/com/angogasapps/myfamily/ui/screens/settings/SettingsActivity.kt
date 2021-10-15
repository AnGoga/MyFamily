package com.angogasapps.myfamily.ui.screens.settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceActivity;

import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.angogasapps.myfamily.R;
import com.angogasapps.myfamily.app.AppApplication;
import com.angogasapps.myfamily.async.notification.FcmMessageManager;
import com.angogasapps.myfamily.databinding.ActivitySettingsBinding;

import es.dmoral.toasty.Toasty;


public class SettingsActivity extends AppCompatActivity {
    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportFragmentManager().beginTransaction()
                .add(binding.settingsContainer.getId(), new SettingFragment())
                .commit();
    }


    public static class SettingFragment extends PreferenceFragmentCompat{
        private CheckBoxPreference chatNotificationPref;
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            addPreferencesFromResource(R.xml.settings);
            chatNotificationPref = findPreference("chat_notification");

            chatNotificationPref.setOnPreferenceClickListener(preference -> {
                FcmMessageManager.INSTANCE.setPermissionToGetChatNotifications(chatNotificationPref.isChecked());
                return false;
            });
        }
    }


}