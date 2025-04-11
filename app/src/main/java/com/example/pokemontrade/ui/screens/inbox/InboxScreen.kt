package com.example.pokemontrade.ui.screens.inbox

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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pokemontrade.R
import com.example.pokemontrade.ui.theme.GreenPrimary
import com.example.pokemontrade.ui.theme.LightGray
import com.example.pokemontrade.ui.theme.LightGreen


@Composable
fun InboxScreen(
    userName: String = "Carlota",
    conversations: List<Conversation> = sampleConversations,
    onConversationClick: (Conversation) -> Unit = {}
) {
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
                        text = "Hola",
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
                    modifier = Modifier.run {
                        size(36.dp)
                            .background(Color.White, shape = CircleShape)
                            .padding(8.dp)
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Lista de conversaciones
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(conversations) { convo ->
                ConversationItem(convo, onClick = { onConversationClick(convo) })
            }
        }
    }
}

@Composable
fun ConversationItem(convo: Conversation, onClick: () -> Unit) {
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
                .background(LightGray, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = convo.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "En trámite",
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

data class Conversation(val name: String, val time: String)

val sampleConversations = listOf(
    Conversation("Antonio R.", "09:10 am"),
    Conversation("Paula G.", "20:23 am")
)

@Preview(showBackground = true)
@Composable
fun PreviewInboxScreen() {
    InboxScreen(
        userName = "Carlota",
        conversations = sampleConversations
    )
}
