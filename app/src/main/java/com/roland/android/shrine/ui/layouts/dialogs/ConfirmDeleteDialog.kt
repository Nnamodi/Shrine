package com.roland.android.shrine.ui.layouts.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roland.android.shrine.R
import com.roland.android.shrine.viewmodel.AccountViewModel
import com.roland.android.shrine.viewmodel.ViewModelFactory

@Composable
fun ConfirmDeleteDialog(
	viewModel: AccountViewModel = viewModel(factory = ViewModelFactory()),
	openDialog: (Boolean) -> Unit,
    closeApp: () -> Unit
) {
    var password by rememberSaveable { mutableStateOf("") }
    var passwordError by rememberSaveable { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = { openDialog(false) },
        title = {
            Column(
                Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(stringResource(R.string.enter_login_password))
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    value = password,
                    onValueChange = { password = it; passwordError = false },
                    isError = passwordError,
                    singleLine = true,
                    shape = CutCornerShape(12.dp),
                    trailingIcon = { if (passwordError) { Icon(imageVector = Icons.Default.Info, contentDescription = null) } },
                    placeholder = { Text(
                        text = stringResource(R.string.password_holder),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    ) },
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                )
                Text(
                    text = stringResource(R.string.delete_account_warning),
                    modifier = Modifier.padding(12.dp),
                    fontWeight = FontWeight.Light
                )
            }
        },
        confirmButton = {
            TextButton(onClick = {
                if (password == viewModel.user.login_pin) {
                    viewModel.deleteAccount()
                    closeApp()
                } else { passwordError = true }
            }) {
                Text(
                    text = stringResource(R.string.delete),
                    color = MaterialTheme.colors.error
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { openDialog(false) }) {
                Text(
                    text = stringResource(R.string.cancel),
                    color = MaterialTheme.colors.onSecondary
                )
            }
        }
    )
}