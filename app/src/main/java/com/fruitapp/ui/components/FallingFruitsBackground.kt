package com.fruitapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
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
                Random.nextFloat(),               // horizontal position seed
                0.5f + Random.nextFloat(),         // individual speed multiplier
                fruits[Random.nextInt(fruits.size)]
            )
        }
    }

    // elapsedSeconds keeps growing forever — it never resets, so nothing
    // ever "restarts" all together. Each fruit just wraps on its own.
    var elapsedSeconds by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(Unit) {
        var lastNanos = 0L
        while (true) {
            withFrameNanos { nanos ->
                if (lastNanos != 0L) {
                    val deltaSeconds = (nanos - lastNanos) / 1_000_000_000f
                    elapsedSeconds += deltaSeconds
                }
                lastNanos = nanos
            }
        }
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val width = size.width
        val height = size.height
        val cycleDuration = 8f // seconds for one fall at speed multiplier = 1

        particles.forEach { (xSeed, speedSeed, emoji) ->
            val x = xSeed * width
            // Each fruit's own progress, wraps individually — never a shared reset
            val progress = (elapsedSeconds * speedSeed / cycleDuration) % 1f
            val y = progress * (height + 100f) - 50f

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
