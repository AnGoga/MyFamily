package com.angogasapps.myfamily.ui.screens.family_clock

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateUtils
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.angogasapps.myfamily.R
import com.angogasapps.myfamily.databinding.ActivityAlarmClockBuilderBinding
import com.angogasapps.myfamily.firebase.FirebaseVarsAndConsts.USER
import com.angogasapps.myfamily.firebase.sendFamilyClock
import com.angogasapps.myfamily.models.family_clock.ClockObject
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import java.util.*


class AlarmClockBuilderActivity : AppCompatActivity() {
    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Default + job)

    lateinit var binding: ActivityAlarmClockBuilderBinding
    private val calendar: Calendar = Calendar.getInstance()
    lateinit var layoutManager: LinearLayoutManager
    lateinit var adapter: AlarmFamilyMembersAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmClockBuilderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFields()
        initRecyclerView()
        initOnClicks()
    }

    private fun initFields() {
        initDateText()
        initTimeText()
    }

    private fun initRecyclerView() {
        layoutManager = LinearLayoutManager(this)
        adapter = AlarmFamilyMembersAdapter(this)
        binding.recycleView.layoutManager = layoutManager
        binding.recycleView.adapter = adapter
    }

    private fun initOnClicks() {
        binding.datePickBtn.setOnClickListener { showDatePickerDialog() }
        binding.timePickBtn.setOnClickListener { showTimePickerDialog() }
        binding.saveBtn.setOnClickListener { onSaveButtonClick(it) }
    }

    private fun initDateText(){
        binding.dateText.text = DateUtils.formatDateTime(this, calendar.timeInMillis,
                DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR)
    }

    private fun initTimeText(){
        binding.timeText.text = DateUtils.formatDateTime(this, calendar.timeInMillis,
                DateUtils.FORMAT_SHOW_TIME)
    }

    private fun showDatePickerDialog() {
        val onDataSet: (view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) -> Unit = { view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, monthOfYear);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            initDateText()
        }

        DatePickerDialog(this, onDataSet,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH))
                .show()
    }

    private fun showTimePickerDialog() {
        val onTimeSet: (view: TimePicker, hourOfDay: Int, minute: Int) -> Unit = { view, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            initTimeText()
        }

        TimePickerDialog(this, onTimeSet,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true)
                .show()
    }

    private fun onSaveButtonClick(view: View){
        val obj: ClockObject = buildClockObject()
        if (obj.time <= System.currentTimeMillis()){
            Toasty.warning(this, getString(R.string.selected_time_is_end)).show()
            return
        }
        if (obj.to.size == 0){
            Toasty.warning(this, getString(R.string.notone_not_selected)).show()
            return
        }
        sendFamilyClock(obj, scope)
        this.finish()
    }

    private fun buildClockObject() = ClockObject(
                "",
                adapter.getIdOfCheckedUsers(),
                USER.phone,
                USER.id,
                calendar.timeInMillis
        )
}
