package com.roland.android.shrine.ui.layouts.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roland.android.shrine.R
import com.roland.android.shrine.viewmodel.CheckoutViewModel

@Composable
fun OrderInfoDialog(
	viewModel: CheckoutViewModel,
	openDialog: (Boolean) -> Unit,
) {
	AlertDialog(
		onDismissRequest = { openDialog(false) },
		title = {
			if (viewModel.latestOrder.isNotEmpty()) {
				Column(
					Modifier.fillMaxWidth(),
					horizontalAlignment = Alignment.CenterHorizontally,
					verticalArrangement = Arrangement.spacedBy(12.dp)
				) {
					val purchaseDate = viewModel.date(viewModel.latestOrder.random().purchaseDate)

					Text(
						text = "Details",
						style = MaterialTheme.typography.h6
					)
					Divider(
						modifier = Modifier.fillMaxWidth(),
						color = MaterialTheme.colors.onSecondary
					)
					Text(
						text = "Order number:",
						style = MaterialTheme.typography.subtitle1
					)
					Text(viewModel.orderNumber)
					Divider(
						modifier = Modifier.width(40.dp),
						color = MaterialTheme.colors.onSecondary.copy(alpha = 0.5f)
					)
					Text(
						text = "Purchased on:",
						style = MaterialTheme.typography.subtitle1
					)
					Text(purchaseDate)
					Divider(
						modifier = Modifier.width(40.dp),
						color = MaterialTheme.colors.onSecondary.copy(alpha = 0.5f)
					)
					Text(
						text = "Delivery status:",
						style = MaterialTheme.typography.subtitle1
					)
					Text("En route")
				}
			} else {
				openDialog(false)
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