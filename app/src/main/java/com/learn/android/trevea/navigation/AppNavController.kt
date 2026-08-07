package com.learn.android.trevea.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.learn.android.trevea.ui.screens.HomeScreen
import com.learn.android.trevea.ui.screens.RegisterScreen

@Composable
fun AppNavController(
    modifier: Modifier = Modifier
) {
    val navHostController = rememberNavController()

    NavHost(
        navController = navHostController,
        startDestination = "HomeScreen"
    ) {
        composable(
            route = "HomeScreen"
        ) {
            HomeScreen(
                modifier = modifier,
                navController = navHostController
            )
        }

        composable(
            route = "register"
        ) {
            RegisterScreen(
                modifier = modifier,
                navController = navHostController
            )
        }
    }
}