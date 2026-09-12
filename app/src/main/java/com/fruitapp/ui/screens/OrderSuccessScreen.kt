package com.fruitapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fruitapp.data.PaymentMethod
import com.fruitapp.ui.theme.BgTop
import com.fruitapp.ui.theme.PinkMid
import com.fruitapp.ui.theme.TextBrown

@Composable
fun OrderSuccessScreen(
    paymentMethod: PaymentMethod,
    totalAmount: Double,
    onBackToHome: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(BgTop).padding(24.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🎉🍓🍊🍌", fontSize = 40.sp)
            Spacer(Modifier.height(16.dp))
            Text("Order Placed!", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = TextBrown)
            Spacer(Modifier.height(8.dp))
            Text(
                text = if (paymentMethod == PaymentMethod.CASH_ON_DELIVERY)
                    "Your order of ₹${totalAmount.toInt()} will be paid on delivery."
                else
                    "Payment of ₹${totalAmount.toInt()} received successfully.",
                textAlign = TextAlign.Center,
                color = TextBrown
            )
            Spacer(Modifier.height(28.dp))
            Button(
                onClick = onBackToHome,
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PinkMid),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Continue Shopping", fontSize = 16.sp)
            }
        }
    }
}
