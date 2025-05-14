package com.example.kidconnect.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kidconnect.R
import com.example.kidconnect.model.CalendarItem
import java.util.*

class CalendarAdapter(
    private var items: List<CalendarItem>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<CalendarAdapter.CalendarViewHolder>() {

    inner class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dayText: TextView = view.findViewById(R.id.textDay)
        val dateText: TextView = view.findViewById(R.id.textDate)
        val background: LinearLayout = view.findViewById(R.id.dateContainer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar, parent, false)
        return CalendarViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val item = items[position]
        holder.dayText.text = item.dayName
        holder.dateText.text = item.date.toString()

        // Ubah warna teks menjadi putih secara default
        holder.dayText.setTextColor(Color.WHITE)
        holder.dateText.setTextColor(Color.WHITE)

        // Ubah background dan teks untuk item yang terpilih
        if (item.isSelected) {
            holder.background.setBackgroundColor(Color.WHITE) // Background putih saat terpilih
            holder.dayText.setTextColor(Color.parseColor("#259E73"))  // Teks berwarna hijau (#259E73)
            holder.dateText.setTextColor(Color.parseColor("#259E73"))  // Teks berwarna hijau (#259E73)
        } else {
            holder.background.setBackgroundResource(android.R.color.transparent)  // Background transparan jika tidak terpilih
        }

        holder.itemView.setOnClickListener { onItemClick(position) }
    }

    fun updateSelection(newPosition: Int) {
        items = items.mapIndexed { index, item ->
            item.copy(isSelected = index == newPosition)
        }
        notifyDataSetChanged()  // Memberitahukan perubahan pada RecyclerView
    }

    // Menambahkan fungsi untuk mendapatkan date pada posisi tertentu
    fun getDateAt(position: Int): Date {
        return items[position].dateObj  // Mengembalikan objek Date yang benar
    }
}
