package com.roland.android.shrine.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.roland.android.shrine.data.ExpandedCartItem
import com.roland.android.shrine.ui.layouts.CartSummedValue
import com.roland.android.shrine.ui.layouts.OrderComplete
import com.roland.android.shrine.viewmodel.CheckoutViewModel
import com.roland.android.shrine.viewmodel.SharedViewModel

@Composable
fun ReceiptScreen(
	checkoutViewModel: CheckoutViewModel,
	sharedViewModel: SharedViewModel,
	onNavigateUp: () -> Unit
) {
	val items = sharedViewModel.cartItems
	val expandedItems by remember(items) {
		derivedStateOf {
			items.mapIndexed { index, it -> ExpandedCartItem(index = index, data = it) }
		}
	}

	Box {
		OrderComplete(
			checkoutViewModel = checkoutViewModel,
			sharedViewModel = sharedViewModel,
			onNavigateUp = onNavigateUp
		) {
			CartSummedValue(
				modifier = Modifier.padding(start = 72.dp, bottom = 20.dp),
				items = expandedItems,
				shippingFee = 2,
				promoCodeApplied = false,
				backgroundColor = MaterialTheme.colors.secondary
			)
		}
	}
}