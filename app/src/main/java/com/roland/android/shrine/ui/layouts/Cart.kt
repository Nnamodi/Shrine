package com.roland.android.shrine.ui.layouts

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.roland.android.shrine.data.ExpandedCartItem
import com.roland.android.shrine.data.ItemData
import com.roland.android.shrine.data.SampleItemsData
import com.roland.android.shrine.ui.screens.CartBottomSheetState
import com.roland.android.shrine.ui.theme.ShrineTheme
import java.lang.Integer.min

@ExperimentalAnimationApi
@Composable
fun ExpandedCart(
    expandedCartItems: List<ExpandedCartItem>,
    wishlist: List<ItemData>,
    onCollapse: () -> Unit = {},
    removeFromCart: (ExpandedCartItem) -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colors.secondary
    ) {
        CartHeader(expandedCartItems.size) { onCollapse() }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 50.dp),
            contentPadding = PaddingValues(bottom = 72.dp)
        ) {
            itemsIndexed(
                items = expandedCartItems,
                key = { index, item -> "$index-${item.data.id}" }
            ) { _, item ->
                AnimatedVisibility(
                    visibleState = item.visible,
                    exit = fadeOut() + slideOut(targetOffset = { IntOffset(x = -it.width / 2, y = 0) })
                ) {
                    CartItem(
                        item = item.data,
                        itemIsFirst = expandedCartItems.first() == item,
                        removeFromCart = { removeFromCart(item) }
                    )
                }
            }
            item {
                OtherItems(
                    header = "Wishlist",
                    modifier = Modifier
                        .background(color = MaterialTheme.colors.surface)
                        .padding(vertical = 24.dp),
                    bottomPadding = 40.dp,
                    otherItems = wishlist,
                    addToCart = {},
                    navigateToDetail = {},
                    shownInWishlist = true
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun CollapsedCart(
    items: List<ItemData>,
    onTap: () -> Unit = {},
    isFirstItem: Boolean
) {
    val imageSize = updateTransition(MutableTransitionState(isFirstItem), label = "Image size")
        .animateDp(
            transitionSpec = { tween(durationMillis = 400, delayMillis = 400) },
            label = "Animated size"
        ) {
            if (it) 0.dp else 40.dp
        }
    Row(
        modifier = Modifier
            .padding(start = 24.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
            .clickable { onTap() },
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            Modifier.size(40.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Shopping cart icon"
            )
        }
        items.take(3).forEach { item ->
            CollapsedCartItem(item.photoResId, item.title, imageSize.value)
        }
        if (items.size > 3) {
            Box(
                modifier = Modifier.size(width = 32.dp, height = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "+${items.size - 3}",
                    style = MaterialTheme.typography.subtitle2
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
private fun CollapsedCartItem(
    photoId: Int,
    description: String,
    imageSize: Dp
) {
    Box(modifier = Modifier.size(40.dp)) {
        Image(
            painter = painterResource(id = photoId),
            contentDescription = description,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(imageSize)
                .clip(RoundedCornerShape(10.dp))
        )
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalAnimationApi
@Composable
fun CartBottomSheet(
    modifier: Modifier = Modifier,
    items: List<ItemData>,
    wishlist: List<ItemData>,
    maxHeight: Dp,
    maxWidth: Dp,
    sheetState: CartBottomSheetState,
    isFirstItem: Boolean = false,
    onRemoveFromCart: (Int) -> Unit = {},
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
            if (it != null) { onRemoveFromCart(it.index) }
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
                val size = min(3, items.size)
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
                        expandedCartItems = expandedCartItems,
                        wishlist = wishlist,
                        onCollapse = { onSheetStateChanged(CartBottomSheetState.Collapsed) },
                        removeFromCart = { it.visible.targetState = false }
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
                visible = { it == CartBottomSheetState.Expanded },
                enter = fadeIn(animationSpec = tween(durationMillis = 150, delayMillis = 150, LinearEasing)) +
                        expandIn(animationSpec = tween(durationMillis = 250, delayMillis = 250, easing = LinearOutSlowInEasing), initialSize = { IntSize.Zero }),
                exit = fadeOut(animationSpec = tween(durationMillis = 117, easing = LinearEasing)) +
                        shrinkOut(animationSpec = tween(durationMillis = 100, easing = FastOutLinearInEasing), targetSize = { IntSize.Zero })
            ) {
                CheckoutButton()
            }
        }
    }
}

@Composable
private fun CheckoutButton() {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        onClick = {}
    ) {
        Icon(
            imageVector = Icons.Outlined.ShoppingCart,
            contentDescription = "Shopping cart icon"
        )
        Spacer(Modifier.width(16.dp))
        Text(text = "Proceed to checkout".uppercase())
    }
}

@Composable
private fun CartHeader(
    itemSize: Int,
    onCollapse: () -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colors.surface
    ) {
        Column {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onCollapse() }) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Collapse cart icon"
                    )
                }
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "Cart".uppercase(),
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    if (itemSize != 1) { "$itemSize items" }
                    else { "$itemSize item" }.uppercase()
                )
            }
            Divider(color = MaterialTheme.colors.onSecondary.copy(alpha = 0.3f))
        }
    }
}

@Composable
private fun CartItem(
    item: ItemData,
    itemIsFirst: Boolean = false,
    removeFromCart: (ItemData) -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colors.surface
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { removeFromCart(item) },
                Modifier.padding(horizontal = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.RemoveCircleOutline,
                    contentDescription = "Remove item icon"
                )
            }
            Column(
                Modifier.fillMaxWidth()
            ) {
                if (!itemIsFirst) {
                    Divider(color = MaterialTheme.colors.onSecondary.copy(alpha = 0.3f))
                }
                Row(
                    Modifier.padding(vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = item.photoResId),
                        modifier = Modifier.size(width = 100.dp, height = 100.dp),
                        contentDescription = item.title
                    )
                    Spacer(modifier = Modifier.padding(end = 16.dp))
                    Column(
                        Modifier.padding(end = 16.dp)
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.vendor.name.uppercase(),
                                style = MaterialTheme.typography.body2
                            )
                            Text(
                                text = item.price.toString().prependIndent("$"),
                                style = MaterialTheme.typography.body2
                            )
                        }
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.subtitle2
                        )
                    }
                }
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
            var sheetState by remember { mutableStateOf(CartBottomSheetState.Collapsed) }
            val cartItems = remember { mutableStateListOf(*SampleItemsData.take(10).toTypedArray()) }
            val wishlist = remember { SampleItemsData.takeLast(10) }

            CartBottomSheet(
                modifier = Modifier.align(Alignment.BottomEnd),
                items = cartItems,
                wishlist = wishlist,
                maxHeight = maxHeight,
                maxWidth = maxWidth,
                sheetState = sheetState,
                onRemoveFromCart = { cartItems.removeAt(it) },
                onSheetStateChanged = { sheetState = it }
            )
        }
    }
}