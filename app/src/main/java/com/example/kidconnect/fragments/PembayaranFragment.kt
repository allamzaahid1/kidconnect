package com.example.kidconnect.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.kidconnect.R

class PembayaranFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_pembayaran_ot, container, false)
}
