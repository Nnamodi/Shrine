@file:Suppress("UNCHECKED_CAST")

package com.roland.android.shrine.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.roland.android.shrine.ShrineApp

/**
 * ViewModel provider factory to instantiate ViewModels in the module.
 * Required, given that said ViewModel has a non-empty constructor
 */
class ViewModelFactory : ViewModelProvider.Factory {
	override fun <T : ViewModel> create(modelClass: Class<T>): T {
		if (modelClass.isAssignableFrom(SharedViewModel::class.java)) {
			return SharedViewModel(ShrineApp.itemDao) as T
		}
		if (modelClass.isAssignableFrom(CheckoutViewModel::class.java)) {
			return CheckoutViewModel(
				appDataStore = ShrineApp.appDataStore,
				itemDao = ShrineApp.itemDao
			) as T
		}
		if (modelClass.isAssignableFrom(AccountViewModel::class.java)) {
			return AccountViewModel(ShrineApp.appDataStore) as T
		}
		throw IllegalArgumentException("Unknown ViewModel class")
	}
}