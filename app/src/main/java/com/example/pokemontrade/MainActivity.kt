package com.example.pokemontrade

import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokemontrade.data.models.cards.CardResponse
import com.example.pokemontrade.data.storage.TokenManager
import com.example.pokemontrade.ui.components.BottomNavigationBar
import com.example.pokemontrade.ui.screens.WelcomeScreen
import com.example.pokemontrade.ui.screens.auth.AuthScreen
import com.example.pokemontrade.ui.screens.auth.LoginScreen
import com.example.pokemontrade.ui.screens.auth.RegisterScreen
import com.example.pokemontrade.ui.screens.home.HomeScreen
import com.example.pokemontrade.ui.screens.home.cards.CardDetailHomeScreen
import com.example.pokemontrade.ui.screens.home.trades.OfferTradeScreen
import com.example.pokemontrade.ui.screens.home.trades.SelectCardForTradeScreen
import com.example.pokemontrade.ui.screens.home.users.UserProfileScreen
import com.example.pokemontrade.ui.screens.home.users.reviews.UserReviewsScreen
import com.example.pokemontrade.ui.screens.inbox.InboxScreen
import com.example.pokemontrade.ui.screens.inbox.TradeDetail.TradeDetailScreen
import com.example.pokemontrade.ui.screens.inbox.reviews.LeaveReviewScreen
import com.example.pokemontrade.ui.screens.location.LocationScreen
import com.example.pokemontrade.ui.screens.location.SelectLocationMapScreenWrapper
import com.example.pokemontrade.ui.screens.profile.ProfileScreen
import com.example.pokemontrade.ui.screens.profile.cards.AddCardScreen
import com.example.pokemontrade.ui.screens.profile.cards.CardDetailProfileScreen
import com.example.pokemontrade.ui.screens.profile.reviews.ReviewsScreen
import com.example.pokemontrade.ui.screens.profile.settings.EditProfileScreen
import com.example.pokemontrade.ui.screens.profile.settings.SettingsScreen
import com.example.pokemontrade.ui.theme.PokemonTradeTheme
import com.google.gson.Gson

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PokemonTradeTheme {
                AppNavigation(context = this)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavigation(context: Context) {
    val navController = rememberNavController()
    val tokenManager = remember { TokenManager(context) }
    val token by tokenManager.getTokenFlow().collectAsState(initial = null)

    val isLoggedIn = token != null

    val startDestination = if (isLoggedIn) "home" else "welcome"
    val currentBackStack by navController.currentBackStackEntryAsState()

    val currentRoute = currentBackStack?.destination?.route
    val prefixes =
        listOf("user_profile/", "card/", "detail/", "user_reviews/", "offer_trade", "leave_review")
    val showBottomBar = prefixes.any { prefix -> currentRoute?.startsWith(prefix) == true } ||
            currentRoute in listOf("home", "inbox", "profile", "reviews")

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

            composable("user_reviews/{userId}") { backStackEntry ->
                val userId =
                    backStackEntry.arguments?.getString("userId")?.toInt() ?: return@composable
                UserReviewsScreen(
                    userId = userId,
                    navController = navController,
                    tokenManager = tokenManager
                )
            }

            composable("offer_trade/{cardId}") { backStackEntry ->
                val cardId = backStackEntry.arguments?.getString("cardId")?.toIntOrNull()
                    ?: return@composable
                OfferTradeScreen(cardId = cardId, navController = navController)
            }

            composable("select_card_for_trade") {
                SelectCardForTradeScreen(navController = navController)
            }

            composable("inbox") {
                InboxScreen(
                    onConversationClick = { convo ->
                        navController.navigate("detail/${convo.tradeId}")
                    }
                )
            }

            composable("detail/{tradeId}") { backStackEntry ->
                val tradeId = backStackEntry.arguments?.getString("tradeId")?.toIntOrNull()
                    ?: return@composable
                TradeDetailScreen(
                    tradeId = tradeId,
                    navController = navController,
                    onFinish = { navController.popBackStack() })
            }

            composable("leave_review/{tradeId}") { backStackEntry ->
                val tradeId = backStackEntry.arguments?.getString("tradeId")?.toIntOrNull()
                    ?: return@composable
                LeaveReviewScreen(
                    tradeId = tradeId,
                    navController = navController
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

            composable("reviews") {
                ReviewsScreen(navController = navController, tokenManager = tokenManager)
            }
        }
    }
}
