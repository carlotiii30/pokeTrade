package com.example.pokemontrade.ui.screens

import android.content.Context
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pokemontrade.ui.theme.BluePrimary

@Composable
fun LocationScreen(
    context: Context,
    navController: NavController,
    userName: String
) {
    var country by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }

    val isFormValid = country.isNotBlank() && address.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        Text(
            text = "¡Hola, $userName!",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 28.sp,
                fontWeight = FontWeight.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Selecciona la localización desde la que usarás Poketrade",
            fontSize = 18.sp,
            color = BluePrimary
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = country,
            onValueChange = { country = it },
            label = { Text("País") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Dirección") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                val prefs = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
                prefs.edit()
                    .putBoolean("isLoggedIn", true)
                    .putString("name", userName)
                    .putString("country", country)
                    .putString("address", address)
                    .apply()

                navController.navigate("home") {
                    popUpTo("location/$userName") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isFormValid) BluePrimary else Color(0xFFB0C4E5),
                contentColor = Color.White
            ),
            enabled = isFormValid
        ) {
            Text("Crear cuenta")
        }
    }
}
