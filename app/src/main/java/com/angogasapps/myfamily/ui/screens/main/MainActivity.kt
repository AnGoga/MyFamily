package com.angogasapps.myfamily.ui.screens.main


import androidx.appcompat.app.AppCompatActivity
import com.angogasapps.myfamily.ui.screens.main.adapters.MainActivityAdapter
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import android.os.Bundle
import com.angogasapps.myfamily.async.LoadFamilyThread
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts
import com.angogasapps.myfamily.ui.screens.main.cards.MainActivityUtils
import com.angogasapps.myfamily.ui.screens.main.adapters.ItemTouchHelperCallback
import com.angogasapps.myfamily.utils.Async
import com.angogasapps.myfamily.utils.Async.doInThread
import android.content.Intent
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.angogasapps.myfamily.ui.screens.personal_data.PersonalDataActivity
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.async.notification.FcmMessageManager
import com.angogasapps.myfamily.database.DatabaseManager
import com.angogasapps.myfamily.databinding.ActivityMainBinding
import com.angogasapps.myfamily.ui.screens.splash.SplashActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var cardsAdapter: MainActivityAdapter
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var itemTouchHelper: ItemTouchHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        LoadFamilyThread(this).execute(FirebaseVarsAndConsts.USER)
        initToolbar()
        initRecyclerView()
        initNewsLayout()
        FcmMessageManager.subscribeToTopics()
        //        FcmMessageManager.updateToken();
    }

    private fun initRecyclerView() {
        cardsAdapter = MainActivityAdapter(this, MainActivityUtils.getPreferCardsArray(this))
        layoutManager = GridLayoutManager(this, 2)
        val callback: ItemTouchHelper.Callback = ItemTouchHelperCallback(cardsAdapter)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.recycleView)
        binding.recycleView.adapter = cardsAdapter
        binding.recycleView.layoutManager = layoutManager
    }

    private fun initToolbar() {
        supportActionBar!!.hide()
        binding.toolbar.setOnMenuItemClickListener { item: MenuItem -> onOptionsItemSelected(item) }
        lifecycleScope.launch(Dispatchers.IO) {
            while (!LoadFamilyThread.isEnd) { }
            if (FirebaseVarsAndConsts.USER.userPhoto != null)
                withContext(Dispatchers.Main) {
                    binding.toolbarUserImage.setImageBitmap(FirebaseVarsAndConsts.USER.userPhoto)
            }
        }
        binding.toolbarUserImage.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    this@MainActivity,
                    PersonalDataActivity::class.java
                )
            )
        }
    }

    private fun initNewsLayout() {
        binding.newsCenter.setUpCenter(this, lifecycleScope)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.menu_item_signout) {
            FirebaseVarsAndConsts.AUTH.signOut()

            lifecycleScope.launch(Dispatchers.IO) {
                // reset all managers and services
                DatabaseManager.resetDatabase()
                withContext(Dispatchers.Main) {
                    startActivity(Intent(this@MainActivity, SplashActivity::class.java))
                    finish()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPause() {
        super.onPause()
        MainActivityUtils.savePreferCardPlaces(this, cardsAdapter.list)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.newsCenter.destroyCenter()
    }
}