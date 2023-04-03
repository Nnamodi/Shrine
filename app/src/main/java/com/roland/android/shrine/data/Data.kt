package com.roland.android.shrine.data

import androidx.compose.animation.core.MutableTransitionState
import com.roland.android.shrine.R
import com.roland.android.shrine.data.model.ItemData

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