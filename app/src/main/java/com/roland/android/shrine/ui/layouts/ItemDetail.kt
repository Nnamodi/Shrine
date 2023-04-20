package com.roland.android.shrine.ui.layouts

import android.annotation.SuppressLint
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.roland.android.shrine.R
import com.roland.android.shrine.data.FetchData.getMoreItemsFromVendor
import com.roland.android.shrine.data.FetchData.getSimilarItems
import com.roland.android.shrine.data.SampleItemsData
import com.roland.android.shrine.data.getVendorResId
import com.roland.android.shrine.data.model.ItemData
import com.roland.android.shrine.ui.layouts.dialogs.WishlistDialog
import com.roland.android.shrine.ui.screens.CartBottomSheetState
import com.roland.android.shrine.ui.theme.ShrineTheme
import com.roland.android.shrine.utils.FirstCartItemData
import com.roland.android.shrine.utils.SnackbarMessage
import com.roland.android.shrine.utils.onFavoriteClicked
import com.roland.android.shrine.viewmodel.SharedViewModel
import com.roland.android.shrine.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch
import kotlin.math.max

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ItemDetail(
    item: ItemData,
    viewModel: SharedViewModel,
    userIsNull: Boolean = false,
    addToCart:(FirstCartItemData) -> Unit = {},
    addToWishlist: (ItemData) -> Unit = {},
    removeFromWishlist: (ItemData) -> Unit = {},
    onLogin: () -> Unit = {},
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
                                Text(stringResource(R.string.snack_action_text))
                            }
                        }
                        if (userIsNull) {
                            TextButton(onClick = onLogin) {
                                Text(stringResource(R.string.login))
                            }
                        }
                    }
                ) { SnackbarMessage(favourite, userIsNull) }
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
                    Icon(imageVector = Icons.Default.Close, contentDescription = stringResource(R.string.close_icon_desc))
                }
            }
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Description(
                    item = item,
                    viewModel = viewModel,
                    imageSize = imageSize,
                    position = position,
                    addToCart = {
                        if (!userIsNull) { addToCart(it) }
                        else { scope.launch { snackbarHostState.showSnackbar("") } }
                    },
                    addToWishlist = {
                        if (!userIsNull) { addToWishlist(item); favourite = true }
                        scope.launch { snackbarHostState.showSnackbar("") }
                    },
                    removeFromWishlist = {
                        removeFromWishlist(item); favourite = false
                        scope.launch { snackbarHostState.showSnackbar("") }
                    }
                )
            }
            OtherItems(
                viewModel = viewModel,
                header = stringResource(R.string.items_from_vendor, item.vendor.name),
                bottomPadding = 20.dp,
                otherItems = getMoreItemsFromVendor(item),
                addToCart = {
                    if (!userIsNull) { addToWishlist(it.data); favourite = true }
                    scope.launch { snackbarHostState.showSnackbar("") }
                },
                addToWishlist = {
                    if (!userIsNull) { addToWishlist(it); favourite = true }
                    scope.launch { snackbarHostState.showSnackbar("") }
                },
                removeFromWishlist = {
                    removeFromWishlist(it); favourite = false
                    scope.launch { snackbarHostState.showSnackbar("") }
                },
                navigateToDetail = navigateToDetail
            )
            OtherItems(
                viewModel = viewModel,
                header = stringResource(R.string.might_like),
                bottomPadding = 60.dp,
                otherItems = getSimilarItems(item),
                addToCart = {
                    if (!userIsNull) { addToWishlist(it.data); favourite = true }
                    scope.launch { snackbarHostState.showSnackbar("") }
                },
                addToWishlist = {
                    if (!userIsNull) { addToWishlist(it); favourite = true }
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
    viewModel: SharedViewModel,
    imageSize: IntSize,
    position: Offset,
    addToCart: (FirstCartItemData) -> Unit,
    addToWishlist: (ItemData) -> Unit,
    removeFromWishlist: (ItemData) -> Unit
) {
    val openDialog = remember { mutableStateOf(false) }
    val isInWishlist = viewModel.wishlist.any { item.id == it.id }
    val favouriteIcon = if (isInWishlist) { Icons.Outlined.Favorite } else { Icons.Outlined.FavoriteBorder }
    item.favourited = isInWishlist

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
                contentDescription = stringResource(R.string.vendor_logo_desc, item.vendor),
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
            Text(stringResource(R.string.add_to_cart).uppercase())
        }
        IconButton(onClick = {
            onFavoriteClicked(item, addToWishlist) { openDialog.value = it }
        }) {
            Icon(
                imageVector = favouriteIcon,
                contentDescription = stringResource(R.string.add_to_wishlist)
            )
        }
    }
    Divider(color = MaterialTheme.colors.onSurface.copy(alpha = 0.3f))

    if (openDialog.value) {
        WishlistDialog(
            item = item,
            removeFromWishlist = removeFromWishlist,
            openDialog = { openDialog.value = it }
        )
    }
}

@Composable
fun OtherItems(
    viewModel: SharedViewModel,
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
            if (shownInWishlist) {
                Divider(color = MaterialTheme.colors.onSecondary.copy(alpha = 0.3f))
            }
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
                        viewModel = viewModel,
                        modifier = Modifier
                            .size(200.dp)
                            .padding(end = 20.dp),
                        data = item,
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
        ItemDetail(SampleItemsData[10], viewModel(factory = ViewModelFactory()))
    }
}