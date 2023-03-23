package com.roland.android.shrine.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.roland.android.shrine.ShrineApp
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
            itemDao.getItems(
	            isCartItem = true,
	            favourited = false
            ).collect {
                cartItems = it
            }
        }
        viewModelScope.launch {
            itemDao.getItems(
	            isCartItem = false,
	            favourited = true
            ).collect {
                wishlist = it
                cartIsLoaded = true
            }
        }
    }

    fun addToCart(item: ItemData) {
	    item.isCartItem = true
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
	    item.favourited = true
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

@Suppress("UNCHECKED_CAST")
class SharedViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SharedViewModel(ShrineApp.itemDao) as T
    }
}