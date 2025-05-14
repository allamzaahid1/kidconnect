package com.example.kidconnect

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.constraintlayout.widget.ConstraintLayout


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.layout_splash)

        val logo = findViewById<ImageView>(R.id.logo)
        val rootLayout = findViewById<ConstraintLayout>(R.id.root_layout)
        val brandText = findViewById<TextView>(R.id.brand_text)

        // 1. Animasi jatuh dan rotasi logo
        val anim = AnimationUtils.loadAnimation(this, R.anim.logo_drop_rotate)
        logo.startAnimation(anim)

        // 2. Setelah 1 detik, ubah latar → kuning dan logo → putih
        logo.postDelayed({
            val warnaAwal = ContextCompat.getColor(this, R.color.black)
            val warnaAkhir = ContextCompat.getColor(this, R.color.yellow)

            // Fade warna background
            val colorAnim = ValueAnimator.ofObject(ArgbEvaluator(), warnaAwal, warnaAkhir)
            colorAnim.duration = 1500
            colorAnim.addUpdateListener {
                val color = it.animatedValue as Int
                rootLayout.setBackgroundColor(color)
            }
            colorAnim.start()

            // Ganti logo putih
            logo.setImageResource(R.drawable.connect_logo_white)

        }, 1000)

        // 3. Munculkan teks dengan animasi scale & fade
        logo.postDelayed({
            brandText.visibility = TextView.VISIBLE
            brandText.alpha = 0f
            brandText.scaleX = 0.5f
            brandText.scaleY = 0.5f
            brandText.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(800)
                .start()
        }, 1600)

        // 4. Selesai splash → pindah activity
        logo.postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 2800)
    }
}