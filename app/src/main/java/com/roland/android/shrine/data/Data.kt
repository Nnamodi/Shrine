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
        title = "Kitchen quattro",
        price = 129,
        vendor = Vendor.Alphi,
        category = Category.Home,
        photoResId = R.drawable.photo_18
    ),
    ItemData(
        id = 1,
        title = "Copper wire rack",
        price = 18,
        vendor = Vendor.Alphi,
        category = Category.Home,
        photoResId = R.drawable.photo_10
    ),
    ItemData(
        id = 2,
        title = "Sea tunic",
        price = 48,
        vendor = Vendor.Labrjk,
        category = Category.Clothing,
        photoResId = R.drawable.photo_20
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
        title = "Succulent planters",
        price = 16,
        vendor = Vendor.Alphi,
        category = Category.Home,
        photoResId = R.drawable.photo_16
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
        title = "Navy trousers",
        price = 74,
        vendor = Vendor.Six,
        category = Category.Clothing,
        photoResId = R.drawable.photo_26
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
        title = "Chambray shirt",
        price = 38,
        vendor = Vendor.Mal,
        category = Category.Clothing,
        photoResId = R.drawable.photo_31
    ),
    ItemData(
        id = 10,
        title = "Rainwater tray",
        price = 27,
        vendor = Vendor.Six,
        category = Category.Home,
        photoResId = R.drawable.photo_14
    ),
    ItemData(
        id = 11,
        title = "White pinstripe shirt",
        price = 70,
        vendor = Vendor.Labrjk,
        category = Category.Clothing,
        photoResId = R.drawable.photo_22
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
        title = "Seabreeze sweater",
        price = 60,
        vendor = Vendor.Six,
        category = Category.Clothing,
        photoResId = R.drawable.photo_24
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
        title = "Ginger scarf",
        price = 98,
        vendor = Vendor.Alphi,
        category = Category.Clothing,
        photoResId = R.drawable.photo_29
    ),
    ItemData(
        id = 17,
        title = "Shoulder rolls tee",
        price = 27,
        vendor = Vendor.Six,
        category = Category.Clothing,
        photoResId = R.drawable.photo_34
    ),
    ItemData(
        id = 18,
        title = "Vagabond sack",
        price = 120,
        vendor = Vendor.Squiggle,
        category = Category.Accessories,
        photoResId = R.drawable.photo_0
    ),
    ItemData(
        id = 19,
        title = "Clay sweater",
        price = 48,
        vendor = Vendor.Labrjk,
        category = Category.Clothing,
        photoResId = R.drawable.photo_19
    ),
    ItemData(
        id = 20,
        title = "Whitney belt",
        price = 35,
        vendor = Vendor.Labrjk,
        category = Category.Accessories,
        photoResId = R.drawable.photo_2
    ),
    ItemData(
        id = 21,
        title = "Plastic tunic",
        price = 38,
        vendor = Vendor.Labrjk,
        category = Category.Clothing,
        photoResId = R.drawable.photo_21
    ),
    ItemData(
        id = 22,
        title = "Soothe ceramic set",
        price = 28,
        vendor = Vendor.Mal,
        category = Category.Home,
        photoResId = R.drawable.photo_11
    ),
    ItemData(
        id = 23,
        title = "Quartet table",
        price = 175,
        vendor = Vendor.Squiggle,
        category = Category.Home,
        photoResId = R.drawable.photo_17
    ),
    ItemData(
        id = 24,
        title = "Stella sunglasses",
        price = 50,
        vendor = Vendor.Mal,
        category = Category.Accessories,
        photoResId = R.drawable.photo_1
    ),
    ItemData(
        id = 25,
        title = "Sunshine dress",
        price = 58,
        vendor = Vendor.Alphi,
        category = Category.Clothing,
        photoResId = R.drawable.photo_36
    ),
    ItemData(
        id = 26,
        title = "Gatsby hat",
        price = 40,
        vendor = Vendor.Six,
        category = Category.Accessories,
        photoResId = R.drawable.photo_7
    ),
    ItemData(
        id = 27,
        title = "Walter henley white",
        price = 38,
        vendor = Vendor.Six,
        category = Category.Clothing,
        photoResId = R.drawable.photo_27
    ),
    ItemData(
        id = 28,
        title = "Surf and perf shirt",
        price = 48,
        vendor = Vendor.Squiggle,
        category = Category.Clothing,
        photoResId = R.drawable.photo_28
    ),
    ItemData(
        id = 29,
        title = "Strut earrings",
        price = 34,
        vendor = Vendor.Six,
        category = Category.Accessories,
        photoResId = R.drawable.photo_4
    ),
    ItemData(
        id = 30,
        title = "Ramona crossover",
        price = 68,
        vendor = Vendor.Alphi,
        category = Category.Clothing,
        photoResId = R.drawable.photo_30
    ),
    ItemData(
        id = 31,
        title = "Gilt desk trio",
        price = 58,
        vendor = Vendor.Alphi,
        category = Category.Home,
        photoResId = R.drawable.photo_9
    ),
//    ItemData(
//        id = 32,
//        title = "Class white collar",
//        price = 58,
//        vendor = Vendor.Labrjk,
//        category = Category.Clothing,
//        photoResId = R.drawable.photo_32
//    ),
    ItemData(
        id = 33,
        title = "Cerise scallop tee",
        price = 42,
        vendor = Vendor.Mal,
        category = Category.Clothing,
        photoResId = R.drawable.photo_33
    ),
    ItemData(
        id = 34,
        title = "Chambray shirt",
        price = 70,
        vendor = Vendor.Alphi,
        category = Category.Clothing,
        photoResId = R.drawable.photo_23
    ),
    ItemData(
        id = 35,
        title = "Grey slouch tank",
        price = 24,
        vendor = Vendor.Six,
        category = Category.Clothing,
        photoResId = R.drawable.photo_35
    ),
    ItemData(
        id = 36,
        title = "Gentry jacket",
        price = 178,
        vendor = Vendor.Labrjk,
        category = Category.Clothing,
        photoResId = R.drawable.photo_25
    ),
    ItemData(
        id = 37,
        title = "Fine lines tee",
        price = 58,
        vendor = Vendor.Squiggle,
        category = Category.Clothing,
        photoResId = R.drawable.photo_37
    )
)