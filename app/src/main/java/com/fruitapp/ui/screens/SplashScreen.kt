package com.fruitapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fruitapp.ui.components.FallingFruitsBackground
import com.fruitapp.ui.components.FruitSLogo
import com.fruitapp.ui.theme.BgBottom
import com.fruitapp.ui.theme.BgTop
import com.fruitapp.ui.theme.TextBrown
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1500)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BgTop, BgBottom)))
    ) {
        FallingFruitsBackground(modifier = Modifier.fillMaxSize())

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
        ) {
            FruitSLogo(sizeDp = 90)
            Text(
                text = "Fresh fruits, delivered fast",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = TextBrown,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}
