package com.example.photo_calendar

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.GridLayoutManager
import com.example.photo_calendar.databinding.ActivityCalendarBinding
import layout.CalendarUtil.Companion.selectedDate
import java.io.IOException
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter


class CalendarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCalendarBinding
    private lateinit var calendarAdapter: CalendarAdapter

    private val launcher: ActivityResultLauncher<Intent> = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedImageUri: Uri? = result.data?.data
            val bitmap: Bitmap

            if(selectedImageUri != null){
                if(Build.VERSION.SDK_INT >= 29){
                    val source: ImageDecoder.Source = ImageDecoder.createSource(contentResolver, selectedImageUri)
                    try{
                        bitmap = ImageDecoder.decodeBitmap(source)
                        calendarAdapter.setSelectedImage(bitmap)
                    } catch (e: IOException){
                        e.printStackTrace()
                    }
                } else {
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)
                        calendarAdapter.setSelectedImage(bitmap)
                    } catch (e: IOException){
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        selectedDate = LocalDate.now()

        setMonthView()

        binding.preBtn.setOnClickListener {
            selectedDate = selectedDate.minusMonths(1)
            setMonthView()
        }

        binding.nextBtn.setOnClickListener {
            selectedDate = selectedDate.plusMonths(1)
            setMonthView()
        }
    }

    private fun monthYearFromDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("MM월 yyyy")
        return date.format(formatter)
    }

//    private fun yearMonthFromDate(date: LocalDate): String {
//        val formatter = DateTimeFormatter.ofPattern("yyyy 년 MM월")
//        return date.format(formatter)
//    }

    private fun setMonthView() {
        binding.monthYearText.text = monthYearFromDate(selectedDate)
        val dayList = daysInMonthArray(selectedDate)
        calendarAdapter = CalendarAdapter(dayList, launcher)
        val manager = GridLayoutManager(applicationContext, 7)
        binding.recyclerView.layoutManager = manager
        binding.recyclerView.adapter = calendarAdapter
    }

    private fun daysInMonthArray(date: LocalDate): ArrayList<LocalDate?> {
        val dayList = ArrayList<LocalDate?>()
        val yearMonth = YearMonth.from(date)
        val lastDay = yearMonth.lengthOfMonth()
        val firstDay = date.withDayOfMonth(1)
        val dayOfWeek = firstDay.dayOfWeek.value

        for (i in 1 until 42) {
            if (i <= dayOfWeek || i > lastDay + dayOfWeek) {
                dayList.add(null)
            } else {
                dayList.add(LocalDate.of(date.year, date.month, i - dayOfWeek))
            }
        }

        return dayList
    }
}
