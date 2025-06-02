package com.example.kidconnect.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.kidconnect.R
import com.example.kidconnect.model.BankItem

class BankAdapter(
    context: Context,
    private val banks: List<BankItem>
) : ArrayAdapter<BankItem>(context, 0, banks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createItemView(position, convertView, parent)
    }

    private fun createItemView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = LayoutInflater.from(context)
        val view = convertView ?: inflater.inflate(R.layout.item_bank, parent, false)

        val imgLogo = view.findViewById<ImageView>(R.id.imgLogoBank)
        val tvNama = view.findViewById<TextView>(R.id.tvNama)
        val tvNoRekening = view.findViewById<TextView>(R.id.tvNoRekening)

        val bank = banks[position]
        tvNama.text = bank.namaPemilik
        tvNoRekening.text = bank.nomorRekening

        when (bank.namaBank) {
            "BCA" -> imgLogo.setImageResource(R.drawable.bca)
            "BRI" -> imgLogo.setImageResource(R.drawable.bri)
            "BNI" -> imgLogo.setImageResource(R.drawable.bni)
            "Mandiri" -> imgLogo.setImageResource(R.drawable.mandiri)
            else -> imgLogo.setImageResource(R.drawable.mandiri)
        }

        return view
    }
}
