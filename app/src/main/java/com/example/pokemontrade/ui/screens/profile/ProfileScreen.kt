package com.example.pokemontrade.ui.screens.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.pokemontrade.R
import com.example.pokemontrade.ui.theme.RedPrimary

@Composable
fun ProfileScreen(
    userName: String = "Carlota de la V.",
    averageRating: Float = 4.0f,
    ratingCount: Int = 3,
    navController: NavController
) {
    Column(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(RedPrimary)
                .padding(bottom = 40.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.semi_pokeball),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.TopCenter),
                contentScale = ContentScale.FillWidth
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(top = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF0F0F0))
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = userName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Entrenadora Pokémon",
                    fontSize = 16.sp,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { i ->
                        val icon = when {
                            averageRating >= i + 1 -> Icons.Filled.Star
                            averageRating > i -> Icons.AutoMirrored.Filled.StarHalf
                            else -> Icons.Filled.StarBorder
                        }
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "${"%.1f".format(averageRating)} ($ratingCount)",
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }
            }

            IconButton(
                onClick = {
                    navController.navigate("settings")
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(horizontal = 16.dp)
            ) {
                Icon(
                    Icons.Default.Settings,
                    contentDescription = "Ajustes",
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Galería",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, bottom = 16.dp)
        )

        val cards = listOf(
            R.drawable.cards_header,
            R.drawable.cards_header,
            R.drawable.cards_header,
            R.drawable.cards_header,
            R.drawable.cards_header,
            R.drawable.cards_header,
            R.drawable.cards_header,
            R.drawable.cards_header
        )

        val galleryItems = cards + -1

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            items(galleryItems) { card ->
                Box(
                    modifier = Modifier
                        .padding(6.dp)
                        .height(120.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    if (card == -1) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Añadir carta",
                            tint = Color.DarkGray,
                            modifier = Modifier.size(32.dp)
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    navController.navigate("profile_card/Togedemaru-Básico")
                                }
                        ) {
                            Image(
                                painter = painterResource(card),
                                contentDescription = "Carta",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }

                    }
                }
            }
        }
    }
}


@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PreviewProfileScreen() {
    val navController = rememberNavController()

    ProfileScreen(
        averageRating = 3.5f,
        ratingCount = 3,
        navController = navController
    )
}



