package com.roland.android.shrine.ui.layouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AddShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roland.android.shrine.data.ItemData
import com.roland.android.shrine.data.SampleItemsData
import com.roland.android.shrine.data.getVendorResId
import com.roland.android.shrine.ui.theme.ShrineTheme

@Composable
fun ItemDetail(
    item: ItemData,
    onBackPressed: () -> Unit = {}
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp / 2.5

    Surface(
        color = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Box {
                Image(
                    painterResource(id = item.photoResId),
                    contentDescription = item.title,
                    modifier = Modifier.size(screenWidth.dp, screenHeight.dp),
                    contentScale = ContentScale.Crop
                )
                IconButton(onClick = { onBackPressed() }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }
            Column(Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painterResource(id = getVendorResId(item.vendor)),
                            contentDescription = "Logo of ${item.vendor}",
                            modifier = Modifier.padding(end = 20.dp),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = item.vendor.name.uppercase(),
                            style = MaterialTheme.typography.subtitle1
                        )
                    }
                    Text(
                        text = "$${item.price}",
                        style = MaterialTheme.typography.h5
                    )
                }
                Column(
                    modifier = Modifier.padding(top = 24.dp, bottom = 24.dp)
                ) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.h3
                    )
                    Spacer(Modifier.height(24.dp))
                    Text("Handmade item, carved from the trunk of an African Iroko tree to soothe your desire. Is very suitable for many, if not all, purposes.")
                }
                Divider(color = MaterialTheme.colors.onSurface.copy(alpha =  0.3f))
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 16.dp),
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AddShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    Text("Add to cart".uppercase())
                }
            }
        }
    }
}

@Preview
@Composable
fun ItemDetailPreview() {
    ShrineTheme {
        ItemDetail(SampleItemsData[17])
    }
}