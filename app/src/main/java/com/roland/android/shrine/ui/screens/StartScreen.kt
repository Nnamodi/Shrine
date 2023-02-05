package com.roland.android.shrine.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.roland.android.shrine.data.SampleItemsData
import com.roland.android.shrine.ui.layouts.BackDrop
import com.roland.android.shrine.ui.layouts.CartBottomSheet
import com.roland.android.shrine.utils.FirstCartItem
import com.roland.android.shrine.utils.FirstCartItemData

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun StartScreen(logout: () -> Unit = {}) {
    var sheetState by rememberSaveable  { mutableStateOf(CartBottomSheetState.Collapsed) }
    val cartItems = remember { mutableStateListOf(*SampleItemsData.take(0).toTypedArray()) }
    var firstCartItem by remember { mutableStateOf<FirstCartItemData?>(null) }

    BoxWithConstraints(
        Modifier.fillMaxSize()
    ) {
        BackDrop(
            onReveal = { revealed ->
                sheetState = if (revealed) CartBottomSheetState.Hidden else CartBottomSheetState.Collapsed
            },
            addToCart = {
                if (cartItems.isEmpty()) firstCartItem = it
                cartItems.add(it.data)
            },
            logout = logout
        )
        CartBottomSheet(
            modifier = Modifier.align(Alignment.BottomEnd),
            items = cartItems,
            sheetState = sheetState,
            maxHeight = maxHeight,
            maxWidth = maxWidth,
            isFirstItem = firstCartItem != null,
            onSheetStateChanged = { sheetState = it },
            onRemoveFromCart = {
                cartItems.removeAt(it)
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

enum class CartBottomSheetState {
    Collapsed,
    Expanded,
    Hidden
}