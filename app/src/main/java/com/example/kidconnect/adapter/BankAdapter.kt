package com.example.kidconnect.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kidconnect.R
import com.example.kidconnect.model.BankItem

class BankAdapter(
    private val bankList: List<BankItem>,
    private val onItemClick: (BankItem) -> Unit
) : RecyclerView.Adapter<BankAdapter.BankViewHolder>() {

    inner class BankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgLogoBank: ImageView = itemView.findViewById(R.id.imgLogoBank)
        val tvNama: TextView = itemView.findViewById(R.id.tvNama)
        val tvNoRekening: TextView = itemView.findViewById(R.id.tvNoRekening)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BankViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_bank, parent, false)
        return BankViewHolder(view)
    }

    override fun onBindViewHolder(holder: BankViewHolder, position: Int) {
        val bank = bankList[position]
        holder.imgLogoBank.setImageResource(bank.logoResId)
        holder.tvNama.text = bank.nama
        holder.tvNoRekening.text = bank.noRekening

        holder.itemView.setOnClickListener {
            onItemClick(bank)
        }
    }

    override fun getItemCount(): Int = bankList.size
}
