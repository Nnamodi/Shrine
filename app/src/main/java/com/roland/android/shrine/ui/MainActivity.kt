package com.roland.android.shrine.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.navigation.compose.rememberNavController
import com.roland.android.shrine.ui.screens.Navigation
import com.roland.android.shrine.ui.theme.ShrineTheme

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalAnimationApi
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            ShrineTheme {
                Navigation(
                    navController = navController,
                    logout = { finish() }
                )
            }
        }
    }
}