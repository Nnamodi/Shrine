package com.roland.android.shrine

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.roland.android.shrine.ui.theme.ShrineTheme

@Composable
fun CatalogueCard(
    modifier: Modifier = Modifier,
    data: ItemData
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box {
            Image(
                painter = painterResource(id = data.photoResId),
                contentDescription = data.title,
                modifier = Modifier.size(300.dp, 250.dp),
                contentScale = ContentScale.Crop
            )
            Icon(
                imageVector = Icons.Outlined.AddShoppingCart,
                contentDescription = "Add to cart",
                modifier = Modifier.padding(12.dp)
            )
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
        CatalogueCard(data = SampleItemsData[5])
    }
}