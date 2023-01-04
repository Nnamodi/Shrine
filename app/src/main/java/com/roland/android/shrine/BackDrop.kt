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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.roland.android.shrine.ui.theme.ShrineTheme
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun BackDrop() {
    val scope = rememberCoroutineScope()
    var menuSelection by remember { mutableStateOf(Category.Featured) }
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
            if (menuSelection == Category.Featured) {
                Cart()
            } else {
                Column(
                    Modifier.padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) { Text("This is content for category: $menuSelection") }
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
                tint = LocalContentColor.current.copy(alpha = ContentAlpha.high),
                modifier = Modifier.padding(end = 12.dp)
            )
        },
        elevation = 0.dp
    )
}

@Composable
private fun TopAppBarText(
    modifier: Modifier = Modifier,
    text: String = "Shrine"
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
        Divider(
            modifier = Modifier.align(Alignment.BottomCenter),
            color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled)
        )
    }
}

@Composable
private fun BackdropMenuItem(
    modifier: Modifier = Modifier,
    activeMenuItem: Category = Category.Featured,
    onMenuItemSelect: (Category) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Category.values().forEach { menu ->
                MenuItem(
                    modifier = Modifier.clickable { onMenuItemSelect(menu) }
                ) {
                    MenuText(
                        text = menu.name,
                        activeDecoration = {
                            if (menu == activeMenuItem) {
                                Image(
                                    painterResource(id = R.drawable.tab_indicator),
                                    contentDescription = "Active category icon"
                                )
                            }
                        }
                    )
                }
            }
            MenuItem {
                Divider(
                    modifier = Modifier
                        .width(56.dp)
                        .padding(vertical = 12.dp),
                    color = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled)
                )
            }
            MenuItem { MenuText() }
        }
    }
}

@Composable
fun MenuText(
    text: String = "My Account",
    activeDecoration: @Composable () -> Unit = {}
) {
    Box(
        modifier = Modifier.height(44.dp),
        contentAlignment = Alignment.Center
    ) {
        activeDecoration()
        Text(
            text = text.uppercase(),
            style = MaterialTheme.typography.subtitle1
        )
    }
}

@Composable
fun MenuItem(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxWidth(0.5f)
            .clip(MaterialTheme.shapes.medium)
            .then(modifier)
    ) { content() }
}

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Preview
@Composable
fun BackDropPreview() {
    ShrineTheme { BackDrop() }
}