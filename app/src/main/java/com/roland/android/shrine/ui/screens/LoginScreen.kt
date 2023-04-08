package com.roland.android.shrine.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roland.android.shrine.ui.layouts.LoginButtons
import com.roland.android.shrine.ui.layouts.LoginFields
import com.roland.android.shrine.ui.layouts.WelcomeMessage
import com.roland.android.shrine.viewmodel.AccountViewModel
import com.roland.android.shrine.viewmodel.ViewModelFactory

@Composable
fun LoginScreen(
	viewModel: AccountViewModel = viewModel(factory = ViewModelFactory()),
	onMoveToShop: () -> Unit
) {
	Surface(
		modifier = Modifier.verticalScroll(rememberScrollState()),
		color = MaterialTheme.colors.surface
	) {
		Column(
			modifier = Modifier.fillMaxSize(),
			horizontalAlignment = Alignment.CenterHorizontally,
			verticalArrangement = Arrangement.Center
		) {
			WelcomeMessage()

			LoginFields(viewModel)

			LoginButtons(
				enableLoginButton = viewModel.firstName.isNotEmpty() && viewModel.password.length >= 6,
				onSkip = { viewModel.saveLaunchStatus(); onMoveToShop() },
				onLogin = {
					viewModel.apply { saveUser(); saveLaunchStatus() }
					onMoveToShop()
				}
			)
		}
	}
}