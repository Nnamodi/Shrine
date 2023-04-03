package com.roland.android.shrine.ui.screens

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roland.android.shrine.R
import com.roland.android.shrine.ui.layouts.Checkout
import com.roland.android.shrine.ui.layouts.dialogs.ProcessOrderDialog
import com.roland.android.shrine.ui.theme.ShrineTheme
import com.roland.android.shrine.viewmodel.CheckoutViewModel
import com.roland.android.shrine.viewmodel.SharedViewModel
import com.roland.android.shrine.viewmodel.ViewModelFactory

@ExperimentalAnimationApi
@Composable
fun CheckoutScreen(
    sharedViewModel: SharedViewModel,
    navigateToCompleteOrder: () -> Unit,
    onNavigateUp: () -> Unit
) {
    Box {
        Checkout(
            cartItems = sharedViewModel.cartItems,
            viewModel = viewModel(factory = ViewModelFactory()),
            navigateToCompleteOrder = navigateToCompleteOrder,
            onNavigateUp = onNavigateUp
        )
        AnimatedVisibility(
            modifier = Modifier.align(Alignment.BottomCenter),
            visible = true
        ) {
            PlaceOrderButton()
        }
    }
}

@Composable
fun PlaceOrderButton(viewModel: CheckoutViewModel = viewModel()) {
    val context = LocalContext.current.applicationContext
    val openDialog = rememberSaveable { mutableStateOf(false) }
    val toastMessage = when {
        !viewModel.addressSet -> stringResource(R.string.set_delivery_address)
        !viewModel.cardDetailsSet -> stringResource(R.string.set_card_detail)
        else -> ""
    }

    Button(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        onClick = {
            if (toastMessage.isNotEmpty()) {
                Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
            } else { openDialog.value = true }
        }
    ) {
        Text(
            text = stringResource(R.string.place_order_text).uppercase(),
            modifier = Modifier.padding(vertical = 4.dp)
        )
    }

    if (openDialog.value) {
        ProcessOrderDialog { openDialog.value = it }
    }
}

@ExperimentalAnimationApi
@Preview
@Composable
fun CheckoutScreenPreview() {
    ShrineTheme {
        CheckoutScreen(
            sharedViewModel = viewModel(factory = ViewModelFactory()),
            navigateToCompleteOrder = {},
            onNavigateUp = {}
        )
    }
}
