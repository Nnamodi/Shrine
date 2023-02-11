package com.roland.android.shrine.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.roland.android.shrine.viewmodel.SharedViewModel

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun Navigation(
    navController: NavHostController,
    sharedViewModel: SharedViewModel,
    logout: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "startScreen"
    ) {
        composable("startScreen") {
            StartScreen(
                navigateToDetail = { navController.navigate("detailScreen") },
                sharedViewModel = sharedViewModel,
                logout = logout
            )
        }
        composable("detailScreen") {
            DetailScreen(
                navigateToDetail = { navController.navigate("detailScreen") },
                sharedViewModel = sharedViewModel,
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}