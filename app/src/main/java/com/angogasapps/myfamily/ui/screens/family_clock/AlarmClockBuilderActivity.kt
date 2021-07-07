package com.angogasapps.myfamily.ui.screens.family_clock

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.format.DateUtils
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.angogasapps.myfamily.databinding.ActivityAlarmClockBuilderBinding
import java.util.*


class AlarmClockBuilderActivity : AppCompatActivity() {
    lateinit var binding: ActivityAlarmClockBuilderBinding
    private val calendar: Calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlarmClockBuilderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initFields()
        initOnClicks()
    }

    private fun initFields() {
        initDateText()
        initTimeText()
    }

    private fun initOnClicks() {
        binding.datePickBtn.setOnClickListener { showDatePickerDialog() }
        binding.timePickBtn.setOnClickListener { showTimePickerDialog() }
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
            initTimeText()
        }

        TimePickerDialog(this, onTimeSet,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true)
                .show()
    }
}
