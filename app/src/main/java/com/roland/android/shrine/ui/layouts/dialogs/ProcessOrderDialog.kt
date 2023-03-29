package com.roland.android.shrine.ui.layouts.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roland.android.shrine.R
import com.roland.android.shrine.viewmodel.CheckoutViewModel
import kotlinx.coroutines.delay

@Composable
fun ProcessOrderDialog(
	viewModel: CheckoutViewModel = viewModel(),
	openDialog: (Boolean) -> Unit
) {
	AlertDialog(
		onDismissRequest = {},
		title = {
			Column(
				modifier = Modifier.padding(bottom = 12.dp)
			) {
				Text(
					text = stringResource(R.string.processing_order),
					modifier = Modifier
						.padding(12.dp)
						.align(Alignment.CenterHorizontally)
				)
				Spacer(Modifier.height(16.dp))
				LinearProgressIndicator(
					modifier = Modifier.fillMaxWidth(),
					color = MaterialTheme.colors.onSecondary.copy(alpha = 0.6f),
					trackColor = Color.LightGray
				)
			}
			if (viewModel.orderSent) {
				viewModel.orderSent = false
//				openDialog(false)
			}
		},
		confirmButton = {
			TextButton(onClick = {
				openDialog(false)
				viewModel.orderSent = false
			}) {
				Text(
					text = stringResource(R.string.stop),
					color = MaterialTheme.colors.onSecondary
				)
			}
		}
	)

	LaunchedEffect(key1 = true) {
		delay(4000)
		viewModel.orderSent = true
	}
}