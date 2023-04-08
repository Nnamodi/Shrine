package com.roland.android.shrine.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.roland.android.shrine.data.AppDataStore
import com.roland.android.shrine.data.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AccountViewModel(
	private val appDataStore: AppDataStore
) : ViewModel() {
	var firstLaunch by mutableStateOf(false); private set
	var firstName by mutableStateOf("")
	var lastName by mutableStateOf("")
	var password by mutableStateOf("")

	init {
		viewModelScope.launch {
			appDataStore.getLaunchStatus().collect {
				firstLaunch = !it
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
}