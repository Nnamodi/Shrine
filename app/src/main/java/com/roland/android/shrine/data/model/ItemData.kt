package com.roland.android.shrine.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.roland.android.shrine.data.Category
import com.roland.android.shrine.data.Vendor

@Entity(tableName = "item_data")
data class ItemData(
	@PrimaryKey(autoGenerate = true)
	val generatedId: Int = 0,
	val id: Int,
	val title: String,
	val price: Int,
	var favourited: Boolean = false,
	val vendor: Vendor,
	val category: Category,
	val photoResId: Int,
)