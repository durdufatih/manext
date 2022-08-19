package com.fatihdurdu.manext

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.fatihdurdu.manext.components.BottomBarNavigation
import com.fatihdurdu.manext.components.TopBar
import com.fatihdurdu.manext.navigation.NavigationHost
import com.fatihdurdu.manext.ui.theme.ManextTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
@ExperimentalPermissionsApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ManextTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Scaffold(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(),
                        backgroundColor = Color.White,
                        topBar = { TopBar() }, content = {
                            Column(
                                modifier = Modifier
                                    .padding(20.dp)
                                    .fillMaxWidth()
                            ) {
                                NavigationHost(navController = navController)
                            }
                        }, bottomBar = {
                            BottomBarNavigation(navController)
                        })

                }
            }
        }
    }

}