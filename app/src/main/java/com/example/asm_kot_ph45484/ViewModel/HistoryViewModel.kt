package com.example.asm_kot_ph45484.ViewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.asm_kot_ph45484.CartData
import com.example.asm_kot_ph45484.Model.History.HistoryData

class HistoryViewModel : ViewModel() {
    private val _historyItems = MutableLiveData<List<HistoryData>>(emptyList())
    val historyItems: LiveData<List<HistoryData>> = _historyItems
    fun addHistory(
        id: String,
        paymentMethod: String,
        products: List<CartData>,
        totalAmount: Double,
        date: String,
    ) {
        val currentList = _historyItems.value.orEmpty().toMutableList()
        products.forEach { cartItem ->
            currentList.add(
                HistoryData(
                    id = id,
                    paymentMethod = paymentMethod,
                    product = cartItem,
                    totalAmount = totalAmount,
                    date = date
                )
            )
        }
        _historyItems.value = currentList
        Log.e("HistoryViewModel", _historyItems.toString())
    }

//    fun getOrderById(orderId: String?): HistoryData {
//        return historyItems.value?.firstOrNull { it.id == orderId } ?: HistoryData()
//    }
}