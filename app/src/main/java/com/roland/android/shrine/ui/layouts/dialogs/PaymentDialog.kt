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
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roland.android.shrine.R
import com.roland.android.shrine.data.CardDetails
import com.roland.android.shrine.utils.CardCheck.*
import com.roland.android.shrine.utils.CardNumbers.CARD_EXPIRED
import com.roland.android.shrine.utils.CardNumbers.EXPIRY_DATE
import com.roland.android.shrine.utils.CardNumbers.INVALID_DATE
import com.roland.android.shrine.utils.CardNumbers.SECURITY_CODE
import com.roland.android.shrine.utils.checkCardDetails
import com.roland.android.shrine.viewmodel.CheckoutViewModel
import com.roland.android.shrine.viewmodel.CheckoutViewModelFactory

@Composable
fun PaymentDialog(
    viewModel: CheckoutViewModel = viewModel(factory = CheckoutViewModelFactory()),
    openDialog: (Boolean) -> Unit
) {
    var cardNumber by remember { mutableStateOf(viewModel.cardDetails.cardNumber) }
    var expiryMonth by remember { mutableStateOf(viewModel.cardDetails.expiryMonth) }
    var expiryYear by remember { mutableStateOf(viewModel.cardDetails.expiryYear) }
    var securityCode by remember { mutableStateOf(viewModel.cardDetails.securityCode) }

    var cardNumberError by rememberSaveable { mutableStateOf(false) }
    var expiryMonthError by rememberSaveable { mutableStateOf(false) }
    var expiryYearError by rememberSaveable { mutableStateOf(false) }
    var securityCodeError by rememberSaveable { mutableStateOf(false) }

    var infoToShow by rememberSaveable { mutableStateOf("") }
    val info = when (infoToShow) {
        CARD_EXPIRED -> stringResource(R.string.card_expired_notice)
        INVALID_DATE -> stringResource(R.string.invalid_date_notice)
        EXPIRY_DATE -> stringResource(R.string.expiry_date_info)
        SECURITY_CODE -> stringResource(R.string.security_code_info)
        else -> ""
    }

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
                        onValueChange = {
                            if (it.length <= 19 && it.isDigitsOnly()) {
                                cardNumber = it; cardNumberError = false
                            }
                        },
                        isError = cardNumberError,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
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
                                infoShown = { infoToShow = if (infoToShow == EXPIRY_DATE) "" else EXPIRY_DATE }
                            )
                            Row(Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)) {
                                OutlinedTextField(
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(end = 8.dp),
                                    value = expiryMonth,
                                    onValueChange = {
                                        if (it.length <= 2 && it.isDigitsOnly()) {
                                            expiryMonth = it; infoToShow = ""
                                            expiryMonthError = false
                                        }
                                    },
                                    isError = expiryMonthError,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.NumberPassword,
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
                                    onValueChange = {
                                        if (it.length <= 2 && it.isDigitsOnly()) {
                                            expiryYear = it; infoToShow = ""
                                            expiryYearError = false
                                        }
                                    },
                                    isError = expiryYearError,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.NumberPassword,
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
                                infoShown = { infoToShow = if (infoToShow == SECURITY_CODE) "" else SECURITY_CODE }
                            )
                            Row(Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp)) {
                                OutlinedTextField(
                                    modifier = Modifier
                                        .weight(1f),
                                    value = securityCode,
                                    onValueChange = {
                                        if (it.length <= 3 && it.isDigitsOnly()) {
                                            securityCode = it; securityCodeError = false
                                        }
                                    },
                                    isError = securityCodeError,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.NumberPassword,
                                        imeAction = ImeAction.Done
                                    ),
                                    singleLine = true,
                                    shape = CutCornerShape(12.dp),
                                    label = { Text(stringResource(R.string.cvv)) }
                                )
                            }
                        }
                    }

                    if (infoToShow.isNotEmpty()) {
                        Text(
                            modifier = Modifier.padding(vertical = 12.dp),
                            text = info,
                            style = MaterialTheme.typography.body2
                        )
                    } else { Spacer(Modifier.height(52.dp)); infoToShow = "" }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
                when (checkCardDetails(cardNumber, expiryMonth, expiryYear, securityCode)) {
                    IncorrectCardNumber -> cardNumberError = true
                    IncorrectMonth -> expiryMonthError = true
                    IncorrectYear -> expiryYearError = true
                    IncompleteCode -> securityCodeError = true
                    InvalidDate -> infoToShow = INVALID_DATE
                    CardExpired -> infoToShow = CARD_EXPIRED
                    GoodToGo -> {
                        val cardDetails = CardDetails(cardNumber, expiryMonth, expiryYear, securityCode)
                        viewModel.saveCardDetails(cardDetails)
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