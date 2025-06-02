package com.example.kidconnect

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.view.LayoutInflater
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kidconnect.model.Murid
import com.google.firebase.database.*

class DetailSiswaActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var btnNotif: ImageButton

    private lateinit var database: DatabaseReference
    private lateinit var linearDaftarSiswa: LinearLayout
    private lateinit var kelastxt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_siswa_gr)

        btnBack = findViewById(R.id.btnBack)
        btnNotif = findViewById(R.id.notifsiswa)

        btnBack.setOnClickListener {
            finish()
        }

        btnNotif.setOnClickListener {
            Toast.makeText(this, "Notifikasi dibuka", Toast.LENGTH_SHORT).show()
            // TODO: Navigasi ke fragment notifikasi
        }

        database = FirebaseDatabase.getInstance().reference
        linearDaftarSiswa = findViewById(R.id.linearDaftarSiswa)
        kelastxt = findViewById(R.id.kelastxt)

        val kelas = intent.getStringExtra("kelas") ?: "A"
        kelastxt.text = "KELAS $kelas"

        getDaftarSiswa(kelas)
        getNamaWali(kelas)

    }

    private fun getDaftarSiswa(kelas: String) {
        val kelasRef = database.child("Kelas").child(kelas).child("murid")

        kelasRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                linearDaftarSiswa.removeAllViews()
                val inflater = LayoutInflater.from(this@DetailSiswaActivity)

                for (muridSnapshot in snapshot.children) {
                    val muridData = muridSnapshot.getValue(Murid::class.java)
                    if (muridData != null) {
                        val viewItem = inflater.inflate(R.layout.item_siswa, linearDaftarSiswa, false)

                        val namaSiswa = viewItem.findViewById<TextView>(R.id.namaSiswa)
                        val iconSiswa = viewItem.findViewById<ImageView>(R.id.iconSiswa)

                        namaSiswa.text = muridData.nama

                        // Set icon berdasarkan gender
                        if (muridData.gender.equals("perempuan", ignoreCase = true)) {
                            iconSiswa.setImageResource(R.drawable.siswa_ce)
                        } else {
                            iconSiswa.setImageResource(R.drawable.siswa_co)
                        }

                        linearDaftarSiswa.addView(viewItem)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetailSiswaActivity, "Gagal mengambil data", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getNamaWali(kelas: String) {
        val kelasRef = database.child("Kelas").child(kelas)

        kelasRef.child("guruId").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val guruId = snapshot.getValue(String::class.java)
                if (!guruId.isNullOrEmpty()) {
                    database.child("Users").child(guruId).child("username")
                        .addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(userSnapshot: DataSnapshot) {
                                val namaWali = userSnapshot.getValue(String::class.java)
                                val txtNamaWali = findViewById<TextView>(R.id.namawalitxt)
                                txtNamaWali.text = namaWali ?: "Tidak diketahui"
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(this@DetailSiswaActivity, "Gagal mengambil nama wali", Toast.LENGTH_SHORT).show()
                            }
                        })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DetailSiswaActivity, "Gagal mengambil guruId", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
