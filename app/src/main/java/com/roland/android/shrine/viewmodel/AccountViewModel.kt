package com.roland.android.shrine.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.shrine.data.Address
import com.roland.android.shrine.data.AppDataStore
import com.roland.android.shrine.data.CardDetails
import com.roland.android.shrine.data.User
import com.roland.android.shrine.data.database.ItemDao
import com.roland.android.shrine.data.model.ItemData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountViewModel(
	private val itemDao: ItemDao,
	private val appDataStore: AppDataStore
) : ViewModel() {
	var orderedItems by mutableStateOf<List<ItemData>>(emptyList()); private set
	var user by mutableStateOf(User("", "", "")); private set
	var firstLaunch by mutableStateOf(false); private set
	var firstName by mutableStateOf("")
	var lastName by mutableStateOf("")
	var password by mutableStateOf("")

	init {
		viewModelScope.launch {
			itemDao.getOrderHistory().collect {
				orderedItems = it
			}
		}
		viewModelScope.launch {
			appDataStore.getLaunchStatus().collect {
				firstLaunch = !it
			}
		}
		viewModelScope.launch {
			appDataStore.getUser().collect {
				user = it
			}
		}
	}

	fun saveLaunchStatus() {
		viewModelScope.launch(Dispatchers.IO) {
			appDataStore.saveLaunchStatus()
		}
	}

	fun saveUser() {
		viewModelScope.launch(Dispatchers.IO) {
			val user = User(firstName, lastName, password)
			appDataStore.saveUser(user)
		}
	}

	fun deleteAccount() {
		viewModelScope.launch(Dispatchers.IO) {
			itemDao.clearDatabase()
			appDataStore.apply {
				saveDeliveryAddress(Address())
				saveCardDetails(CardDetails())
				saveUser(User())
			}
		}
	}
}