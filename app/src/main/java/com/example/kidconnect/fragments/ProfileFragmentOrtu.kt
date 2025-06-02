package com.example.kidconnect.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kidconnect.OrtuActivity
import com.example.kidconnect.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class ProfileFragmentOrtu : Fragment() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var etUsername: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etEmailAddress: EditText
    private lateinit var etPwd: EditText

    private lateinit var btnSimpan: ImageButton
    private lateinit var btnBatal: ImageButton

    // Variabel untuk menyimpan data asli supaya bisa batal
    private var originalNama = ""
    private var originalTelepon = ""
    private var originalEmail = ""
    private var originalPassword = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_ot, container, false)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        etUsername = view.findViewById(R.id.etUsername)
        etPhoneNumber = view.findViewById(R.id.etPhoneNumber)
        etEmailAddress = view.findViewById(R.id.etEmailAddress)
        etPwd = view.findViewById(R.id.etPwd)

        btnSimpan = view.findViewById(R.id.simpan)
        btnBatal = view.findViewById(R.id.batal)

        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            (requireActivity() as OrtuActivity).switchToHome()
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
            etUsername.setText(originalNama)
            etPhoneNumber.setText(originalTelepon)
            etEmailAddress.setText(originalEmail)
            etPwd.setText(originalPassword)

            Toast.makeText(requireContext(), "Perubahan dibatalkan", Toast.LENGTH_SHORT).show()
        }

        return view
    }

    private fun loadProfileData() {
        val uidOrtu = auth.currentUser?.uid ?: return

        database.child("Users").child(uidOrtu)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        originalNama = snapshot.child("username").getValue(String::class.java) ?: "Ortu"
                        originalEmail = snapshot.child("email").getValue(String::class.java) ?: ""
                        originalTelepon = snapshot.child("phone").getValue(String::class.java) ?: ""
                        originalPassword = "" // Biasanya password gak disimpan di sini

                        etUsername.setText(originalNama)
                        etEmailAddress.setText(originalEmail)
                        etPhoneNumber.setText(originalTelepon)
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
        val uidOrtu = auth.currentUser?.uid ?: return

        val namaBaru = etUsername.text.toString()
        val teleponBaru = etPhoneNumber.text.toString()
        val emailBaru = etEmailAddress.text.toString()
        val pwdBaru = etPwd.text.toString()

        val updates = mapOf<String, Any>(
            "username" to namaBaru,
            "phone" to teleponBaru,
            "email" to emailBaru
        )

        database.child("Users").child(uidOrtu).updateChildren(updates)
            .addOnSuccessListener {
                // Update password via FirebaseAuth jika diperlukan
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
                originalNama = namaBaru
                originalTelepon = teleponBaru
                originalEmail = emailBaru
                originalPassword = pwdBaru
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Gagal menyimpan data", Toast.LENGTH_SHORT).show()
            }
    }
}
