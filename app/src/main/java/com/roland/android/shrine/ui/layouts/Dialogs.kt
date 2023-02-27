package com.roland.android.shrine.ui.layouts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.roland.android.shrine.R
import com.roland.android.shrine.data.ItemData

@Composable
fun WishlistDialog(
    item: ItemData,
    removeFromWishlist: (ItemData) -> Unit,
    openDialog: (Boolean) -> Unit,
    favoriteIcon: (ImageVector) -> Unit
) {
    AlertDialog(
        onDismissRequest = { openDialog(false) },
        title = { Text(stringResource(R.string.wishlist)) },
        text = { Text(stringResource(R.string.snack_text, item.title)) },
        confirmButton = {
            TextButton(onClick = {
                openDialog(false)
                removeFromWishlist(item)
                favoriteIcon(Icons.Outlined.FavoriteBorder)
            }) {
                Text(
                    text = stringResource(R.string.remove),
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

@Composable
fun AddressDialog(
    initialAddress: String,
    initialVicinity: String,
    setStreetAddress: (String) -> Unit,
    setVicinity: (String) -> Unit,
    openDialog: (Boolean) -> Unit
) {
    var streetAddress by remember { mutableStateOf(initialAddress) }
    var vicinity by remember { mutableStateOf(initialVicinity) }

    AlertDialog(
        onDismissRequest = {},
        title = {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
            ) {
                Column {
                    Text("Enter Delivery Address")
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        value = streetAddress,
                        onValueChange = { streetAddress = it },
                        singleLine = true,
                        maxLines = 10,
                        shape = CutCornerShape(12.dp),
                        label = {
                            Text("Street")
                        }
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        value = vicinity,
                        onValueChange = { vicinity = it },
                        singleLine = true,
                        maxLines = 10,
                        shape = CutCornerShape(12.dp),
                        label = {
                            Text("Town, State")
                        }
                    )
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                setStreetAddress(streetAddress)
                setVicinity(vicinity)
                openDialog(false)
            }) {
                Text(
                    text = "Save",
                    color = MaterialTheme.colors.onSecondary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { openDialog(false) }) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colors.onSecondary
                )
            }
        }
    )
}

@Composable
fun PaymentDialog(
    initialCardNumber: String,
    initialMonth: String,
    initialYear: String,
    initialCode: String,
    setCardDetails: (String, String, String, String) -> Unit,
    openDialog: (Boolean) -> Unit
) {
    var cardNumber by remember { mutableStateOf(initialCardNumber) }
    var expiryMonth by remember { mutableStateOf(initialMonth) }
    var expiryYear by remember { mutableStateOf(initialYear) }
    var securityCode by remember { mutableStateOf(initialCode) }

    AlertDialog(
        onDismissRequest = {},
        title = {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
            ) {
                Column {
                    Text("Enter Card Details")
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        value = cardNumber,
                        onValueChange = { if (it.length <= 19) cardNumber = it },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        maxLines = 1,
                        shape = CutCornerShape(12.dp),
                        label = { Text("Enter card number") },
                        placeholder = { Text("· · · ·  · · · ·  · · · ·") }
                    )
                    Row {
                        OutlinedTextField(
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 12.dp, end = 12.dp),
                            value = expiryMonth,
                            onValueChange = { if (it.length <= 2) expiryMonth = it },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            singleLine = true,
                            maxLines = 1,
                            shape = CutCornerShape(12.dp),
                            label = { Text("MM") }
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 12.dp, end = 12.dp),
                            value = expiryYear,
                            onValueChange = { if (it.length <= 2) expiryYear = it },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            ),
                            singleLine = true,
                            maxLines = 1,
                            shape = CutCornerShape(12.dp),
                            label = { Text("YY") }
                        )
                        OutlinedTextField(
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 12.dp),
                            value = securityCode,
                            onValueChange = { if (it.length <= 3) securityCode = it },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            singleLine = true,
                            maxLines = 1,
                            shape = CutCornerShape(12.dp),
                            label = { Text("CVV") }
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                setCardDetails(cardNumber, expiryMonth, expiryYear, securityCode)
                openDialog(false)
            }) {
                Text(
                    text = "Save",
                    color = MaterialTheme.colors.onSecondary
                )
            }
        },
        dismissButton = {
            TextButton(onClick = { openDialog(false) }) {
                Text(
                    text = "Cancel",
                    color = MaterialTheme.colors.onSecondary
                )
            }
        }
    )
}