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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun FallingFruitsBackground(
    modifier: Modifier = Modifier,
    fruitCount: Int = 26
) {
    val fruits = listOf("🍎", "🍓", "🍇", "🍊", "🍌", "🍒", "🍋", "🍍", "🥝", "🍑")

    val particles = remember {
        List(fruitCount) {
            Triple(
                Random.nextFloat(),
                Random.nextFloat(),
                fruits[Random.nextInt(fruits.size)]
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "falling")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "fallProgress"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height

        particles.forEach { (xSeed, speedSeed, emoji) ->
            val x = xSeed * width
            val fallSpeed = 0.5f + speedSeed
            val y = ((progress * fallSpeed) % 1f) * (height + 100f) - 50f

            drawIntoCanvas { canvas ->
                val paint = android.graphics.Paint().apply {
                    textSize = 28.sp.toPx()
                    isAntiAlias = true
                }
                canvas.nativeCanvas.drawText(emoji, x, y, paint)
            }
        }
    }
}
