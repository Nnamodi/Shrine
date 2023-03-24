package com.roland.android.shrine.ui

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.roland.android.shrine.R
import com.roland.android.shrine.ui.screens.Navigation
import com.roland.android.shrine.ui.theme.ShrineTheme
import com.roland.android.shrine.viewmodel.SharedViewModel
import com.roland.android.shrine.viewmodel.SharedViewModelFactory

@RequiresApi(Build.VERSION_CODES.N)
@ExperimentalAnimationApi
@ExperimentalMaterialApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Shrine)
        setContent {
            val navController = rememberNavController()
            val sharedViewModel: SharedViewModel = viewModel(factory = SharedViewModelFactory())

            ShrineTheme {
                Navigation(
                    navController = navController,
                    sharedViewModel = sharedViewModel,
                    logout = { finish() }
                )
            }
        }
    }
}