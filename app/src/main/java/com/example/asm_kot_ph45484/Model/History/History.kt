package com.example.asm_kot_ph45484.Model.History

import com.example.asm_kot_ph45484.CartData

data class HistoryData(
    val id: String,
    val paymentMethod: String,
    val product: CartData,
    val totalAmount: Double,
    val date: String
)
