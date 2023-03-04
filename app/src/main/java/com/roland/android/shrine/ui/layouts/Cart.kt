package com.roland.android.shrine.ui.layouts

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.roland.android.shrine.R
import com.roland.android.shrine.data.ExpandedCartItem
import com.roland.android.shrine.data.ItemData
import com.roland.android.shrine.ui.theme.ShrineTheme

@ExperimentalAnimationApi
@Composable
fun ExpandedCart(
    expandedCartItems: List<ExpandedCartItem>,
    wishlist: List<ItemData>,
    onCollapse: () -> Unit = {},
    navigateToDetail: (ItemData) -> Unit = {},
    removeFromCart: (ExpandedCartItem) -> Unit = {},
    moveToCatalogue: () -> Unit = {}
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
                        removeFromCart = { removeFromCart(item) },
                        navigateToDetail = navigateToDetail
                    )
                }
            }
            item {
                if (expandedCartItems.isNotEmpty()) {
                    CartSummedValue(
                        modifier = Modifier.padding(start = 54.dp),
                        items = expandedCartItems
                    )
                }
            }
            item {
                if (expandedCartItems.isEmpty()) {
                    EmptyCartInfo(moveToCatalogue)
                }
            }
            item {
                OtherItems(
                    header = stringResource(R.string.wishlist),
                    modifier = Modifier.background(color = MaterialTheme.colors.surface),
                    bottomPadding = 40.dp,
                    otherItems = wishlist,
                    addToCart = {},
                    navigateToDetail = navigateToDetail,
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
                contentDescription = stringResource(R.string.shopping_icon_desc)
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

@Composable
fun CheckoutButton(proceedToCheckout: () -> Unit) {
    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        onClick = { proceedToCheckout() }
    ) {
        Icon(
            imageVector = Icons.Outlined.ShoppingCart,
            contentDescription = stringResource(R.string.shopping_icon_desc)
        )
        Spacer(Modifier.width(16.dp))
        Text(text = stringResource(R.string.checkout_button_text).uppercase())
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
                        contentDescription = stringResource(R.string.collapse_cart_desc)
                    )
                }
                Spacer(Modifier.width(4.dp))
                Text(
                    text = stringResource(R.string.cart).uppercase(),
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(Modifier.width(12.dp))
                Text(
                    if (itemSize != 1) { stringResource(R.string.cart_sizes, itemSize) }
                    else { stringResource(R.string.cart_size) }.uppercase()
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
    removeFromCart: (ItemData) -> Unit = {},
    navigateToDetail: (ItemData) -> Unit = {}
) {
    Surface(
        color = MaterialTheme.colors.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navigateToDetail(item) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { removeFromCart(item) },
                Modifier.padding(start = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.RemoveCircleOutline,
                    contentDescription = stringResource(R.string.remove_desc)
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
                        modifier = Modifier
                            .size(width = 100.dp, height = 100.dp)
                            .padding(start = 4.dp),
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

@Composable
fun EmptyCartInfo(
    moveToCatalogue: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.surface)
            .then(Modifier.padding(vertical = 60.dp)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = stringResource(R.string.empty_cart_desc),
            modifier = Modifier
                .rotate(-45f)
                .size(100.dp),
            alpha = 0.7f
        )
        Text(
            text = stringResource(R.string.empty_cart_info),
            modifier = Modifier.padding(vertical = 10.dp),
            fontStyle = FontStyle.Italic,
            style = MaterialTheme.typography.h5
        )
        Spacer(Modifier.height(16.dp))
        Button(onClick = { moveToCatalogue() }) {
            Text(stringResource(R.string.shop_button_text).uppercase())
        }
    }
}

@Preview
@Composable
fun EmptyCartInfoPreview() {
    ShrineTheme {
        EmptyCartInfo()
    }
}

@Composable
fun CartSummedValue(
    modifier: Modifier = Modifier,
    items: List<ExpandedCartItem>,
    shippingFee: Int = 0,
    promoCodeApplied: Boolean = false,
    backgroundColor: Color = MaterialTheme.colors.surface
) {
    val subtotal = items.sumOf { it.data.price }
    val tax = ((3 / 100.0) * subtotal)
    val promoDiscount = if (promoCodeApplied) 5 else 0
    val total = subtotal + shippingFee + tax - promoDiscount

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = backgroundColor)
            .padding(bottom = 24.dp)
            .then(modifier)
    ) {
        Divider(color = MaterialTheme.colors.onSecondary.copy(alpha = 0.3f))
        Row(
            modifier = Modifier.padding(top = 15.dp, end = 16.dp, bottom = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(stringResource(R.string.total).uppercase())
            Spacer(Modifier.weight(1f))
            Text(text = "$$total", style = MaterialTheme.typography.h5)
        }
        Row(Modifier.padding(end = 16.dp)) {
            Text(stringResource(R.string.subtotal))
            Spacer(Modifier.weight(1f))
            Text("$$subtotal")
        }
        Row(Modifier.padding(end = 16.dp)) {
            Text(stringResource(R.string.shipping))
            Spacer(Modifier.weight(1f))
            Text(if (shippingFee == 0) "---" else "$$shippingFee")
        }
        if (promoCodeApplied) {
            Row(Modifier.padding(end = 16.dp)) {
                Text(text = stringResource(R.string.promo_discount), color = Color.Green)
                Spacer(Modifier.weight(1f))
                Text(text = "-$$promoDiscount", color = Color.Green)
            }
        }
        Row(Modifier.padding(end = 16.dp)) {
            Text(stringResource(R.string.tax))
            Spacer(Modifier.weight(1f))
            Text("$${tax.toFloat()}")
        }
    }
}