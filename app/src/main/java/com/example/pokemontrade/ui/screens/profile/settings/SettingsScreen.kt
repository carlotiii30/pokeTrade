package com.example.pokemontrade.ui.screens.profile.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pokemontrade.data.storage.TokenManager
import com.example.pokemontrade.ui.theme.PokemonTradeTheme
import com.example.pokemontrade.ui.theme.RedPrimary
import kotlinx.coroutines.launch


@Composable
fun SettingsScreen(navController: NavController) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Configuración", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        }

        Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp)) {
            SettingsItem("Editar perfil") {
                navController.navigate("edit_profile")
            }
            Divider(color = RedPrimary.copy(alpha = 0.5f))

            SettingsItem("Notificaciones") {
                navController.navigate("notifications")
            }
            Divider(color = RedPrimary.copy(alpha = 0.5f))

            SettingsItem("Cerrar sesión") {
                scope.launch {
                    tokenManager.clearToken()
                    navController.navigate("auth") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                navController.navigate("auth") {
                    popUpTo("home") { inclusive = true }
                }
            },
            modifier = Modifier
                .padding(vertical = 52.dp, horizontal = 32.dp)
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(40.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = Color.White,
                contentColor = Color.Red
            ),
            border = BorderStroke(1.dp, RedPrimary.copy(alpha = 0.5f)),
        ) {
            Text(
                text = "Eliminar cuenta",
                fontWeight = FontWeight.Bold,
                modifier = Modifier
            )
        }
    }
}


@Composable
fun SettingsItem(text: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = text, modifier = Modifier.weight(1f), fontSize = 16.sp)
        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewSettingsScreen() {
    PokemonTradeTheme {
        SettingsScreen(navController = rememberNavController())
    }
}

