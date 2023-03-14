package com.roland.android.shrine.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.roland.android.shrine.data.model.ItemData

@Database(entities = [ItemData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
	abstract fun itemDao(): ItemDao
}