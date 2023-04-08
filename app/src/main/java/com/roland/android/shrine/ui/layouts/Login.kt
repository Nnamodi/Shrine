package com.roland.android.shrine.ui.layouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roland.android.shrine.R
import com.roland.android.shrine.viewmodel.AccountViewModel

@Composable
fun WelcomeMessage() {
	Column(
		modifier = Modifier.padding(bottom = 35.dp),
		horizontalAlignment = Alignment.CenterHorizontally
	) {
		Image(
			painter = painterResource(id = R.drawable.launch_icon),
			contentDescription = null
		)
		Text(
			text = stringResource(R.string.welcome_text),
			style = MaterialTheme.typography.h3
		)
	}
}

@Composable
fun LoginFields(viewModel: AccountViewModel) {
	LoginColumn(
		viewModel = viewModel,
		columnHeader = stringResource(R.string.first_name_text),
		textFieldPlaceholder = stringResource(R.string.first_name_holder)
	)
	LoginColumn(
		viewModel = viewModel,
		columnHeader = stringResource(R.string.last_name_text),
		textFieldPlaceholder = stringResource(R.string.last_name_holder)
	)
	LoginColumn(
		viewModel = viewModel,
		columnHeader = stringResource(R.string.password_text),
		isPassword = true,
		textFieldPlaceholder = stringResource(R.string.password_holder)
	)
}

@Composable
fun LoginColumn(
	viewModel: AccountViewModel,
	columnHeader: String,
	isPassword: Boolean = false,
	textFieldPlaceholder: String
) {
	val columnIndex: Int
	val input = when (columnHeader) {
		stringResource(R.string.first_name_text) -> { columnIndex = 1; viewModel.firstName }
		stringResource(R.string.last_name_text) -> { columnIndex = 2; viewModel.lastName }
		else -> { columnIndex = 3; viewModel.password }
	}

	Column(
		modifier = Modifier
			.fillMaxWidth(0.6f)
			.padding(vertical = 15.dp)
	) {
		Text(
			text = columnHeader,
			style = MaterialTheme.typography.subtitle1
		)
		OutlinedTextField(
			modifier = Modifier.padding(top = 12.dp),
			value = input,
			onValueChange = {
				when (columnIndex) {
					1 -> viewModel.firstName = it
					2 -> viewModel.lastName = it
					else -> viewModel.password = it
				}
			},
			singleLine = true,
			shape = CutCornerShape(12.dp),
			placeholder = { Text(textFieldPlaceholder) }
		)
		if (isPassword) {
			Text(
				text = stringResource(R.string.password_rule),
				modifier = Modifier.padding(top = 8.dp)
			)
		}
	}
}

@Composable
fun LoginButtons(
	enableLoginButton: Boolean,
	onSkip: () -> Unit,
	onLogin: () -> Unit,
) {
	val buttonColor = if (enableLoginButton) {
		ButtonDefaults.outlinedButtonColors(backgroundColor = MaterialTheme.colors.onSecondary)
	} else {
		ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.onSecondary)
	}

	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 20.dp),
		horizontalArrangement = Arrangement.SpaceEvenly
	) {
		Button(
			onClick = onSkip
		) {
			Text(stringResource(R.string.skip).uppercase())
		}
		Button(
			enabled = enableLoginButton,
			onClick = onLogin,
			colors = buttonColor
		) {
			Text(stringResource(R.string.login).uppercase())
		}
	}
}