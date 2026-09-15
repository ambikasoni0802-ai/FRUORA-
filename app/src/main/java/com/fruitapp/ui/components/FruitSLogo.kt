package com.fruitapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun FruitFLogo(
    modifier: Modifier = Modifier,
    sizeDp: Dp = 46.dp
) {
    Canvas(modifier = modifier.size(sizeDp)) {
        val w = size.width
        val h = size.height

        drawRoundRect(
            color = Color(0xFFFFD6E6),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(w * 0.25f, h * 0.25f)
        )

        val stroke = w * 0.16f
        drawLine(
            color = Color(0xFFFF6F91),
            start = Offset(w * 0.35f, h * 0.22f),
            end = Offset(w * 0.35f, h * 0.78f),
            strokeWidth = stroke
        )
        drawLine(
            color = Color(0xFFFF6F91),
            start = Offset(w * 0.35f, h * 0.22f),
            end = Offset(w * 0.72f, h * 0.22f),
            strokeWidth = stroke
        )
        drawLine(
            color = Color(0xFFFF6F91),
            start = Offset(w * 0.35f, h * 0.50f),
            end = Offset(w * 0.65f, h * 0.50f),
            strokeWidth = stroke * 0.85f
        )

        drawCircle(color = Color(0xFF7ED957), radius = w * 0.06f, center = Offset(w * 0.20f, h * 0.20f))
        drawCircle(color = Color(0xFFFFA94D), radius = w * 0.05f, center = Offset(w * 0.80f, h * 0.30f))
        drawCircle(color = Color(0xFFC86DD7), radius = w * 0.05f, center = Offset(w * 0.22f, h * 0.80f))
    }
}
