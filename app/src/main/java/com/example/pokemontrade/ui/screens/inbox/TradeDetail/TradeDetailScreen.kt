package com.example.pokemontrade.ui.screens.inbox.TradeDetail

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pokemontrade.R
import com.example.pokemontrade.data.api.RetrofitInstance
import com.example.pokemontrade.data.models.cards.CardResponse
import com.example.pokemontrade.data.models.trades.TradeResponse
import com.example.pokemontrade.data.models.users.UserProfile
import com.example.pokemontrade.data.storage.TokenManager
import com.example.pokemontrade.ui.screens.profile.UsersViewModel
import com.example.pokemontrade.ui.screens.profile.UsersViewModelFactory
import com.example.pokemontrade.ui.theme.GreenPrimary
import com.example.pokemontrade.ui.theme.LightGray
import com.example.pokemontrade.ui.theme.LightGreen
import com.example.pokemontrade.ui.theme.RedPrimary
import com.example.pokemontrade.utils.resolveImageUrl
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun TradeDetailScreen(
    tradeId: Int,
    navController: NavController,
    onFinish: () -> Unit
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val scope = rememberCoroutineScope()

    var trade by remember { mutableStateOf<TradeResponse?>(null) }
    var otherUser by remember { mutableStateOf<UserProfile?>(null) }
    var profile by remember { mutableStateOf<UserProfile?>(null) }


    val userViewModel: UsersViewModel = viewModel(factory = UsersViewModelFactory(tokenManager))
    var isLoading by remember { mutableStateOf(true) }

    var offered by remember { mutableStateOf<CardResponse?>(null) }
    var requested by remember { mutableStateOf<CardResponse?>(null) }

    LaunchedEffect(Unit) {
        profile = userViewModel.getUserProfile()
    }

    var currentUserId = profile?.id ?: 0

    LaunchedEffect(tradeId) {
        scope.launch {
            try {
                val api = RetrofitInstance.getAuthenticatedApi(tokenManager)

                val tradeData = api.getTradeById(tradeId)
                trade = tradeData

                offered = api.getCardById(tradeData.offeredCardId)
                requested = api.getCardById(tradeData.requestedCardId)

                val currentUserId = profile!!.id
                val otherUserId = if (tradeData.requesterId == currentUserId) tradeData.receiverId else tradeData.requesterId
                otherUser = api.getUserById(otherUserId)

            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                isLoading = false
            }
        }
    }

    if (isLoading || trade == null || otherUser == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    val isProposedByUser = trade!!.requesterId == currentUserId
    val currentUserStatus = if (isProposedByUser) trade!!.statusRequester else trade!!.statusReceiver
    val otherUserStatus = if (isProposedByUser) trade!!.statusReceiver else trade!!.statusRequester

    val resolvedUrl = resolveImageUrl(otherUser!!.profilePictureUrl)

    Column(Modifier.fillMaxSize()) {
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
                            .clip(CircleShape)
                            .background(LightGray)
                    ) {
                        AsyncImage(
                            model = resolvedUrl,
                            contentDescription = "Imagen de perfil",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = otherUser!!.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Entrenador Pokémon",
                            color = LightGreen,
                            fontSize = 14.sp
                        )
                    }
                    Text(
                        text = trade!!.createdAt.toFormattedHour(),
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

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "${offered!!.name} por ${requested!!.name}",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "¿Trato hecho?",
                color = GreenPrimary,
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
                        model = offered!!.imageUrl,
                        contentDescription = "Carta de ${offered!!.name}",
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
                            painter = painterResource(R.drawable.flecha_abajo),
                            contentDescription = "Flecha",
                            modifier = Modifier
                                .size(70.dp)
                                .align(Alignment.TopStart)
                        )
                    }
                    AsyncImage(
                        model = requested!!.imageUrl,
                        contentDescription = "Carta de ${requested!!.name}",
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

            when {
                currentUserStatus.equals("completed", ignoreCase = true) -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .width(300.dp)
                                .height(56.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = LightGray)
                        ) {
                            Text(
                                text = "Ya has valorado este intercambio",
                                fontSize = 16.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                otherUserStatus.equals("completed", ignoreCase = true) -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(
                            onClick = {
                                scope.launch {
                                    navController.navigate("leave_review/$tradeId")
                                }
                            },
                            modifier = Modifier
                                .width(300.dp)
                                .height(56.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                        ) {
                            Text(
                                text = "Completa tú también",
                                fontSize = 16.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                currentUserStatus.equals("declined", ignoreCase = true) || otherUserStatus.equals("declined", ignoreCase = true) -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(
                            onClick = { navController.popBackStack() },
                            modifier = Modifier
                                .width(300.dp)
                                .height(56.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = RedPrimary)
                        ) {
                            Text(
                                text = "Intercambio rechazado",
                                fontSize = 16.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                currentUserStatus.equals("accepted", ignoreCase = true) && otherUserStatus.equals("accepted", ignoreCase = true) -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Button(
                            onClick = {
                                scope.launch {
                                    navController.navigate("leave_review/$tradeId")
                                }
                            },
                            modifier = Modifier
                                .width(300.dp)
                                .height(56.dp),
                            shape = CircleShape,
                            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                        ) {
                            Text(
                                text = "Completar intercambio",
                                fontSize = 16.sp,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                else -> {
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
                                onClick = {
                                    scope.launch {
                                        RetrofitInstance.getAuthenticatedApi(tokenManager)
                                            .declineTrade(tradeId)
                                        onFinish()
                                    }
                                },
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(60.dp),
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEEECEC))
                            ) {
                                Text(
                                    "Rechazar",
                                    color = Color.Black,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Button(
                                onClick = {
                                    scope.launch {
                                        RetrofitInstance.getAuthenticatedApi(tokenManager)
                                            .acceptTrade(tradeId)
                                        onFinish()
                                    }
                                },
                                modifier = Modifier
                                    .width(150.dp)
                                    .height(60.dp),
                                shape = CircleShape,
                                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
                            ) {
                                Text(
                                    "Aceptar",
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.toFormattedHour(): String {
    return try {
        val parsed = OffsetDateTime.parse(this)
        val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
        parsed.format(formatter)
    } catch (e: Exception) {
        this
    }
}
