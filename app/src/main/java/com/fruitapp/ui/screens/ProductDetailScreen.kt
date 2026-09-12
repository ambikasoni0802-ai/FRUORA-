package com.fruitapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fruitapp.data.Fruit
import com.fruitapp.ui.theme.BgTop
import com.fruitapp.ui.theme.PinkMid
import com.fruitapp.ui.theme.TextBrown
import com.fruitapp.viewmodel.CartViewModel

@Composable
fun ProductDetailScreen(
    fruit: Fruit,
    viewModel: CartViewModel,
    onBack: () -> Unit,
    onGoToCart: () -> Unit
) {
    var quantity by remember { mutableIntStateOf(1) }

    Column(modifier = Modifier.fillMaxSize().background(BgTop)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = TextBrown,
                modifier = Modifier.clickable { onBack() }
            )
            Spacer(Modifier.width(12.dp))
            Text("Product Details", fontWeight = FontWeight.Bold, color = TextBrown, fontSize = 18.sp)
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(fruit.emoji, fontSize = 120.sp)
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(fruit.name, fontSize = 24.sp, fontWeight = FontWeight.Bold, color = TextBrown)
            Text(fruit.category, color = Color.Gray, fontSize = 13.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                "₹${fruit.price.toInt()} / ${fruit.unit}",
                color = PinkMid,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            Spacer(Modifier.height(20.dp))
            Text("Quantity", fontWeight = FontWeight.Bold, color = TextBrown)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                QuantityButton("-") { if (quantity > 1) quantity-- }
                Text(
                    "$quantity",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                QuantityButton("+") { quantity++ }
            }

            Spacer(Modifier.height(28.dp))
            Button(
                onClick = {
                    repeat(quantity) { viewModel.addToCart(fruit) }
                    onGoToCart()
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PinkMid),
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("Add to Cart — ₹${(fruit.price * quantity).toInt()}", fontSize = 16.sp)
            }
        }
    }
}

@Composable
private fun QuantityButton(label: String, onClick: () -> Unit) {
    Surface(
        shape = CircleShape,
        color = Color.White,
        shadowElevation = 3.dp,
        modifier = Modifier.size(38.dp).clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(label, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PinkMid)
        }
    }
}
