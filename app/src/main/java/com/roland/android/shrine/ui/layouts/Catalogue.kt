package com.roland.android.shrine.ui.layouts

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.roland.android.shrine.data.Category
import com.roland.android.shrine.data.ItemData
import com.roland.android.shrine.data.SampleItemsData
import com.roland.android.shrine.data.getVendorResId
import com.roland.android.shrine.ui.theme.ShrineTheme
import com.roland.android.shrine.utils.FirstCartItemData

@Composable
fun CatalogueCard(
    modifier: Modifier = Modifier,
    data: ItemData,
    addToCart: (FirstCartItemData) -> Unit = {},
    addToWishlist: (ItemData) -> Unit = {},
    navigateToDetail: (ItemData) -> Unit = {},
    shownInWishlist: Boolean = false
) {
    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    var position by remember { mutableStateOf(Offset.Zero) }
    var favourited by remember { mutableStateOf(Icons.Outlined.FavoriteBorder) }
    val favouriteIcon = if (favourited == Icons.Outlined.Favorite || data.favourited) {
        Icons.Outlined.Favorite } else { Icons.Outlined.FavoriteBorder }

    Column(
        modifier = modifier.clickable { navigateToDetail(data) }
            .onGloballyPositioned { position = it.positionInRoot() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(Modifier.weight(1f)) {
            Image(
                painter = painterResource(id = data.photoResId),
                contentDescription = "Photo of ${data.title}",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .onGloballyPositioned { imageSize = it.size }
            )
            if (!shownInWishlist) {
                Column {
                    IconButton(onClick = { addToCart(FirstCartItemData(data, imageSize, position)) }) {
                        Icon(
                            imageVector = Icons.Outlined.AddShoppingCart,
                            contentDescription = "Add to cart"
                        )
                    }
                    IconButton(onClick = { addToWishlist(data); favourited = Icons.Outlined.Favorite }) {
                        Icon(
                            imageVector = favouriteIcon,
                            contentDescription = "Add to wishlist"
                        )
                    }
                }
            }
            Image(
                painter = painterResource(id = getVendorResId(data.vendor)),
                contentDescription = "Logo of ${data.vendor}",
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 12.dp)
            )
        }
        Text(
            modifier = Modifier.padding(top = 20.dp),
            text = data.title,
            style = MaterialTheme.typography.subtitle2
        )
        Text(
            modifier = Modifier.padding(top = 8.dp, bottom = 20.dp),
            text = "$${data.price}",
            style = MaterialTheme.typography.body2
        )
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
    addToCart: (FirstCartItemData) -> Unit = {},
    addToWishlist: (ItemData) -> Unit = {},
    navigateToDetail: (ItemData) -> Unit
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
                            addToCart = addToCart,
                            addToWishlist = addToWishlist,
                            navigateToDetail = navigateToDetail
                        )
                        Spacer(Modifier.height(40.dp))
                        CatalogueCard(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(0.85f),
                            data = item[0],
                            addToCart = addToCart,
                            addToWishlist = addToWishlist,
                            navigateToDetail = navigateToDetail
                        )
                    } else {
                        Row(Modifier.fillMaxHeight()) {
                            CatalogueCard(
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .fillMaxHeight(0.5f)
                                    .align(Alignment.Bottom),
                                data = item[0],
                                addToCart = addToCart,
                                addToWishlist = addToWishlist,
                                navigateToDetail = navigateToDetail
                            )
                        }
                    }
                } else {
                    CatalogueCard(
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .fillMaxHeight(0.5f),
                        data = item[0],
                        addToCart = addToCart,
                        addToWishlist = addToWishlist,
                        navigateToDetail = navigateToDetail
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
                ) {}
            }
        }
    }
}