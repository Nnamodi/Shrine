package com.roland.android.shrine.ui.layouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.AddShoppingCart
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.roland.android.shrine.data.ItemData
import com.roland.android.shrine.data.SampleItemsData
import com.roland.android.shrine.data.getVendorResId
import com.roland.android.shrine.ui.theme.ShrineTheme
import com.roland.android.shrine.utils.FirstCartItemData
import kotlin.math.max

@Composable
fun ItemDetail(
    item: ItemData,
    addToCart:(FirstCartItemData) -> Unit = {},
    navigateToDetail: (ItemData) -> Unit = {},
    onNavigateUp: () -> Unit = {}
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp / 2.5
    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    var position by remember { mutableStateOf(Offset.Zero) }

    Surface(
        color = MaterialTheme.colors.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .onGloballyPositioned { position = it.positionInRoot() }
        ) {
            Box {
                val imageHeight = max(300, screenHeight.toInt())

                Image(
                    painterResource(id = item.photoResId),
                    contentDescription = item.title,
                    modifier = Modifier
                        .size(screenWidth.dp, imageHeight.dp)
                        .onGloballyPositioned { imageSize = it.size },
                    contentScale = ContentScale.Crop
                )
                IconButton(onClick = { onNavigateUp() }) {
                    Icon(imageVector = Icons.Default.Close, contentDescription = "Close")
                }
            }
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
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
                    onClick = { addToCart(FirstCartItemData(item, imageSize, position)) }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.AddShoppingCart,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 16.dp)
                    )
                    Text("Add to cart".uppercase())
                }
                Divider(color = MaterialTheme.colors.onSurface.copy(alpha =  0.3f))
            }
            OtherItems(
                header = "More from ${item.vendor.name}",
                bottomPadding = 20.dp,
                otherItems = SampleItemsData.filter { it.vendor == item.vendor && it != item },
                addToCart = addToCart,
                navigateToDetail = navigateToDetail
            )
            OtherItems(
                header = "You might also like",
                bottomPadding = 60.dp,
                otherItems = SampleItemsData.filter { it.category == item.category && it != item },
                addToCart = addToCart,
                navigateToDetail = navigateToDetail
            )
        }
    }
}

@Composable
private fun OtherItems(
    header: String,
    bottomPadding: Dp,
    otherItems: List<ItemData>,
    addToCart: (FirstCartItemData) -> Unit,
    navigateToDetail: (ItemData) -> Unit
) {
    if (otherItems.isNotEmpty()) {
        Text(
            text = header.uppercase(),
            style = MaterialTheme.typography.subtitle1,
            modifier = Modifier.padding(20.dp)
        )
    }
    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = bottomPadding),
        contentPadding = PaddingValues(start = 20.dp)
    ) {
        itemsIndexed(
            items = otherItems,
            key = { _, item -> item.id }
        ) { _, item ->
            CatalogueCard(
                data = item,
                modifier = Modifier
                    .size(200.dp)
                    .padding(end = 20.dp),
                addToCart = addToCart,
                navigateToDetail = navigateToDetail
            )
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