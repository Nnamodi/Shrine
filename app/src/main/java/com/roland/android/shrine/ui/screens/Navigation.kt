package com.roland.android.shrine.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
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
    logout: () -> Unit
) {
    val sharedViewModel: SharedViewModel = viewModel()

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
                sharedViewModel = sharedViewModel
            ) { navController.navigateUp() }
        }
    }
}