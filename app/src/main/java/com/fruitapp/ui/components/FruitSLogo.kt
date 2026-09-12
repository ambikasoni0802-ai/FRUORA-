package com.fruitapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun FruitSLogo(
    modifier: Modifier = Modifier,
    sizeDp: Dp = 46.dp
) {
    val fruits = listOf("🍎", "🍓", "🍇", "🍊", "🍌", "🍒", "🍋", "🍍", "🥝", "🍑")

    Canvas(modifier = modifier.size(sizeDp)) {
        val w = size.width
        val h = size.height
        val pointCount = 100

        drawIntoCanvas { canvas ->
            val paint = android.graphics.Paint().apply {
                textSize = (w / 12f)
                isAntiAlias = true
            }

            for (i in 0 until pointCount) {
                val t = i / (pointCount - 1f)
                val angle = t * 2 * PI

                // S-curve path using sine wave
                val baseX = w * 0.5f + (w * 0.28f) * sin(angle * 1.0).toFloat()
                val baseY = h * t

                val jitterX = (Random.nextFloat() - 0.5f) * (w * 0.08f)
                val jitterY = (Random.nextFloat() - 0.5f) * (h * 0.02f)

                val x = baseX + jitterX
                val y = baseY + jitterY

                val emoji = fruits[i % fruits.size]
                canvas.nativeCanvas.drawText(emoji, x, y, paint)
            }
        }
    }
}
