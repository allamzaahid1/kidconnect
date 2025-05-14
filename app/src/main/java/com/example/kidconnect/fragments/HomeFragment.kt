package com.example.kidconnect.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.kidconnect.R
import com.example.kidconnect.model.HomeItem

class HomeFragment : Fragment() {

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

        // Inisialisasi view dari layout
        tvName = view.findViewById(R.id.tvName)
        tvAnnouncement = view.findViewById(R.id.tvAnnouncement)
        tvTodayActivity = view.findViewById(R.id.tvTodayActivity)
        tvTimeActivity = view.findViewById(R.id.tvTimeActivity)
        ivBanner = view.findViewById(R.id.ivBanner)

        // Dummy data
        val homeItem = HomeItem(
            name = "Nova",
            announcement = "Hari ini libur dikarenakan cuaca yang sangat buruk",
            todayActivity = "Mewarnai bersama",
            timeActivity = "08.00 - 09.00",
            imageResId = R.drawable.car
        )

        showHomeData(homeItem)

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
}
