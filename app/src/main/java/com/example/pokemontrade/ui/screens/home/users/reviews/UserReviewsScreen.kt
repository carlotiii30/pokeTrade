package com.example.pokemontrade.ui.screens.home.users.reviews

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pokemontrade.data.models.users.UserProfile
import com.example.pokemontrade.data.storage.TokenManager
import com.example.pokemontrade.ui.screens.home.cards.CardDetailHomeViewModel
import com.example.pokemontrade.ui.screens.home.cards.CardDetailHomeViewModelFactory
import com.example.pokemontrade.ui.theme.BluePrimary
import com.example.pokemontrade.ui.theme.DisabledBlue
import kotlinx.coroutines.launch
import java.time.OffsetDateTime
import java.time.format.TextStyle
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UserReviewsScreen(userId: Int, navController: NavController, tokenManager: TokenManager) {
    var profile by remember { mutableStateOf<UserProfile?>(null) }
    var showAll by remember { mutableStateOf(false) }

    val viewModel: UserReviewsViewModel =
        viewModel(factory = UserReviewsViewModelFactory(tokenManager))
    val usersViewModel: CardDetailHomeViewModel =
        viewModel(factory = CardDetailHomeViewModelFactory(tokenManager))

    val reviews by viewModel.reviews.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(userId) {
        scope.launch {
            viewModel.loadUserReviews(userId)
            profile = usersViewModel.getUserById(userId)
        }
    }


    val userName = profile?.name ?: "Entrenador"
    val averageRating = profile?.rating ?: 0f
    val ratingCount = profile?.reviewsCount ?: 0

    Column(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BluePrimary)
                .padding(vertical = 24.dp, horizontal = 16.dp)
        ) {
            IconButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Volver",
                    tint = Color.White
                )
            }

            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    userName,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    "Entrenador Pokémon",
                    fontSize = 16.sp,
                    color = Color.White
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(5) { i ->
                        val icon =
                            if (averageRating >= i + 1) Icons.Default.Star else Icons.Default.StarBorder
                        Icon(
                            icon,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "${"%.1f".format(averageRating)} ($ratingCount)",
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Valoraciones",
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 24.dp, bottom = 12.dp)
        )

        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else {
            val visibleReviews = if (showAll) reviews else reviews.take(3)

            LazyColumn(modifier = Modifier.padding(horizontal = 24.dp)) {
                items(visibleReviews) { review ->
                    ReviewItem(review)
                }
            }

            if (reviews.size > 3 && !showAll) {
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = { showAll = true },
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = BluePrimary
                    ),
                    border = BorderStroke(1.dp, BluePrimary.copy(alpha = 0.5f))
                ) {
                    Text(
                        "Ver todas las valoraciones",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ReviewItem(reviewWithAuthor: ReviewWithAuthor) {
    val review = reviewWithAuthor.review
    val authorName = reviewWithAuthor.authorName

    Column(modifier = Modifier.padding(vertical = 14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF1F1F1))
            )
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(authorName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(
                    text = review.createdAt.toFriendlyDate(),
                    fontSize = 16.sp,
                    color = DisabledBlue
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row {
            repeat(review.rating) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = BluePrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
            repeat(5 - review.rating) {
                Icon(
                    Icons.Default.StarBorder,
                    contentDescription = null,
                    tint = BluePrimary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = review.comment,
            fontSize = 18.sp,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        Divider(color = BluePrimary.copy(alpha = 0.2f))
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun String.toFriendlyDate(): String {
    return try {
        val dateTime = OffsetDateTime.parse(this)
        val day = "%02d".format(dateTime.dayOfMonth)
        val month = dateTime.month.getDisplayName(TextStyle.SHORT, Locale("es")).lowercase()
        val year = dateTime.year
        "$day $month $year"
    } catch (e: Exception) {
        this
    }
}
