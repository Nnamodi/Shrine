package com.roland.android.shrine.utils

import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.roland.android.shrine.data.ItemData

fun onFavoriteClicked(
    item: ItemData,
    addToWishlist: (ItemData) -> Unit = {},
    removeFromWishlist: (ItemData) -> Unit = {},
    favoriteIcon: (ImageVector) -> Unit = {}
) {
    if (item.favourited) {
        favoriteIcon(Icons.Outlined.FavoriteBorder)
        removeFromWishlist(item)
    } else {
        favoriteIcon(Icons.Outlined.Favorite)
        addToWishlist(item)
    }
}

@Composable
fun SnackbarMessage(favourite: Boolean) {
    return if (favourite) {
        Text("Item added to wishlist")
    } else {
        Text("Item removed from wishlist")
    }
}