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
        Text("Item added to wishlist")
    } else {
        Text("Item removed from wishlist")
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
        title = { Text("Wishlist") },
        text = { Text("Remove '${item.title}' from your wishlist?") },
        confirmButton = {
            TextButton(onClick = {
                openDialog(false)
                removeFromWishlist(item)
                favoriteIcon(Icons.Outlined.FavoriteBorder)
            }) {
                Text(
                    text = "Remove",
                    color = MaterialTheme.colors.onSecondary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { openDialog(false) }) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colors.onSecondary
                )
            }
        }
    )
}