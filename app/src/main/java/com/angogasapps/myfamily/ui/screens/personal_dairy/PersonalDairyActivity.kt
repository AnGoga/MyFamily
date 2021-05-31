package com.angogasapps.myfamily.ui.screens.personal_dairy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.angogasapps.myfamily.database.DatabaseManager
import com.angogasapps.myfamily.databinding.ActivityPersonalDairyBinding
import com.angogasapps.myfamily.models.DairyObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class PersonalDairyActivity : AppCompatActivity() {
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

    private fun initRecyclerView() {
        layoutManager = LinearLayoutManager(this)
        adapter = DairyAdapter(this, ArrayList())

        binding.recycleView.adapter = adapter
        binding.recycleView.layoutManager = layoutManager

        GlobalScope.launch {
            setDataFromRoom()
        }
    }

    private suspend fun setDataFromRoom(){
        coroutineScope {
            var dairyList: List<DairyObject?>?
            var arrayList: ArrayList<DairyObject?> = ArrayList()
            launch(Dispatchers.Default) {
                dairyList = DatabaseManager.getInstance().dairyDao.getAll()

                if (dairyList.isNullOrEmpty()) {
                    return@launch
                }else {
                    arrayList = ArrayList(dairyList!!.toMutableList())
                    arrayList.forEachIndexed { index, it -> if (it == null) arrayList.removeAt(index) }
                }
            }

            launch(Dispatchers.Main) {
                adapter.addAll(arrayList as List<DairyObject>)
            }
        }
    }
}
