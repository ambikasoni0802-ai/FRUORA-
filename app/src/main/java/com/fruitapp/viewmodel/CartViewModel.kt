package com.fruitapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.fruitapp.data.CartItem
import com.fruitapp.data.Fruit
import com.fruitapp.data.FruitRepository
import com.fruitapp.data.Order
import com.fruitapp.data.PaymentMethod

class CartViewModel : ViewModel() {

    // ---- Search & category state ----
    var searchQuery by mutableStateOf("")
        private set

    var selectedCategory by mutableStateOf("All")
        private set

    val displayedFruits: List<Fruit>
        get() = if (searchQuery.isNotBlank()) {
            FruitRepository.search(searchQuery)
        } else {
            FruitRepository.byCategory(selectedCategory)
        }

    fun onSearchQueryChange(query: String) {
        searchQuery = query
    }

    fun onCategorySelected(category: String) {
        selectedCategory = category
        searchQuery = "" // clear search when browsing by category
    }

    // ---- Cart state ----
    private val _cartItems = mutableStateOf<List<CartItem>>(emptyList())
    val cartItems: List<CartItem> get() = _cartItems.value

    val cartTotal: Double
        get() = cartItems.sumOf { it.totalPrice }

    val cartCount: Int
        get() = cartItems.sumOf { it.quantity }

    fun addToCart(fruit: Fruit) {
        val current = _cartItems.value.toMutableList()
        val existingIndex = current.indexOfFirst { it.fruit.id == fruit.id }
        if (existingIndex >= 0) {
            val existing = current[existingIndex]
            current[existingIndex] = existing.copy(quantity = existing.quantity + 1)
        } else {
            current.add(CartItem(fruit, 1))
        }
        _cartItems.value = current
    }

    fun increaseQuantity(fruitId: Int) {
        _cartItems.value = _cartItems.value.map {
            if (it.fruit.id == fruitId) it.copy(quantity = it.quantity + 1) else it
        }
    }

    fun decreaseQuantity(fruitId: Int) {
        _cartItems.value = _cartItems.value.mapNotNull {
            if (it.fruit.id == fruitId) {
                if (it.quantity > 1) it.copy(quantity = it.quantity - 1) else null
            } else it
        }
    }

    fun removeFromCart(fruitId: Int) {
        _cartItems.value = _cartItems.value.filterNot { it.fruit.id == fruitId }
    }

    fun clearCart() {
        _cartItems.value = emptyList()
    }

    // ---- Checkout state ----
    var selectedPaymentMethod by mutableStateOf(PaymentMethod.CASH_ON_DELIVERY)
        private set

    fun onPaymentMethodSelected(method: PaymentMethod) {
        selectedPaymentMethod = method
    }

    var deliveryAddress by mutableStateOf("")
        private set

    var deliveryPhone by mutableStateOf("")
        private set

    fun onAddressChange(value: String) { deliveryAddress = value }
    fun onPhoneChange(value: String) { deliveryPhone = value }

    var lastOrder: Order? = null
        private set

    fun placeOrder(): Order {
        val order = Order(
            items = cartItems,
            paymentMethod = selectedPaymentMethod,
            totalAmount = cartTotal,
            address = deliveryAddress,
            phone = deliveryPhone
        )
        lastOrder = order
        return order
    }

    fun onOrderCompleted() {
        clearCart()
    }
}
