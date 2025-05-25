package com.example.kidconnect

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kidconnect.adapter.BankAdapter
import com.example.kidconnect.model.BankItem

class RekeningActivity : AppCompatActivity() {

    private lateinit var btnBack: ImageButton
    private lateinit var btnNotif: ImageButton

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BankAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.rekening)

        btnBack = findViewById<ImageButton>(R.id.backrekening)
        btnNotif = findViewById<ImageButton>(R.id.notifpembayaran)
        recyclerView = findViewById(R.id.listbank)

        btnBack.setOnClickListener {
            finish()
        }

        btnNotif.setOnClickListener {
            Toast.makeText(this, "Notifikasi dibuka", Toast.LENGTH_SHORT).show()
            // TODO: Navigasi ke fragment notifikasi
        }

        recyclerView.layoutManager = LinearLayoutManager(this)

        val listRekening = listOf(
            BankItem(R.drawable.bca, "KATTY PERRY", "1234567890"),
            BankItem(R.drawable.mandiri, "JOHN DOE", "0987654321"),
            BankItem(R.drawable.bri, "JANE SMITH", "1122334455"),
            BankItem(R.drawable.bni, "ALBERT EINSTEIN", "5566778899")
        )

        adapter = BankAdapter(listRekening)
        recyclerView.adapter = adapter
    }
}
