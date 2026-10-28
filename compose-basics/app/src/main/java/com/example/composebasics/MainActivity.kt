package com.example.composebasics

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/** The only activity. Compose draws everything inside setContent. */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeBasicsTheme {
                App()
            }
        }
    }
}

/** Two screens. The NavHost shows one at a time; "greeting/{name}" carries the name across. */
@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(onSayHello = { name -> navController.navigate("greeting/" + Uri.encode(name)) })
        }
        composable("greeting/{name}") { entry ->
            GreetingScreen(
                name = entry.arguments?.getString("name").orEmpty(),
                onBack = { navController.popBackStack() },
            )
        }
    }
}
