package com.fruitapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fruitapp.data.CartItem
import com.fruitapp.ui.theme.BgTop
import com.fruitapp.ui.theme.PinkMid
import com.fruitapp.ui.theme.TextBrown
import com.fruitapp.viewmodel.CartViewModel

@Composable
fun CartScreen(
    viewModel: CartViewModel,
    onBack: () -> Unit,
    onCheckout: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(BgTop)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = TextBrown,
                modifier = Modifier.clickable { onBack() })
            Spacer(Modifier.width(12.dp))
            Text("Your Cart", fontWeight = FontWeight.Bold, color = TextBrown, fontSize = 18.sp)
        }

        if (viewModel.cartItems.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Your cart is empty 🍉\nGo add some fresh fruits!", color = TextBrown, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(viewModel.cartItems, key = { it.fruit.id }) { item ->
                    CartRow(
                        item = item,
                        onIncrease = { viewModel.increaseQuantity(item.fruit.id) },
                        onDecrease = { viewModel.decreaseQuantity(item.fruit.id) },
                        onRemove = { viewModel.removeFromCart(item.fruit.id) }
                    )
                }
            }

            Surface(shadowElevation = 8.dp, color = Color.White) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Total", fontWeight = FontWeight.Bold, color = TextBrown, fontSize = 16.sp)
                        Text("₹${viewModel.cartTotal.toInt()}", fontWeight = FontWeight.Bold, color = PinkMid, fontSize = 18.sp)
                    }
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = onCheckout,
                        shape = RoundedCornerShape(20.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PinkMid),
                        modifier = Modifier.fillMaxWidth().height(50.dp)
                    ) {
                        Text("Proceed to Checkout", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun CartRow(
    item: CartItem,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    Surface(shape = RoundedCornerShape(18.dp), color = Color.White, shadowElevation = 3.dp) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(item.fruit.emoji, fontSize = 32.sp)
            Spacer(Modifier.width(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(item.fruit.name, fontWeight = FontWeight.Bold, color = TextBrown)
                Text("₹${item.fruit.price.toInt()}/${item.fruit.unit}", fontSize = 12.sp, color = Color.Gray)
            }
            QtyStepper(item.quantity, onIncrease, onDecrease)
            Spacer(Modifier.width(8.dp))
            Icon(
                Icons.Filled.Delete,
                contentDescription = "Remove",
                tint = Color.Gray,
                modifier = Modifier.clickable { onRemove() }
            )
        }
    }
}

@Composable
private fun QtyStepper(quantity: Int, onIncrease: () -> Unit, onDecrease: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(shape = CircleShape, color = Color(0xFFFFE7F1), modifier = Modifier.size(28.dp).clickable { onDecrease() }) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) { Text("-", color = PinkMid, fontWeight = FontWeight.Bold) }
        }
        Text("$quantity", modifier = Modifier.padding(horizontal = 10.dp), fontWeight = FontWeight.Bold)
        Surface(shape = CircleShape, color = Color(0xFFFFE7F1), modifier = Modifier.size(28.dp).clickable { onIncrease() }) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) { Text("+", color = PinkMid, fontWeight = FontWeight.Bold) }
        }
    }
}
