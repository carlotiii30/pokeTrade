package com.example.pokemontrade.ui.screens.inbox

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokemontrade.R
import com.example.pokemontrade.ui.theme.GreenPrimary

@Composable
fun TradeDetailScreen(
    proposer: String = "Antonio R.",
    time: String = "09:10 am",
    offeredCard: Int,
    requestedCard: Int,
    onAccept: () -> Unit = {},
    onReject: () -> Unit = {}
) {
    Column(Modifier.fillMaxSize()) {
        // Cabecera
        Box(
            Modifier
                .fillMaxWidth()
                .background(GreenPrimary)
                .padding(16.dp)
        ) {
            Column {
                Text("Intercambio Pokémon", fontWeight = FontWeight.Bold, color = Color.White)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(Color(0xFFF0F0F0), shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(proposer, fontWeight = FontWeight.Bold)
                        Text("Entrenador Pokémon", fontSize = 12.sp)
                    }
                    Spacer(Modifier.weight(1f))
                    Text(time, color = Color.White, fontSize = 12.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Cuerpo
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Text("Gyarados por Zapdos", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text("¿Trato hecho?", color = Color(0xFF93CDBB), fontWeight = FontWeight.SemiBold)

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(painterResource(id = offeredCard), contentDescription = null, modifier = Modifier.size(140.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = GreenPrimary
                )
                Image(painterResource(id = requestedCard), contentDescription = null, modifier = Modifier.size(140.dp))
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onReject,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5F5F5))
                ) {
                    Text("Rechazar", color = Color.Black)
                }
                Button(
                    onClick = onAccept,
                    colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                ) {
                    Text("Aceptar", color = Color.White)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTradeDetailScreen() {
    TradeDetailScreen(
        offeredCard = R.drawable.cards_header,
        requestedCard = R.drawable.cards_header,
        onAccept = {},
        onReject = {}
    )
}

