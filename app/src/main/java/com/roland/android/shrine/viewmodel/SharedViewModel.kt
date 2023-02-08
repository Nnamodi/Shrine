package com.roland.android.shrine.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.roland.android.shrine.data.ItemData
import com.roland.android.shrine.data.SampleItemsData

class SharedViewModel : ViewModel() {
    var data by mutableStateOf(SampleItemsData[0])
        private set

    fun setItemData(itemData: ItemData) {
        data = itemData
    }
}