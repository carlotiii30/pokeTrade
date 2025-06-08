package com.example.pokemontrade.ui.screens.inbox.reviews

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pokemontrade.data.api.RetrofitInstance
import com.example.pokemontrade.data.models.reviews.ReviewRequest
import com.example.pokemontrade.data.models.trades.TradeResponse
import com.example.pokemontrade.data.models.users.UserProfile
import com.example.pokemontrade.data.storage.TokenManager
import com.example.pokemontrade.ui.screens.profile.UsersViewModel
import com.example.pokemontrade.ui.screens.profile.UsersViewModelFactory
import com.example.pokemontrade.ui.theme.GreenPrimary
import com.example.pokemontrade.ui.theme.LightGray
import com.example.pokemontrade.ui.theme.LightGreen
import com.example.pokemontrade.utils.resolveImageUrl
import kotlinx.coroutines.launch

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LeaveReviewScreen(
    tradeId: Int,
    navController: NavController
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val scope = rememberCoroutineScope()

    var trade by remember { mutableStateOf<TradeResponse?>(null) }
    var otherUser by remember { mutableStateOf<UserProfile?>(null) }
    var profile by remember { mutableStateOf<UserProfile?>(null) }

    var isLoading by remember { mutableStateOf(true) }
    val userViewModel: UsersViewModel = viewModel(factory = UsersViewModelFactory(tokenManager))

    var rating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        profile = userViewModel.getUserProfile()
    }

    LaunchedEffect(tradeId) {
        scope.launch {
            try {
                val api = RetrofitInstance.getAuthenticatedApi(tokenManager)

                val tradeData = api.getTradeById(tradeId)
                trade = tradeData

                val currentUserId = profile!!.id
                val otherUserId =
                    if (tradeData.requesterId == currentUserId) tradeData.receiverId else tradeData.requesterId
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
                    text = "Intercambio realizado con:",
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
                }
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(40.dp)
        ) {
            Text(
                text = "Deja una valoración",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "a este entrenador",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                lineHeight = 28.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(7.dp))

            Row {
                (1..5).forEach { star ->
                    Icon(
                        imageVector = if (star <= rating) Icons.Default.Star else Icons.Default.StarBorder,
                        contentDescription = null,
                        tint = GreenPrimary,
                        modifier = Modifier
                            .size(32.dp)
                            .clickable { rating = star }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                label = { Text("Escribe un comentario...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                shape = RoundedCornerShape(12.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    scope.launch {
                        RetrofitInstance.getAuthenticatedApi(tokenManager)
                            .leaveReview(
                                ReviewRequest(
                                    tradeId = tradeId,
                                    recipientId = otherUser!!.id,
                                    rating = rating,
                                    comment = comment
                                )
                            )
                        RetrofitInstance.getAuthenticatedApi(tokenManager)
                            .completeTrade(tradeId = tradeId)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier
                    .width(300.dp)
                    .height(56.dp),
                shape = CircleShape,
                enabled = rating > 0,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (rating > 0) GreenPrimary else Color.Gray,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Enviar valoración",
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
