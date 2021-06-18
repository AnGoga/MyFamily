package com.angogasapps.myfamily.ui.screens.personal_dairy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angogasapps.myfamily.databinding.ActivityPersonalDairyBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach

class PersonalDairyActivity : AppCompatActivity() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    private lateinit var binding: ActivityPersonalDairyBinding
    private lateinit var adapter: DairyAdapter
    private lateinit var layoutManager: LinearLayoutManager

    private lateinit var manager: PersonalDairyManager
    private lateinit var channel: ReceiveChannel<DairyEvent>

    override fun onCreate(savedInstanceState: Bundle?) {
        manager = PersonalDairyManager.getInstance()
        channel = manager.channel.openSubscription()

        super.onCreate(savedInstanceState)
        binding = ActivityPersonalDairyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.floatingBtn.setOnClickListener {
            startActivity(Intent(this, DairyBuilderActivity::class.java))
        }

        scope.launch {
            subscribeToChannel()
        }
        initRecyclerView()
    }

    private suspend fun subscribeToChannel() {
        channel.consumeEach {
            withContext(Dispatchers.Main) {
                adapter?.update(it)
                println("it -> $it")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        channel.cancel()
    }

    private fun initRecyclerView() {
        layoutManager = object: LinearLayoutManager(this){
            override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
                try {
                    super.onLayoutChildren(recycler, state)
                }catch (e: Exception){
                    Log.w("PersonalDairyActivity", e.stackTrace.toString())
                }
            }
        }
        adapter = DairyAdapter(this, manager.list, scope)
        binding.recycleView.adapter = adapter
        binding.recycleView.layoutManager = layoutManager
    }


}
