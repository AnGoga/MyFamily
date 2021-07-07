package com.angogasapps.myfamily.ui.screens.family_clock

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.databinding.ActivityFamilyClockBinding
import com.angogasapps.myfamily.firebase.EFirebaseEvents
import com.angogasapps.myfamily.models.family_clock.ClockObject
import com.angogasapps.myfamily.models.family_clock.FamilyClockEvent
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.consumeEach
import kotlin.coroutines.coroutineContext

class FamilyClockActivity : AppCompatActivity() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)
    private val manager = FamilyClockManager.getInstance(scope)
    private lateinit var binding: ActivityFamilyClockBinding
    lateinit var channel: ReceiveChannel<FamilyClockEvent>

    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var adapter: ClockAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFamilyClockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        initSubscribe()
        initOnClicks()
    }

    private fun initOnClicks() {
        binding.floatingBtn.setOnClickListener {
            startActivity(Intent(this, AlarmClockBuilderActivity::class.java))
        }
    }

    private fun initSubscribe() = scope.launch{
        channel = manager.channel.openSubscription()
        channel.consumeEach {
            withContext(Dispatchers.Main) {
                onGetNewClockEvent(it)
            }
        }
    }

    private fun initRecyclerView() {
        adapter = ClockAdapter(this)
        layoutManager = LinearLayoutManager(this)
        binding.recycleView.adapter = adapter
        binding.recycleView.layoutManager = layoutManager
    }

    private fun onGetNewClockEvent(obj: FamilyClockEvent) {
        adapter.update(obj)
        when(obj.event){
            EFirebaseEvents.added -> {  }
            EFirebaseEvents.changed -> {  }
            EFirebaseEvents.removed -> {  }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        FamilyClockManager.killManager()
        channel.cancel()
        job.cancel()
    }
}