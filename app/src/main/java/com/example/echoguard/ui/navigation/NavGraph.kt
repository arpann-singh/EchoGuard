package com.example.echoguard.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
// Corrected imports to match your actual folders (No 'screens' folder).
import com.example.echoguard.ui.dashboard.DashboardScreen
import com.example.echoguard.ui.history.HistoryScreen
import com.example.echoguard.ui.settings.SettingsScreen
import com.example.echoguard.ui.login.LoginScreen
import com.example.echoguard.ui.register.RegisterScreen
import com.example.echoguard.ui.splash.SplashScreen

@Composable
fun EchoGuardNavGraph(
    navController: NavHostController,
    darkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = "splash"
    ) {
        composable("splash") { SplashScreen(navController) }
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }
        composable("dashboard") { DashboardScreen(navController) }
        composable("history") { HistoryScreen() }
        composable("settings") {
            SettingsScreen(darkTheme = darkTheme, onThemeChange = onThemeChange)
        }
    }
}