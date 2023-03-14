package com.roland.android.shrine.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.roland.android.shrine.ShrineApp
import com.roland.android.shrine.data.SampleItemsData
import com.roland.android.shrine.data.database.ItemDao
import com.roland.android.shrine.data.model.ItemData

class SharedViewModel(
    private val itemDao: ItemDao
) : ViewModel() {
    private val openedDetailScreens = mutableStateListOf<ItemData>()

    var data by mutableStateOf<ItemData?>(null)
        private set

    val cartItems = mutableStateListOf(*SampleItemsData.take(0).toTypedArray())

    val wishlist = mutableStateListOf(*SampleItemsData.take(0).toTypedArray())

    fun addToCart(item: ItemData) {
        cartItems.add(item)
    }

    fun removeFromCart(index: Int) {
        cartItems.removeAt(index)
    }

    fun addToWishlist(item: ItemData) {
        wishlist.add(item)
        item.favourited = true
    }

    fun removeFromWishlist(item: ItemData) {
        wishlist.remove(item)
        item.favourited = false
    }

    fun addScreen(data: ItemData) {
        openedDetailScreens.add(data)
        this.data = data
    }

    fun removeLastScreen() {
        if (openedDetailScreens.isNotEmpty()) {
            val lastScreen = openedDetailScreens.last()
            openedDetailScreens.remove(lastScreen)
        }
        if (openedDetailScreens.isNotEmpty()) {
            data = openedDetailScreens.last()
        }
    }
}

class SharedViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SharedViewModel(ShrineApp.itemDao) as T
    }
}