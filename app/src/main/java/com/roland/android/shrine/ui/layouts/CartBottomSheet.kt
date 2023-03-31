package com.roland.android.shrine.ui.layouts

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roland.android.shrine.data.ExpandedCartItem
import com.roland.android.shrine.data.SampleItemsData
import com.roland.android.shrine.data.model.ItemData
import com.roland.android.shrine.ui.screens.CartBottomSheetState
import com.roland.android.shrine.ui.theme.ShrineTheme
import com.roland.android.shrine.viewmodel.SharedViewModel
import com.roland.android.shrine.viewmodel.ViewModelFactory

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalAnimationApi
@Composable
fun CartBottomSheet(
    modifier: Modifier = Modifier,
    viewModel: SharedViewModel,
    items: List<ItemData>,
    wishlist: List<ItemData>,
    maxHeight: Dp,
    maxWidth: Dp,
    sheetState: CartBottomSheetState,
    isFirstItem: Boolean = false,
    onRemoveFromCart: (ItemData) -> Unit = {},
    navigateToDetail: (ItemData) -> Unit = {},
    moveToCatalogue: () -> Unit = {},
    proceedToCheckout: () -> Unit = {},
    onSheetStateChanged: (CartBottomSheetState) -> Unit = {}
) {
    val expandedCartItems by remember(items) {
        derivedStateOf {
            items.mapIndexed { index, it -> ExpandedCartItem(index = index, data = it) }
        }
    }

    LaunchedEffect(expandedCartItems) {
        snapshotFlow {
            expandedCartItems.firstOrNull {
                it.visible.isIdle && !it.visible.targetState
            }
        }.collect {
            if (it != null) { onRemoveFromCart(it.data) }
        }
    }

    val cartTransition = updateTransition(
        targetState = sheetState,
        label = "cartTransition"
    )

    val cartOffset by cartTransition.animateDp(
        transitionSpec = {
            when {
                CartBottomSheetState.Expanded isTransitioningTo CartBottomSheetState.Collapsed ->
                    tween(durationMillis = 433, delayMillis = 67)
                CartBottomSheetState.Collapsed isTransitioningTo CartBottomSheetState.Expanded ->
                    tween(durationMillis = 150)
                else -> tween(durationMillis = 450)
            }
        },
        label = "cartOffset"
    ) {
        when (it) {
            CartBottomSheetState.Hidden -> maxWidth
            CartBottomSheetState.Expanded -> 0.dp
            else -> {
                val size = Integer.min(3, items.size)
                var width = 24 + (40 + 16) + (size * (40 + 16))
                if (items.size > 3) width += (32 + 16)
                (maxWidth.value - width).dp
            }
        }
    }

    val cartHeight by cartTransition.animateDp(
        transitionSpec = {
            when {
                CartBottomSheetState.Expanded isTransitioningTo CartBottomSheetState.Collapsed ->
                    tween(durationMillis = 283)
                else -> tween(durationMillis = 500)
            }
        },
        label = "cartHeight"
    ) {
        if (it == CartBottomSheetState.Expanded) maxHeight else 56.dp
    }

    val cornerSize by cartTransition.animateDp(
        transitionSpec = {
            when {
                CartBottomSheetState.Expanded isTransitioningTo CartBottomSheetState.Collapsed ->
                    tween(durationMillis = 435, delayMillis = 67)
                else -> tween(durationMillis = 250)
            }
        },
        label = "cartCornerSize"
    ) {
        if (it == CartBottomSheetState.Expanded) 0.dp else 24.dp
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(cartHeight)
            .offset(x = cartOffset),
        shape = CutCornerShape(topStart = cornerSize),
        color = MaterialTheme.colors.secondary,
        elevation = 8.dp
    ) {
        Box {
            cartTransition.AnimatedContent(
                transitionSpec = {
                    when {
                        CartBottomSheetState.Expanded isTransitioningTo CartBottomSheetState.Collapsed ->
                            fadeIn(animationSpec = tween(durationMillis = 117,
                                delayMillis = 117,
                                easing = LinearEasing)) with
                                    fadeOut(animationSpec = tween(durationMillis = 117,
                                        easing = LinearEasing))
                        CartBottomSheetState.Collapsed isTransitioningTo CartBottomSheetState.Expanded ->
                            fadeIn(animationSpec = tween(durationMillis = 150,
                                delayMillis = 150,
                                easing = LinearEasing)) with
                                    fadeOut(animationSpec = tween(durationMillis = 150,
                                        easing = LinearEasing))
                        else -> EnterTransition.None with ExitTransition.None
                    }.using(SizeTransform(clip = false))
                },
            ) { targetState ->
                if (targetState == CartBottomSheetState.Expanded) {
                    ExpandedCart(
                        viewModel = viewModel,
                        expandedCartItems = expandedCartItems,
                        wishlist = wishlist,
                        onCollapse = { onSheetStateChanged(CartBottomSheetState.Collapsed) },
                        navigateToDetail = navigateToDetail,
                        removeFromCart = { it.visible.targetState = false },
                        moveToCatalogue = moveToCatalogue
                    )
                } else {
                    CollapsedCart(
                        items = items,
                        isFirstItem = isFirstItem,
                        onTap = { onSheetStateChanged(CartBottomSheetState.Expanded) }
                    )
                }
            }

            cartTransition.AnimatedVisibility(
                modifier = Modifier.align(Alignment.BottomCenter),
                visible = { it == CartBottomSheetState.Expanded }
            ) {
                if (items.isNotEmpty()) { CheckoutButton(proceedToCheckout) }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalAnimationApi
@Preview
@Composable
fun CartBottomSheetPreview() {
    ShrineTheme {
        BoxWithConstraints(
            Modifier.fillMaxSize()
        ) {
            var sheetState by remember { mutableStateOf(CartBottomSheetState.Expanded) }
            val cartItems = remember { mutableStateListOf(*SampleItemsData.takeLast(4).toTypedArray()) }
            val wishlist = remember { SampleItemsData.takeLast(10) }

            CartBottomSheet(
                modifier = Modifier.align(Alignment.BottomEnd),
                viewModel = viewModel(factory = ViewModelFactory()),
                items = cartItems,
                wishlist = wishlist,
                maxHeight = maxHeight,
                maxWidth = maxWidth,
                sheetState = sheetState,
                onRemoveFromCart = { cartItems.remove(it) },
                onSheetStateChanged = { sheetState = it }
            )
        }
    }
}