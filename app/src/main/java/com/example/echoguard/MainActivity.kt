package com.example.echoguard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.echoguard.ui.navigation.EchoGuardNavGraph
import com.example.echoguard.ui.components.BottomNavigationBar
import com.example.echoguard.ui.theme.EchoGuardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Tactical Dark Mode enabled by default for battery and visibility
            var darkTheme by remember { mutableStateOf(true) }
            val navController = rememberNavController()

            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route

            // Navigation bar visibility logic
            val showBottomBar = currentRoute in listOf("dashboard", "history", "settings")

            EchoGuardTheme(darkTheme = darkTheme) {
                Scaffold(
                    bottomBar = {
                        if (showBottomBar) {
                            BottomNavigationBar(navController)
                        }
                    }
                ) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        EchoGuardNavGraph(
                            navController = navController,
                            darkTheme = darkTheme,
                            onThemeChange = { darkTheme = it }
                        )
                    }
                }
            }
        }
    }
}