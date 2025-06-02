package com.example.kidconnect.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.example.kidconnect.DetailSiswaActivity
import com.example.kidconnect.R

class SiswaFragmentGuru : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_siswa_gr, container, false)

        val kelasAButton = view.findViewById<ImageButton>(R.id.kelasa)
        val kelasBButton = view.findViewById<ImageButton>(R.id.kelasb)

        kelasAButton.setOnClickListener {
            val intent = Intent(requireContext(), DetailSiswaActivity::class.java)
            intent.putExtra("kelas", "A")
            startActivity(intent)
        }

        kelasBButton.setOnClickListener {
            val intent = Intent(requireContext(), DetailSiswaActivity::class.java)
            intent.putExtra("kelas", "B")
            startActivity(intent)
        }

        return view
    }
}
