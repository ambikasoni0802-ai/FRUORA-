package com.fruitapp.ui.components

import android.graphics.Path
import android.graphics.PathMeasure
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
        val sx = w / 100f
        val sy = h / 100f

        // Same S-curve as the web design (100x100 viewBox)
        val path = Path().apply {
            moveTo(78f * sx, 20f * sy)
            cubicTo(78f * sx, 5f * sy, 22f * sx, 5f * sy, 22f * sx, 27f * sy)
            cubicTo(22f * sx, 48f * sy, 78f * sx, 48f * sy, 78f * sx, 68f * sy)
            cubicTo(78f * sx, 90f * sy, 22f * sx, 90f * sy, 22f * sx, 78f * sy)
        }

        val measure = PathMeasure(path, false)
        val pathLength = measure.length
        val fruitCount = 100
        val pos = FloatArray(2)
        val tan = FloatArray(2)

        drawIntoCanvas { canvas ->
            val paint = android.graphics.Paint().apply {
                textSize = w / 9f
                isAntiAlias = true
                textAlign = android.graphics.Paint.Align.CENTER
            }

            for (i in 0 until fruitCount) {
                val dist = (i / (fruitCount - 1).toFloat()) * pathLength
                measure.getPosTan(dist, pos, tan)
                val nx = -tan[1]
                val ny = tan[0]
                val jitter = (Random.nextFloat() - 0.5f) * (w * 0.06f)
                val fx = pos[0] + nx * jitter
                val fy = pos[1] + ny * jitter
                canvas.nativeCanvas.drawText(fruits[i % fruits.size], fx, fy, paint)
            }
        }
    }
}
