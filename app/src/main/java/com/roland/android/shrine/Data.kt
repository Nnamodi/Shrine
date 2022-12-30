package com.roland.android.shrine

data class ItemData(
    val id: Int,
    val title: String,
    val price: Int,
    val vendor: Vendor,
    val photoResId: Int,
)

enum class Vendor {
    Alphi,
    Labrjk,
    Mal,
    Six,
    Squiggle
}

val SampleItemsData = listOf(
    ItemData(
        id = 0,
        title = "Vagabond sack",
        price = 120,
        vendor = Vendor.Squiggle,
        photoResId = R.drawable.img
    ),
    ItemData(
        id = 1,
        title = "Stella sunglasses",
        price = 50,
        vendor = Vendor.Mal,
        photoResId = R.drawable.ic_launcher_foreground
    ),
    ItemData(
        id = 2,
        title = "Whitney belt",
        price = 35,
        vendor = Vendor.Labrjk,
        photoResId = R.drawable.ic_launcher_background
    ),
    ItemData(
        id = 3,
        title = "Garden stand",
        price = 98,
        vendor = Vendor.Alphi,
        photoResId = R.drawable.ic_launcher_foreground
    ),
    ItemData(
        id = 4,
        title = "Strut earrings",
        price = 34,
        vendor = Vendor.Six,
        photoResId = R.drawable.ic_launcher_foreground
    ),
    ItemData(
        id = 5,
        title = "Varsity socks",
        price = 12,
        vendor = Vendor.Labrjk,
        photoResId = R.drawable.ic_launcher_foreground
    ),
    ItemData(
        id = 6,
        title = "Weave key ring",
        price = 16,
        vendor = Vendor.Six,
        photoResId = R.drawable.ic_launcher_background
    ),
    ItemData(
        id = 7,
        title = "Gatsby hat",
        price = 40,
        vendor = Vendor.Six,
        photoResId = R.drawable.ic_launcher_background
    ),
    ItemData(
        id = 8,
        title = "Shrug bag",
        price = 198,
        vendor = Vendor.Squiggle,
        photoResId = R.drawable.img
    ),
    ItemData(
        id = 9,
        title = "Gilt desk trio",
        price = 58,
        vendor = Vendor.Alphi,
        photoResId = R.drawable.ic_launcher_background
    )
)