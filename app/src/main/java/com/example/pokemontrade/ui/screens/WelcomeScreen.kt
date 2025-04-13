package com.example.pokemontrade.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokemontrade.ui.components.SlideToStartButton
import com.example.pokemontrade.ui.theme.BluePrimary
import com.example.pokemontrade.ui.theme.PokemonTradeTheme

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
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Busca una carta,\nPropón un intercambio,\nHaz match y Completa tu Pokédex\n\n¡Qué empiece la caza!",
                    color = Color.White,
                    fontSize = 50.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 60.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Left
                )
            }

            SlideToStartButton(
                text = "Comencemos",
                onSlideComplete = onSlideComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .height(64.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {
    PokemonTradeTheme {
        WelcomeScreen(onSlideComplete = {})
    }
}
