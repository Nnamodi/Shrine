package com.roland.android.shrine

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roland.android.shrine.ui.theme.ShrineTheme

@Composable
fun CatalogueCard(
    modifier: Modifier = Modifier,
    data: ItemData,
    addToCart: (ItemData) -> Unit = {}
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.weight(1f)) {
            Image(
                painter = painterResource(id = data.photoResId),
                modifier = Modifier.fillMaxSize(),
                contentDescription = data.title,
                contentScale = ContentScale.Crop
            )
            IconButton(onClick = { addToCart(data) }) {
                Icon(
                    imageVector = Icons.Outlined.AddShoppingCart,
                    contentDescription = "Add to cart"
                )
            }
            Image(
                painter = painterResource(id = getVendorResId(data.vendor)),
                contentDescription = "Logo of ${data.vendor}",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 12.dp)
            )
        }
        Spacer(Modifier.height(20.dp))
        Text(
            text = data.title,
            style = MaterialTheme.typography.subtitle2
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text = "$${data.price}",
            style = MaterialTheme.typography.body2
        )
        Spacer(Modifier.height(20.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun CatalogueCardPreview() {
    ShrineTheme {
        CatalogueCard(
            modifier = Modifier.height(380.dp),
            data = SampleItemsData[5]
        )
    }
}

private fun <T> transformToWeavedList(items: List<T>): List<List<T>> {
    var i = 0
    val list = mutableListOf<List<T>>()
    while (i < items.size) {
        val even = i % 3 == 0
        val wList = mutableListOf<T>()
        wList.add(items[i])
        if (even && i + 1 < items.size) { wList.add(items[i + 1]) }
        list.add(wList.toList())
        i += if (even) 2 else 1
    }
    return list.toList()
}

@Composable
fun Catalogue(
    modifier: Modifier = Modifier,
    items: List<ItemData>,
    addToCart: (ItemData) -> Unit = {}
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp

    LazyRow(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(end = 32.dp)
    ) {
        itemsIndexed(
            items = transformToWeavedList(items),
            key = { _, item -> item[0].id }
        ) { index, item ->
            val even = index % 2 == 0

            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(vertical = 48.dp, horizontal = 16.dp)
                    .width((screenWidth * 0.66f).dp),
                horizontalAlignment = if (even) Alignment.Start else Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                if (even) {
                    if (item.getOrNull(1) != null) {
                        CatalogueCard(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(0.85f)
                                .align(Alignment.End),
                            data = item[1],
                            addToCart = addToCart
                        )
                        Spacer(Modifier.height(40.dp))
                        CatalogueCard(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(0.85f),
                            data = item[0],
                            addToCart = addToCart
                        )
                    } else {
                        CatalogueCard(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                                .fillMaxHeight(0.5f),
                            data = item[0],
                            addToCart = addToCart
                        )
                    }
                } else {
                    CatalogueCard(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .fillMaxHeight(0.5f),
                        data = item[0],
                        addToCart = addToCart
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun CataloguePreview() {
    ShrineTheme {
        Surface {
            Box(
                Modifier.fillMaxSize()
            ) {
                Catalogue(
                    items = SampleItemsData.filter { it.category == Category.Home }
                )
            }
        }
    }
}