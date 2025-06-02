package com.example.kidconnect.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.kidconnect.FeedbackActivity
import com.example.kidconnect.OrtuActivity
import com.example.kidconnect.R
import com.example.kidconnect.model.HomeItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HomeFragmentOrtu : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var tvName: TextView
    private lateinit var tvAnnouncement: TextView
    private lateinit var tvTodayActivity: TextView
    private lateinit var tvTimeActivity: TextView
    private lateinit var ivBanner: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home_ot, container, false)

        tvName = view.findViewById(R.id.tvName)
        tvAnnouncement = view.findViewById(R.id.tvAnnouncement)
        tvTodayActivity = view.findViewById(R.id.tvTodayActivity)
        tvTimeActivity = view.findViewById(R.id.tvTimeActivity)
        ivBanner = view.findViewById(R.id.ivBanner)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        val currentUser = auth.currentUser
        if (currentUser != null) {
            val uid = currentUser.uid

            database.child("Users").child(uid).get().addOnSuccessListener { snapshot ->
                val namaAnak = snapshot.child("namaAnak").value?.toString() ?: "Anak"
                tvName.text = namaAnak

                // Cari kelas berdasarkan nama anak
                database.child("Kelas").get().addOnSuccessListener { kelasSnapshot ->
                    var kelasAnak: String? = null
                    for (kelas in kelasSnapshot.children) {
                        val muridSnapshot = kelas.child("murid")
                        for (murid in muridSnapshot.children) {
                            val namaMurid = murid.child("nama").value?.toString()
                            if (namaMurid == namaAnak) {
                                kelasAnak = kelas.key
                                break
                            }
                        }
                        if (kelasAnak != null) break
                    }

                    if (kelasAnak != null) {
                        // Ambil pengumuman dari kelas anak
                        database.child("PengumumanPerKelas").child(kelasAnak!!).get()
                            .addOnSuccessListener { pengumumanSnapshot ->
                                val isiPengumuman = pengumumanSnapshot.child("isi").value?.toString()
                                val waktu = pengumumanSnapshot.child("waktu").value?.toString()?.toLongOrNull()
                                val waktuFormatted = waktu?.let { formatWaktu(it) } ?: "-"

                                tvAnnouncement.text = isiPengumuman ?: "Belum ada pengumuman"
                                tvTodayActivity.text = "-"
                                tvTimeActivity.text = waktuFormatted
                            }
                            .addOnFailureListener {
                                tvAnnouncement.text = "Gagal memuat pengumuman"
                            }
                    } else {
                        tvAnnouncement.text = "Kelas anak tidak ditemukan"
                    }

                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Gagal memuat kelas", Toast.LENGTH_SHORT).show()
                }

            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Gagal memuat data user", Toast.LENGTH_SHORT).show()
            }
        }

        // Dummy image (jika tidak pakai image dari Firebase)
        val homeItem = HomeItem(
            name = "",
            announcement = "",
            todayActivity = "",
            timeActivity = "",
            imageResId = R.drawable.car
        )
        showHomeData(homeItem)

        val btnNotif = view.findViewById<ImageView>(R.id.notifhome)
        val btnPembayaranGedung = view.findViewById<ImageButton>(R.id.homepemgedung)
        val btnKritikSaran = view.findViewById<ImageButton>(R.id.homekritik)
        val btnRiwayat = view.findViewById<ImageButton>(R.id.homeriwayat)

        btnNotif.setOnClickListener {
            Toast.makeText(requireContext(), "Notifikasi dibuka", Toast.LENGTH_SHORT).show()
        }

        btnPembayaranGedung.setOnClickListener {
            (requireActivity() as OrtuActivity).switchToPembayaran()
        }

        btnKritikSaran.setOnClickListener {
            val intent = Intent(requireContext(), FeedbackActivity::class.java)
            startActivity(intent)
        }

        btnRiwayat.setOnClickListener {
            Toast.makeText(requireContext(), "Riwayat Pembayaran dibuka", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun showHomeData(data: HomeItem) {
        tvName.text = data.name
        tvAnnouncement.text = data.announcement
        tvTodayActivity.text = data.todayActivity
        tvTimeActivity.text = data.timeActivity

        Glide.with(this)
            .load(data.imageResId)
            .into(ivBanner)
    }

    private fun formatWaktu(timestamp: Long): String {
        val sdf = java.text.SimpleDateFormat("dd MMM yyyy, HH:mm", java.util.Locale("id", "ID"))
        return sdf.format(java.util.Date(timestamp))
    }

}
