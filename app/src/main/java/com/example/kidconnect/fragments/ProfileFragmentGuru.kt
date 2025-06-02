package com.example.kidconnect.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.kidconnect.GuruActivity
import com.example.kidconnect.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileFragmentGuru : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var tvNamaGuru: TextView
    private lateinit var etKelas: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etEmailAddress: EditText
    private lateinit var etPwd: EditText

    private lateinit var btnSimpan: ImageButton
    private lateinit var btnBatal: ImageButton

    // Variabel untuk menyimpan data asli supaya bisa batal
    private var originalKelas = ""
    private var originalTelepon = ""
    private var originalEmail = ""
    private var originalPassword = ""
    private var originalNamaGuru = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_gr, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        tvNamaGuru = view.findViewById(R.id.profiletxt)
        etKelas = view.findViewById(R.id.etKelas)
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber)
        etEmailAddress = view.findViewById(R.id.etEmailAddress)
        etPwd = view.findViewById(R.id.etPwd)

        btnSimpan = view.findViewById(R.id.simpan)
        btnBatal = view.findViewById(R.id.batal)

        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            (requireActivity() as GuruActivity).switchToHome()
        }

        val btnNotif = view.findViewById<ImageButton>(R.id.notifprofile)
        btnNotif.setOnClickListener {
            Toast.makeText(requireContext(), "Notifikasi dibuka", Toast.LENGTH_SHORT).show()
        }

        loadProfileData()

        btnSimpan.setOnClickListener {
            saveProfileData()
        }

        btnBatal.setOnClickListener {
            // Reset ke data asli
            etKelas.setText(originalKelas)
            etPhoneNumber.setText(originalTelepon)
            etEmailAddress.setText(originalEmail)
            etPwd.setText(originalPassword)
            tvNamaGuru.text = originalNamaGuru

            Toast.makeText(requireContext(), "Perubahan dibatalkan", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun loadProfileData() {
        val uidGuru = auth.currentUser?.uid ?: return

        database.child("Users").child(uidGuru)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        // Ambil data dari database
                        originalNamaGuru = snapshot.child("username").getValue(String::class.java) ?: "Guru"
                        originalEmail = snapshot.child("email").getValue(String::class.java) ?: ""
                        originalTelepon = snapshot.child("phone").getValue(String::class.java) ?: ""
                        originalKelas = snapshot.child("kelas").getValue(String::class.java) ?: ""
                        // Password biasanya tidak disimpan plain di database, kamu bisa pakai Firebase Auth untuk update password
                        originalPassword = "" // kosongkan, atau ambil dari user input

                        // Set ke UI
                        tvNamaGuru.text = originalNamaGuru
                        etEmailAddress.setText(originalEmail)
                        etPhoneNumber.setText(originalTelepon)
                        etKelas.setText(originalKelas)
                        etPwd.setText(originalPassword)
                    } else {
                        Toast.makeText(requireContext(), "Data profil tidak ditemukan", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(requireContext(), "Gagal memuat profil: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun saveProfileData() {
        val uidGuru = auth.currentUser?.uid ?: return

        val kelasBaru = etKelas.text.toString()
        val teleponBaru = etPhoneNumber.text.toString()
        val emailBaru = etEmailAddress.text.toString()
        val pwdBaru = etPwd.text.toString()

        // Update ke database
        val updates = mapOf<String, Any>(
            "kelas" to kelasBaru,
            "phone" to teleponBaru,
            "email" to emailBaru
        )

        database.child("Users").child(uidGuru).updateChildren(updates)
            .addOnSuccessListener {
                // Update nama guru (username) dari email (contoh)
                val namaGuruBaru = emailBaru.substringBefore("@").replaceFirstChar { it.uppercase() }
                database.child("Users").child(uidGuru).child("username").setValue(namaGuruBaru)

                tvNamaGuru.text = namaGuruBaru

                // Update password via FirebaseAuth (jika diizinkan)
                if (pwdBaru.isNotEmpty()) {
                    auth.currentUser?.updatePassword(pwdBaru)?.addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(requireContext(), "Password berhasil diubah", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(requireContext(), "Gagal mengubah password: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                Toast.makeText(requireContext(), "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()

                // Update original data supaya bisa batal lagi
                originalKelas = kelasBaru
                originalTelepon = teleponBaru
                originalEmail = emailBaru
                originalNamaGuru = namaGuruBaru
                originalPassword = pwdBaru
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
            }
    }
}