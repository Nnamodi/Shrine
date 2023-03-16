package com.roland.android.shrine.ui.layouts.dialogs

import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.roland.android.shrine.R
import com.roland.android.shrine.data.model.ItemData

@Composable
fun WishlistDialog(
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