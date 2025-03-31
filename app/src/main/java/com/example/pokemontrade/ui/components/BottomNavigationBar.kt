package com.example.pokemontrade.ui.components

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.example.pokemontrade.ui.theme.BluePrimary
import com.example.pokemontrade.ui.theme.GreenPrimary
import com.example.pokemontrade.ui.theme.RedPrimary

@Composable
fun BottomNavigationBar(navController: NavController, currentRoute: String) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            val selected = currentRoute == item.route

            val selectedColor = when (item.route) {
                "home" -> BluePrimary
                "inbox" -> GreenPrimary
                "profile" -> RedPrimary
                else -> MaterialTheme.colorScheme.primary
            }

            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(item.route) {
                            popUpTo("home") { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (selected) selectedColor else NavigationBarItemDefaults.colors().unselectedIconColor
                    )
                },
                label = {
                    Text(item.label)
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = selectedColor,
                    unselectedIconColor = NavigationBarItemDefaults.colors().unselectedIconColor,
                    selectedTextColor = selectedColor,
                    unselectedTextColor = NavigationBarItemDefaults.colors().unselectedTextColor
                )
            )
        }
    }
}
