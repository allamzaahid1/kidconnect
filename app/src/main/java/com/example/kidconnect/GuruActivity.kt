package com.example.kidconnect

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.kidconnect.fragments.*
import com.google.android.material.bottomnavigation.BottomNavigationView

class GuruActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.navbar_gr)

        bottomNavigationView = findViewById(R.id.bottom_navigation)

        loadFragment(HomeFragmentGuru())

        bottomNavigationView.setOnItemSelectedListener { item ->
            val fragment: Fragment = when (item.itemId) {
                R.id.home_gr -> HomeFragmentGuru()
                R.id.siswa_gr -> SiswaFragmentGuru()
                R.id.laporan_gr -> LaporanFragmentGuru()
                R.id.profile_gr -> ProfileFragmentGuru()
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
        bottomNav.selectedItemId = R.id.home_gr
    }

}
