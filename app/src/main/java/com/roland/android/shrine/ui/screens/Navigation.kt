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
        startDestination = Destination.StartScreen.route
    ) {
        composable(Destination.StartScreen.route) {
            StartScreen(
                navigateToDetail = { navController.navigate(Destination.DetailScreen.route) },
                sharedViewModel = sharedViewModel,
                logout = logout
            )
        }
        composable(Destination.DetailScreen.route) {
            DetailScreen(
                navigateToDetail = { navController.navigate(Destination.DetailScreen.route) },
                sharedViewModel = sharedViewModel,
                moveToCatalogue = {
                    navController.navigate(
                        route = Destination.StartScreen.route,
                        builder = { popUpToRoute }
                    )
                },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}

sealed class Destination(val route: String) {
    object StartScreen: Destination("start_screen")
    object DetailScreen: Destination("detail_screen")
}