package com.roland.android.shrine.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.roland.android.shrine.R
import com.roland.android.shrine.data.Address
import com.roland.android.shrine.data.SampleItemsData
import com.roland.android.shrine.data.User
import com.roland.android.shrine.data.UserWithAddress
import com.roland.android.shrine.data.model.ItemData
import com.roland.android.shrine.ui.layouts.DeleteAccount
import com.roland.android.shrine.ui.layouts.Orders
import com.roland.android.shrine.ui.layouts.PendingDelivery
import com.roland.android.shrine.ui.layouts.UserDetails
import com.roland.android.shrine.ui.theme.ShrineTheme

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AccountScreen(
	userDetails: UserWithAddress,
	viewPendingDelivery: () -> Unit,
	orderHistory: List<ItemData>,
	onNavigateUp: () -> Unit
) {
	Scaffold(
		topBar = {
			TopAppBar(
				title = { Text(stringResource(R.string.my_account).uppercase()) },
				navigationIcon = {
					IconButton(onClick = onNavigateUp) {
						Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
					}
				},
				backgroundColor = MaterialTheme.colors.secondary
			)
		},
		backgroundColor = MaterialTheme.colors.secondary
	) {
		Column(
			modifier = Modifier
				.fillMaxSize()
				.verticalScroll(rememberScrollState())
		) {
			UserDetails(
				userFullName = userDetails.user.firstName + " " + userDetails.user.lastName,
				userStreet = userDetails.address.street,
				userVicinity = userDetails.address.vicinity
			)

			PendingDelivery(
				hasOrderedBefore = orderHistory.isNotEmpty(),
				viewPendingDelivery = viewPendingDelivery
			)

			Orders(
				orderHistory = orderHistory
			)

			DeleteAccount {}
		}
	}
}

@Preview
@Composable
fun UserDetailsPreview() {
	ShrineTheme {
		val userDetails = UserWithAddress(
			User("Nnamodi", "Igede"),
			Address("No 13 Baruwa str. Agboju", "Kuje-Amuwo, Lagos")
		)

		AccountScreen(
			userDetails = userDetails,
			viewPendingDelivery = {},
			orderHistory = SampleItemsData.takeLast(3),
			onNavigateUp = {}
		)
	}
}