package com.fruitapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items as lazyListItems
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items as lazyGridItems
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fruitapp.data.Fruit
import com.fruitapp.data.FruitRepository
import com.fruitapp.ui.components.DeliveryDetailsDialog
import com.fruitapp.ui.components.FallingFruitsBackground
import com.fruitapp.ui.components.FruitFLogo
import com.fruitapp.ui.theme.*
import com.fruitapp.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: CartViewModel,
    onFruitClick: (Fruit) -> Unit,
    onCartClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(BgTop)) {
        FallingFruitsBackground(modifier = Modifier.fillMaxSize())

        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(listOf(OrangeStart, PinkMid, PurpleEnd))
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        FruitFLogo(sizeDp = 46.dp)
                        BadgedBox(badge = {
                            Badge { Text("${viewModel.cartCount}") }
                        }) {
                            IconButton(onClick = onCartClick) {
                                Icon(
                                    Icons.Filled.ShoppingCart,
                                    contentDescription = "Cart",
                                    tint = Color.White
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(12.dp))
                    OutlinedTextField(
                        value = viewModel.searchQuery,
                        onValueChange = { viewModel.onSearchQueryChange(it) },
                        placeholder = { Text("Search fresh fruits... e.g. mango, kiwi") },
                        singleLine = true,
                        shape = RoundedCornerShape(22.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            val results = viewModel.displayedFruits

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(bottom = 100.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize().weight(1f)
            ) {
                if (viewModel.searchQuery.isBlank()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        LazyRow(
                            modifier = Modifier.padding(vertical = 12.dp),
                            contentPadding = PaddingValues(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            lazyListItems(FruitRepository.categories) { category ->
                                val isActive = category == viewModel.selectedCategory
                                Surface(
                                    shape = RoundedCornerShape(18.dp),
                                    color = if (isActive) PinkMid else Color.White,
                                    shadowElevation = 3.dp,
                                    modifier = Modifier.clickable { viewModel.onCategorySelected(category) }
                                ) {
                                    Text(
                                        text = category,
                                        color = if (isActive) Color.White else PinkMid,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp)
                                    )
                                }
                            }
                        }
                    }

                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = Color(0xFF9EE6C4),
                            shadowElevation = 4.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .background(Brush.linearGradient(listOf(Color(0xFF9EE6C4), Color(0xFFDCEDC1))))
                                    .padding(horizontal = 20.dp, vertical = 18.dp)
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Fresh Picks!", color = Color(0xFF1F6B4D), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                    Text("Farm to home, same day 🌱", color = Color(0xFF3F8E6A), fontSize = 12.sp)
                                }
                                Text("🍉", fontSize = 38.sp)
                            }
                        }
                    }
                }

                item(span = { GridItemSpan(maxLineSpan) }) {
                    Text(
                        text = if (viewModel.searchQuery.isNotBlank())
                            "Results for \"${viewModel.searchQuery}\" (${results.size})"
                        else
                            "🍊 ${viewModel.selectedCategory} Fruits",
                        fontWeight = FontWeight.Bold,
                        color = TextBrown,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                    )
                }

                if (results.isEmpty()) {
                    item(span = { GridItemSpan(maxLineSpan) }) {
                        Box(
                            modifier = Modifier.fillMaxWidth().padding(32.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No fruit found. Try a different search 🍉", color = TextBrown)
                        }
                    }
                } else {
                    lazyGridItems(results) { fruit ->
                        FruitCard(
                            fruit = fruit,
                            onClick = { onFruitClick(fruit) },
                            onAddToCart = { viewModel.requestAddToCart(fruit) }
                        )
                    }
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(topStart = 26.dp, topEnd = 26.dp),
            color = Color.White,
            shadowElevation = 12.dp,
            modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                BottomNavItem("🏠", "Home", isActive = true) {}
                BottomNavItem("🍇", "Fruits", isActive = false) { viewModel.onCategorySelected("All") }
                BottomNavItem("🛒", "Cart", isActive = false) { onCartClick() }
                BottomNavItem("👤", "Profile", isActive = false) { onProfileClick() }
            }
        }

        val pending = viewModel.pendingFruit
        if (pending != null) {
            DeliveryDetailsDialog(
                initialName = viewModel.deliveryName,
                initialPhone = viewModel.deliveryPhone,
                initialAddress = viewModel.deliveryAddress,
                onConfirm = { name, phone, address ->
                    viewModel.confirmDeliveryDetailsAndAddToCart(name, phone, address)
                },
                onDismiss = { viewModel.cancelPendingAddToCart() }
            )
        }
    }
}

@Composable
private fun BottomNavItem(icon: String, label: String, isActive: Boolean, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(icon, fontSize = 20.sp)
        Text(
            label,
            fontSize = 11.sp,
            color = if (isActive) PinkMid else Color(0xFFCCCCCC),
            fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun FruitCard(fruit: Fruit, onClick: () -> Unit, onAddToCart: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(22.dp),
        color = CardBackground,
        shadowElevation = 4.dp,
        modifier = Modifier.clickable { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            fruit.badge?.let {
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = if (fruit.isExotic) Color(0xFFFFD3E0) else Color(0xFFA8E6CF)
                ) {
                    Text(
                        text = it,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (fruit.isExotic) Color(0xFFC2185B) else Color(0xFF1F6B4D),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
                Spacer(Modifier.height(4.dp))
            }
            Text(fruit.emoji, fontSize = 40.sp)
            Text(fruit.name, fontWeight = FontWeight.Bold, color = TextBrown, fontSize = 13.sp)
            Text("₹${fruit.price.toInt()}/${fruit.unit}", color = PinkMid, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(Modifier.height(6.dp))
            Button(
                onClick = onAddToCart,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PinkMid),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp)
            ) {
                Text("+ Add", fontSize = 12.sp)
            }
        }
    }
}
