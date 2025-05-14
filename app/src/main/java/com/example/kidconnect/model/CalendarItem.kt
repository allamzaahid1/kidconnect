package com.example.kidconnect.model
import java.util.Date

data class CalendarItem(
    val dayName: String,  // Nama hari (Sen, Sel, dll)
    val date: Int,        // Tanggal (misalnya: 3)
    val dateObj: Date,    // Objek Date untuk tanggal tersebut
    val isSelected: Boolean
)