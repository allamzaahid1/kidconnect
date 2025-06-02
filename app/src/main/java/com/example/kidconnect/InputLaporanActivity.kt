package com.example.kidconnect

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class InputLaporanActivity : AppCompatActivity() {

    private lateinit var btnBack: View
    private lateinit var btnNotif: View

    private lateinit var etNamaAktivitas: EditText
    private lateinit var etTanggal: EditText
    private lateinit var etPengajar: EditText
    private lateinit var etDesc: EditText

    private lateinit var btnSimpan: ImageButton
    private lateinit var btnBatal: ImageButton

    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.input_laporan_gr)

        // Inisialisasi view
        btnBack = findViewById(R.id.btnBack)
        btnNotif = findViewById(R.id.notiflaporan)

        etNamaAktivitas = findViewById(R.id.etnamaaktivitas)
        etTanggal = findViewById(R.id.ettanggal)
        etPengajar = findViewById(R.id.etpengajar)
        etDesc = findViewById(R.id.etdesc)

        btnSimpan = findViewById(R.id.simpan)
        btnBatal = findViewById(R.id.batal)

        btnBack.setOnClickListener {
            finish()
        }

        btnNotif.setOnClickListener {
            Toast.makeText(this, "Notifikasi dibuka", Toast.LENGTH_SHORT).show()
            // TODO: Navigasi ke fragment notifikasi
        }

        // Setup DatePicker saat klik pada etTanggal
        etTanggal.setOnClickListener {
            showDatePickerDialog()
        }
        // juga bisa setup agar etTanggal tidak bisa diedit manual
        etTanggal.keyListener = null

        btnBatal.setOnClickListener {
            finish() // kembali tanpa simpan
        }

        btnSimpan.setOnClickListener {
            simpanLaporan()
        }
    }

    private fun showDatePickerDialog() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val dpd = DatePickerDialog(this, { _, y, m, d ->
            // Update tanggal ke EditText dalam format "12 Januari 2025"
            calendar.set(y, m, d)
            val format = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
            etTanggal.setText(format.format(calendar.time))
        }, year, month, day)

        dpd.show()
    }

    private fun simpanLaporan() {
        val namaAktivitas = etNamaAktivitas.text.toString().trim()
        val tanggal = etTanggal.text.toString().trim()
        val pengajar = etPengajar.text.toString().trim()
        val deskripsi = etDesc.text.toString().trim()

        // Validasi sederhana
        if (namaAktivitas.isEmpty()) {
            etNamaAktivitas.error = "Nama aktivitas harus diisi"
            etNamaAktivitas.requestFocus()
            return
        }

        if (tanggal.isEmpty()) {
            etTanggal.error = "Tanggal harus diisi"
            etTanggal.requestFocus()
            return
        }

        if (pengajar.isEmpty()) {
            etPengajar.error = "Nama pengajar harus diisi"
            etPengajar.requestFocus()
            return
        }

        if (deskripsi.isEmpty()) {
            etDesc.error = "Deskripsi harus diisi"
            etDesc.requestFocus()
            return
        }

        // Kalau sudah valid, misal tampilkan Toast sebagai contoh
        Toast.makeText(
            this,
            "Laporan disimpan:\n$namaAktivitas\n$tanggal\n$pengajar\n$deskripsi",
            Toast.LENGTH_LONG
        ).show()

        // TODO: Simpan data ke database atau kirim ke server di sini

        // Setelah simpan selesai, bisa finish atau reset form
        finish()
    }
}