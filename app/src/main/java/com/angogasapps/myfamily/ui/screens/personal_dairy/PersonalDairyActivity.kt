package com.angogasapps.myfamily.ui.screens.personal_dairy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.angogasapps.myfamily.database.DatabaseManager
import com.angogasapps.myfamily.databinding.ActivityPersonalDairyBinding
import com.angogasapps.myfamily.models.DairyObject
import com.angogasapps.myfamily.utils.Permissions
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PersonalDairyActivity : AppCompatActivity() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    private lateinit var binding: ActivityPersonalDairyBinding
    private lateinit var adapter: DairyAdapter
    private lateinit var layoutManager: LinearLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalDairyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floatingBtn.setOnClickListener {
            startActivity(Intent(this, DairyBuilderActivity::class.java))
        }

        initRecyclerView()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private fun initRecyclerView() {
        layoutManager = LinearLayoutManager(this)
        adapter = DairyAdapter(this, ArrayList())

        binding.recycleView.adapter = adapter
        binding.recycleView.layoutManager = layoutManager

        scope.launch {
            setDataFromRoom()
        }
    }

    private suspend fun setDataFromRoom() = withContext(Dispatchers.Default){
        val dairyList: List<DairyObject?>? = DatabaseManager.getInstance().dairyDao.getAll()
        if (dairyList.isNullOrEmpty()) {
            return@withContext
        }

        val arrayList = ArrayList(dairyList.toMutableList())
        arrayList.forEachIndexed { index, it -> if (it == null) arrayList.removeAt(index) }
        withContext(Dispatchers.Main) {
            adapter.addAll(arrayList as List<DairyObject>)
        }
    }
}
