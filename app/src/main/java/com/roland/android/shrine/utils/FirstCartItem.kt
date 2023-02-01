package com.roland.android.shrine.utils

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.roland.android.shrine.data.ItemData
import com.roland.android.shrine.ui.theme.ShrinePink200

@Composable
fun FirstCartItem(
    modifier: Modifier = Modifier,
    data: FirstCartItemData,
    onAddFirstItem: () -> Unit
) {
    val (itemData, imageSize, cardOffset) = data
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp

    val transitionState = remember { MutableTransitionState(FirstItemState.Initial) }
    transitionState.targetState = FirstItemState.Added

    if (transitionState.currentState == FirstItemState.Added && transitionState.isIdle) {
        onAddFirstItem()
    }

    val addAnimation = updateTransition(
        transitionState = transitionState, label = "Add first cart item animation"
    )

    val itemAlpha by addAnimation.animateFloat(
        transitionSpec = { tween(durationMillis = 300) }, label = "Scrim fade"
    ) {
        if (it == FirstItemState.Initial) 0f else 1f
    }

    val itemSize by addAnimation.animateSize(
        transitionSpec = { tween(durationMillis = 500, delayMillis = 400, easing = FastOutLinearInEasing) },
        label = "Item size"
    ) {
        if (it == FirstItemState.Initial) {
            imageSize.toSize()
        } else {
            with (LocalDensity.current) {
                Size(40.dp.toPx(), 40.dp.toPx())
            }
        }
    }

    val itemCornerSize by addAnimation.animateDp(
        transitionSpec = { tween(durationMillis = 1000, delayMillis = 400, easing = LinearOutSlowInEasing) },
        label = "Item corner size"
    ) {
        if (it == FirstItemState.Initial) 0.dp else 10.dp
    }

    val scrimAlpha by addAnimation.animateFloat(
        transitionSpec = { tween(durationMillis = 1000, delayMillis = 400, easing = LinearOutSlowInEasing) },
        label = "Scrim fade"
    ) {
        if (it == FirstItemState.Initial) 0.6f else 0f
    }

    val itemOffsetX by addAnimation.animateFloat(
        transitionSpec = { tween(durationMillis = 500, delayMillis = 400) },
        label = "Item offsetX"
    ) {
        if (it == FirstItemState.Initial) {
            cardOffset.x
        } else {
            with (LocalDensity.current) {
                (screenWidth - 40 - 16).dp.toPx()
            }
        }
    }

    val itemOffsetY by addAnimation.animateFloat(
        transitionSpec = { tween(durationMillis = 500, delayMillis = 550, easing = FastOutLinearInEasing) },
        label = "Item offsetY"
    ) {
        if (it == FirstItemState.Initial) {
            cardOffset.y
        } else {
            with(LocalDensity.current) {
                (screenHeight - 40 - 8).dp.toPx()
            }
        }
    }

    val itemElevation by addAnimation.animateDp(
        transitionSpec = { tween(durationMillis = 500, delayMillis = 400) },
        label = "Scrim fade"
    ) {
        if (it == FirstItemState.Initial) 16.dp else 0.dp
    }

    Box(
        modifier = modifier
            .offset { IntOffset(itemOffsetX.toInt(), itemOffsetY.toInt()) }
            .graphicsLayer { alpha = itemAlpha }
    ) {
        Image(
            painter = painterResource(id = itemData.photoResId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(itemSize.width.dp, itemSize.height.dp)
                .drawWithContent {
                    drawContent()
                    drawRect(ShrinePink200.copy(alpha = scrimAlpha))
                }
                .shadow(itemElevation, RoundedCornerShape(itemCornerSize))
                .clip(RoundedCornerShape(itemCornerSize))
        )
    }
}

//@Composable
//private fun Size.toDpSize(): DpSize {
//    with(LocalDensity.current) {
//        return DpSize(
//            this@toDpSize.width.toDp(), this@toDpSize.height.toDp()
//        )
//    }
//}

data class FirstCartItemData(
    val data: ItemData,
    val imageSize: IntSize,
    val cardOffset: Offset
)

enum class FirstItemState {
    Initial,
    Added
}