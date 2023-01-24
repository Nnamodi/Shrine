package com.roland.android.shrine

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.roland.android.shrine.ui.theme.ShrineTheme

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalAnimationApi
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShrineTheme { AppUI { finish() } }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Preview
@Composable
fun AppPreview() {
    ShrineTheme { AppUI { println("Logged out") } }
}

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalAnimationApi
@ExperimentalMaterialApi
@Composable
fun AppUI(logout: () -> Unit = {}) {
    var expanded by remember { mutableStateOf(false) }
    var hidden by remember { mutableStateOf(false) }
    val cartItems = remember { mutableStateListOf(*SampleItemsData.take(0).toTypedArray()) }

    BoxWithConstraints(
        Modifier.fillMaxSize()
    ) {
        BackDrop(
            onReveal = { hidden = it },
            addToCart = { cartItems.add(it) },
            logout = logout
        )
        CartBottomSheet(
            modifier = Modifier.align(Alignment.BottomEnd),
            items = cartItems,
            expanded = expanded,
            hidden = hidden,
            maxHeight = maxHeight,
            maxWidth = maxWidth,
            onExpand = { expanded = it },
            removeFromCart = { cartItems.removeAt(it) }
        )
    }
}