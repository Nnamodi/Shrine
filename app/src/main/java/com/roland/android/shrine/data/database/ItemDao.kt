package com.roland.android.shrine.data.database

import androidx.room.*
import com.roland.android.shrine.data.model.ItemData
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
	@Query("SELECT * FROM item_data WHERE isCartItem LIKE :isCartItem")
	fun getCartItems(isCartItem: Boolean = true): Flow<List<ItemData>>

	@Query("SELECT * FROM item_data WHERE favourited LIKE :favourited")
	fun getWishlist(favourited: Boolean = true): Flow<List<ItemData>>

	@Query("SELECT * FROM item_data WHERE purchased LIKE :purchased")
	fun getOrderHistory(purchased: Boolean = true): Flow<List<ItemData>>

	@Insert
	fun addItem(item: ItemData)

	@Delete
	fun removeItem(item: ItemData)
}