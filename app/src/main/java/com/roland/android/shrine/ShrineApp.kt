package com.roland.android.shrine

import android.app.Application
import androidx.room.Room
import com.roland.android.shrine.data.database.AppDatabase
import com.roland.android.shrine.data.database.ItemDao

class ShrineApp : Application() {
	override fun onCreate() {
		super.onCreate()
		val database = Room.databaseBuilder(
			applicationContext,
			AppDatabase::class.java,
			"item_data"
		).build()
		itemDao = database.itemDao()
	}

	companion object {
		lateinit var itemDao: ItemDao
	}
}