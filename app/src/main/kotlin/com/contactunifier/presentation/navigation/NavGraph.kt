package com.contactunifier.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.contactunifier.data.model.DuplicateGroup
import com.contactunifier.presentation.main.MainScreen
import com.contactunifier.presentation.splash.SplashScreen

/**
 * Navigation setup for the app.
 * Defines all routes and screen transitions.
 */
sealed class Route(val route: String) {
    object Splash : Route("splash")
    object Main : Route("main")
    object Detail : Route("detail")
    object Settings : Route("settings")
}

@Composable
fun NavGraph(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Route.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Route.Splash.route) {
            SplashScreen(
                onSplashComplete = {
                    navController.navigate(Route.Main.route) {
                        popUpTo(Route.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Route.Main.route) {
            MainScreen(
                onNavigateToDetail = { group ->
                    // Navigate with group data
                    navController.navigate(Route.Detail.route)
                },
                onNavigateToSettings = {
                    navController.navigate(Route.Settings.route)
                }
            )
        }

        composable(Route.Detail.route) {
            // DetailScreen will be implemented
        }

        composable(Route.Settings.route) {
            // SettingsScreen will be implemented
        }
    }
}
