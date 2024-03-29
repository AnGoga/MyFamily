package com.angogasapps.myfamily.ui.screens.personal_dairy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angogasapps.myfamily.app.appComponent
import com.angogasapps.myfamily.databinding.ActivityPersonalDairyBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import javax.inject.Inject

class PersonalDairyActivity : AppCompatActivity() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    private lateinit var binding: ActivityPersonalDairyBinding
    private lateinit var adapter: DairyAdapter
    private lateinit var layoutManager: LinearLayoutManager

    @Inject
    lateinit var viewModel: PersonalDairyViewModel
    private lateinit var channel: ReceiveChannel<DairyEvent>

    init {
        appComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        channel = viewModel.channel.openSubscription()

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
        adapter = DairyAdapter(this, viewModel.list, scope)
        binding.recycleView.adapter = adapter
        binding.recycleView.layoutManager = layoutManager
    }


}
