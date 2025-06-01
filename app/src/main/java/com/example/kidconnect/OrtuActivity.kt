package com.example.kidconnect

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kidconnect.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class OrtuActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navbar_ot)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        // Tampilkan fragment default (misalnya Home)
        loadFragment(HomeFragmentOrtu())

        bottomNavigationView.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.home_ot -> HomeFragmentOrtu()
                R.id.pembayaran_ot -> PembayaranFragmentOrtu()
                R.id.laporan_ot -> LaporanFragmentOrtu()
                R.id.profile_ot -> ProfileFragmentOrtu()
                else -> HomeFragmentOrtu()
            }
            loadFragment(fragment)
            true
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    fun switchToHome() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, HomeFragmentOrtu())
            .commit()
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNav.selectedItemId = R.id.home_ot
    }
}
