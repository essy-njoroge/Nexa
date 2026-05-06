package com.essy.nexa

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.essy.nexa.navigation.NexaNavGraph
import com.essy.nexa.navigation.bottomNavRoutes
import com.essy.nexa.ui.screens.NexaBottomNav
import com.essy.nexa.ui.theme.NexaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NexaTheme {
                val navController = rememberNavController()
                val navBackStack by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStack?.destination?.route
                val showBottomNav = currentRoute in bottomNavRoutes

                // Use Box + Column instead of Scaffold to avoid padding conflicts
                Box(modifier = Modifier.fillMaxSize()) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Nav graph takes all space minus bottom nav
                        Box(modifier = Modifier.weight(1f)) {
                            NexaNavGraph(navController = navController)
                        }
                        // Bottom nav sits at the bottom, always visible on main screens
                        if (showBottomNav) {
                            NexaBottomNav(
                                currentRoute = currentRoute ?: "home",
                                onNavItemClick = { route ->
                                    navController.navigate(route) {
                                        popUpTo("home") { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
