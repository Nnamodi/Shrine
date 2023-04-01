package com.roland.android.shrine.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.shrine.data.database.ItemDao
import com.roland.android.shrine.data.model.ItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SharedViewModel(
    private val itemDao: ItemDao
) : ViewModel() {
    var cartItems by mutableStateOf<List<ItemData>>(emptyList()); private set
    var wishlist by mutableStateOf<List<ItemData>>(emptyList()); private set
    var cartIsLoaded by mutableStateOf(false); private set

    init {
        viewModelScope.launch {
            itemDao.getCartItems().collect {
                cartItems = it
            }
        }
        viewModelScope.launch {
            itemDao.getWishlist().collect {
                wishlist = it
                cartIsLoaded = true
            }
        }
    }

    fun addToCart(item: ItemData) {
	    item.apply { isCartItem = true; favourited = false }
        viewModelScope.launch(Dispatchers.IO) {
            itemDao.addItem(item)
        }
    }

    fun removeFromCart(item: ItemData) {
	    item.isCartItem = false
        viewModelScope.launch(Dispatchers.IO) {
            itemDao.removeItem(item)
        }
    }

    fun addToWishlist(item: ItemData) {
	    item.apply { favourited = true; isCartItem = false }
        viewModelScope.launch(Dispatchers.IO) {
	        itemDao.addItem(item)
        }
    }

    fun removeFromWishlist(item: ItemData) {
	    item.favourited = false
        val data = wishlist.find { item.id == it.id }
        viewModelScope.launch(Dispatchers.IO) {
	        itemDao.removeItem(data!!)
        }
    }
}