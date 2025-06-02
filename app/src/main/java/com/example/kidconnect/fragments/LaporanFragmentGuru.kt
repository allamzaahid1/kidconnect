package com.example.kidconnect.fragments

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kidconnect.DetailLaporanActivity
import com.example.kidconnect.GuruActivity
import com.example.kidconnect.InputLaporanActivity
import com.example.kidconnect.R
import com.example.kidconnect.adapter.CalendarAdapter
import com.example.kidconnect.model.CalendarItem
import com.example.kidconnect.model.LaporanItem
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class LaporanFragmentGuru : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    private lateinit var btnBack: View
    private lateinit var btnNotif: View

    private lateinit var recyclerViewCalendar: RecyclerView
    private lateinit var layoutAktivitas: LinearLayout
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var textBulan: TextView
    private lateinit var btnTambah: ImageButton

    private var calendarList = mutableListOf<CalendarItem>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_laporan_gr, container, false)

        recyclerViewCalendar = view.findViewById(R.id.recyclerViewCalendar)
        layoutAktivitas = view.findViewById(R.id.layoutAktivitasContainer)
        textBulan = view.findViewById(R.id.textBulan)
        btnTambah = view.findViewById(R.id.addlaporan)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        setupCalendar()

        btnBack = view.findViewById(R.id.btnBack)
        btnNotif = view.findViewById(R.id.btnNotif)

        btnBack.setOnClickListener {
            (requireActivity() as GuruActivity).switchToHome()
        }

        btnNotif.setOnClickListener {
            Toast.makeText(requireContext(), "Notifikasi dibuka", Toast.LENGTH_SHORT).show()
            // TODO: Navigasi ke fragment notifikasi
        }

        btnTambah.setOnClickListener {
            val intent = Intent(requireContext(), InputLaporanActivity::class.java)
            startActivity(intent)
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
            val selectedDate = calendarAdapter.getDateAt(position)
            updateMonthText(selectedDate)

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
            val tanggalKey = dateFormat.format(selectedDate)

            getLaporanFromFirebase(tanggalKey)
        }

        recyclerViewCalendar.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewCalendar.adapter = calendarAdapter

    }

    private fun updateMonthText(date: Date) {
        val monthFormat = SimpleDateFormat("MMMM yyyy", Locale("id", "ID"))
        textBulan.text = monthFormat.format(date)
        textBulan.setTextColor(Color.WHITE)
    }

    private fun loadAktivitas(aktivitasList: List<LaporanItem>) {
        layoutAktivitas.removeAllViews()
        val inflater = LayoutInflater.from(requireContext())

        for (item in aktivitasList) {
            val card = inflater.inflate(R.layout.item_laporan, layoutAktivitas, false)

            val tvJudul = card.findViewById<TextView>(R.id.textViewJudul)
            val tvPengajar = card.findViewById<TextView>(R.id.textViewPengajar)
            val tvDeskripsi = card.findViewById<TextView>(R.id.textViewDeskripsi)

            tvJudul.text = item.judul
            tvPengajar.text = item.pengajar
            tvDeskripsi.text = item.deskripsi

            card.setOnClickListener {
                val intent = Intent(requireContext(), DetailLaporanActivity::class.java).apply {
                    putExtra("judul", item.judul)
                    putExtra("pengajar", item.pengajar)
                    putExtra("deskripsi", item.deskripsi)
                    putExtra("waktu", item.waktu)
                    putExtra("gambarResId", item.gambarResId)
                }
                startActivity(intent)
            }

            layoutAktivitas.addView(card)
        }
    }

    private fun getLaporanFromFirebase(tanggal: String) {
        val uidGuru = auth.currentUser?.uid ?: return
        val laporanList = mutableListOf<LaporanItem>()
        val dbRef = database

        // Bersihkan UI dulu
        layoutAktivitas.removeAllViews()

        dbRef.child("LaporanGuru").child(uidGuru).child(tanggal)
            .get()
            .addOnSuccessListener { laporanSnapshot ->
                if (!laporanSnapshot.exists()) {
                    Toast.makeText(requireContext(), "Tidak ada laporan untuk tanggal ini", Toast.LENGTH_SHORT).show()
                    loadAktivitas(emptyList())
                    return@addOnSuccessListener
                }

                for (laporanSnap in laporanSnapshot.children) {
                    val waktu = laporanSnap.child("waktu").value?.toString() ?: "-"
                    val judul = laporanSnap.child("judul").value?.toString() ?: "-"
                    val pengajar = laporanSnap.child("pengajar").value?.toString() ?: "-"
                    val deskripsi = laporanSnap.child("deskripsi").value?.toString() ?: "-"
                    val laporan = LaporanItem(waktu, judul, pengajar, deskripsi, R.drawable.car)
                    laporanList.add(laporan)
                }

                // Urutkan laporan berdasarkan waktu (opsional)
                laporanList.sortBy { it.waktu }
                loadAktivitas(laporanList)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Gagal mengambil laporan: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

}
