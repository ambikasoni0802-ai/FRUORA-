package com.fruitapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.fruitapp.ui.theme.BgTop
import com.fruitapp.ui.theme.PinkMid
import com.fruitapp.ui.theme.TextBrown
import com.fruitapp.viewmodel.CartViewModel

@Composable
fun PaymentScreen(
    viewModel: CartViewModel,
    launchRazorpay: (amountInPaise: Int) -> Unit
) {
    LaunchedEffect(Unit) {
        val amountInPaise = (viewModel.cartTotal * 100).toInt()
        launchRazorpay(amountInPaise)
    }

    Box(
        modifier = Modifier.fillMaxSize().background(BgTop),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = PinkMid)
            Spacer(Modifier.height(16.dp))
            Text("Opening secure payment...", color = TextBrown)
        }
    }
}
