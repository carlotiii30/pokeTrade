package com.example.pokemontrade.ui.screens.location

import android.content.Context
import androidx.compose.runtime.*
import androidx.navigation.NavController
import com.example.pokemontrade.permissions.RequestLocationPermission
import com.example.pokemontrade.ui.components.GoogleMapScreen
import com.google.android.gms.maps.model.LatLng

@Composable
fun SelectLocationMapScreenWrapper(
    context: Context,
    navController: NavController,
    userName: String
) {
    var hasPermission by remember { mutableStateOf(false) }

    RequestLocationPermission(context = context) {
        hasPermission = true
    }

    if (hasPermission) {
        GoogleMapScreen { coords, address ->
            val prefs = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
            prefs.edit().putString("address", address).apply()

            navController.navigate("location/$userName") {
                popUpTo("select_location_map/$userName") { inclusive = true }
            }
        }
    }
}

