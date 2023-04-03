package com.roland.android.shrine.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.roland.android.shrine.data.Category
import com.roland.android.shrine.data.Vendor
import java.util.*

@Entity(tableName = "item_data")
data class ItemData(
	@PrimaryKey(autoGenerate = true)
	val generatedId: Int = 0,
	val id: Int,
	val title: String,
	val price: Int,
	var purchaseDate: Date = Date(),
	var purchased: Boolean = false,
	var isCartItem: Boolean = false,
	var favourited: Boolean = false,
	val vendor: Vendor,
	val category: Category,
	val photoResId: Int,
)