package com.example.photo_calendar

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.recyclerview.widget.RecyclerView
import layout.CalendarUtil
import java.time.LocalDate

class CalendarAdapter(private val dayList: ArrayList<LocalDate?>, private val launcher: ActivityResultLauncher<Intent>) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {
    private lateinit var recyclerView: RecyclerView

    inner class CalendarViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayText: TextView = itemView.findViewById(R.id.dayText)
        val parentView: View = itemView.findViewById(R.id.parentView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.calendar_item, parent, false)
        recyclerView = parent.findViewById(R.id.recyclerView)

        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val day = dayList[position]

        if (day == null) {
            holder.dayText.text = ""
        } else {
            holder.dayText.text = day.dayOfMonth.toString()

//            if (day == CalendarUtil.selectedDate) {
//                holder.parentView.setBackgroundResource(R.drawable.ic_launcher_background)
//                holder.dayText.setTextColor(Color.WHITE)
//            } else {
//                holder.parentView.setBackgroundResource(0)
//                holder.dayText.setTextColor(Color.BLACK)
//            }
        }


        holder.itemView.setOnClickListener {
            val day = dayList[position]

            if (day != null) {
                val iYear = day.year
                val iMonth = day.monthValue
                val iDay = day.dayOfMonth

                val yearMonDay = "$iYear 년 $iMonth 월 $iDay 일"
                Toast.makeText(holder.itemView.context, yearMonDay, Toast.LENGTH_SHORT).show()

//                var prevDate = CalendarUtil.selectedDate
                CalendarUtil.selectedDate = LocalDate.of(iYear, iMonth, iDay)

//                if (prevDate != null) {
//                    val prevPosition = dayList.indexOf(prevDate)
//                    if (prevPosition != -1) {
//                        notifyItemChanged(prevPosition)
//                    }
//                }
//                holder.parentView.setBackgroundResource(R.drawable.ic_launcher_background)

                openGalleryForImage()
            }
        }
    }

    private fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        launcher.launch(intent)
    }

    fun setSelectedImage(imageBitmap: Bitmap){
        val holder = recyclerView.findViewHolderForAdapterPosition(dayList.indexOf(CalendarUtil.selectedDate)) as CalendarViewHolder
        holder.parentView.background = BitmapDrawable(holder.itemView.resources, imageBitmap)
        holder.dayText.setTextColor(Color.parseColor("#80000000"))
    }

    override fun getItemCount(): Int {
        return dayList.size
    }

}
