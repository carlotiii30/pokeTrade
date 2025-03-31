package com.example.pokemontrade.ui.screens

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokemontrade.R
import com.example.pokemontrade.ui.theme.BluePrimary


@Composable
fun HomeScreen(
    context: Context,
    onCardClick: (String) -> Unit = {}
) {
    val prefs = context.getSharedPreferences("UserSession", Context.MODE_PRIVATE)
    val userName = prefs.getString("name", "Entrenador")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        Column {
            Text(
                text = "Hola,",
                fontSize = 40.sp,
                fontWeight = FontWeight.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = userName ?: "Entrenador",
                fontSize = 40.sp,
                fontWeight = FontWeight.Black
            )
        }


        Spacer(modifier = Modifier.height(24.dp))

        // Barra de búsqueda
        var searchText by remember { mutableStateOf("") }

        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = {
                Text(
                    text = "Buscar",
                    fontWeight = FontWeight.Black
                )
            },
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.buscar),
                    contentDescription = "Buscar",
                    modifier = Modifier.size(18.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            },
            trailingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_pokeball),
                    contentDescription = "Pokeball",
                    modifier = Modifier.size(56.dp),
                    colorFilter = ColorFilter.tint(Color.White)
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = BluePrimary,
                unfocusedContainerColor = BluePrimary,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedLeadingIconColor = Color.White,
                unfocusedLeadingIconColor = Color.White,
                focusedTrailingIconColor = Color.White,
                unfocusedTrailingIconColor = Color.White,
                focusedPlaceholderColor = Color.White.copy(alpha = 0.7f),
                unfocusedPlaceholderColor = Color.White.copy(alpha = 0.7f)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = CircleShape,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Categorías
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(Color(0xFFF2F2F2), shape = RoundedCornerShape(24.dp)),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = "Categorías",
                fontSize = 16.sp,
                color = Color.Black,
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 👇 Aquí pasamos onCardClick
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(0.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item {
                Section(title = "Nuevas cartas registradas", onCardClick = onCardClick)
            }
            item {
                Section(title = "Intercambio rápido", onCardClick = onCardClick)
            }
            item {
                Section(title = "Set más reciente", onCardClick = onCardClick)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun Section(title: String, onCardClick: (String) -> Unit = {}) {
    Column {
        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            items(4) { index ->
                val cardId = "$title-$index"
                Card(
                    onClick = { onCardClick(cardId) },
                    modifier = Modifier
                        .size(width = 100.dp, height = 140.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.cards_header),
                        contentDescription = "Carta $index",
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    HomeScreen(
        context = LocalContext.current,
        onCardClick = {}
    )
}
