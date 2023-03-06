package com.roland.android.shrine.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roland.android.shrine.R
import com.roland.android.shrine.data.ExpandedCartItem
import com.roland.android.shrine.ui.layouts.Checkout
import com.roland.android.shrine.ui.theme.ShrineTheme
import com.roland.android.shrine.viewmodel.CheckoutViewModel
import com.roland.android.shrine.viewmodel.SharedViewModel

@ExperimentalAnimationApi
@Composable
fun CheckoutScreen(
    sharedViewModel: SharedViewModel,
    onNavigateUp: () -> Unit
) {
    Box {
        val items = sharedViewModel.cartItems
        val cartItems by remember(items) {
            derivedStateOf {
                items.mapIndexed { index, item -> ExpandedCartItem(index = index, data = item) }
            }
        }
        Checkout(
            cartItems = cartItems,
            onNavigateUp = onNavigateUp
        )
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = true,
            enter = fadeIn(animationSpec = tween(durationMillis = 150, delayMillis = 150, LinearEasing)),
            exit = fadeOut(animationSpec = tween(durationMillis = 117, easing = LinearEasing))
        ) {
            PlaceOrderButton()
        }
    }
}

@Composable
fun PlaceOrderButton(viewModel: CheckoutViewModel = viewModel()) {
    val context = LocalContext.current.applicationContext
    val toastMessage = when {
        !viewModel.addressSet -> stringResource(R.string.set_delivery_address)
        !viewModel.cardDetailsSet -> stringResource(R.string.set_card_detail)
        else -> "You'll be able to place orders soon."
    }

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        onClick = {
            Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        }
    ) {
        Text(
            text = stringResource(R.string.place_order_text).uppercase(),
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun CheckoutScreenPreview() {
    ShrineTheme {
        CheckoutScreen(
            sharedViewModel = SharedViewModel(),
            onNavigateUp = {}
        )
    }
}
