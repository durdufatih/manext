package com.fatihdurdu.manext.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import com.fatihdurdu.manext.screens.Screens

object NavBarItems {
    val BarItems = listOf(
        BarItem(
            label = "Home",
            icon = Icons.Filled.Home,
            route = Screens.HomeScreen.name
        ),
        BarItem(
            label = "Random",
            icon = Icons.Filled.Favorite,
            route = Screens.RandomScreen.name
        ),
        BarItem(
            label = "Search",
            icon = Icons.Filled.Search,
            route = Screens.SearchScreen.name
        ),
        BarItem(
            label = "Settings",
            icon = Icons.Filled.Settings,
            route = Screens.SettingsScreen.name
        )
    )
}