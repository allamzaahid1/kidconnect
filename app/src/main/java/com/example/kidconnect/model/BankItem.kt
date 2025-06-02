package com.example.kidconnect.model

data class BankItem(
    val namaBank: String,
    val namaPemilik: String,
    val nomorRekening: String,
    val logoResId: Int
) {
    override fun toString(): String {
        return namaBank
    }
}
