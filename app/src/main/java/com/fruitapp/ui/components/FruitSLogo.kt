package com.fruitapp.ui.components

import android.graphics.Paint
import android.graphics.PathMeasure
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.unit.dp
import kotlin.random.Random

/**
 * Small logo made of ~100 tiny fruit emojis arranged densely along an S-curve,
 * so the "S" shape itself is formed by fruits rather than a colored letter.
 * Kept intentionally small (46dp) per the agreed design.
 */
@Composable
fun FruitSLogo(modifier: Modifier = Modifier, sizeDp: Int = 46) {
    val fruitEmojis = listOf("🍎", "🍓", "🍇", "🍊", "🍌", "🍒", "🍋", "🍍", "🥝", "🍑")

    // Precompute jittered points once so the logo doesn't reshuffle on recomposition
    val points = remember {
        val path = Path().apply {
            // Same S-curve shape as the web preview, scaled to a 0..100 box
            moveTo(78f, 20f)
            cubicTo(78f, 5f, 22f, 5f, 22f, 27f)
            cubicTo(22f, 48f, 78f, 48f, 78f, 68f)
            cubicTo(78f, 90f, 22f, 90f, 22f, 78f)
        }
        val androidPath = path.asAndroidPath()
        val measure = PathMeasure(androidPath, false)
        val length = measure.length
        val fruitCount = 100
        val coords = FloatArray(2)
        val tan = FloatArray(2)

        (0 until fruitCount).map { i ->
            val dist = (i / (fruitCount - 1).toFloat()) * length
            measure.getPosTan(dist, coords, tan)
            // perpendicular normal for slight thickness/jitter
            val nx = -tan[1]
            val ny = tan[0]
            val jitter = (Random.nextFloat() - 0.5f) * 6f
            Triple(coords[0] + nx * jitter, coords[1] + ny * jitter, fruitEmojis[i % fruitEmojis.size])
        }
    }

    Canvas(modifier = modifier.size(sizeDp.dp)) {
        val scaleX = size.width / 100f
        val scaleY = size.height / 100f
        val paint = Paint().apply {
            textSize = 6f * scaleX * 1.6f // tiny emoji size, matches web version's 6px feel
            textAlign = Paint.Align.CENTER
        }
        drawContext.canvas.nativeCanvas.apply {
            points.forEach { (x, y, emoji) ->
                drawText(emoji, x * scaleX, y * scaleY, paint)
            }
        }
    }
}
