package com.roland.android.shrine.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
    sharedViewModel: SharedViewModel,
    onBackPressed: () -> Unit
) {
    var sheetState by rememberSaveable { mutableStateOf(CartBottomSheetState.Collapsed) }
    val cartItems = sharedViewModel.cartItems
    var firstCartItem by remember { mutableStateOf<FirstCartItemData?>(null) }

    BoxWithConstraints(
        Modifier.fillMaxSize()
    ) {
        ItemDetail(
            item = sharedViewModel.data,
            addToCart = {
                if (cartItems.isEmpty()) firstCartItem = it
                sharedViewModel.addToCart(it.data)
            },
            onBackPressed = { onBackPressed() }
        )
        CartBottomSheet(
            modifier = Modifier.align(Alignment.BottomEnd),
            items = cartItems,
            maxHeight = maxHeight,
            maxWidth = maxWidth,
            sheetState = sheetState,
            isFirstItem = firstCartItem != null,
            onSheetStateChanged = { sheetState = it },
            onRemoveFromCart = {
                sharedViewModel.removeFromCart(it)
                if (cartItems.isEmpty()) {
                    sheetState = CartBottomSheetState.Collapsed
                }
            }
        )
        if (firstCartItem != null) {
            FirstCartItem(data = firstCartItem!!) {
                // Temporary to dismiss
                firstCartItem = null
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
        DetailScreen(sharedViewModel = SharedViewModel()) {}
    }
}