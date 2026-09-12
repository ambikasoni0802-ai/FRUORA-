package com.fruitapp.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp), // Line 41 fix: integer ke aage .dp zaroori hai
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Splash Screen Loading...",
            modifier = Modifier.size(100.dp)
        )
    }
}
