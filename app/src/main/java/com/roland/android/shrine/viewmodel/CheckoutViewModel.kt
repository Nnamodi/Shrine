package com.roland.android.shrine.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.shrine.data.Address
import com.roland.android.shrine.data.AppDataStore.getAddress
import com.roland.android.shrine.data.AppDataStore.getCardDetails
import com.roland.android.shrine.data.AppDataStore.saveCardDetails
import com.roland.android.shrine.data.AppDataStore.saveDeliveryAddress
import com.roland.android.shrine.data.CardDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class CheckoutViewModel(private val app: Application) : AndroidViewModel(app) {
    var address by mutableStateOf(Address())
    var cardDetails by mutableStateOf(CardDetails())

    init {
        viewModelScope.launch {
            app.getAddress().collectLatest { address = it }
        }
        viewModelScope.launch {
            app.getCardDetails().collectLatest { cardDetails = it }
        }
    }

    fun saveAddress(address: Address) {
        viewModelScope.launch(Dispatchers.IO) {
            app.saveDeliveryAddress(address)
        }
    }

    fun saveCardDetails(cardDetails: CardDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            app.saveCardDetails(cardDetails)
        }
    }
}