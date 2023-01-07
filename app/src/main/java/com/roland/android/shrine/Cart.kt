package com.roland.android.shrine

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.roland.android.shrine.ui.theme.ShrineTheme
import java.lang.Integer.min

@Composable
fun ExpandedCart() {
    Surface(
        color = MaterialTheme.colors.surface
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            CartHeader(SampleItemsData.size)

            SampleItemsData.forEach {
                CartItem(it)
            }

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                Text(text = "Proceed to checkout".uppercase())
            }
        }
    }
}

@Composable
fun CollapsedCart(
    items: List<ItemData> = SampleItemsData.subList(fromIndex = 0, toIndex = 3)
) {
    Row(
        Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp)
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
        items.forEach { item ->
            CollapsedCartItem(item.photoResId, item.title)
        }
    }
}

@Composable
private fun CollapsedCartItem(
    photoId: Int,
    description: String
) {
    Image(
        painter = painterResource(id = photoId),
        contentDescription = description,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(10.dp))
    )
}

enum class CartBottomSheetState {
    Collapsed,
    Expanded,
    Hidden
}

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalAnimationApi
@Composable
fun CartExpandedBottomSheet(
    modifier: Modifier = Modifier,
    items: List<ItemData> = SampleItemsData,
    expanded: Boolean = false,
    maxHeight: Dp,
    maxWidth: Dp
) {
    val cartTransition = updateTransition(
        targetState = when {
//            hidden -> CartBottomSheetState.Hidden
            expanded -> CartBottomSheetState.Expanded
            else -> CartBottomSheetState.Collapsed
        },
        label = "cartTransition"
    )

    val cartOffset by cartTransition.animateDp(
        transitionSpec = {
            when {
                CartBottomSheetState.Expanded isTransitioningTo CartBottomSheetState.Collapsed ->
                    tween(durationMillis = 455, delayMillis = 67)
                CartBottomSheetState.Collapsed isTransitioningTo CartBottomSheetState.Expanded ->
                    tween(durationMillis = 150)
                else -> tween(durationMillis = 450)
            }
        },
        label = "cartOffset"
    ) {
        when (it) {
            CartBottomSheetState.Hidden -> maxHeight
            CartBottomSheetState.Expanded -> 0.dp
            else -> {
                val size = min(2, items.size)
                val width = 24 + 48 + (size + 1) + 16 + size + 16
                (maxWidth.value + width).dp
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

    Box(
        modifier
            .shadow(
                elevation = 8.dp,
                shape = CutCornerShape(topStart = cornerSize)
            )
            .height(cartHeight)
            .background(
                color = MaterialTheme.colors.secondary,
                shape = CutCornerShape(topStart = cornerSize)
            )
    ) {
        cartTransition.AnimatedContent { targetState ->
            if (targetState == CartBottomSheetState.Expanded) {
                ExpandedCart()
            } else {
                CollapsedCart()
            }
        }
    }
}

@Composable
private fun CartHeader(itemSize: Int) {
    Surface(
        color = MaterialTheme.colors.surface
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Down Arrow"
                )
            }
            Spacer(Modifier.width(4.dp))
            Text("Cart".uppercase())
            Spacer(Modifier.width(12.dp))
            Text("$itemSize items".uppercase())
        }
    }
}

@Composable
private fun CartItem(item: ItemData) {
    Surface(
        color = MaterialTheme.colors.surface
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {},
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
                Divider(color = MaterialTheme.colors.onSecondary.copy(alpha = 0.3f))
                Row(
                    Modifier.padding(vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = item.photoResId),
                        modifier = Modifier.size(width = 100.dp, height = 100.dp),
                        contentDescription = "Item image"
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
fun CollapsedCartPreview() {
    ShrineTheme {
        BoxWithConstraints(
            Modifier.fillMaxSize()
        ) {
            var expanded by remember { mutableStateOf(false) }

            CartExpandedBottomSheet(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .clickable { expanded = !expanded },
                expanded = expanded,
                maxHeight = maxHeight,
                maxWidth = maxWidth
            )
        }
    }
}

//@Preview
//@Composable
//fun CartPreview() {
//    ShrineTheme { ExpandedCart() }
//}