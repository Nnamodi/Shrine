package com.roland.android.shrine.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.roland.android.shrine.data.UserWithAddress
import com.roland.android.shrine.viewmodel.AccountViewModel
import com.roland.android.shrine.viewmodel.CheckoutViewModel
import com.roland.android.shrine.viewmodel.SharedViewModel
import com.roland.android.shrine.viewmodel.ViewModelFactory

@ExperimentalAnimationApi
@ExperimentalMaterialApi
@RequiresApi(Build.VERSION_CODES.N)
@Composable
fun Navigation(
    navController: NavHostController,
    checkoutViewModel: CheckoutViewModel,
    sharedViewModel: SharedViewModel,
    accountViewModel: AccountViewModel,
    closeApp: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = findStartDestination()
    ) {
        composable(Destination.LoginScreen.route) {
            LoginScreen {
                navController.apply {
                    popBackStack()
                    if (backQueue.isEmpty()) {
                        navigate(Destination.HomeScreen.route)
                    }
                }
            }
        }
        composable(Destination.HomeScreen.route) {
            HomeScreen(
                navigateToDetail = { navController.navigate(Destination.DetailScreen.routeWithId(it.id)) },
                sharedViewModel = sharedViewModel,
                userIsNull = accountViewModel.user.firstName.isEmpty(),
                proceedToCheckout = { navController.navigate(Destination.CheckoutScreen.route) },
                onAccountButtonPressed = {
                    if (accountViewModel.user.firstName.isEmpty()) {
                        navController.navigate(Destination.LoginScreen.route)
                    } else { navController.navigate(Destination.AccountScreen.route) }
                },
                onLogin = { navController.navigate(Destination.LoginScreen.route) },
                closeApp = closeApp
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
                userIsNull = accountViewModel.user.firstName.isEmpty(),
                moveToCatalogue = {
                    navController.apply {
                        while (currentDestination?.route != Destination.HomeScreen.route) { navigateUp() }
                    }
                },
                proceedToCheckout = { navController.navigate(Destination.CheckoutScreen.route) },
                onLogin = { navController.navigate(Destination.LoginScreen.route) },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(Destination.CheckoutScreen.route) {
            CheckoutScreen(
                sharedViewModel = sharedViewModel,
                navigateToCompleteOrder = {
                    navController.apply { popBackStack(); navigate(Destination.ReceiptScreen.route) }
                },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(Destination.ReceiptScreen.route) {
            ReceiptScreen(
                checkoutViewModel = checkoutViewModel,
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(Destination.AccountScreen.route) {
            AccountScreen(
                userDetails = UserWithAddress(
                    accountViewModel.user,
                    checkoutViewModel.address
                ),
                viewPendingDelivery = { navController.navigate(Destination.ReceiptScreen.route) },
                orderHistory = accountViewModel.orderedItems,
                onNavigateUp = { navController.navigateUp() },
	            closeApp = closeApp
            )
        }
    }
}

@Composable
private fun findStartDestination(
    viewModel: AccountViewModel = viewModel(factory = ViewModelFactory())
): String {
    return if (viewModel.firstLaunch) {
        Destination.LoginScreen.route
    } else {
        Destination.HomeScreen.route
    }
}

sealed class Destination(val route: String) {
    object LoginScreen: Destination("login_screen")
    object HomeScreen: Destination("home_screen")
    object AccountScreen: Destination("account_screen")
    object DetailScreen: Destination("detail_screen/{itemId}") {
        fun routeWithId(itemId: Int) = String.format("detail_screen/%d", itemId)
    }
    object CheckoutScreen: Destination("checkout_screen")
    object ReceiptScreen: Destination("receipt_screen")
}