package com.roland.android.shrine

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.room.Room
import com.roland.android.shrine.data.AppDataStore
import com.roland.android.shrine.data.database.AppDatabase
import com.roland.android.shrine.data.database.ItemDao

class ShrineApp : Application() {
	private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("app_preferences")

	override fun onCreate() {
		super.onCreate()
		val database = Room.databaseBuilder(
			applicationContext,
			AppDatabase::class.java,
			"item_data"
		).build()
		itemDao = database.itemDao()
		appDataStore = AppDataStore(dataStore)
	}

	companion object {
		lateinit var itemDao: ItemDao
		lateinit var appDataStore: AppDataStore
	}
}