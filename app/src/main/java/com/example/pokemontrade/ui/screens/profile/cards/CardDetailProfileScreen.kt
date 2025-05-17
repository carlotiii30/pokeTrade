package com.example.pokemontrade.ui.screens.profile.cards

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.example.pokemontrade.data.storage.TokenManager
import com.example.pokemontrade.ui.theme.RedPrimary
import kotlinx.coroutines.launch

@Composable
fun CardDetailProfileScreen(
    navController: NavController,
    card: CardResponse
) {
    val context = LocalContext.current
    val tokenManager = remember { TokenManager(context) }
    val viewModel: CardsViewModel = viewModel(factory = CardsViewModelFactory(tokenManager))
    val scope = rememberCoroutineScope()


    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(RedPrimary)
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

        AsyncImage(
            model = card.img,
            contentDescription = "Carta Pokémon",
            modifier = Modifier
                .fillMaxWidth()
                .height(460.dp)
                .padding(horizontal = 42.dp, vertical = 12.dp)
                .clip(RoundedCornerShape(12.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                scope.launch {
                    viewModel.deleteCard(
                        cardId = card.id,
                        onSuccess = {
                            Toast.makeText(context, "Carta eliminada", Toast.LENGTH_SHORT).show()
                            navController.previousBackStackEntry
                                ?.savedStateHandle
                                ?.set("card_deleted", true)

                            navController.popBackStack()
                        },
                        onError = {
                            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                        }
                    )
                }
            },
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(40.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = RedPrimary
            ),
            border = BorderStroke(1.dp, RedPrimary.copy(alpha = 0.5f)),
        ) {
            Text(text = "Eliminar carta", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
    }
}