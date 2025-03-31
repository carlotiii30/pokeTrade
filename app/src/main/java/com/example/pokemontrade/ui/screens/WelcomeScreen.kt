package com.example.pokemontrade.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.pokemontrade.ui.components.SlideToStartButton
import com.example.pokemontrade.ui.theme.BluePrimary

@Composable
fun WelcomeScreen(onSlideComplete: () -> Unit) {
    Surface(
        color = BluePrimary,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 48.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Busca una carta,\nPropón un intercambio,\nHaz match y completa tu Pokédex\n\n¡Que empiece la caza!",
                color = Color.White
            )

            SlideToStartButton(
                text = "Comencemos",
                onSlideComplete = onSlideComplete
            )
        }
    }
}
