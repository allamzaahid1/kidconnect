package com.example.kidconnect

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
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

        DatePickerDialog(this, { _, y, m, d ->
            calendar.set(y, m, d)
            val format = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
            etTanggal.setText(format.format(calendar.time))
        }, year, month, day).show()
    }

    private fun simpanLaporan() {
        val namaAktivitas = etNamaAktivitas.text.toString().trim()
        val tanggal = etTanggal.text.toString().trim()
        val pengajar = etPengajar.text.toString().trim()

        val deskripsi = etDesc.text.toString().trim()

        if (namaAktivitas.isEmpty() || tanggal.isEmpty() || pengajar.isEmpty() || deskripsi.isEmpty()) {
            Toast.makeText(this, "Harap lengkapi semua data", Toast.LENGTH_SHORT).show()
            return
        }

        val uidGuru = FirebaseAuth.getInstance().currentUser?.uid ?: run {
            Toast.makeText(this, "User belum login", Toast.LENGTH_SHORT).show()
            return
        }

        val sdfInput = SimpleDateFormat("dd MMMM yyyy", Locale("id", "ID"))
        val sdfOutput = SimpleDateFormat("yyyy-MM-dd", Locale("id", "ID"))
        val tanggalFormat = try {
            sdfOutput.format(sdfInput.parse(tanggal)!!)
        } catch (e: Exception) {
            tanggal
        }

        val laporanData = mapOf(
            "waktu" to "08:00",
            "judul" to namaAktivitas,
            "pengajar" to pengajar,
            "deskripsi" to deskripsi
        )

        val dbRef = FirebaseDatabase.getInstance().reference

        // Simpan untuk murid seperti sebelumnya
        dbRef.child("Kelas").get().addOnSuccessListener { kelasSnapshot ->
            for (kelas in kelasSnapshot.children) {
                val guruId = kelas.child("guruId").value?.toString()
                if (guruId == uidGuru) {
                    val muridRef = kelas.child("murid")
                    for (murid in muridRef.children) {
                        val muridId = murid.key
                        if (muridId != null) {
                            dbRef.child("Laporan")
                                .child(muridId)
                                .child(tanggalFormat)
                                .push()
                                .setValue(laporanData)
                        }
                    }
                }
            }
            // Tambah simpan laporan di node LaporanGuru untuk guru yang login
            dbRef.child("LaporanGuru")
                .child(uidGuru)
                .child(tanggalFormat)
                .push()
                .setValue(laporanData)

            Toast.makeText(this, "Laporan disimpan untuk semua murid dan guru", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Gagal menyimpan laporan: ${it.message}", Toast.LENGTH_LONG).show()
        }
    }

}