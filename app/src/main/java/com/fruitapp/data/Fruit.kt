package com.fruitapp.data

data class Fruit(
    val id: Int,
    val name: String,
    val emoji: String,
    val price: Double,
    val unit: String,       // e.g. "kg", "dozen", "pc", "box"
    val category: String,   // e.g. "Seasonal", "Exotic", "Berries", "Melons", "Tropical", "Dry Fruits"
    val badge: String? = null,
    val isExotic: Boolean = false
)

data class CartItem(
    val fruit: Fruit,
    var quantity: Int
) {
    val totalPrice: Double
        get() = fruit.price * quantity
}

enum class PaymentMethod {
    CASH_ON_DELIVERY,
    ONLINE_PAYMENT
}

data class Order(
    val items: List<CartItem>,
    val paymentMethod: PaymentMethod,
    val totalAmount: Double,
    val address: String,
    val phone: String
)
