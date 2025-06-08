package com.example.pokemontrade.ui.screens.home

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.pokemontrade.R
import com.example.pokemontrade.data.api.RetrofitInstance
import com.example.pokemontrade.data.models.cards.CardResponse
import com.example.pokemontrade.data.models.cards.CardType
import com.example.pokemontrade.data.models.cards.toBackendValue
import com.example.pokemontrade.ui.screens.profile.UsersViewModel
import com.example.pokemontrade.ui.screens.profile.UsersViewModelFactory
import com.example.pokemontrade.ui.theme.BluePrimary
import com.example.pokemontrade.ui.theme.LightGray

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    context: Context,
    onCardClick: (CardResponse) -> Unit = {}
) {
    val tokenManager = remember { com.example.pokemontrade.data.storage.TokenManager(context) }
    val userViewModel: UsersViewModel = viewModel(factory = UsersViewModelFactory(tokenManager))
    val homeViewModel: HomeViewModel =
        viewModel(factory = HomeViewModelFactory(RetrofitInstance.getAuthenticatedApi(tokenManager)))

    val cards by homeViewModel.cards.collectAsState()
    var isLoading by remember { mutableStateOf(true) }

    val recommendedCards by homeViewModel.recommendedCards.collectAsState()
    val mostPopularCards by homeViewModel.mostPopularCards.collectAsState()

    val recommendedLoaded by homeViewModel.recommendedLoaded.collectAsState()
    val popularLoaded by homeViewModel.popularLoaded.collectAsState()

    var searchText by remember { mutableStateOf("") }
    var profile by remember {
        mutableStateOf<com.example.pokemontrade.data.models.users.UserProfile?>(
            null
        )
    }

    LaunchedEffect(Unit) {
        try {
            homeViewModel.loadAllCards()
            homeViewModel.loadRecommendedCards()
            homeViewModel.loadMostPopularCards()
            profile = userViewModel.getUserProfile()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    if (isLoading || profile == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val userName = profile?.name ?: "Entrenador"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        Column {
            Text("Hola,", fontSize = 40.sp, fontWeight = FontWeight.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Text(userName, fontSize = 40.sp, fontWeight = FontWeight.Black)
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = {
                Text(
                    text = "Buscar",
                    fontWeight = FontWeight.Black,
                    color = LightGray
                )
            },
            leadingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.buscar),
                    contentDescription = "Buscar",
                    tint = Color.White,
                    modifier = Modifier.size(18.dp)
                )
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_pokeball),
                    contentDescription = "Pokeball",
                    tint = Color.White,
                    modifier = Modifier.size(56.dp)
                )
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = BluePrimary,
                unfocusedContainerColor = BluePrimary,
                cursorColor = Color.White,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = CircleShape,
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        var expanded by remember { mutableStateOf(false) }
        val types = CardType.entries
        val allTypes = listOf(null) + types

        var selectedType by remember { mutableStateOf<CardType?>(null) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier.fillMaxWidth()
        ) {
            TextField(
                value = selectedType?.name?.lowercase()?.replaceFirstChar { it.uppercase() }
                    ?: "Todos",
                onValueChange = {},
                readOnly = true,
                label = { Text("Tipos") },
                trailingIcon = {
                    Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFF2F2F2),
                    unfocusedContainerColor = Color(0xFFF2F2F2),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                allTypes.forEach { type ->
                    val typeName =
                        type?.name?.lowercase()?.replaceFirstChar { it.uppercase() } ?: "Todos"
                    DropdownMenuItem(
                        text = { Text(typeName) },
                        onClick = {
                            selectedType = type
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val filteredCards = cards.filter { card ->
            val matchesType = selectedType?.toBackendValue()?.let {
                card.type.equals(it, ignoreCase = true)
            } ?: true
            val matchesSearch = card.name.contains(searchText, ignoreCase = true)
            matchesType && matchesSearch
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if (recommendedLoaded && recommendedCards.isNotEmpty()) {
                item {
                    Text(
                        text = "Para ti",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 220.dp)
                    ) {
                        items(recommendedCards) { card ->
                            Card(
                                onClick = { onCardClick(card) },
                                modifier = Modifier
                                    .height(140.dp)
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                AsyncImage(
                                    model = card.imageUrl,
                                    contentDescription = "Carta recomendada de ${card.name}",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }

            if (popularLoaded && mostPopularCards.isNotEmpty() && selectedType == null) {
                item {
                    Text(
                        text = "Lo más visto",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
                item {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 500.dp)
                    ) {
                        items(mostPopularCards) { card ->
                            Card(
                                onClick = { onCardClick(card) },
                                modifier = Modifier
                                    .height(140.dp)
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                AsyncImage(
                                    model = card.imageUrl,
                                    contentDescription = "Carta de ${card.name}",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }
                }
            }

            if (selectedType == null) {
                item {
                    Text(
                        text = "Todas las cartas",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }
            }
            item {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 500.dp)
                ) {
                    items(filteredCards) { card ->
                        Card(
                            onClick = { onCardClick(card) },
                            modifier = Modifier
                                .height(140.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            AsyncImage(
                                model = card.imageUrl,
                                contentDescription = "Carta de ${card.name}",
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