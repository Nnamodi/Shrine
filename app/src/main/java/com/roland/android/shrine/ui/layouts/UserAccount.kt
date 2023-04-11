package com.roland.android.shrine.ui.layouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roland.android.shrine.R
import com.roland.android.shrine.data.model.ItemData
import com.roland.android.shrine.utils.RowContainer

@Composable
fun UserDetails(
	userFullName: String = "Nnamodi Roland",
	userStreet: String = "No 13 Baruwa str. Agboju",
	userVicinity: String = "Kuje-Amuwo, Lagos"
) {
	Column(
		modifier = Modifier
			.fillMaxWidth()
			.padding(30.dp)
	) {
		Text(
			text = stringResource(R.string.personal_details_text),
			modifier = Modifier.padding(bottom = 8.dp)
		)
		RowContainer {
			Image(
				modifier = Modifier.size(70.dp),
				imageVector = Icons.Default.AccountCircle,
				contentDescription = stringResource(R.string.avatar_icon_desc)
			)
			Column {
				Text(
					text = userFullName,
					style = MaterialTheme.typography.subtitle1
				)
				Divider(
					modifier = Modifier.padding(vertical = 8.dp),
					color = MaterialTheme.colors.onSecondary.copy(alpha = 0.5f)
				)
				Text(userStreet)
				Text(userVicinity.ifEmpty { stringResource(R.string.no_address_text) })
			}
		}
	}
}

@Composable
fun PendingDelivery(
	hasOrderedBefore: Boolean,
	viewPendingDelivery: () -> Unit
) {
	RowContainer(
		modifier = Modifier
			.fillMaxWidth()
			.padding(30.dp)
			.clickable(enabled = hasOrderedBefore) { viewPendingDelivery() }
	) {
		Text(
			text = stringResource(R.string.pending_delivery),
			style = MaterialTheme.typography.h6
		)
		Spacer(Modifier.weight(1f))
		Image(
			imageVector = Icons.Default.KeyboardArrowRight,
			contentDescription = null
		)
	}
}

@Composable
fun Orders(
	orderHistory: List<ItemData>
) {
	var revealOrder by remember { mutableStateOf(false) }

	RowContainer(
		modifier = Modifier
			.fillMaxWidth()
			.padding(30.dp)
			.clickable { revealOrder = !revealOrder }
	) {
		Column {
			Row {
				Text(
					text = stringResource(R.string.orders),
					style = MaterialTheme.typography.h6
				)
				Spacer(Modifier.weight(1f))
				Image(
					imageVector = if (revealOrder) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
					contentDescription = null
				)
			}

			if (revealOrder) {
				Spacer(Modifier.height(12.dp))
				if (orderHistory.isNotEmpty()) {
					OrderList(columnPadding = 0.dp, items = orderHistory)
				} else {
					Column(
						modifier = Modifier.fillMaxWidth(),
						horizontalAlignment = Alignment.CenterHorizontally
					) {
						Text(
							text = stringResource(R.string.no_orders_placed),
							modifier = Modifier.padding(12.dp)
						)
					}
				}
			}
		}
	}
}

@Composable
fun DeleteAccount(onDeleteAccount: () -> Unit) {
	RowContainer(
		modifier = Modifier
			.fillMaxWidth()
			.padding(30.dp)
			.clickable { onDeleteAccount() }
	) {
		Text(
			text = stringResource(R.string.delete_account),
			color = MaterialTheme.colors.error,
			style = MaterialTheme.typography.h6
		)
	}
}