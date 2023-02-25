package com.roland.android.shrine.ui.screens

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.roland.android.shrine.data.ItemData
import com.roland.android.shrine.ui.layouts.CartBottomSheet
import com.roland.android.shrine.ui.layouts.ItemDetail
import com.roland.android.shrine.ui.theme.ShrineTheme
import com.roland.android.shrine.utils.FirstCartItem
import com.roland.android.shrine.utils.FirstCartItemData
import com.roland.android.shrine.viewmodel.SharedViewModel

@ExperimentalAnimationApi
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun DetailScreen(
    navigateToDetail: (ItemData) -> Unit,
    sharedViewModel: SharedViewModel,
    moveToCatalogue: () -> Unit,
    proceedToCheckout: () -> Unit,
    onNavigateUp: () -> Unit
) {
    var sheetState by rememberSaveable { mutableStateOf(CartBottomSheetState.Collapsed) }
    val cartItems = sharedViewModel.cartItems
    val wishlist = sharedViewModel.wishlist
    var firstCartItem by remember { mutableStateOf<FirstCartItemData?>(null) }

    if (sharedViewModel.data == null) { onNavigateUp() }
    else {
        BoxWithConstraints(
            Modifier.fillMaxSize()
        ) {
            ItemDetail(
                item = sharedViewModel.data!!,
                addToCart = {
                    if (cartItems.isEmpty()) firstCartItem = it
                    sharedViewModel.addToCart(it.data)
                },
                addToWishlist = {
                    sharedViewModel.addToWishlist(it)
                },
                removeFromWishlist = {
                    sharedViewModel.removeFromWishlist(it)
                },
                navigateToDetail = {
                    sharedViewModel.addScreen(it)
                    navigateToDetail(it)
                },
                onViewWishlist = { sheetState = it },
                onNavigateUp = {
                    onNavigateUp()
                    sharedViewModel.removeLastScreen()
                }
            )
            CartBottomSheet(
                modifier = Modifier.align(Alignment.BottomEnd),
                items = cartItems,
                wishlist = wishlist,
                maxHeight = maxHeight,
                maxWidth = maxWidth,
                sheetState = sheetState,
                isFirstItem = firstCartItem != null,
                onSheetStateChanged = { sheetState = it },
                navigateToDetail = {
                    sharedViewModel.addScreen(it)
                    navigateToDetail(it)
                },
                onRemoveFromCart = {
                    sharedViewModel.removeFromCart(it)
                },
                moveToCatalogue = moveToCatalogue,
                proceedToCheckout = proceedToCheckout
            )
            if (firstCartItem != null) {
                FirstCartItem(data = firstCartItem!!) {
                    // Temporary to dismiss
                    firstCartItem = null
                }
            }
            BackHandler {
                if (sheetState != CartBottomSheetState.Collapsed) {
                    sheetState = CartBottomSheetState.Collapsed
                } else {
                    onNavigateUp()
                    sharedViewModel.removeLastScreen()
                }
            }
        }
    }
}

@ExperimentalAnimationApi
@RequiresApi(Build.VERSION_CODES.N)
@Preview
@Composable
fun DetailScreenPreview() {
    ShrineTheme {
        DetailScreen(
            navigateToDetail = {},
            sharedViewModel = SharedViewModel(),
            moveToCatalogue = {},
            proceedToCheckout = {},
            onNavigateUp = {}
        )
    }
}