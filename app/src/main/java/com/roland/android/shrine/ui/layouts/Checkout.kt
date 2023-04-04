package com.roland.android.shrine.ui.layouts

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roland.android.shrine.R
import com.roland.android.shrine.data.ExpandedCartItem
import com.roland.android.shrine.data.SampleItemsData
import com.roland.android.shrine.data.model.ItemData
import com.roland.android.shrine.ui.layouts.dialogs.AddressDialog
import com.roland.android.shrine.ui.layouts.dialogs.PaymentDialog
import com.roland.android.shrine.ui.theme.ShrineTheme
import com.roland.android.shrine.utils.cardType
import com.roland.android.shrine.viewmodel.CheckoutViewModel
import com.roland.android.shrine.viewmodel.ViewModelFactory

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun Checkout(
    viewModel: CheckoutViewModel,
    cartItems: List<ItemData>,
    navigateToCompleteOrder: () -> Unit,
    onNavigateUp: () -> Unit
) {
    val openAddressDialog = rememberSaveable { mutableStateOf(false) }
    val openPaymentDialog = rememberSaveable { mutableStateOf(false) }
    var promoCodeApplied by rememberSaveable { mutableStateOf(false) }
    var promoCode by rememberSaveable { mutableStateOf("") }
    val expandedCartItems by remember(cartItems) {
        derivedStateOf {
            cartItems.mapIndexed { index, it -> ExpandedCartItem(index = index, data = it) }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.checkout).uppercase()) },
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
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    modifier = Modifier.padding(24.dp),
                    imageVector = Icons.Default.LocalShipping,
                    contentDescription = stringResource(R.string.shipping_icon_desc)
                )
                Column(Modifier.padding(vertical = 24.dp)) {
                    Text(stringResource(R.string.shipping).uppercase())
                    Text(
                        text = viewModel.address.street,
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text(
                        text = viewModel.address.vicinity.ifEmpty { stringResource(R.string.no_address_text) },
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                Spacer(Modifier.weight(1f))
                IconButton(
                    modifier = Modifier.padding(vertical = 24.dp),
                    onClick = { openAddressDialog.value = true }
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = stringResource(R.string.edit_address_icon_desc))
                }
            }
            Divider(
                modifier = Modifier.padding(start = 72.dp),
                color = MaterialTheme.colors.onSecondary.copy(alpha = 0.3f)
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    modifier = Modifier.padding(24.dp),
                    imageVector = Icons.Default.CreditCard,
                    contentDescription = stringResource(R.string.payment_icon_desc)
                )
                Column(Modifier.padding(vertical = 24.dp)) {
                    val cardNumber = viewModel.cardDetails.cardNumber

                    Text(stringResource(R.string.payment).uppercase())
                    Text(
                        text = cardType(cardNumber),
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text(
                        text = if (cardNumber.isEmpty()) { stringResource(R.string.no_card_text) }
                                else { "· · · ·  · · · ·  ${cardNumber.takeLast(4)}"},
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                Spacer(Modifier.weight(1f))
                IconButton(
                    modifier = Modifier.padding(vertical = 24.dp),
                    onClick = { openPaymentDialog.value = true }
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = stringResource(R.string.edit_payment_desc))
                }
            }
            Divider(
                modifier = Modifier.padding(start = 72.dp),
                color = MaterialTheme.colors.onSecondary.copy(alpha = 0.3f)
            )
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    modifier = Modifier.padding(24.dp),
                    imageVector = Icons.Default.Redeem,
                    contentDescription = stringResource(R.string.voucher_icon_desc)
                )
                Column(Modifier.padding(vertical = 24.dp)) {
                    Text(stringResource(R.string.voucher_text).uppercase())
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, end = 16.dp),
                        value = promoCode,
                        onValueChange = { if (it.length <= 8) promoCode = it },
                        singleLine = true,
                        shape = CutCornerShape(12.dp),
                        label = { Text(stringResource(R.string.enter_voucher)) },
                        trailingIcon = {
                            if (promoCode.length > 4) {
                                IconButton(onClick = { promoCodeApplied = true }) {
                                    Icon(imageVector = Icons.Default.Send, contentDescription = stringResource(R.string.apply_icon_desc))
                                }
                            }
                        }
                    )
                }
            }
            CartSummedValue(
                modifier = Modifier.padding(start = 72.dp),
                items = expandedCartItems,
                shippingFee = if (viewModel.addressSet) 2 else 0,
                promoCodeApplied = promoCodeApplied,
                backgroundColor = MaterialTheme.colors.secondary
            )
            Divider(
                modifier = Modifier.padding(bottom = 72.dp),
                color = MaterialTheme.colors.onSecondary.copy(alpha = 0.3f)
            )
        }

        if (openAddressDialog.value) {
            AddressDialog { openAddressDialog.value = it }
        }

        if (openPaymentDialog.value) {
            PaymentDialog { openPaymentDialog.value = it }
        }

        if (viewModel.orderSent) {
            navigateToCompleteOrder()
            viewModel.addOrderList(cartItems)
        }
    }
}

@Preview
@Composable
fun CheckoutPreview() {
    ShrineTheme {
        Checkout(
            cartItems = SampleItemsData.take(5),
            viewModel = viewModel(factory = ViewModelFactory()),
            navigateToCompleteOrder = {},
            onNavigateUp = {}
        )
    }
}