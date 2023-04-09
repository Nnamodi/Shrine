package com.roland.android.shrine.ui.layouts

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.outlined.Info
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roland.android.shrine.R
import com.roland.android.shrine.data.SampleItemsData
import com.roland.android.shrine.data.model.ItemData
import com.roland.android.shrine.ui.layouts.dialogs.OrderInfoDialog
import com.roland.android.shrine.ui.theme.ShrineTheme
import com.roland.android.shrine.utils.cardType
import com.roland.android.shrine.viewmodel.CheckoutViewModel
import com.roland.android.shrine.viewmodel.ViewModelFactory

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun OrderComplete(
	checkoutViewModel: CheckoutViewModel,
	orderedItems: List<ItemData>,
	onNavigateUp: () -> Unit,
	purchaseAmount: @Composable () -> Unit
) {
	val openDialog = rememberSaveable { mutableStateOf(false) }

	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(stringResource(R.string.order_complete).uppercase()) },
				navigationIcon = {
					IconButton(onClick = onNavigateUp) {
						Icon(imageVector = Icons.Default.Close, contentDescription = stringResource(R.string.close_icon_desc))
					}
				},
				actions = {
					IconButton(onClick = { openDialog.value = true }) {
						Icon(
							imageVector = Icons.Outlined.Info,
							contentDescription = stringResource(R.string.info_icon_desc),
							tint = LocalContentColor.current.copy(alpha = ContentAlpha.high)
						)
					}
				},
				backgroundColor = MaterialTheme.colors.secondary
			)
		},
		backgroundColor = MaterialTheme.colors.secondary
	) {
		Column(
			modifier = Modifier
				.fillMaxWidth()
				.verticalScroll(rememberScrollState())
		) {
			Text(
				text = stringResource(R.string.order_complete_message),
				modifier = Modifier.padding(start = 72.dp, top = 16.dp, bottom = 16.dp, end = 60.dp),
				style = MaterialTheme.typography.h5
			)
			Divider(
				modifier = Modifier.padding(start = 72.dp),
				color = MaterialTheme.colors.onSecondary.copy(alpha = 0.3f)
			)
			Row(
				modifier = Modifier.fillMaxWidth()
			) {
				Icon(
					modifier = Modifier.padding(24.dp),
					imageVector = Icons.Default.LocalShipping,
					contentDescription = stringResource(R.string.shipping_icon_desc)
				)
				Column(Modifier.padding(vertical = 24.dp)) {
					Text(stringResource(R.string.shipping).uppercase())
					Text(
						text = checkoutViewModel.address.street,
						style = MaterialTheme.typography.subtitle1
					)
					Text(
						text = checkoutViewModel.address.vicinity.ifEmpty { stringResource(R.string.no_address_text) },
						style = MaterialTheme.typography.subtitle1
					)
				}
			}
			Divider(
				modifier = Modifier.padding(start = 72.dp),
				color = MaterialTheme.colors.onSecondary.copy(alpha = 0.3f)
			)
			Row(
				modifier = Modifier.fillMaxWidth()
			) {
				Icon(
					modifier = Modifier.padding(24.dp),
					imageVector = Icons.Default.CreditCard,
					contentDescription = stringResource(R.string.payment_icon_desc)
				)
				Column(Modifier.padding(vertical = 24.dp)) {
					val cardNumber = checkoutViewModel.cardDetails.cardNumber

					Text(stringResource(R.string.payment).uppercase())
					Text(
						text = cardType(cardNumber),
						style = MaterialTheme.typography.subtitle1
					)
					Text(
						text = if (cardNumber.isEmpty()) {
							stringResource(R.string.no_card_text)
						} else {
							"· · · ·  · · · ·  ${cardNumber.takeLast(4)}"
						},
						style = MaterialTheme.typography.subtitle1
					)
				}
			}
			Divider(color = MaterialTheme.colors.onSecondary.copy(alpha = 0.3f))

			OrderList(items = orderedItems)

			purchaseAmount()
		}
	}

	if (openDialog.value) {
		OrderInfoDialog(viewModel = checkoutViewModel) { openDialog.value = it }
	}
}

@Composable
fun OrderList(
	items: List<ItemData>,
	columnPadding: Dp = 72.dp
) {
	items.forEach {
		CartItem(
			item = it,
			enabled = false,
			itemIsFirst = items.first() == it,
			isShownInCart = false,
			columnPadding = columnPadding,
			backgroundColor = MaterialTheme.colors.secondary
		)
	}
}

@Preview
@Composable
fun OrderCompletePreview() {
	ShrineTheme {
		OrderComplete(
			checkoutViewModel = viewModel(factory = ViewModelFactory()),
			orderedItems = SampleItemsData.takeLast(5),
			onNavigateUp = {},
			purchaseAmount = {}
		)
	}
}