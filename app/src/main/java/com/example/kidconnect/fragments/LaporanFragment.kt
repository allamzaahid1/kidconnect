package com.example.kidconnect.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kidconnect.R
import com.example.kidconnect.adapter.CalendarAdapter
import com.example.kidconnect.model.AktivitasItem
import com.example.kidconnect.model.CalendarItem
import java.text.SimpleDateFormat
import java.util.*

class LaporanFragment : Fragment() {

    private lateinit var btnBack: View
    private lateinit var btnNotif: View

    private lateinit var recyclerViewCalendar: RecyclerView
    private lateinit var layoutAktivitas: LinearLayout
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var textBulan: TextView
    private lateinit var mainInflater: LayoutInflater

    private var calendarList = mutableListOf<CalendarItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_laporan_ot, container, false)

        // Simpan inflater untuk digunakan nanti
        mainInflater = inflater

        recyclerViewCalendar = view.findViewById(R.id.recyclerViewCalendar)
        layoutAktivitas = view.findViewById(R.id.layoutAktivitasContainer)
        textBulan = view.findViewById(R.id.textBulan)

        setupCalendar()

        btnBack = view.findViewById(R.id.btnBack)
        btnNotif = view.findViewById(R.id.btnNotif)

        btnBack.setOnClickListener {
            val fragmentTransaction = requireActivity()
                .supportFragmentManager
                .beginTransaction()
            fragmentTransaction.replace(R.id.container, HomeFragment())
            fragmentTransaction.commit()
        }

        btnNotif.setOnClickListener {
            Toast.makeText(requireContext(), "Notifikasi dibuka", Toast.LENGTH_SHORT).show()
            // TODO: Navigasi ke fragment notifikasi
        }

        return view
    }

    private fun setupCalendar() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY)

        val currentDate = Calendar.getInstance()
        val currentDay = currentDate.get(Calendar.DAY_OF_MONTH)

        calendarList.clear()

        for (i in 0 until 7) {
            val dayName = SimpleDateFormat("EEE", Locale("id", "ID")).format(calendar.time)
            val dateNum = calendar.get(Calendar.DAY_OF_MONTH)
            val dateObj = calendar.time

            val isSelected = dateNum == currentDay

            calendarList.add(CalendarItem(dayName, dateNum, dateObj, isSelected))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        updateMonthText(currentDate.time)

        calendarAdapter = CalendarAdapter(calendarList) { position ->
            calendarAdapter.updateSelection(position)
            updateMonthText(calendarAdapter.getDateAt(position))
            loadAktivitas(position)
        }

        recyclerViewCalendar.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewCalendar.adapter = calendarAdapter

        val selectedPosition = calendarList.indexOfFirst { it.isSelected }
        loadAktivitas(selectedPosition)
    }

    private fun updateMonthText(date: Date) {
        val monthFormat = SimpleDateFormat("MMMM yyyy", Locale("id", "ID"))
        val formattedMonth = monthFormat.format(date)
        textBulan.text = formattedMonth
        textBulan.setTextColor(Color.WHITE)
    }

    private fun loadAktivitas(hariIndex: Int) {
        layoutAktivitas.removeAllViews()

        val aktivitasList = when (hariIndex) {
            1 -> listOf(
                AktivitasItem("07.00", "Senam Pagi", "Ust. Irwan & Ust. Cahya", "Belajar mengenal dan menghafal huruf"),
                AktivitasItem("09.00", "Pengenalan Huruf", "Ust. Irwan & Ust. Cahya", "Belajar mengenal dan menghafal huruf"),
                AktivitasItem("12.00", "Pengenalan Huruf", "Ust. Irwan & Ust. Cahya", "Belajar mengenal dan menghafal huruf")
            )
            else -> listOf(
                AktivitasItem("08.00", "Belajar Mewarnai", "Ust. Dinda", "Latihan koordinasi motorik halus"),
                AktivitasItem("10.00", "Istirahat", "", "Waktu makan dan bermain")
            )
        }

        aktivitasList.forEach {
            val card = LayoutInflater.from(requireContext())
                .inflate(R.layout.item_aktivitas, layoutAktivitas, false)  // INI PERUBAHANNYA
            card.findViewById<TextView>(R.id.textViewJudul).text = it.judul
            card.findViewById<TextView>(R.id.textViewPengajar).text = "(${it.pengajar})"
            card.findViewById<TextView>(R.id.textViewDeskripsi).text = it.deskripsi
            layoutAktivitas.addView(card)
        }
    }
}