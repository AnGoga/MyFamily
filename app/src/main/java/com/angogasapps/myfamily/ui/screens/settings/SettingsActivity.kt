package com.angogasapps.myfamily.ui.screens.settings

import com.angogasapps.myfamily.async.notification.FcmMessageManager.setPermissionToGetChatNotifications
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.Preference
import com.angogasapps.myfamily.ui.screens.settings.SettingsActivity.SettingFragment
import androidx.preference.PreferenceFragmentCompat
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.async.notification.FcmMessageManager
import com.angogasapps.myfamily.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .add(binding.settingsContainer.id, SettingFragment())
            .commit()
    }

    class SettingFragment : PreferenceFragmentCompat() {
        private lateinit var chatNotificationPref: CheckBoxPreference

        override fun onCreatePreferences(savedInstanceState: Bundle, rootKey: String) {
            addPreferencesFromResource(R.xml.settings)
            chatNotificationPref = findPreference("chat_notification")!!
            chatNotificationPref.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                setPermissionToGetChatNotifications(chatNotificationPref.isChecked)
                false
            }
        }
    }
}