package com.example.pokemontrade.ui.screens.inbox

import android.os.Build
import androidx.annotation.RequiresApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.pokemontrade.R
import com.example.pokemontrade.data.api.RetrofitInstance
import com.example.pokemontrade.data.storage.TokenManager
import com.example.pokemontrade.ui.screens.profile.UsersViewModel
import com.example.pokemontrade.ui.screens.profile.UsersViewModelFactory
import com.example.pokemontrade.ui.theme.GreenPrimary
import com.example.pokemontrade.ui.theme.LightGray
import com.example.pokemontrade.ui.theme.LightGreen
import com.example.pokemontrade.utils.resolveImageUrl
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class Conversation(
    val tradeId: Int,
    val name: String,
    val picture: String?,
    val status: String,
    val time: String
)

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun InboxScreen(
    onConversationClick: (Conversation) -> Unit = {}
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }

    val usersApi = remember { RetrofitInstance.getAuthenticatedApi(tokenManager) }
    var profile by remember {
        mutableStateOf<com.example.pokemontrade.data.models.users.UserProfile?>(
            null
        )
    }

    val userViewModel: UsersViewModel = viewModel(factory = UsersViewModelFactory(tokenManager))
    var conversations by remember { mutableStateOf<List<Conversation>>(emptyList()) }

    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            profile = userViewModel.getUserProfile()

            profile?.let {
                val trades = usersApi.getMyTrades()

                val result = trades.map { trade ->
                    val otherUserId =
                        if (trade.requesterId == it.id) trade.receiverId else trade.requesterId
                    val otherUser = usersApi.getUserById(otherUserId)

                    val userStatus = if (trade.requesterId == it.id) trade.statusRequester else trade.statusReceiver
                    val statusText = when (userStatus.lowercase()) {
                        "completed" -> "Finalizado"
                        "accepted" -> "Aceptado"
                        "declined" -> "Rechazado"
                        else -> "Pendiente"
                    }

                    Conversation(
                        tradeId = trade.id,
                        name = otherUser.name,
                        picture = otherUser.profilePictureUrl,
                        status = statusText,
                        time = trade.createdAt.toFormattedHour()
                    )
                }
                conversations = result
            }
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

    Column(Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
                .background(GreenPrimary)
                .padding(32.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "Hola,",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = LightGreen
                    )
                    Text(
                        text = userName,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.buscar),
                    contentDescription = "Buscar",
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.White, shape = CircleShape)
                        .padding(8.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(conversations) { convo ->
                ConversationItem(convo = convo) {
                    onConversationClick(convo)
                }
            }
        }
    }
}

@Composable
fun ConversationItem(convo: Conversation, onClick: () -> Unit) {
    val resolvedUrl = resolveImageUrl(convo.picture)

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
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
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = convo.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = convo.status,
                color = GreenPrimary,
                fontSize = 14.sp
            )
        }
        Text(
            text = convo.time,
            fontSize = 14.sp,
            color = LightGray,
        )
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
