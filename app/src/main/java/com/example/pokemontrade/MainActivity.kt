package com.example.pokemontrade

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.pokemontrade.data.models.cards.CardResponse
import com.example.pokemontrade.data.storage.TokenManager
import com.example.pokemontrade.ui.components.BottomNavigationBar
import com.example.pokemontrade.ui.screens.*
import com.example.pokemontrade.ui.screens.auth.*
import com.example.pokemontrade.ui.screens.home.*
import com.example.pokemontrade.ui.screens.home.users.UserProfileScreen
import com.example.pokemontrade.ui.screens.inbox.*
import com.example.pokemontrade.ui.screens.location.*
import com.example.pokemontrade.ui.screens.profile.*
import com.example.pokemontrade.ui.screens.profile.cards.AddCardScreen
import com.example.pokemontrade.ui.screens.profile.settings.EditProfileScreen
import com.example.pokemontrade.ui.theme.PokemonTradeTheme
import com.google.gson.Gson

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
    val tokenManager = remember { TokenManager(context) }
    val token by tokenManager.getTokenFlow().collectAsState(initial = null)
    val userProfile by tokenManager.getUserProfileFlow().collectAsState(initial = null)

    val isLoggedIn = token != null
    val userName = userProfile?.name ?: "Entrenador"

    val startDestination = if (isLoggedIn) "home" else "welcome"
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route
    val showBottomBar = currentRoute?.startsWith("detail/") == true ||
            currentRoute in listOf("home", "inbox", "profile")

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
                WelcomeScreen {
                    navController.navigate("auth") {
                        popUpTo("welcome") { inclusive = true }
                    }
                }
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
                    onRegisterClick = { name, _, _, _ ->
                        navController.navigate("location/$name")
                    }
                )
            }

            composable("location/{name}") { backStackEntry ->
                val name = backStackEntry.arguments?.getString("name") ?: ""
                LocationScreen(context, navController, userName = name)
            }

            composable("select_location_map/{userName}") { backStackEntry ->
                val name = backStackEntry.arguments?.getString("userName") ?: ""
                SelectLocationMapScreenWrapper(context, navController, userName = name)
            }

            composable("login") {
                LoginScreen(
                    onBackClick = { navController.popBackStack() },
                    onLoginSuccess = {
                        navController.navigate("home") {
                            popUpTo("auth") { inclusive = true }
                        }
                    },
                    onForgotPasswordClick = {}
                )
            }

            composable("home") {
                HomeScreen(context = context) { card ->
                    val cardJson = Uri.encode(Gson().toJson(card))
                    navController.navigate("card/$cardJson")
                }
            }

            composable(
                "card/{cardJson}",
                arguments = listOf(navArgument("cardJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val cardJson = backStackEntry.arguments?.getString("cardJson") ?: return@composable
                val card = Gson().fromJson(cardJson, CardResponse::class.java)
                CardDetailHomeScreen(navController = navController, card = card)
            }

            composable(
                "user_profile/{userId}",
                arguments = listOf(navArgument("userId") { type = NavType.StringType })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getString("userId") ?: return@composable
                UserProfileScreen(navController = navController, userId = userId)
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
                ProfileScreen(navController = navController)
            }

            composable(
                "profile_card/{cardJson}",
                arguments = listOf(navArgument("cardJson") { type = NavType.StringType })
            ) { backStackEntry ->
                val cardJson = backStackEntry.arguments?.getString("cardJson") ?: return@composable
                val card = Gson().fromJson(cardJson, CardResponse::class.java)
                CardDetailProfileScreen(navController = navController, card = card)
            }

            composable("add_card") {
                AddCardScreen(navController = navController)
            }

            composable("settings") {
                SettingsScreen(navController = navController)
            }

            composable("edit_profile") {
                EditProfileScreen(navController = navController)
            }
        }
    }
}
