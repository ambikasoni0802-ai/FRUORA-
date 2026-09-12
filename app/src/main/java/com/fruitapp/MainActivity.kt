package com.fruitapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.fruitapp.ui.screens.HomeScreen
import com.fruitapp.ui.screens.SplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Agar HomeScreen bina kisi argument ke hai:
                    HomeScreen()

                    // YA agar SplashScreen call kar rahe hain:
                    // SplashScreen()
                }
            }
        }
    }
}
