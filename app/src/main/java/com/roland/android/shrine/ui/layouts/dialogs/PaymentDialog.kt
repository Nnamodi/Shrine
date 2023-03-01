package com.roland.android.shrine.ui.layouts.dialogs

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.roland.android.shrine.R
import com.roland.android.shrine.utils.CardCheck
import com.roland.android.shrine.utils.checkCardDetails

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

    var cardNumberError by rememberSaveable { mutableStateOf(false) }
    var expiryMonthError by rememberSaveable { mutableStateOf(false) }
    var expiryYearError by rememberSaveable { mutableStateOf(false) }
    var securityCodeError by rememberSaveable { mutableStateOf(false) }

    var dateInfoShown by rememberSaveable { mutableStateOf(false) }
    var codeInfoShown by rememberSaveable { mutableStateOf(false) }
    val info = if (dateInfoShown) { stringResource(R.string.expiry_date_info) }
                else { stringResource(R.string.security_code_info) }

    AlertDialog(
        onDismissRequest = {},
        title = {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight()
            ) {
                Column {
                    Text(stringResource(R.string.enter_card_details))
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp),
                        value = cardNumber,
                        onValueChange = { if (it.length <= 19) cardNumber = it; cardNumberError = false },
                        isError = cardNumberError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        shape = CutCornerShape(12.dp),
                        label = { Text(stringResource(R.string.enter_card_number)) },
                        placeholder = { Text(stringResource(R.string.card_no_placeholder)) }
                    )

                    Row(Modifier.padding(top = 12.dp)) {
                        Column(
                            Modifier
                                .border(width = 1.dp,
                                    color = MaterialTheme.colors.onSecondary.copy(alpha = 0.4f),
                                    shape = CutCornerShape(12.dp))
                                .weight(2f)
                        ) {
                            ColumnDescription(
                                description = R.string.expiry_date,
                                infoShown = { dateInfoShown = !dateInfoShown; codeInfoShown = false }
                            )
                            Row(Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)) {
                                OutlinedTextField(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp),
                                    value = expiryMonth,
                                    onValueChange = { if (it.length <= 2) expiryMonth = it; expiryMonthError = false },
                                    isError = expiryMonthError,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Next
                                    ),
                                    singleLine = true,
                                    shape = CutCornerShape(12.dp),
                                    label = { Text(stringResource(R.string.month)) }
                                )
                                OutlinedTextField(
                                    modifier = Modifier
                                        .weight(1f),
                                    value = expiryYear,
                                    onValueChange = { if (it.length <= 2) expiryYear = it; expiryYearError = false },
                                    isError = expiryYearError,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Next
                                    ),
                                    singleLine = true,
                                    shape = CutCornerShape(12.dp),
                                    label = { Text(stringResource(R.string.year)) }
                                )
                            }
                        }

                        Spacer(Modifier.width(12.dp))

                        Column(
                            Modifier
                                .border(width = 1.dp,
                                    color = MaterialTheme.colors.onSecondary.copy(alpha = 0.4f),
                                    shape = CutCornerShape(12.dp))
                                .weight(1f)
                        ) {
                            ColumnDescription(
                                description = R.string.security_code,
                                infoShown = { codeInfoShown = !codeInfoShown; dateInfoShown = false }
                            )
                            Row(Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)) {
                                OutlinedTextField(
                                    modifier = Modifier
                                        .weight(1f),
                                    value = securityCode,
                                    onValueChange = { if (it.length <= 3) securityCode = it; securityCodeError = false },
                                    isError = securityCodeError,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Number,
                                        imeAction = ImeAction.Done
                                    ),
                                    singleLine = true,
                                    shape = CutCornerShape(12.dp),
                                    label = { Text(stringResource(R.string.cvv)) }
                                )
                            }
                        }
                    }

                    if (dateInfoShown || codeInfoShown) {
                        Text(
                            modifier = Modifier.padding(vertical = 12.dp),
                            text = info,
                            style = MaterialTheme.typography.body2
                        )
                    } else { Spacer(Modifier.height(52.dp)) }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                when (checkCardDetails(cardNumber, expiryMonth, expiryYear, securityCode)) {
                    CardCheck.IncorrectCardNumber -> cardNumberError = true
                    CardCheck.IncorrectMonth -> expiryMonthError = true
                    CardCheck.IncorrectYear -> expiryYearError = true
                    CardCheck.IncompleteCode -> securityCodeError = true
                    CardCheck.GoodToGo -> {
                        setCardDetails(cardNumber, expiryMonth, expiryYear, securityCode)
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

@Composable
private fun ColumnDescription(
    description: Int,
    infoShown: () -> Unit
) {
    Row {
        Text(
            modifier = Modifier.padding(start = 12.dp, top = 8.dp),
            text = stringResource(description),
            style = MaterialTheme.typography.subtitle2
        )
        Spacer(Modifier.weight(1f))
        IconButton(
            modifier = Modifier
                .padding(end = 12.dp, top = 8.dp)
                .size(14.dp),
            onClick = infoShown
        ) {
            Icon(imageVector = Icons.Default.Info, contentDescription = null)
        }
    }
}