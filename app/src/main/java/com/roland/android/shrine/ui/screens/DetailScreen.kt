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
import com.roland.android.shrine.data.SampleItemsData
import com.roland.android.shrine.ui.layouts.CartBottomSheet
import com.roland.android.shrine.ui.layouts.ItemDetail
import com.roland.android.shrine.ui.theme.ShrineTheme
import com.roland.android.shrine.viewmodel.SharedViewModel

@ExperimentalAnimationApi
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun DetailScreen(
    sharedViewModel: SharedViewModel,
    onBackPressed: () -> Unit
) {
    var sheetState by rememberSaveable { mutableStateOf(CartBottomSheetState.Collapsed) }
    val cartItems = remember { mutableStateListOf(*SampleItemsData.take(0).toTypedArray()) }

    BoxWithConstraints(
        Modifier.fillMaxSize()
    ) {
        ItemDetail(item = sharedViewModel.data) { onBackPressed() }

        CartBottomSheet(
            modifier = Modifier.align(Alignment.BottomEnd),
            items = cartItems,
            maxHeight = maxHeight,
            maxWidth = maxWidth,
            sheetState = sheetState,
            onSheetStateChanged = { sheetState = it },
            onRemoveFromCart = {
                cartItems.removeAt(it)
                if (cartItems.isEmpty()) {
                    sheetState = CartBottomSheetState.Collapsed
                }
            }
        )
    }
}

@ExperimentalAnimationApi
@RequiresApi(Build.VERSION_CODES.N)
@Preview
@Composable
fun ScreenPreview() {
    ShrineTheme {
        DetailScreen(sharedViewModel = SharedViewModel()) {}
    }
}