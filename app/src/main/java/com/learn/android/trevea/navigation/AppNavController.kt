package com.learn.android.trevea.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.learn.android.trevea.ui.screens.HomeScreen
import com.learn.android.trevea.ui.screens.ProfileScreen
import com.learn.android.trevea.ui.screens.QuizConfigScreen
import com.learn.android.trevea.ui.screens.QuizScreen

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

//        TODO: Update the route to profile
        composable(
            route = "register"
        ) {
            ProfileScreen(
                modifier = modifier,
                navController = navHostController
            )
        }

        composable (
            route = "quiz/{categoryId}"
        ) { navBackStackEntry ->
            val categoryId = navBackStackEntry.arguments?.getString("categoryId")
            QuizScreen(
                navController = navHostController,
                categoryId = categoryId
            )
        }

        composable (
            route = "quiz_config"
        ) {
            QuizConfigScreen(
                navController = navHostController
            )
        }
    }
}