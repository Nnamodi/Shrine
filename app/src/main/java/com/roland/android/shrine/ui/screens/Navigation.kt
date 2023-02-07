package com.roland.android.shrine.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.roland.android.shrine.data.SampleItemsData

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun Navigation(
    navController: NavHostController,
    logout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "startScreen"
    ) {
        composable("startScreen") {
            StartScreen(
                navigateToDetail = { navController.navigate("detailScreen/{data}") },
                logout = logout
            )
        }
        composable("detailScreen") {
            DetailScreen(SampleItemsData[17])
        }
    }
}