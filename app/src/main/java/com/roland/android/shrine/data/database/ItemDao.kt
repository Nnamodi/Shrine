package com.roland.android.shrine.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.roland.android.shrine.data.model.ItemData
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
	@Query("SELECT * FROM item_data WHERE isCartItem LIKE :isCartItem AND favourited LIKE :favourited")
	fun getItems(isCartItem: Boolean, favourited: Boolean): Flow<List<ItemData>>

	@Insert
	fun addItem(item: ItemData)

	@Delete
	fun removeItem(item: ItemData)
}