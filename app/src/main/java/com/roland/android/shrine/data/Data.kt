package com.roland.android.shrine.data

import androidx.compose.animation.core.MutableTransitionState
import com.roland.android.shrine.R

data class ItemData(
    val id: Int,
    val title: String,
    val price: Int,
    var favourited: Boolean = false,
    val vendor: Vendor,
    val category: Category,
    val photoResId: Int,
)

data class ExpandedCartItem(
    val index: Int,
    val visible: MutableTransitionState<Boolean> = MutableTransitionState(true),
    val data: ItemData
)

enum class Vendor {
    Alphi,
    Labrjk,
    Mal,
    Six,
    Squiggle
}

fun getVendorResId(vendor: Vendor): Int =
    when (vendor) {
        Vendor.Alphi -> R.drawable.logo_alphi
        Vendor.Labrjk -> R.drawable.logo_lmb
        Vendor.Mal -> R.drawable.logo_mal
        Vendor.Six -> R.drawable.logo_6
        else -> R.drawable.logo_squiggle
    }

enum class Category {
    All,
    Accessories,
    Clothing,
    Home
}

val SampleItemsData = listOf(
    ItemData(
        id = 0,
        title = "Vagabond sack",
        price = 120,
        vendor = Vendor.Squiggle,
        category = Category.Accessories,
        photoResId = R.drawable.photo_0
    ),
    ItemData(
        id = 1,
        title = "Stella sunglasses",
        price = 50,
        vendor = Vendor.Mal,
        category = Category.Accessories,
        photoResId = R.drawable.photo_1
    ),
    ItemData(
        id = 2,
        title = "Whitney belt",
        price = 35,
        vendor = Vendor.Labrjk,
        category = Category.Accessories,
        photoResId = R.drawable.photo_2
    ),
    ItemData(
        id = 3,
        title = "Garden stand",
        price = 98,
        vendor = Vendor.Alphi,
        category = Category.Accessories,
        photoResId = R.drawable.photo_3
    ),
    ItemData(
        id = 4,
        title = "Strut earrings",
        price = 34,
        vendor = Vendor.Six,
        category = Category.Accessories,
        photoResId = R.drawable.photo_4
    ),
    ItemData(
        id = 5,
        title = "Varsity socks",
        price = 12,
        vendor = Vendor.Labrjk,
        category = Category.Accessories,
        photoResId = R.drawable.photo_5
    ),
    ItemData(
        id = 6,
        title = "Weave key ring",
        price = 16,
        vendor = Vendor.Six,
        category = Category.Accessories,
        photoResId = R.drawable.photo_6
    ),
    ItemData(
        id = 7,
        title = "Gatsby hat",
        price = 40,
        vendor = Vendor.Six,
        category = Category.Accessories,
        photoResId = R.drawable.photo_7
    ),
    ItemData(
        id = 8,
        title = "Shrug bag",
        price = 198,
        vendor = Vendor.Squiggle,
        category = Category.Accessories,
        photoResId = R.drawable.photo_8
    ),
    ItemData(
        id = 9,
        title = "Gilt desk trio",
        price = 58,
        vendor = Vendor.Alphi,
        category = Category.Home,
        photoResId = R.drawable.photo_9
    ),
    ItemData(
        id = 10,
        title = "Copper wire rack",
        price = 18,
        vendor = Vendor.Alphi,
        category = Category.Home,
        photoResId = R.drawable.photo_10
    ),
    ItemData(
        id = 11,
        title = "Soothe ceramic set",
        price = 28,
        vendor = Vendor.Mal,
        category = Category.Home,
        photoResId = R.drawable.photo_11
    ),
    ItemData(
        id = 12,
        title = "Hurrahs tea set",
        price = 34,
        vendor = Vendor.Six,
        category = Category.Home,
        photoResId = R.drawable.photo_12
    ),
    ItemData(
        id = 13,
        title = "Blue stone mug",
        price = 18,
        vendor = Vendor.Mal,
        category = Category.Home,
        photoResId = R.drawable.photo_13
    ),
    ItemData(
        id = 14,
        title = "Rainwater tray",
        price = 27,
        vendor = Vendor.Six,
        category = Category.Home,
        photoResId = R.drawable.photo_14
    ),
    ItemData(
        id = 15,
        title = "Chambray napkin",
        price = 27,
        vendor = Vendor.Six,
        category = Category.Home,
        photoResId = R.drawable.photo_15
    ),
    ItemData(
        id = 16,
        title = "Succulent planters",
        price = 16,
        vendor = Vendor.Alphi,
        category = Category.Home,
        photoResId = R.drawable.photo_16
    ),
    ItemData(
        id = 17,
        title = "Quartet table",
        price = 175,
        vendor = Vendor.Squiggle,
        category = Category.Home,
        photoResId = R.drawable.photo_17
    ),
    ItemData(
        id = 18,
        title = "Kitchen quattro",
        price = 129,
        vendor = Vendor.Alphi,
        category = Category.Home,
        photoResId = R.drawable.photo_18
    ),
    ItemData(
        id = 19,
        title = "Clay sweater",
        price = 48,
        vendor = Vendor.Labrjk,
        category = Category.Clothing,
        photoResId = R.drawable.photo_19
    )
)