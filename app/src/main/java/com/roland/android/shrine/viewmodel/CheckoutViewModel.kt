package com.roland.android.shrine.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.shrine.data.Address
import com.roland.android.shrine.data.AppDataStore
import com.roland.android.shrine.data.CardDetails
import com.roland.android.shrine.data.database.ItemDao
import com.roland.android.shrine.data.model.ItemData
import com.roland.android.shrine.utils.CardNumbers.ORDER_DATE_FORMAT
import com.roland.android.shrine.utils.CardNumbers.VALID_DATE
import com.roland.android.shrine.utils.cardExpired
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CheckoutViewModel(
    private val appDataStore: AppDataStore,
    private val itemDao: ItemDao
) : ViewModel() {
    var address by mutableStateOf(Address()); private set
    var cardDetails by mutableStateOf(CardDetails()); private set
    var addressSet by mutableStateOf(false); private set
    var cardDetailsSet by mutableStateOf(false); private set
    var orderSent by mutableStateOf(false)
    var latestOrder by mutableStateOf<List<ItemData>>(emptyList()); private set
    var orderNumber by mutableStateOf(""); private set

    init {
        viewModelScope.launch {
            appDataStore.getAddress().collect {
                address = it
                addressSet = it.street.isNotBlank()
            }
        }
        viewModelScope.launch {
            appDataStore.getCardDetails().collect {
                cardDetails = it
                cardDetailsSet = if (it.cardNumber.isNotBlank()) {
                    cardExpired(it.expiryMonth, it.expiryYear) == VALID_DATE
                } else false
            }
        }
        viewModelScope.launch {
            itemDao.getOrderHistory().collect {
                val recent = date(it.last().purchaseDate)
                latestOrder = it.filter { data ->
                    date(data.purchaseDate) == recent
                }
            }
        }
        viewModelScope.launch {
            appDataStore.getOrderNo().collect { orderNumber = it }
        }
    }

    fun saveAddress(address: Address) {
        viewModelScope.launch(Dispatchers.IO) {
            appDataStore.saveDeliveryAddress(address)
        }
    }

    fun saveCardDetails(cardDetails: CardDetails) {
        viewModelScope.launch(Dispatchers.IO) {
            appDataStore.saveCardDetails(cardDetails)
        }
    }

    fun addOrderList(items: List<ItemData>) {
        viewModelScope.launch(Dispatchers.IO) {
            appDataStore.saveOrderNo()
            items.map {
                it.apply {
                    isCartItem = false
                    purchased = true
                    purchaseDate = Calendar.getInstance().time
                }
                itemDao.apply { removeItem(it); addItem(it) }
            }
        }
    }

    fun date(date: Date): String {
        val style = SimpleDateFormat(ORDER_DATE_FORMAT, Locale.getDefault())
        return style.format(date)
    }
}