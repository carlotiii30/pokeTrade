package com.example.pokemontrade.ui.screens.home.cards

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pokemontrade.data.models.cards.CardResponse
import com.example.pokemontrade.data.models.users.UserProfile
import com.example.pokemontrade.data.storage.TokenManager
import com.example.pokemontrade.ui.screens.profile.UsersViewModel
import com.example.pokemontrade.ui.screens.profile.UsersViewModelFactory
import com.example.pokemontrade.ui.theme.BluePrimary
import com.google.gson.Gson

@Composable
fun CardDetailHomeScreen(
    navController: NavController,
    card: CardResponse
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val userViewModel: UsersViewModel = viewModel(factory = UsersViewModelFactory(tokenManager))
    val viewModel: CardDetailHomeViewModel =
        viewModel(factory = CardDetailHomeViewModelFactory(tokenManager))

    var ownerProfile by remember { mutableStateOf<UserProfile?>(null) }
    var profile by remember { mutableStateOf<UserProfile?>(null) }
    var currentUserId by remember { mutableIntStateOf(0) }

    var similarCards by remember { mutableStateOf<List<CardResponse>>(emptyList()) }


    LaunchedEffect(card.id) {
        ownerProfile = viewModel.getUserById(card.userId)
        profile = userViewModel.getUserProfile()
        currentUserId = profile!!.id

        try {
            if (card.userId != currentUserId)
                viewModel.registerCardView(card.id)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        try {
            similarCards = viewModel.getSimilarCardsView(card.id)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BluePrimary)
                    .padding(horizontal = 24.dp, vertical = 24.dp)
            ) {
                Column {
                    Text(
                        text = card.name,
                        fontSize = 20.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = card.type,
                        fontSize = 14.sp,
                        color = Color.White
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
        }

        item {
            Spacer(modifier = Modifier.height(32.dp))
        }

        item {
            AsyncImage(
                model = card.imageUrl,
                contentDescription = "Carta Pokémon",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(horizontal = 42.dp, vertical = 12.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )

            ownerProfile?.let { profile ->
                if (currentUserId != card.userId) {
                    Button(
                        onClick = {
                            navController.navigate("offer_trade/${card.id}")
                        },
                        modifier = Modifier
                            .padding(horizontal = 24.dp, vertical = 8.dp)
                            .fillMaxWidth()
                            .height(56.dp)
                            .clip(RoundedCornerShape(40.dp)),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BluePrimary,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "Ofrecer intercambio",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Button(
                    onClick = {
                        navController.navigate("user_profile/${profile.id}")
                    },
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                        .fillMaxWidth()
                        .height(56.dp)
                        .clip(RoundedCornerShape(40.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = BluePrimary
                    ),
                    border = BorderStroke(1.dp, BluePrimary.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = profile.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedTextField(
                    value = card.description ?: "Sin descripción disponible",
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Descripción") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 8.dp)
                        .height(100.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.LightGray,
                        unfocusedBorderColor = Color.LightGray,
                        focusedContainerColor = Color(0xFFF9FBFA),
                        unfocusedContainerColor = Color(0xFFF9FBFA),
                        focusedLabelColor = Color.Gray,
                        unfocusedLabelColor = Color.Gray
                    )
                )
            }
        }
    }
    if (similarCards.isNotEmpty()) {
        Text(
            text = "Cartas del mismo usuario",
            modifier = Modifier.padding(start = 24.dp, top = 16.dp),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )

        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentPadding = PaddingValues(horizontal = 24.dp)
        ) {
            items(similarCards) { similarCard ->
                val cardJson = Uri.encode(Gson().toJson(similarCard))
                val currentCardJson = Uri.encode(Gson().toJson(card))

                Column(
                    modifier = Modifier
                        .padding(end = 12.dp)
                        .width(120.dp)
                        .clickable {
                            navController.navigate("card/$cardJson") {
                                popUpTo("card/$currentCardJson") { inclusive = true }
                            }
                        }
                ) {
                    AsyncImage(
                        model = similarCard.imageUrl,
                        contentDescription = similarCard.name,
                        modifier = Modifier
                            .height(160.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}

