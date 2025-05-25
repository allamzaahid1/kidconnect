package com.example.kidconnect.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.kidconnect.MainActivity
import com.example.kidconnect.R

class ProfileFragment : Fragment() {

    // Data dummy
    private var dummyNama = "ortu"
    private var dummyTelepon = "08123456789"
    private var dummyEmail = "ortu@gmail.com"
    private var dummyPassword = "1234"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile_ot, container, false)

        val btnBack = view.findViewById<ImageButton>(R.id.btnBack)
        btnBack.setOnClickListener {
            (requireActivity() as MainActivity).switchToHome()
        }

        val etUsername = view.findViewById<EditText>(R.id.etUsername)
        val etPhoneNumber = view.findViewById<EditText>(R.id.etPhoneNumber)
        val etEmailAddress = view.findViewById<EditText>(R.id.etEmailAddress)
        val etPwd = view.findViewById<EditText>(R.id.etPwd)

        val btnSimpan = view.findViewById<ImageButton>(R.id.simpan)
        val btnBatal = view.findViewById<ImageButton>(R.id.batal)

        // Set dummy data ke form
        etUsername.setText(dummyNama)
        etPhoneNumber.setText(dummyTelepon)
        etEmailAddress.setText(dummyEmail)
        etPwd.setText(dummyPassword)

        // Simpan perubahan
        btnSimpan.setOnClickListener {
            dummyNama = etUsername.text.toString()
            dummyTelepon = etPhoneNumber.text.toString()
            dummyEmail = etEmailAddress.text.toString()
            dummyPassword = etPwd.text.toString()

            Toast.makeText(requireContext(), "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()
        }

        // Batal (kembalikan data ke semula)
        btnBatal.setOnClickListener {
            etUsername.setText(dummyNama)
            etPhoneNumber.setText(dummyTelepon)
            etEmailAddress.setText(dummyEmail)
            etPwd.setText(dummyPassword)

            Toast.makeText(requireContext(), "Perubahan dibatalkan", Toast.LENGTH_SHORT).show()
        }

        return view
    }
}
