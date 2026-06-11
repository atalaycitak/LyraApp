package com.turkcell.lyraapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.turkcell.lyraapp.ui.auth.login.LoginRoute

// Rota tanımları
object Routes {
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val HOME = "home"
    const val FORGOT_PASSWORD = "forgot_password"
    const val SEARCH = "search"
    const val LIBRARY = "library"
    const val FAVORITES = "favorites"
    const val PROFILE = "profile"
}

@Composable
fun LyraAppNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN,
        modifier = modifier
    ) {
        composable(Routes.LOGIN) {
            LoginRoute(
                onNavigateToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Routes.REGISTER)
                },
                onNavigateToForgotPassword = {
                    navController.navigate(Routes.FORGOT_PASSWORD)
                }
            )
        }

        composable(Routes.REGISTER) {
            com.turkcell.lyraapp.ui.auth.register.RegisterRoute(
                onNavigateToLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(Routes.HOME) {
            com.turkcell.lyraapp.ui.home.HomeRoute()
        }

        composable(Routes.FORGOT_PASSWORD) {
            // ForgotPasswordRoute
        }

        composable(Routes.SEARCH) {
            // SearchRoute
        }

        composable(Routes.LIBRARY) {
            // LibraryRoute
        }

        composable(Routes.FAVORITES) {
            // FavoritesRoute
        }

        composable(Routes.PROFILE) {
            // ProfileRoute
        }
    }
}
