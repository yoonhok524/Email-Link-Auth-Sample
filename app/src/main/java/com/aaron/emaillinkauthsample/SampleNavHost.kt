package com.aaron.emaillinkauthsample

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
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
        ) {
            val emailLink = navController.previousBackStackEntry?.savedStateHandle?.get<String>("emailLink")
            HomeScreen(
                vm = hiltViewModel(),
                emailLink = emailLink
            ) {
                navController.navigate(SignIn.route)
            }
        }
    }
}