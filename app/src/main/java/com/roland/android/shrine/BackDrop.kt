package com.roland.android.shrine

import androidx.compose.animation.*
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.shrine.ui.theme.ShrineTheme
import kotlinx.coroutines.launch

val menuData = listOf("Featured", "Apartment", "Accessories", "Shoes", "Tops", "Bottoms", "Dresses")

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun BackDrop() {
    val scope = rememberCoroutineScope()
    var menuSelection by remember { mutableStateOf(0) }
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    var backdropRevealed by remember { mutableStateOf(scaffoldState.isRevealed) }

    BackdropScaffold(
        appBar = {
            TopAppBar(
                backdropRevealed = backdropRevealed,
                onBackdropReveal = {
                    scope.launch {
                        if (scaffoldState.isConcealed) {
                            scaffoldState.reveal()
                        } else {
                            scaffoldState.conceal()
                        }
                    }
                    backdropRevealed = it
                }
            )
        },
        backLayerContent = {
            BackdropMenuItem(
                activeMenuItem = menuSelection,
                onMenuItemSelect = {
                    menuSelection = it
                }
            )
        },
        frontLayerContent = {
            if (menuSelection == 0) {
                Cart()
            } else {
                Column(
                    Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) { Text("This is content for category: ${menuData[menuSelection]}") }
            }
        },
        frontLayerShape = MaterialTheme.shapes.large,
        scaffoldState = scaffoldState
    )
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
private fun TopAppBar(
    backdropRevealed: Boolean,
    onBackdropReveal: (Boolean) -> Unit = {},
) {
    TopAppBar(
        title = {
            val density = LocalDensity.current

            Box(
                Modifier
                    .width(46.dp)
                    .fillMaxHeight()
                    .toggleable(
                        value = backdropRevealed,
                        onValueChange = { onBackdropReveal(it) },
                        indication = rememberRipple(bounded = false, radius = 56.dp),
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                contentAlignment = Alignment.CenterStart
            ) {
                AnimatedVisibility(
                    visible = !backdropRevealed,
                    enter = fadeIn(animationSpec = tween(100, 90, LinearEasing))
                            + slideInHorizontally(initialOffsetX = { with(density) { (-20).dp.roundToPx() } }, animationSpec = tween(durationMillis = 270, easing = LinearEasing)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 120, easing = LinearEasing))
                            + slideOutHorizontally(targetOffsetX = { with(density) { (-20).dp.roundToPx() } }, animationSpec = tween(durationMillis = 120, easing = LinearEasing))
                ) {
                    Icon(
                        painterResource(id = R.drawable.menu),
                        contentDescription = "Menu navigation icon"
                    )
                }

                val logoTransition = updateTransition(
                    targetState = backdropRevealed,
                    label = "logoTransition"
                )
                val logoOffset = logoTransition.animateDp(
                    transitionSpec = {
                        val millis = if (targetState) 120 else 270
                        tween(durationMillis = millis, easing = LinearEasing)
                    },
                    label = "logoOffset"
                ) { revealed ->
                    if (revealed) 0.dp else 20.dp
                }
                Icon(
                    painterResource(id = R.drawable.logo),
                    contentDescription = "Shrine logo",
                    modifier = Modifier.offset(x = logoOffset.value)
                )
            }

            if (!backdropRevealed) {
                TopAppBarText(text = "Shrine")
            } else {
                MenuSearchField()
            }
        },
        actions = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search icon",
                modifier = Modifier.padding(end = 8.dp)
            )
        },
        elevation = 0.dp
    )
}

@Composable
private fun TopAppBarText(
    text: String,
    modifier: Modifier = Modifier
) {
    Text(
        text.uppercase(),
        modifier = modifier,
        style = MaterialTheme.typography.subtitle1,
        fontSize = 17.sp
    )
}

@Composable
fun MenuSearchField() {
    var searchText by remember { mutableStateOf("") }
    Box(
        modifier = Modifier
            .height(56.dp)
            .padding(end = 12.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = searchText,
            onValueChange = { searchText = it },
            singleLine = true,
            decorationBox = { innerTextField ->
                Row(
                    Modifier
                        .padding(end = 36.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) { innerTextField() }
            }
        )
        if (searchText.isEmpty()) {
            TopAppBarText(
                modifier = Modifier.alpha(ContentAlpha.disabled),
                text = "Search Shrine"
            )
        }
    }
}

@Composable
private fun BackdropMenuItem(
    activeMenuItem: Int,
    onMenuItemSelect : (index: Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        menuData.forEachIndexed { index, item ->
            MenuItem(
                index = index,
                text = item,
                activeMenu = activeMenuItem
            ) {
                onMenuItemSelect(it)
            }
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
private fun MenuItem(
    index: Int = -1,
    text: String,
    activeMenu: Int = -1,
    onClick: (index: Int) -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .height(32.dp)
            .clickable {
                onClick(index)
            }
    ) {
        if (activeMenu == index) {
            Image(
                painterResource(id = R.drawable.tab_indicator),
                contentDescription = null
            )
        }
        Text(
            text.uppercase(),
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Preview
@Composable
fun BackDropPreview() {
    ShrineTheme { BackDrop() }
}