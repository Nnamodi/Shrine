package com.roland.android.shrine.ui.layouts.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.roland.android.shrine.R
import com.roland.android.shrine.utils.AddressCheck
import com.roland.android.shrine.utils.checkAddress

@Composable
fun AddressDialog(
    initialAddress: String,
    initialVicinity: String,
    setAddress: (String, String) -> Unit,
    openDialog: (Boolean) -> Unit
) {
    var streetAddress by remember { mutableStateOf(initialAddress) }
    var vicinity by remember { mutableStateOf(initialVicinity) }

    var streetError by remember { mutableStateOf(false) }
    var vicinityError by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = {},
        title = {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
            ) {
                Column {
                    Text(stringResource(R.string.enter_address))
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        value = streetAddress,
                        onValueChange = { streetAddress = it; streetError = false },
                        isError = streetError,
                        singleLine = true,
                        shape = CutCornerShape(12.dp),
                        label = {
                            Text(stringResource(R.string.street))
                        }
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        value = vicinity,
                        onValueChange = { vicinity = it; vicinityError = false },
                        isError = vicinityError,
                        singleLine = true,
                        shape = CutCornerShape(12.dp),
                        label = {
                            Text(stringResource(R.string.vicinity))
                        }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                when (checkAddress(streetAddress, vicinity)) {
                    AddressCheck.NoStreetSet -> streetError = true
                    AddressCheck.NoVicinitySet -> vicinityError = true
                    AddressCheck.AllSet -> {
                        setAddress(streetAddress, vicinity)
                        openDialog(false)
                    }
                }
            }) {
                Text(
                    text = stringResource(R.string.save),
                    color = MaterialTheme.colors.onSecondary
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