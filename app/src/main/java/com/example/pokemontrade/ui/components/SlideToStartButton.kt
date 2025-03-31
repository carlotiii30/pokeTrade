package com.example.pokemontrade.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.pokemontrade.R
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.zIndex
import androidx.compose.foundation.Image
import androidx.compose.ui.platform.LocalDensity
import kotlin.math.roundToInt
import kotlin.math.max
import kotlin.math.min

@Composable
fun SlideToStartButton(text: String, onSlideComplete: () -> Unit) {
    val trackWidth = 300.dp
    val thumbSize = 52.dp
    val maxOffsetPx = with(LocalDensity.current) { (trackWidth - thumbSize).toPx() }

    var offsetX by remember { mutableFloatStateOf(0f) }
    val animatedOffsetX by animateFloatAsState(targetValue = offsetX, label = "slider")

    Box(
        modifier = Modifier
            .width(trackWidth)
            .height(thumbSize)
            .clip(RoundedCornerShape(40.dp))
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
                color = Color(0xFF2B5AA7)
            )
        }

        // Pokéball Thumb
        Image(
            painter = painterResource(id = R.drawable.ic_pokeball),
            contentDescription = "Deslizar para comenzar",
            modifier = Modifier
                .size(thumbSize)
                .offset { IntOffset(animatedOffsetX.roundToInt(), 0) }
                .zIndex(1f)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        change.consume()
                        offsetX = (offsetX + dragAmount).coerceIn(0f, maxOffsetPx)
                        if (offsetX >= maxOffsetPx * 0.95f) {
                            onSlideComplete()
                            offsetX = 0f
                        }
                    }
                }
        )
    }
}
