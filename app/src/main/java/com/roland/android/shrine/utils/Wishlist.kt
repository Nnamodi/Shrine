package com.roland.android.shrine.utils

import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.roland.android.shrine.R
import com.roland.android.shrine.data.model.ItemData

fun onFavoriteClicked(
    item: ItemData,
    addToWishlist: (ItemData) -> Unit,
    openDialog: (Boolean) -> Unit,
    favoriteIcon: (ImageVector) -> Unit
) {
    if (item.favourited) {
        openDialog(true)
    } else {
        favoriteIcon(Icons.Outlined.Favorite)
        addToWishlist(item)
    }
}

@Composable
fun SnackbarMessage(favourite: Boolean) {
    return if (favourite) {
        Text(stringResource(R.string.favorite_text))
    } else {
        Text(stringResource(R.string.unfavorite_text))
    }
}