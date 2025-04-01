package com.example.pokemontrade.ui.screens.location

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pokemontrade.ui.components.GoogleMapScreen
import com.example.pokemontrade.ui.theme.BluePrimary
import com.example.pokemontrade.ui.theme.DisabledBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectLocationMapScreen(
    context: Context,
    navController: NavController,
    userName: String
) {
    var address by remember { mutableStateOf("") }

    val isAddressValid = address.isNotBlank()

    Column(modifier = Modifier.fillMaxSize()) {

        // Top App Bar
        TopAppBar(
            title = { Text("Cambiar tu ubicación") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
            }
        )

        // Input manual de dirección
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Escribe la dirección", color = BluePrimary) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DisabledBlue,
                unfocusedBorderColor = DisabledBlue,
                cursorColor = BluePrimary,
                focusedLabelColor = BluePrimary,
                unfocusedLabelColor = DisabledBlue
            )
        )

        // Botón para guardar la dirección escrita manualmente
        Button(
            onClick = {
                val prefs = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
                prefs.edit().putString("address", address).apply()
                navController.popBackStack()
            },
            enabled = isAddressValid,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .height(52.dp)
                .clip(RoundedCornerShape(40.dp)),
            shape = RoundedCornerShape(40.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isAddressValid) BluePrimary else DisabledBlue,
                contentColor = Color.White
            )
        ) {
            Text("Usar esta dirección")
        }

        // Mapa interactivo
        GoogleMapScreen(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f), // Ocupa el resto de la pantalla
            onLocationSelected = { coords, addr ->
                address = addr
                val prefs = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
                prefs.edit().putString("address", addr).apply()
                navController.popBackStack()
            }
        )
    }
}
