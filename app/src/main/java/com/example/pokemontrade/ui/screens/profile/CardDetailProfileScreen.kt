package com.example.pokemontrade.ui.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.pokemontrade.R
import com.example.pokemontrade.ui.theme.RedPrimary

@Composable
fun CardDetailProfileScreen(
    navController: NavController,
    cardName: String = "Togedemaru",
    cardType: String = "Básico",
    cardImageRes: Int = R.drawable.cards_header,
    onDeleteClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(RedPrimary)
                .padding(horizontal = 24.dp, vertical = 24.dp)
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
        ) {
            Column {
                Text(
                    text = cardName,
                    fontSize = 20.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = cardType,
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }

            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cerrar",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Imagen de la carta
        Image(
            painter = painterResource(id = cardImageRes),
            contentDescription = "Carta Pokémon",
            modifier = Modifier
                .fillMaxWidth()
                .height(460.dp)
                .padding(horizontal = 42.dp, vertical = 12.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.weight(1f))

        // Botón de eliminar
        Button(
            onClick = onDeleteClick,
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(40.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = RedPrimary,
                disabledContainerColor = Color.Transparent
            ),
            border = BorderStroke(1.dp, RedPrimary.copy(alpha = 0.5f)),
        ) {
            Text(
                text = "Eliminar carta",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
