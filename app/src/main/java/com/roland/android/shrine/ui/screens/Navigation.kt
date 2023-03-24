package com.roland.android.shrine.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
        startDestination = Destination.HomeScreen.route
    ) {
        composable(Destination.HomeScreen.route) {
            HomeScreen(
                navigateToDetail = { navController.navigate(Destination.DetailScreen.routeWithId(it.id)) },
                sharedViewModel = sharedViewModel,
                proceedToCheckout = { navController.navigate(Destination.CheckoutScreen.route) },
                logout = logout
            )
        }
        composable(
            route = Destination.DetailScreen.route,
            arguments = listOf(navArgument("itemId") { type = NavType.IntType })
        ) { backStackEntry ->
            DetailScreen(
                itemId = backStackEntry.arguments?.getInt("itemId"),
                navigateToDetail = { navController.navigate(Destination.DetailScreen.routeWithId(it.id)) },
                sharedViewModel = sharedViewModel,
                moveToCatalogue = { navController.navigate(Destination.HomeScreen.route) { popUpToRoute } },
                proceedToCheckout = { navController.navigate(Destination.CheckoutScreen.route) },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(Destination.CheckoutScreen.route) {
            CheckoutScreen(
                sharedViewModel = sharedViewModel,
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}

sealed class Destination(val route: String) {
    object HomeScreen: Destination("home_screen")
    object DetailScreen: Destination("detail_screen/{itemId}") {
        fun routeWithId(itemId: Int) = String.format("detail_screen/%d", itemId)
    }
    object CheckoutScreen: Destination("checkout_screen")
}