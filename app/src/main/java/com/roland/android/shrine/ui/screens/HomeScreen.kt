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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roland.android.shrine.R
import com.roland.android.shrine.data.model.ItemData
import com.roland.android.shrine.ui.layouts.BackDrop
import com.roland.android.shrine.ui.layouts.CartBottomSheet
import com.roland.android.shrine.ui.theme.ShrineTheme
import com.roland.android.shrine.utils.FirstCartItem
import com.roland.android.shrine.utils.FirstCartItemData
import com.roland.android.shrine.viewmodel.SharedViewModel
import com.roland.android.shrine.viewmodel.ViewModelFactory

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun HomeScreen(
    navigateToDetail: (ItemData) -> Unit,
    sharedViewModel: SharedViewModel,
    userIsNull: Boolean = false,
    proceedToCheckout: () -> Unit,
    onAccountButtonPressed: () -> Unit = {},
    onLogin: () -> Unit = {},
    closeApp: () -> Unit = {}
) {
    var sheetState by rememberSaveable { mutableStateOf(CartBottomSheetState.Collapsed) }
    val cartItems = sharedViewModel.cartItems
    val wishlist = sharedViewModel.wishlist
    var firstCartItem by remember { mutableStateOf<FirstCartItemData?>(null) }
    val accountMenuText = if (!userIsNull) {
        stringResource(R.string.my_account) } else { stringResource(R.string.login) }

    BoxWithConstraints(
        Modifier.fillMaxSize()
    ) {
        BackDrop(
            viewModel = sharedViewModel,
            accountMenuText = accountMenuText,
            userIsNull = userIsNull,
            onReveal = { revealed ->
                sheetState = if (revealed) CartBottomSheetState.Hidden else CartBottomSheetState.Collapsed
            },
            addToCart = {
                if (cartItems.isEmpty()) firstCartItem = it
                sharedViewModel.addToCart(it.data)
            },
            onLogin = onLogin,
            navigateToDetail = navigateToDetail,
            addToWishlist = sharedViewModel::addToWishlist,
            removeFromWishlist = sharedViewModel::removeFromWishlist,
            onViewWishlist = { sheetState = it },
            onAccountButtonPressed = onAccountButtonPressed
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
            if (sheetState == CartBottomSheetState.Collapsed) { closeApp() }
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
fun HomeScreenPreview() {
    ShrineTheme {
        HomeScreen(
            navigateToDetail = {},
            sharedViewModel = viewModel(factory = ViewModelFactory()),
            proceedToCheckout = {}
        )
    }
}