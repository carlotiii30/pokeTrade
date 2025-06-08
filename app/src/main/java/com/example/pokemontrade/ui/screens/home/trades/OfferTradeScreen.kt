package com.example.pokemontrade.ui.screens.home.trades

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pokemontrade.R
import com.example.pokemontrade.data.api.RetrofitInstance
import com.example.pokemontrade.data.models.cards.CardResponse
import com.example.pokemontrade.data.models.trades.TradeCreate
import com.example.pokemontrade.data.models.users.UserProfile
import com.example.pokemontrade.data.storage.TokenManager
import com.example.pokemontrade.ui.theme.BluePrimary
import com.example.pokemontrade.ui.theme.DisabledBlue
import com.example.pokemontrade.ui.theme.LightGray
import com.google.gson.Gson
import kotlinx.coroutines.launch

@Composable
fun OfferTradeScreen(
    cardId: Int,
    navController: NavController
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val scope = rememberCoroutineScope()

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val selectedCardJson = savedStateHandle?.get<String>("selected_card_json")
    val selectedCard = selectedCardJson?.let { Gson().fromJson(it, CardResponse::class.java) }

    var requestedUser by remember { mutableStateOf<UserProfile?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    var requestedCard by remember { mutableStateOf<CardResponse?>(null) }

    LaunchedEffect(cardId) {
        scope.launch {
            try {
                val api = RetrofitInstance.getAuthenticatedApi(tokenManager)
                requestedCard = api.getCardById(cardId)
                val otherUserId = requestedCard!!.userId
                requestedUser = api.getUserById(otherUserId)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    if (isLoading || requestedCard == null || requestedUser == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(BluePrimary)
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
                            text = requestedUser!!.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Entrenador Pokémon",
                            color = LightGray,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "¿Ofrecer intercambio?",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

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
                    AsyncImage(
                        model = requestedCard!!.imageUrl,
                        contentDescription = "Carta de ${requestedCard!!.name}",
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
                            painter = painterResource(R.drawable.flecha_arriba_azul),
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
                modifier = Modifier.fillMaxWidth()
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
                            painter = painterResource(R.drawable.flecha_abajo_azul),
                            contentDescription = "Flecha",
                            modifier = Modifier
                                .size(70.dp)
                                .align(Alignment.TopStart)
                        )
                    }
                    Box(
                        modifier = Modifier
                            .height(160.dp)
                            .width(120.dp)
                            .align(Alignment.CenterEnd)
                            .clip(RoundedCornerShape(12.dp))
                            .background(LightGray)
                            .clickable {
                                navController.navigate("select_card_for_trade")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        if (selectedCard == null) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Seleccionar carta",
                                tint = Color.Black,
                                modifier = Modifier.size(48.dp)
                            )
                        } else {
                            AsyncImage(
                                model = selectedCard!!.imageUrl,
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            Button(
                onClick = {
                    scope.launch {
                        try {
                            val api = RetrofitInstance.getAuthenticatedApi(tokenManager)
                            api.createTrade(TradeCreate(selectedCard!!.id, requestedCard!!.id))
                            Toast.makeText(context, "¡Intercambio solicitado!", Toast.LENGTH_SHORT)
                                .show()
                            navController.popBackStack()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                },
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(40.dp),
                enabled = selectedCard != null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (selectedCard != null) BluePrimary else DisabledBlue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Aceptar",
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}
