package com.roland.android.shrine.ui.screens

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roland.android.shrine.data.model.ItemData
import com.roland.android.shrine.ui.layouts.BackDrop
import com.roland.android.shrine.ui.layouts.CartBottomSheet
import com.roland.android.shrine.ui.theme.ShrineTheme
import com.roland.android.shrine.utils.FirstCartItem
import com.roland.android.shrine.utils.FirstCartItemData
import com.roland.android.shrine.viewmodel.SharedViewModel
import com.roland.android.shrine.viewmodel.SharedViewModelFactory

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun StartScreen(
    navigateToDetail: (ItemData) -> Unit,
    sharedViewModel: SharedViewModel,
    proceedToCheckout: () -> Unit,
    logout: () -> Unit = {}
) {
    var sheetState by rememberSaveable { mutableStateOf(CartBottomSheetState.Collapsed) }
    val cartItems = sharedViewModel.cartItems
    val wishlist = sharedViewModel.wishlist
    var firstCartItem by remember { mutableStateOf<FirstCartItemData?>(null) }

    BoxWithConstraints(
        Modifier.fillMaxSize()
    ) {
        BackDrop(
            viewModel = sharedViewModel,
            onReveal = { revealed ->
                sheetState = if (revealed) CartBottomSheetState.Hidden else CartBottomSheetState.Collapsed
            },
            addToCart = {
                if (cartItems.isEmpty()) firstCartItem = it
                sharedViewModel.addToCart(it.data)
            },
            navigateToDetail = {
                sharedViewModel.addScreen(it)
                navigateToDetail(it)
            },
            addToWishlist = {
                sharedViewModel.addToWishlist(it)
            },
            removeFromWishlist = {
                sharedViewModel.removeFromWishlist(it)
            },
            onViewWishlist = { sheetState = it },
            logout = logout
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
            navigateToDetail = {
                sharedViewModel.addScreen(it)
                navigateToDetail(it)
            },
            onRemoveFromCart = {
                sharedViewModel.removeFromCart(it)
            },
            moveToCatalogue = { sheetState = CartBottomSheetState.Collapsed },
            proceedToCheckout = proceedToCheckout
        )
        if (firstCartItem != null) {
            FirstCartItem(data = firstCartItem!!) {
                // Temporary to dismiss
                firstCartItem = null
            }
        }
        BackHandler {
            if (sheetState == CartBottomSheetState.Collapsed) {
                logout()
            }
            if (sheetState == CartBottomSheetState.Expanded) {
                sheetState = CartBottomSheetState.Collapsed
            }
        }
    }
}

enum class CartBottomSheetState {
    Collapsed,
    Expanded,
    Hidden
}

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Preview
@Composable
fun StartScreenPreview() {
    ShrineTheme {
        StartScreen(
            navigateToDetail = {},
            sharedViewModel = viewModel(factory = SharedViewModelFactory()),
            proceedToCheckout = {}
        )
    }
}