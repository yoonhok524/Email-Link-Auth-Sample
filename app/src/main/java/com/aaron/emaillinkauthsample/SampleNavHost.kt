package com.aaron.emaillinkauthsample

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.aaron.emaillinkauthsample.home.HomeScreen
import com.aaron.emaillinkauthsample.nav.Home
import com.aaron.emaillinkauthsample.nav.SignIn
import com.aaron.emaillinkauthsample.signin.SignInScreen

@Composable
fun SampleNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = Home.route,
    ) {
        composable(route = SignIn.route) {
            SignInScreen(hiltViewModel())
        }

        composable(
            route = Home.route,
            arguments = listOf(navArgument(Home.KEY_EMAIL_LINK) {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { backStackEntry ->
            val emailLink = backStackEntry.arguments?.getString(Home.KEY_EMAIL_LINK)
            HomeScreen(
                vm = hiltViewModel(),
                emailLink = emailLink
            ) {
                navController.navigate(SignIn.route)
            }
        }
    }
}