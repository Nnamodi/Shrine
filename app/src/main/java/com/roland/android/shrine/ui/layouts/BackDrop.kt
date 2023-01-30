package com.roland.android.shrine.ui.layouts

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.roland.android.shrine.R
import com.roland.android.shrine.data.Category
import com.roland.android.shrine.data.ItemData
import com.roland.android.shrine.data.SampleItemsData
import com.roland.android.shrine.ui.theme.ShrineTheme
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun BackDrop(
    onReveal: (Boolean) -> Unit = {},
    addToCart: (ItemData) -> Unit = {},
    logout: () -> Unit = {}
) {
    val scope = rememberCoroutineScope()
    var menuSelection by remember { mutableStateOf(Category.All) }
    val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Concealed)
    var backdropRevealed by remember { mutableStateOf(scaffoldState.isRevealed) }

    BackdropScaffold(
        appBar = {
            TopAppBar(
                backdropRevealed = backdropRevealed,
                onBackdropReveal = {
                    if (!scaffoldState.isAnimationRunning) {
                        backdropRevealed = it
                        onReveal(it)
                        scope.launch {
                            if (scaffoldState.isConcealed) {
                                scaffoldState.reveal()
                            } else {
                                scaffoldState.conceal()
                            }
                        }
                    }
                }
            )
        },
        backLayerContent = {
            BackdropMenuItem(
                activeMenuItem = menuSelection,
                backdropRevealed = backdropRevealed,
                modifier = Modifier.padding(top = 12.dp, bottom = 32.dp),
                onMenuItemSelect = {
                    backdropRevealed = false
                    onReveal(false)
                    menuSelection = it
                    scope.launch { scaffoldState.conceal() }
                },
                logout = logout
            )
        },
        frontLayerContent = {
            Catalogue(
                modifier = Modifier.fillMaxSize(),
                items = SampleItemsData.filter {
                    menuSelection == Category.All || it.category == menuSelection
                },
                addToCart = addToCart
            )
        },
        frontLayerShape = MaterialTheme.shapes.large,
        gesturesEnabled = false,
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
                    enter = fadeIn(animationSpec = tween(100, 90, LinearEasing)) +
                                slideInHorizontally(initialOffsetX = { with(density) { (-20).dp.roundToPx() } },
                                    animationSpec = tween(durationMillis = 270, easing = LinearEasing)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 120, easing = LinearEasing)) +
                               slideOutHorizontally(targetOffsetX = { with(density) { (-20).dp.roundToPx() } },
                                   animationSpec = tween(durationMillis = 120, easing = LinearEasing))
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

            AnimatedContent(
                targetState = backdropRevealed,
                transitionSpec = {
                    if (targetState) {
                        // Conceal to reveal
                        fadeIn(animationSpec = tween(240, 120, LinearEasing)) +
                            slideInHorizontally(initialOffsetX = { with(density) { (30).dp.roundToPx() } },
                                animationSpec = tween(durationMillis = 270, easing = LinearEasing)) with
                        fadeOut(animationSpec = tween(durationMillis = 120, easing = LinearEasing)) +
                            slideOutHorizontally(targetOffsetX = { with(density) { (-30).dp.roundToPx() } },
                                animationSpec = tween(durationMillis = 120, easing = LinearEasing))
                    } else {
                        // Reveal to conceal
                        fadeIn(animationSpec = tween(100, 90, LinearEasing)) +
                            slideInHorizontally(initialOffsetX = { with(density) { (-30).dp.roundToPx() } },
                                animationSpec = tween(durationMillis = 270, easing = LinearEasing)) with
                        fadeOut(animationSpec = tween(durationMillis = 90, easing = LinearEasing)) +
                            slideOutHorizontally(targetOffsetX = { with(density) { (-30).dp.roundToPx() } },
                                animationSpec = tween(durationMillis = 90, easing = LinearEasing))
                    }.using(SizeTransform(clip = false))
                },
                contentAlignment = Alignment.CenterStart
            ) { revealed ->
                if (revealed) MenuSearchField() else TopAppBarText()
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

@ExperimentalAnimationApi
@Composable
private fun BackdropMenuItem(
    modifier: Modifier = Modifier,
    backdropRevealed: Boolean = true,
    activeMenuItem: Category = Category.All,
    onMenuItemSelect: (Category) -> Unit = {},
    logout: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AnimatedVisibility(
            visible = backdropRevealed,
            enter = EnterTransition.None,
            exit = ExitTransition.None
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val menus = Category.values()

                menus.forEachIndexed { index, menu ->
                    MenuItem(
                        modifier = Modifier.clickable { onMenuItemSelect(menu) },
                        index = index
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
                MenuItem(index = menus.size) {
                    Divider(
                        modifier = Modifier
                            .width(56.dp)
                            .padding(vertical = 12.dp),
                        color = MaterialTheme.colors.onSurface.copy(ContentAlpha.disabled)
                    )
                }
                MenuItem(
                    modifier = Modifier.clickable { logout() },
                    index = menus.size + 1
                ) { MenuText() }
            }
        }
    }
}

@Composable
fun MenuText(
    text: String = "Logout",
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

@ExperimentalAnimationApi
@Composable
fun AnimatedVisibilityScope.MenuItem(
    modifier: Modifier = Modifier,
    index: Int,
    content: @Composable () -> Unit = {}
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .animateEnterExit(
                enter = fadeIn(animationSpec = tween(durationMillis = 240,
                    delayMillis = index * 15 + 60,
                    easing = LinearEasing)),
                exit = fadeOut(animationSpec = tween(durationMillis = 90, easing = LinearEasing))
            )
            .fillMaxWidth(0.5f)
            .clip(MaterialTheme.shapes.medium)
            .then(modifier)
    ) { content() }
}

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Preview
@Composable
fun BackDropPreview() {
    ShrineTheme { BackDrop() }
}