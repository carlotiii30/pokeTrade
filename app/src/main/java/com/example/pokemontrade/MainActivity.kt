package com.example.pokemontrade

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.edit
import androidx.navigation.compose.*
import com.example.pokemontrade.ui.components.BottomNavigationBar
import com.example.pokemontrade.ui.screens.*
import com.example.pokemontrade.ui.screens.auth.AuthScreen
import com.example.pokemontrade.ui.screens.auth.LoginScreen
import com.example.pokemontrade.ui.screens.auth.RegisterScreen
import com.example.pokemontrade.ui.screens.home.CardDetailScreen
import com.example.pokemontrade.ui.screens.home.HomeScreen
import com.example.pokemontrade.ui.screens.location.LocationScreen
import com.example.pokemontrade.ui.screens.location.SelectLocationMapScreen
import com.example.pokemontrade.ui.theme.PokemonTradeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonTradeTheme {
                AppNavigation(context = this)
            }
        }
    }
}

@Composable
fun AppNavigation(context: Context) {
    val navController = rememberNavController()

     val prefs = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
     val isLoggedIn = remember { mutableStateOf(prefs.getBoolean("isLoggedIn", false)) }

    // val startDestination = if (isLoggedIn.value) "home" else "welcome"
    val startDestination = "welcome"

    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route
    val showBottomBar = currentRoute in listOf("home", "inbox", "profile", "card/{cardId}")

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(navController = navController, currentRoute = currentRoute ?: "")
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("welcome") {
                WelcomeScreen(onSlideComplete = {
                    navController.navigate("auth") {
                        popUpTo("welcome") { inclusive = true }
                    }
                })
            }

            composable("auth") {
                AuthScreen(
                    onRegisterClick = { navController.navigate("register") },
                    onLoginClick = { navController.navigate("login") }
                )
            }

            composable("register") {
                RegisterScreen(
                    onBackClick = { navController.popBackStack() },
                    onRegisterClick = { name, _, _ ->
                        navController.navigate("location/$name")
                    }
                )
            }

            composable("location/{name}") { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name") ?: ""
                LocationScreen(
                    context = context,
                    navController = navController,
                    userName = name
                )
            }

            composable("select_location_map/{userName}") { backStackEntry ->
                val name = backStackEntry.arguments?.getString("userName") ?: ""
                SelectLocationMapScreen(
                    context = context,
                    navController = navController,
                    userName = name
                )
            }

            composable("login") {
                LoginScreen(
                    onBackClick = { navController.popBackStack() },
                    onLoginClick = { _, _ ->
                        prefs.edit { putBoolean("isLoggedIn", true) }
                        navController.navigate("home") {
                            popUpTo("auth") { inclusive = true }
                        }
                    },
                    onForgotPasswordClick = { }
                )
            }

            composable("home") {
                HomeScreen(
                    context = context,
                    onCardClick = { cardId ->
                        navController.navigate("card/$cardId")
                    }
                )
            }

            composable("inbox") {
                InboxScreen()
            }

            composable("profile") {
                ProfileScreen()
            }

            composable("card/{cardId}") { backStackEntry ->
                val cardId = backStackEntry.arguments?.getString("cardId") ?: "Carta"
                CardDetailScreen(
                    navController = navController,
                    cardName = cardId.split("-").firstOrNull()?.trim() ?: "Carta",
                    cardType = "Básico"
                )
            }
        }
    }
}

@Composable
fun InboxScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Bandeja de entrada")
    }
}

@Composable
fun ProfileScreen() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Perfil")
    }
}
