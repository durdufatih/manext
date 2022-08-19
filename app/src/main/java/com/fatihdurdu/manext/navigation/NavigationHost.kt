package com.fatihdurdu.manext.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.fatihdurdu.manext.screens.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@Composable
@ExperimentalPermissionsApi
fun NavigationHost(navController: NavHostController) {

    NavHost(navController = navController, startDestination = Screens.HomeScreen.name) {
        composable(Screens.HomeScreen.name) {
            HomeScreen(navController)
        }
        composable(Screens.RandomScreen.name)
        {
            RandomScreen(navController)
        }
        composable(Screens.SearchScreen.name)
        {
            SearchScreen(navController)
        }
        composable(Screens.SettingsScreen.name)
        {
            SettingsScreen(navController)
        }

    }
}