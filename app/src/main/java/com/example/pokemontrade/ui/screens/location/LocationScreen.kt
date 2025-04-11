package com.example.pokemontrade.ui.screens.location

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pokemontrade.ui.theme.BluePrimary
import com.example.pokemontrade.ui.theme.DisabledBlue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationScreen(
    context: Context,
    navController: NavController,
    userName: String
) {
    val prefs = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
    val savedAddress = prefs.getString("address", "") ?: ""
    val address = remember { mutableStateOf(savedAddress) }

    val isFormValid = address.value.isNotBlank()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 64.dp)
    ) {
        Text(
            text = "¡Hola, $userName!",
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 42.sp,
                fontWeight = FontWeight.Black,
                lineHeight = 50.sp
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Selecciona la localización desde la que usarás Poketrade",
            fontSize = 22.sp,
            color = BluePrimary,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Dirección
        OutlinedTextField(
            value = address.value,
            onValueChange = {},
            enabled = false,
            readOnly = true,
            label = { Text("Dirección", color = BluePrimary) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    navController.navigate("select_location_map/$userName")
                },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = DisabledBlue,
                unfocusedBorderColor = DisabledBlue,
                disabledBorderColor = DisabledBlue,
                disabledLabelColor = BluePrimary
            )
        )

        Spacer(modifier = Modifier.height(470.dp))

        // Botón de continuar
        Button(
            onClick = {
                prefs.edit()
                    .putBoolean("isLoggedIn", true)
                    .putString("name", userName)
                    .putString("address", address.value)
                    .apply()

                navController.navigate("home") {
                    popUpTo("location/$userName") { inclusive = true }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(40.dp)),
            shape = CircleShape,
            enabled = isFormValid,
            colors = ButtonDefaults.buttonColors(
                containerColor = BluePrimary,
                contentColor = Color.White,
                disabledContainerColor = DisabledBlue,
                disabledContentColor = Color.White
            )
        ) {
            Text(
                text = "Crear cuenta",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocationScreenPreview() {
    val context = LocalContext.current
    val navController = rememberNavController()

    com.example.pokemontrade.ui.theme.PokemonTradeTheme {
        LocationScreen(
            context = context,
            navController = navController,
            userName = "Ash"
        )
    }
}
