package com.example.kidconnect

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetailPembayaranActivity : AppCompatActivity() {

    private lateinit var txtJenisTagihan: TextView
    private lateinit var txtHargaJenisTagihan: TextView
    private lateinit var txtMetodePembayaran: TextView
    private lateinit var txtHargaTotalTagihan: TextView

    private lateinit var btnBack: ImageButton
    private lateinit var btnNotif: ImageButton

    private lateinit var btnSekalibayar: ImageButton
    private lateinit var btnCicilan: ImageButton

    private lateinit var btn2x: ImageButton
    private lateinit var btn4x: ImageButton
    private lateinit var txt2x: TextView
    private lateinit var txt4x: TextView

    private var biaya: Int = 0
    private var metodeDipilih: String = "Belum dipilih"

    private lateinit var nextbutton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.detail_pembayaran_ot)

        // Inisialisasi view
        txtJenisTagihan = findViewById(R.id.jenistagihan)
        txtHargaJenisTagihan = findViewById(R.id.hargajenistagihan)
        txtMetodePembayaran = findViewById(R.id.metodepembayaran)
        txtHargaTotalTagihan = findViewById(R.id.hargatotaltagihan)

        btnBack = findViewById<ImageButton>(R.id.backdetailpembayaran)
        btnNotif = findViewById<ImageButton>(R.id.notifpembayaran)

        btnSekalibayar = findViewById(R.id.sekalibayar)
        btnCicilan = findViewById(R.id.cicilan)

        btn2x = findViewById(R.id.btn2x)
        btn4x = findViewById(R.id.btn4x)
        txt2x = findViewById(R.id.txt2x)
        txt4x = findViewById(R.id.txt4x)

        // Ambil data dari intent
        val jenisTagihan = intent.getStringExtra("jenis_tagihan") ?: "Tidak Diketahui"
        biaya = intent.getIntExtra("biaya", 0)

        // Set data awal
        txtJenisTagihan.text = jenisTagihan
        txtHargaJenisTagihan.text = "Rp $biaya"
        txtMetodePembayaran.text = "Metode Pembayaran: $metodeDipilih"
        txtHargaTotalTagihan.text = "Rp $biaya"

        nextbutton = findViewById(R.id.nextbutton)

        btnBack.setOnClickListener {
            finish()
        }

        btnNotif.setOnClickListener {
            Toast.makeText(this, "Notifikasi dibuka", Toast.LENGTH_SHORT).show()
            // TODO: Navigasi ke fragment notifikasi
        }

        // Tombol Sekali Bayar
        btnSekalibayar.setOnClickListener {
            btnSekalibayar.setBackgroundResource(R.drawable.button_pembayaran_yellow)
            btnCicilan.setBackgroundResource(R.drawable.button_pembayaran)
            btn2x.setBackgroundResource(R.drawable.cicilan_white)
            btn4x.setBackgroundResource(R.drawable.cicilan_white)
            metodeDipilih = "Sekali Bayar"
            updateMetodeDanTotal(1)
        }

        // Tombol Cicilan
        btnCicilan.setOnClickListener {
            btnCicilan.setBackgroundResource(R.drawable.button_pembayaran_yellow)
            btnSekalibayar.setBackgroundResource(R.drawable.button_pembayaran)
            metodeDipilih = "Cicilan"
            updateMetodeDanTotal(1)
            if (btn2x.visibility == View.GONE) {
                btn2x.visibility = View.VISIBLE
                btn4x.visibility = View.VISIBLE
                txt2x.visibility = View.VISIBLE
                txt4x.visibility = View.VISIBLE
            } else {
                btn2x.visibility = View.GONE
                btn4x.visibility = View.GONE
                txt2x.visibility = View.GONE
                txt4x.visibility = View.GONE
            }
        }

        btn2x.setOnClickListener {
            btn2x.setBackgroundResource(R.drawable.cicilan_yellow)
            btn4x.setBackgroundResource(R.drawable.cicilan_white)
            updateMetodeDanTotal(2)
        }

        btn4x.setOnClickListener {
            btn4x.setBackgroundResource(R.drawable.cicilan_yellow)
            btn2x.setBackgroundResource(R.drawable.cicilan_white)
            updateMetodeDanTotal(4)
        }

        nextbutton.setOnClickListener {
            val intent = Intent(this@DetailPembayaranActivity, RekeningActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateMetodeDanTotal(cicilan: Int = 1) {
        txtMetodePembayaran.text = "$metodeDipilih"
        val total = if (cicilan > 1) biaya / cicilan else biaya
        txtHargaTotalTagihan.text = "Rp $total"
    }
}
