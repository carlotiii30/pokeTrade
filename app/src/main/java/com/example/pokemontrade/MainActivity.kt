package com.example.pokemontrade

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.core.content.edit
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokemontrade.ui.components.BottomNavigationBar
import com.example.pokemontrade.ui.screens.WelcomeScreen
import com.example.pokemontrade.ui.screens.auth.AuthScreen
import com.example.pokemontrade.ui.screens.auth.LoginScreen
import com.example.pokemontrade.ui.screens.auth.RegisterScreen
import com.example.pokemontrade.ui.screens.home.CardDetailHomeScreen
import com.example.pokemontrade.ui.screens.home.HomeScreen
import com.example.pokemontrade.ui.screens.inbox.InboxScreen
import com.example.pokemontrade.ui.screens.inbox.TradeDetailScreen
import com.example.pokemontrade.ui.screens.location.LocationScreen
import com.example.pokemontrade.ui.screens.location.SelectLocationMapScreenWrapper
import com.example.pokemontrade.ui.screens.profile.CardDetailProfileScreen
import com.example.pokemontrade.ui.screens.profile.settings.EditProfileScreen
import com.example.pokemontrade.ui.screens.profile.ProfileScreen
import com.example.pokemontrade.ui.screens.profile.SettingsScreen
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
    val userName = prefs.getString("name", "Entrenador") ?: "Entrenador"

    val startDestination = if (isLoggedIn.value) "home" else "welcome"

    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route
    val showBottomBar = currentRoute?.startsWith("detail/") == true ||
            currentRoute in listOf(
        "home",
        "inbox",
        "profile",
    )


    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigationBar(
                    navController = navController,
                    currentRoute = currentRoute ?: ""
                )
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
                SelectLocationMapScreenWrapper(
                    context = context,
                    navController = navController,
                    userName = name
                )
            }

            composable("login") {
                LoginScreen(
                    onBackClick = { navController.popBackStack() },
                    onLoginClick = { name, _ ->
                        prefs.edit {
                            putBoolean("isLoggedIn", true)
                            putString("name", name)
                        }
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

            composable("card/{cardId}") { backStackEntry ->
                val cardId = backStackEntry.arguments?.getString("cardId") ?: "Carta"
                CardDetailHomeScreen(
                    navController = navController,
                    cardName = cardId.split("-").firstOrNull()?.trim() ?: "Carta",
                    cardType = "Básico"
                )
            }

            composable("inbox") {
                InboxScreen(
                    userName = userName,
                    onConversationClick = { convo ->
                        navController.navigate("detail/${convo.name}/${convo.time}")
                    }
                )
            }

            composable(
                "detail/{name}/{time}",
                arguments = listOf(
                    navArgument("name") { type = NavType.StringType },
                    navArgument("time") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name") ?: ""
                val time = backStackEntry.arguments?.getString("time") ?: ""

                TradeDetailScreen(
                    proposer = name,
                    time = time,
                    offeredCard = R.drawable.cards_header,
                    requestedCard = R.drawable.cards_header,
                    onAccept = { navController.popBackStack() },
                    onReject = { navController.popBackStack() }
                )
            }

            composable("profile") {
                ProfileScreen(
                    userName = userName,
                    navController = navController
                )
            }

            composable("profile_card/{cardId}") { backStackEntry ->
                val cardId = backStackEntry.arguments?.getString("cardId") ?: "Carta"
                CardDetailProfileScreen(
                    navController = navController,
                    cardName = cardId.split("-").firstOrNull()?.trim() ?: "Carta",
                    cardType = "Básico"
                )
            }

            composable("settings") {
                SettingsScreen(navController = navController)
            }

            composable("edit_profile") {
                EditProfileScreen(
                    navController = navController
                )
            }

        }
    }
}