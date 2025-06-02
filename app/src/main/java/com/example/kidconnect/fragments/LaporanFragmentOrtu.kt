package com.example.kidconnect.fragments

import android.content.Intent
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
import com.example.kidconnect.DetailLaporanActivity
import com.example.kidconnect.OrtuActivity
import com.example.kidconnect.R
import com.example.kidconnect.adapter.CalendarAdapter
import com.example.kidconnect.model.LaporanItem
import com.example.kidconnect.model.CalendarItem
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class LaporanFragmentOrtu : Fragment() {

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
            (requireActivity() as OrtuActivity).switchToHome()
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
        val formattedMonth = monthFormat.format(date)
        textBulan.text = formattedMonth
        textBulan.setTextColor(Color.WHITE)
    }

    private fun loadAktivitas(aktivitasList: List<LaporanItem>) {
        val layoutAktivitasContainer = view?.findViewById<LinearLayout>(R.id.layoutAktivitasContainer)
        layoutAktivitasContainer?.removeAllViews()

        val inflater = LayoutInflater.from(requireContext())

        for (item in aktivitasList) {
            val card = inflater.inflate(R.layout.item_laporan, layoutAktivitasContainer, false)

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

            layoutAktivitasContainer?.addView(card)
        }
    }

    private fun getLaporanFromFirebase(tanggalKey: String) {
        val uidOrtu = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val database = FirebaseDatabase.getInstance().reference

        // 1. Ambil namaAnak dari Users/{uidOrtu}/namaAnak
        database.child("Users").child(uidOrtu).child("namaAnak").get()
            .addOnSuccessListener { namaAnakSnapshot ->
                val namaAnak = namaAnakSnapshot.value?.toString()
                if (namaAnak.isNullOrEmpty()) {
                    loadAktivitas(emptyList())
                    return@addOnSuccessListener
                }

                // 2. Cari muridId di Kelas/A/murid yang nama-nya sama dengan namaAnak
                database.child("Kelas").child("A").child("murid").get()
                    .addOnSuccessListener { muridSnapshot ->
                        var muridId: String? = null

                        for (murid in muridSnapshot.children) {
                            val namaMurid = murid.child("nama").value?.toString()
                            if (namaMurid == namaAnak) {
                                muridId = murid.key
                                break
                            }
                        }

                        if (muridId == null) {
                            loadAktivitas(emptyList())
                            return@addOnSuccessListener
                        }

                        // 3. Ambil laporan dari Laporan/{muridId}/{tanggalKey}
                        database.child("Laporan").child(muridId).child(tanggalKey)
                            .get()
                            .addOnSuccessListener { laporanSnapshot ->
                                val laporanList = mutableListOf<LaporanItem>()
                                if (!laporanSnapshot.exists()) {
                                    loadAktivitas(emptyList())
                                    return@addOnSuccessListener
                                }

                                for (laporan in laporanSnapshot.children) {
                                    val waktu = laporan.child("waktu").value?.toString() ?: "-"
                                    val judul = laporan.child("judul").value?.toString() ?: "-"
                                    val pengajar = laporan.child("pengajar").value?.toString() ?: "-"
                                    val deskripsi = laporan.child("deskripsi").value?.toString() ?: "-"
                                    laporanList.add(LaporanItem(waktu, judul, pengajar, deskripsi, R.drawable.car))
                                }

                                loadAktivitas(laporanList)
                            }
                            .addOnFailureListener {
                                loadAktivitas(emptyList())
                            }
                    }
            }
            .addOnFailureListener {
                loadAktivitas(emptyList())
            }
    }

}