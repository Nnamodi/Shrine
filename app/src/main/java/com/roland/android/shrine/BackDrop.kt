package com.roland.android.shrine

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.shrine.ui.theme.ShrineTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

val menuData = listOf("Featured", "Apartment", "Accessories", "Shoes", "Tops", "Bottoms", "Dresses")

@ExperimentalMaterialApi
@Composable
fun BackDrop() {
    val scope = rememberCoroutineScope()
    val backdropState = rememberBackdropScaffoldState(BackdropValue.Concealed)

    BackdropScaffold(
        appBar = {
            TopAppBar(scope, backdropState)
        },
        backLayerContent = {
            BackdropMenuItem()
        },
        frontLayerContent = {
            Column(
                Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) { Text("This is content for the front layer") }
        },
        frontLayerShape = MaterialTheme.shapes.large,
        scaffoldState = backdropState
    )
}

@ExperimentalMaterialApi
@Composable
private fun TopAppBar(
    scope: CoroutineScope,
    backdropState: BackdropScaffoldState,
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painterResource(id = R.drawable.menu),
                    contentDescription = "Menu icon",
                    modifier = Modifier.clickable {
                        scope.launch {
                            if (backdropState.isConcealed) {
                                backdropState.reveal()
                            } else {
                                backdropState.conceal()
                            }
                        }
                    }
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    "Shrine".uppercase(),
                    style = MaterialTheme.typography.subtitle1,
                    fontSize = 17.sp
                )
            }
        },
        actions = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search icon"
            )
        },
        elevation = 0.dp
    )
}

@Composable
private fun BackdropMenuItem() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        menuData.forEach { item ->
            MenuItem(text = item)
        }
        Divider(
            modifier = Modifier.width(56.dp),
            color = MaterialTheme.colors.onBackground
        )
        Text(
            text = "My Account".uppercase(),
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Composable
private fun MenuItem(text: String) {
    Text(
        text.uppercase(),
        style = MaterialTheme.typography.subtitle1
    )
}

@ExperimentalMaterialApi
@Preview
@Composable
fun BackDropPreview() {
    ShrineTheme { BackDrop() }
}