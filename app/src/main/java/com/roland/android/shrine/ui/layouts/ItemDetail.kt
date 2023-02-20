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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.roland.android.shrine.data.ItemData
import com.roland.android.shrine.data.SampleItemsData
import com.roland.android.shrine.data.getVendorResId
import com.roland.android.shrine.ui.screens.CartBottomSheetState
import com.roland.android.shrine.ui.theme.ShrineTheme
import com.roland.android.shrine.utils.FirstCartItemData
import com.roland.android.shrine.utils.ShowDialog
import com.roland.android.shrine.utils.SnackbarMessage
import com.roland.android.shrine.utils.onFavoriteClicked
import kotlinx.coroutines.launch
import kotlin.math.max

@Composable
fun ItemDetail(
    item: ItemData,
    addToCart:(FirstCartItemData) -> Unit = {},
    addToWishlist: (ItemData) -> Unit = {},
    removeFromWishlist: (ItemData) -> Unit = {},
    navigateToDetail: (ItemData) -> Unit = {},
    onViewWishlist: (CartBottomSheetState) -> Unit = {},
    onNavigateUp: () -> Unit = {}
) {
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val screenHeight = LocalConfiguration.current.screenHeightDp / 2.5
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    var position by remember { mutableStateOf(Offset.Zero) }
    var favourite by remember { mutableStateOf(false) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) {
                Snackbar(
                    modifier = Modifier.padding(start = 12.dp, end = 12.dp, bottom = 60.dp),
                    action = {
                        if (favourite) {
                            TextButton(onClick = { onViewWishlist(CartBottomSheetState.Expanded) }) {
                                Text("View")
                            }
                        }
                    }
                ) { SnackbarMessage(favourite) }
            }
        },
        backgroundColor = MaterialTheme.colors.surface
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
                Description(
                    item = item,
                    imageSize = imageSize,
                    position = position,
                    addToCart = addToCart,
                    addToWishlist = {
                        addToWishlist(item); favourite = true
                        scope.launch { snackbarHostState.showSnackbar("") }
                    },
                    removeFromWishlist = {
                        removeFromWishlist(item); favourite = false
                        scope.launch { snackbarHostState.showSnackbar("") }
                    }
                )
            }
            OtherItems(
                header = "More from ${item.vendor.name}",
                bottomPadding = 20.dp,
                otherItems = SampleItemsData.filter { it.vendor == item.vendor && it != item },
                addToCart = addToCart,
                addToWishlist = {
                    addToWishlist(it); favourite = true
                    scope.launch { snackbarHostState.showSnackbar("") }
                },
                removeFromWishlist = {
                    removeFromWishlist(it); favourite = false
                    scope.launch { snackbarHostState.showSnackbar("") }
                },
                navigateToDetail = navigateToDetail
            )
            OtherItems(
                header = "You might also like",
                bottomPadding = 60.dp,
                otherItems = SampleItemsData.filter { it.category == item.category && it != item },
                addToCart = addToCart,
                addToWishlist = {
                    addToWishlist(it); favourite = true
                    scope.launch { snackbarHostState.showSnackbar("") }
                },
                removeFromWishlist = {
                    removeFromWishlist(it); favourite = false
                    scope.launch { snackbarHostState.showSnackbar("") }
                },
                navigateToDetail = navigateToDetail
            )
        }
    }
}

@Composable
private fun Description(
    item: ItemData,
    imageSize: IntSize,
    position: Offset,
    addToCart: (FirstCartItemData) -> Unit,
    addToWishlist: (ItemData) -> Unit,
    removeFromWishlist: (ItemData) -> Unit
) {
    val openDialog = remember { mutableStateOf(false) }
    var favourited by remember { mutableStateOf(Icons.Outlined.FavoriteBorder) }
    val favouriteIcon = if (favourited == Icons.Outlined.Favorite || item.favourited) {
        Icons.Outlined.Favorite } else { Icons.Outlined.FavoriteBorder }

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
    Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f))
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Button(
            modifier = Modifier
                .padding(vertical = 16.dp)
                .weight(1f),
            onClick = { addToCart(FirstCartItemData(item, imageSize, position)) }
        ) {
            Icon(
                imageVector = Icons.Outlined.AddShoppingCart,
                contentDescription = null,
                modifier = Modifier.padding(end = 16.dp)
            )
            Text("Add to cart".uppercase())
        }
        IconButton(onClick = {
            onFavoriteClicked(item, addToWishlist, { openDialog.value = it }) { favourited = it }
        }) {
            Icon(
                imageVector = favouriteIcon,
                contentDescription = "Add to wishlist"
            )
        }
    }
    Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f))

    if (openDialog.value) {
        ShowDialog(
            item = item,
            removeFromWishlist = removeFromWishlist,
            openDialog = { openDialog.value = it },
            favoriteIcon = { favourited = it }
        )
    }
}

@Composable
fun OtherItems(
    modifier: Modifier = Modifier,
    header: String,
    bottomPadding: Dp,
    otherItems: List<ItemData>,
    addToCart: (FirstCartItemData) -> Unit,
    addToWishlist: (ItemData) -> Unit = {},
    removeFromWishlist: (ItemData) -> Unit = {},
    navigateToDetail: (ItemData) -> Unit,
    shownInWishlist: Boolean = false
) {
    if (otherItems.isNotEmpty()) {
        Column(modifier) {
            Row {
                Text(
                    text = header.uppercase(),
                    style = MaterialTheme.typography.subtitle1,
                    modifier = Modifier.padding(start = 20.dp, top = 20.dp, bottom = 20.dp)
                )
                if (shownInWishlist) {
                    Text(
                        text = "(${otherItems.size})",
                        modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp)
                    )
                }
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
                        addToWishlist = addToWishlist,
                        removeFromWishlist = removeFromWishlist,
                        navigateToDetail = navigateToDetail,
                        shownInWishlist = shownInWishlist
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun ItemDetailPreview() {
    ShrineTheme {
        ItemDetail(SampleItemsData[10])
    }
}