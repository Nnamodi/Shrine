package com.roland.android.shrine.data

import com.roland.android.shrine.data.model.ItemData

object FetchData {
	fun getItems(menuSelection: Category): List<ItemData> {
		return SampleItemsData.filter {
			menuSelection == Category.All || it.category == menuSelection
		}
	}

	fun getItem(itemId: Int?): ItemData {
		return SampleItemsData.find {
			it.id == itemId
		}!!
	}

	fun searchForItem(query: String): List<ItemData> {
		return SampleItemsData.filter {
			it.title.contains(query) ||
					it.vendor.name.contains(query)
		}
	}

	fun getMoreItemsFromVendor(item: ItemData): List<ItemData> {
		return SampleItemsData.filter {
			it.vendor == item.vendor &&
					it != item
		}
	}

	fun getSimilarItems(item: ItemData): List<ItemData> {
		return SampleItemsData.filter {
			it.category == item.category &&
					it != item
		}
	}
}