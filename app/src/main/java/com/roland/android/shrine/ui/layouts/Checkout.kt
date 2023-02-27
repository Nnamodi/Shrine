package com.roland.android.shrine.ui.layouts

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roland.android.shrine.data.ExpandedCartItem
import com.roland.android.shrine.data.SampleItemsData
import com.roland.android.shrine.ui.theme.ShrineTheme
import com.roland.android.shrine.utils.cardType

@Composable
fun Checkout(
    cartItems: List<ExpandedCartItem>,
    onNavigateUp: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Checkout".uppercase()) },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                backgroundColor = MaterialTheme.colors.secondary
            )
        },
        backgroundColor = MaterialTheme.colors.secondary
    ) {
        val openAddressDialog = rememberSaveable { mutableStateOf(false) }
        val openPaymentDialog = rememberSaveable { mutableStateOf(false) }
        var promoCode by rememberSaveable { mutableStateOf("") }
        var streetAddress by rememberSaveable { mutableStateOf("345 Main St, 4th Floor") }
        var vicinity by rememberSaveable { mutableStateOf("San Francisco, CA 94109") }
        var cardNumber by rememberSaveable { mutableStateOf("") }
        var expiryMonth by rememberSaveable { mutableStateOf("") }
        var expiryYear by rememberSaveable { mutableStateOf("") }
        var securityCode by rememberSaveable { mutableStateOf("") }

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
                    contentDescription = "Shipping icon"
                )
                Column(Modifier.padding(vertical = 24.dp)) {
                    Text("Shipping".uppercase())
                    Text(
                        text = streetAddress,
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text(
                        text = vicinity,
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                Spacer(Modifier.weight(1f))
                IconButton(
                    modifier = Modifier.padding(vertical = 24.dp),
                    onClick = { openAddressDialog.value = true }
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit address")
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
                    contentDescription = "Payment icon"
                )
                Column(Modifier.padding(vertical = 24.dp)) {
                    Text("Payment".uppercase())
                    Text(
                        text = cardType(cardNumber),
                        style = MaterialTheme.typography.subtitle1
                    )
                    Text(
                        text = "· · · ·  · · · ·  ${cardNumber.takeLast(4).ifEmpty { "1234" }}",
                        style = MaterialTheme.typography.subtitle1
                    )
                }
                Spacer(Modifier.weight(1f))
                IconButton(
                    modifier = Modifier.padding(vertical = 24.dp),
                    onClick = { openPaymentDialog.value = true }
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit card details")
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
                    contentDescription = "Voucher icon"
                )
                Column(Modifier.padding(vertical = 24.dp)) {
                    Text("Have a promo code?".uppercase())
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, end = 16.dp),
                        value = promoCode,
                        onValueChange = { promoCode = it },
                        singleLine = true,
                        maxLines = 10,
                        shape = CutCornerShape(12.dp),
                        label = {
                            Text("Enter a Promo Code")
                        }
                    )
                }
            }
            CartSummedValue(
                modifier = Modifier.padding(start = 72.dp),
                items = cartItems,
                shippingFee = 2,
                backgroundColor = MaterialTheme.colors.secondary
            )
            Divider(
                modifier = Modifier.padding(bottom = 72.dp),
                color = MaterialTheme.colors.onSecondary.copy(alpha = 0.3f)
            )
        }

        if (openAddressDialog.value) {
            AddressDialog(
                initialAddress = streetAddress,
                initialVicinity = vicinity,
                setStreetAddress = { streetAddress = it },
                setVicinity = { vicinity = it },
                openDialog = { openAddressDialog.value = it }
            )
        }

        if (openPaymentDialog.value) {
            PaymentDialog(
                initialCardNumber = cardNumber,
                initialMonth = expiryMonth,
                initialYear = expiryYear,
                initialCode = securityCode,
                setCardDetails = { cardNo, month, year, code ->
                    cardNumber = cardNo
                    expiryMonth = month
                    expiryYear = year
                    securityCode = code
                },
                openDialog = { openPaymentDialog.value = it }
            )
        }
    }
}

@Preview
@Composable
fun CheckoutPreview() {
    ShrineTheme {
        val items = SampleItemsData.take(6)
        val cartItems by remember(items) {
            derivedStateOf {
                items.mapIndexed { index, item -> ExpandedCartItem(index = index, data = item) }
            }
        }

        Checkout(
            cartItems = cartItems
        )
    }
}