package com.roland.android.shrine.utils

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.roland.android.shrine.R
import com.roland.android.shrine.data.ItemData

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

@Composable
fun ShowDialog(
    item: ItemData,
    removeFromWishlist: (ItemData) -> Unit,
    openDialog: (Boolean) -> Unit,
    favoriteIcon: (ImageVector) -> Unit
) {
    AlertDialog(
        onDismissRequest = { openDialog(false) },
        title = { Text(stringResource(R.string.wishlist)) },
        text = { Text(stringResource(R.string.snack_text, item.title)) },
        confirmButton = {
            TextButton(onClick = {
                openDialog(false)
                removeFromWishlist(item)
                favoriteIcon(Icons.Outlined.FavoriteBorder)
            }) {
                Text(
                    text = stringResource(R.string.remove),
                    color = MaterialTheme.colors.onSecondary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { openDialog(false) }) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = MaterialTheme.colors.onSecondary
                )
            }
        }
    )
}