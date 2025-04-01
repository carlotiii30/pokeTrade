package com.example.pokemontrade.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.pokemontrade.R
import com.example.pokemontrade.ui.theme.BluePrimary
import com.example.pokemontrade.ui.theme.PokemonTradeTheme
import kotlin.math.roundToInt

@Composable
fun SlideToStartButton(text: String, onSlideComplete: () -> Unit, modifier: Modifier = Modifier) {
    val trackWidth = 360.dp
    val thumbSize = 64.dp
    val maxOffsetPx = with(LocalDensity.current) { (trackWidth - thumbSize).toPx() }

    var offsetX by remember { mutableFloatStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(targetValue = offsetX, label = "slider")

    Box(
        modifier = modifier
            .width(trackWidth)
            .height(thumbSize)
            .clip(CircleShape)
            .background(Color.White)
            .padding(horizontal = 6.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        // Text centered below the slider
        Box(
            modifier = Modifier
                .matchParentSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = BluePrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Image(
            painter = painterResource(id = R.drawable.ic_pokeball),
            contentDescription = "Deslizar para comenzar",
            colorFilter = ColorFilter.tint(BluePrimary),
            modifier = Modifier
                .size(thumbSize)
                .offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
                .zIndex(1f)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX = (offsetX + dragAmount).coerceIn(0f, maxOffsetPx)
                        if (offsetX >= maxOffsetPx * 0.90f) {
                            onSlideComplete()
                            offsetX = 0f
                        }
                    }
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SlideToStartButtonPreview() {
    PokemonTradeTheme {
        SlideToStartButton(
            text = "Comencemos",
            onSlideComplete = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
