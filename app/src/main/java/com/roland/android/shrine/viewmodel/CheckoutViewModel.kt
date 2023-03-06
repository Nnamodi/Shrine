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
import com.roland.android.shrine.utils.CardNumbers.VALID_DATE
import com.roland.android.shrine.utils.cardExpired
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CheckoutViewModel(private val app: Application) : AndroidViewModel(app) {
    var address by mutableStateOf(Address()); private set
    var cardDetails by mutableStateOf(CardDetails()); private set
    var addressSet by mutableStateOf(false); private set
    var cardDetailsSet by mutableStateOf(false); private set

    init {
        viewModelScope.launch {
            app.getAddress().collect {
                address = it
                addressSet = it.street.isNotBlank()
            }
        }
        viewModelScope.launch {
            app.getCardDetails().collect {
                cardDetails = it
                cardDetailsSet = if (it.cardNumber.isNotBlank()) {
                    cardExpired(it.expiryMonth, it.expiryYear) == VALID_DATE
                } else false
            }
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