package com.example.composelogin

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeLoginTheme {
                App()
            }
        }
    }
}

/**
 * Login and Dashboard. After logging in, the login screen is removed from the back stack,
 * so Back on the dashboard closes the app instead of returning to the form.
 */
@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "login") {
        composable("login") {
            LoginScreen(onLoggedIn = { username ->
                navController.navigate("dashboard/" + Uri.encode(username)) {
                    popUpTo("login") { inclusive = true }
                }
            })
        }
        composable("dashboard/{username}") { entry ->
            DashboardScreen(
                username = entry.arguments?.getString("username").orEmpty(),
                onLogOut = {
                    navController.navigate("login") {
                        popUpTo("dashboard/{username}") { inclusive = true }
                    }
                },
            )
        }
    }
}
