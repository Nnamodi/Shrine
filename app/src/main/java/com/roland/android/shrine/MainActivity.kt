package com.roland.android.shrine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.roland.android.shrine.ui.theme.ShrineTheme

@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShrineTheme { BackDrop() }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AppPreview() {
    ShrineTheme { Cart() }
}