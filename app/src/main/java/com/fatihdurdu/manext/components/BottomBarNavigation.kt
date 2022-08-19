package com.fatihdurdu.manext.components

import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.fatihdurdu.manext.navigation.NavBarItems

@Composable
fun BottomBarNavigation(navigationController: NavController) {

    BottomNavigation(backgroundColor = Color.White) {
        val backStackEntry by navigationController.currentBackStackEntryAsState()
        val currentRoute = backStackEntry?.destination?.route

        NavBarItems.BarItems.forEach { navItem ->

            BottomNavigationItem(
                selected = currentRoute == navItem.route,
                onClick = {
                    navigationController.navigate(navItem.route) {

                        launchSingleTop = true
                        restoreState = true
                    }
                },

                icon = {
                    Icon(
                        imageVector = navItem.icon,
                        contentDescription = navItem.label,
                        tint = Color.Gray
                    )
                },
                label = {
                    Text(text = navItem.label, color = Color.Gray)
                },
            )
        }
    }
}