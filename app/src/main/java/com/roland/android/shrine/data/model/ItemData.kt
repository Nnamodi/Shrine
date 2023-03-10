package com.roland.android.shrine.data.model

import com.roland.android.shrine.data.Category
import com.roland.android.shrine.data.Vendor

data class ItemData(
	val id: Int,
	val title: String,
	val price: Int,
	var favourited: Boolean = false,
	val vendor: Vendor,
	val category: Category,
	val photoResId: Int,
)