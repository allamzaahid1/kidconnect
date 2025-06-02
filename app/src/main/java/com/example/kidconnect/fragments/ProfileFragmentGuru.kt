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

class ProfileFragmentGuru : Fragment() {

    // Data dummy
    private var dummyKelas = "Kelas A"
    private var dummyTelepon = "08123456789"
    private var dummyEmail = "guru@kidconnect.com"
    private var dummyPassword = "4321"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_gr, container, false)

        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            (requireActivity() as GuruActivity).switchToHome()
        }

        val tvNamaGuru = view.findViewById<TextView>(R.id.profiletxt)
        val etKelas = view.findViewById<EditText>(R.id.etKelas)
        val etPhoneNumber = view.findViewById<EditText>(R.id.etPhoneNumber)
        val etEmailAddress = view.findViewById<EditText>(R.id.etEmailAddress)
        val etPwd = view.findViewById<EditText>(R.id.etPwd)

        val btnSimpan = view.findViewById<ImageButton>(R.id.simpan)
        val btnBatal = view.findViewById<ImageButton>(R.id.batal)

        // Tambahkan variabel dummy nama
        var dummyNamaGuru = "Guru"

        // Set data awal
        tvNamaGuru.text = dummyNamaGuru
        etKelas.setText(dummyKelas)
        etPhoneNumber.setText(dummyTelepon)
        etEmailAddress.setText(dummyEmail)
        etPwd.setText(dummyPassword)

        // Simpan perubahan
        btnSimpan.setOnClickListener {
            dummyKelas = etKelas.text.toString()
            dummyTelepon = etPhoneNumber.text.toString()
            dummyEmail = etEmailAddress.text.toString()
            dummyPassword = etPwd.text.toString()

            // Update nama guru jika diinginkan (contoh: dari email sebelum @)
            dummyNamaGuru = dummyEmail.substringBefore("@").replaceFirstChar { it.uppercase() }
            tvNamaGuru.text = dummyNamaGuru

            Toast.makeText(requireContext(), "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
        }

        // Batal (kembalikan data ke semula)
        btnBatal.setOnClickListener {
            etKelas.setText(dummyKelas)
            etPhoneNumber.setText(dummyTelepon)
            etEmailAddress.setText(dummyEmail)
            etPwd.setText(dummyPassword)
            tvNamaGuru.text = dummyNamaGuru

            Toast.makeText(requireContext(), "Perubahan dibatalkan", Toast.LENGTH_SHORT).show()
        }

        return view
    }

}
