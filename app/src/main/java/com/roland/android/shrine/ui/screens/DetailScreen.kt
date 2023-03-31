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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roland.android.shrine.data.SampleItemsData
import com.roland.android.shrine.data.model.ItemData
import com.roland.android.shrine.ui.layouts.CartBottomSheet
import com.roland.android.shrine.ui.layouts.ItemDetail
import com.roland.android.shrine.ui.theme.ShrineTheme
import com.roland.android.shrine.utils.FirstCartItem
import com.roland.android.shrine.utils.FirstCartItemData
import com.roland.android.shrine.viewmodel.SharedViewModel
import com.roland.android.shrine.viewmodel.ViewModelFactory

@ExperimentalAnimationApi
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun DetailScreen(
    itemId: Int?,
    navigateToDetail: (ItemData) -> Unit,
    sharedViewModel: SharedViewModel,
    moveToCatalogue: () -> Unit,
    proceedToCheckout: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val item = SampleItemsData.find { itemId == it.id }
    var sheetState by rememberSaveable { mutableStateOf(CartBottomSheetState.Collapsed) }
    val cartItems = sharedViewModel.cartItems
    val wishlist = sharedViewModel.wishlist
    var firstCartItem by remember { mutableStateOf<FirstCartItemData?>(null) }

    BoxWithConstraints(
        Modifier.fillMaxSize()
    ) {
        ItemDetail(
            item = item!!,
            viewModel = sharedViewModel,
            addToCart = {
                if (cartItems.isEmpty()) firstCartItem = it
                sharedViewModel.addToCart(it.data)
            },
            addToWishlist = sharedViewModel::addToWishlist,
            removeFromWishlist = sharedViewModel::removeFromWishlist,
            navigateToDetail = navigateToDetail,
            onViewWishlist = { sheetState = it },
            onNavigateUp = onNavigateUp
        )
        CartBottomSheet(
            modifier = Modifier.align(Alignment.BottomEnd),
            viewModel = sharedViewModel,
            items = cartItems,
            wishlist = wishlist,
            maxHeight = maxHeight,
            maxWidth = maxWidth,
            sheetState = sheetState,
            isFirstItem = firstCartItem != null,
            onSheetStateChanged = { sheetState = it },
            navigateToDetail = navigateToDetail,
            onRemoveFromCart = sharedViewModel::removeFromCart,
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
            } else { onNavigateUp() }
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
            itemId = 10,
            navigateToDetail = {},
            sharedViewModel = viewModel(factory = ViewModelFactory()),
            moveToCatalogue = {},
            proceedToCheckout = {},
            onNavigateUp = {}
        )
    }
}