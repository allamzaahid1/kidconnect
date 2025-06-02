package com.example.kidconnect

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.kidconnect.model.BankItem
import com.example.kidconnect.adapter.BankAdapter
import com.google.android.material.textfield.TextInputLayout

class RekeningActivity : AppCompatActivity() {

    private lateinit var daftarBank: List<BankItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rekening_ot)

        // Inisialisasi view
        val bankDropdown = findViewById<AutoCompleteTextView>(R.id.bankDropdown)
        val bankInputLayout = findViewById<TextInputLayout>(R.id.bankInputLayout)
        val rekeningContainer = findViewById<ImageView>(R.id.rekening)
        val namaRekening = findViewById<TextView>(R.id.namaRekening)
        val noRekeningContainer = findViewById<ImageView>(R.id.norekening)
        val nomorRekening = findViewById<TextView>(R.id.nomorRekening)
        val copyButton = findViewById<ImageButton>(R.id.copyButton)

        // Data bank
        daftarBank = listOf(
            BankItem("BNI", "KATTY PERRY", "0202020202", R.drawable.bni_40),
            BankItem("Mandiri", "JOHN DOE", "1234567890", R.drawable.mandiri_40),
            BankItem("BRI", "SITI AMINAH", "9876543210", R.drawable.bri_40),
            BankItem("BCA", "RAFI AHMAD", "0011223344", R.drawable.bca_40)
        )

        // Pasang adapter
        val adapter = BankAdapter(this, daftarBank)
        bankDropdown.setAdapter(adapter)

        // Tampilkan detail rekening saat bank dipilih
        bankDropdown.setOnItemClickListener { _, _, position, _ ->
            val selectedBank = daftarBank[position]
            namaRekening.text = selectedBank.namaPemilik
            nomorRekening.text = selectedBank.nomorRekening

            bankInputLayout.startIconDrawable = ContextCompat.getDrawable(this, selectedBank.logoResId)
            bankInputLayout.setStartIconTintList(null)

            rekeningContainer.visibility = ImageView.VISIBLE
            namaRekening.visibility = TextView.VISIBLE
            noRekeningContainer.visibility = ImageView.VISIBLE
            nomorRekening.visibility = TextView.VISIBLE
            copyButton.visibility = ImageButton.VISIBLE
        }

        // Aksi tombol copy
        copyButton.setOnClickListener {
            val textToCopy = nomorRekening.text.toString()
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            clipboard.setPrimaryClip(ClipData.newPlainText("Nomor Rekening", textToCopy))
            Toast.makeText(this, "Nomor rekening disalin", Toast.LENGTH_SHORT).show()
        }
    }
}
