package com.roland.android.shrine.ui.layouts.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.roland.android.shrine.R
import com.roland.android.shrine.viewmodel.CheckoutViewModel

@Composable
fun OrderInfoDialog(
	viewModel: CheckoutViewModel,
	isOrderInfo: Boolean = true,
	openDialog: (Boolean) -> Unit,
) {
	AlertDialog(
		onDismissRequest = { openDialog(false) },
		title = {
			if (isOrderInfo) {
				if (viewModel.latestOrder.isNotEmpty()) {
					Column(
						Modifier.fillMaxWidth(),
						horizontalAlignment = Alignment.CenterHorizontally,
						verticalArrangement = Arrangement.spacedBy(12.dp)
					) {
						val purchaseDate = viewModel.date(viewModel.latestOrder.random().purchaseDate)

						Text(
							text = stringResource(R.string.details),
							style = MaterialTheme.typography.h6
						)
						Divider(
							modifier = Modifier.fillMaxWidth(),
							color = MaterialTheme.colors.onSecondary
						)
						Text(
							text = stringResource(R.string.order_number_title),
							style = MaterialTheme.typography.subtitle1
						)
						Text(viewModel.orderNumber)
						Divider(
							modifier = Modifier.width(40.dp),
							color = MaterialTheme.colors.onSecondary.copy(alpha = 0.5f)
						)
						Text(
							text = stringResource(R.string.purchase_date_title),
							style = MaterialTheme.typography.subtitle1
						)
						Text(purchaseDate)
						Divider(
							modifier = Modifier.width(40.dp),
							color = MaterialTheme.colors.onSecondary.copy(alpha = 0.5f)
						)
						Text(
							text = stringResource(R.string.delivery_status_title),
							style = MaterialTheme.typography.subtitle1
						)
						Text(stringResource(R.string.en_route))
					}
				} else {
					openDialog(false)
				}
			} else {
				Text(
					text = stringResource(R.string.so_you_know_text),
					modifier = Modifier.fillMaxWidth().padding(12.dp),
					textAlign = TextAlign.Center
				)
			}
		},
		text = {
			if (!isOrderInfo) {
				Column(
					Modifier.fillMaxWidth(),
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.spacedBy(12.dp)
				) {
					Text(stringResource(R.string.checkout_info_text))
				}
			}
		},
		confirmButton = {
			TextButton(onClick = { openDialog(false) }) {
				Text(
					text = stringResource(R.string.close_icon_desc),
					color = MaterialTheme.colors.onSecondary
				)
			}
		}
	)
}