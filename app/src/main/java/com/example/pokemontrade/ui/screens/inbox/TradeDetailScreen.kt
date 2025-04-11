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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokemontrade.R
import com.example.pokemontrade.ui.theme.GreenPrimary
import com.example.pokemontrade.ui.theme.LightGray
import com.example.pokemontrade.ui.theme.LightGreen

@Composable
fun TradeDetailScreen(
    proposer: String = "Antonio R.",
    time: String = "09:10 am",
    offeredCard: Int,
    requestedCard: Int,
    isProposedByUser: Boolean = true,
    onAccept: () -> Unit = {},
    onReject: () -> Unit = {}
) {
    Column(Modifier.fillMaxSize()) {

        // Cabecera
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(GreenPrimary)
                .padding(32.dp)
        ) {
            Column {
                Text(
                    text = "Intercambio Pokémon",
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    fontSize = 22.sp
                )

                Spacer(modifier = Modifier.height(28.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(LightGray, shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = proposer,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Entrenador",
                            color = LightGreen,
                            fontSize = 14.sp
                        )
                    }

                    Text(
                        text = time,
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.Top)
                            .padding(vertical = 12.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Cuerpo
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Gyarados por Zapdos",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                lineHeight = 28.sp
            )

            Text(
                text = "¿Trato hecho?",
                color = GreenPrimary,
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                lineHeight = 28.sp
            )

            Spacer(modifier = Modifier.height(38.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 60.dp)
                ) {
                    Image(
                        painter = painterResource(id = offeredCard),
                        contentDescription = null,
                        modifier = Modifier
                            .height(160.dp)
                            .width(120.dp)
                            .align(Alignment.CenterStart)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(horizontal = 48.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.flecha_arriba),
                            contentDescription = "Flecha",
                            modifier = Modifier
                                .size(70.dp)
                                .align(Alignment.BottomEnd)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 60.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(horizontal = 48.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.flecha_abajo),
                            contentDescription = "Flecha",
                            modifier = Modifier
                                .size(70.dp)
                                .align(Alignment.TopStart)
                        )
                    }
                    Image(
                        painter = painterResource(id = requestedCard),
                        contentDescription = null,
                        modifier = Modifier
                            .height(160.dp)
                            .width(120.dp)
                            .align(Alignment.CenterEnd)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            if (isProposedByUser) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color(0xFFEEECEC))
                        .padding(vertical = 28.dp, horizontal = 72.dp)
                ) {
                    Text(
                        text = "Esperando respuesta...",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black
                    )
                }
            } else {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = onReject,
                        modifier = Modifier
                            .width(150.dp)
                            .height(60.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEECEC))
                    ) {
                        Text(
                            text = "Rechazar",
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Button(
                        onClick = onAccept,
                        modifier = Modifier
                            .width(150.dp)
                            .height(60.dp),
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                    ) {
                        Text(
                            text = "Aceptar",
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
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
