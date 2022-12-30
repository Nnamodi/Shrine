package com.roland.android.shrine

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.RemoveCircleOutline
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roland.android.shrine.ui.theme.ShrineTheme

@Composable
fun Cart() {
    Surface(
        color = MaterialTheme.colors.surface
    ) {
        Column(
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            CartHeader(SampleItemsData.size)

            SampleItemsData.forEach {
                CartItem(it)
            }

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
            ) {
                Text(text = "Proceed to checkout".uppercase())
            }
        }
    }
}

@Composable
private fun CartHeader(itemSize: Int) {
    Surface(
        color = MaterialTheme.colors.surface
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Down Arrow"
                )
            }
            Spacer(Modifier.width(4.dp))
            Text("Cart".uppercase())
            Spacer(Modifier.width(12.dp))
            Text("$itemSize items".uppercase())
        }
    }
}

@Composable
private fun CartItem(item: ItemData) {
    Surface(
        color = MaterialTheme.colors.surface
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {},
                Modifier.padding(horizontal = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.RemoveCircleOutline,
                    contentDescription = "Remove item icon"
                )
            }
            Column(
                Modifier.fillMaxWidth()
            ) {
                Divider(color = MaterialTheme.colors.onSecondary.copy(alpha = 0.3f))
                Row(
                    Modifier.padding(vertical = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = item.photoResId),
                        modifier = Modifier.size(width = 100.dp, height = 100.dp),
                        contentDescription = "Item image"
                    )
                    Spacer(modifier = Modifier.padding(end = 16.dp))
                    Column(
                        Modifier.padding(end = 16.dp)
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.vendor.name.uppercase(),
                                style = MaterialTheme.typography.body2
                            )
                            Text(
                                text = item.price.toString().prependIndent("$"),
                                style = MaterialTheme.typography.body2
                            )
                        }
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.subtitle2
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun CartHeaderPreview() {
    ShrineTheme {
        CartHeader(SampleItemsData.size)
    }
}

@Preview
@Composable
fun CartItemPreview() {
    ShrineTheme {
        CartItem(SampleItemsData[0])
    }
}

@Preview
@Composable
fun CartPreview() {
    ShrineTheme { Cart() }
}