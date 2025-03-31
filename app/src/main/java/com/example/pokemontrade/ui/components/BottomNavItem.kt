package com.example.pokemontrade.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem("home", Icons.Default.Home, "Inicio"),
    BottomNavItem("inbox", Icons.Default.QuestionAnswer, "Mensajes"),
    BottomNavItem("profile", Icons.Default.Person, "Perfil")
)
