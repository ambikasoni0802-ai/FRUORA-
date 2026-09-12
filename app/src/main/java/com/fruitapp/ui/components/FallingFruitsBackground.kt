package com.fruitapp.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.nativeCanvas
import android.graphics.Paint
import kotlin.random.Random

private data class FallingFruit(
    val emoji: String,
    val xFraction: Float,   // 0..1 horizontal position
    val speedFactor: Float, // relative fall speed
    val phaseOffset: Float, // 0..1 starting offset so fruits are staggered
    val sizeSp: Float
)

/**
 * Continuous "GIF-style" falling fruit emoji background.
 * Drop this behind screen content, e.g. inside a Box { FallingFruitsBackground(); ScreenContent() }
 */
@Composable
fun FallingFruitsBackground(modifier: Modifier = Modifier, fruitCount: Int = 26) {
    val emojis = listOf("🍎", "🍌", "🍇", "🍓", "🍊", "🍉", "🥝", "🍍", "🥭", "🍒", "🍑", "🍋", "🫐", "🍐")

    val fruits = remember {
        List(fruitCount) {
            FallingFruit(
                emoji = emojis.random(),
                xFraction = Random.nextFloat(),
                speedFactor = 0.6f + Random.nextFloat() * 0.8f,
                phaseOffset = Random.nextFloat(),
                sizeSp = 16f + Random.nextFloat() * 18f
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "fallLoop")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val paint = Paint().apply { textAlign = Paint.Align.CENTER }
        fruits.forEach { fruit ->
            val progress = ((time * fruit.speedFactor) + fruit.phaseOffset) % 1f
            val y = progress * (size.height + 200f) - 100f
            val x = fruit.xFraction * size.width
            paint.textSize = fruit.sizeSp * density
            drawContext.canvas.nativeCanvas.drawText(fruit.emoji, x, y, paint)
        }
    }
}
