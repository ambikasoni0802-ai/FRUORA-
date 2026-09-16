package com.fruitapp.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object FirebaseOrderRepository {

    private val db: FirebaseFirestore by lazy { Firebase.firestore }

    /**
     * Saves a completed order + customer details to Firestore.
     * Collection: "orders" — each order is a document with an auto-generated ID.
     */
    fun saveOrder(
        customerName: String,
        customerPhone: String,
        customerAddress: String,
        items: List<CartItem>,
        totalAmount: Double,
        paymentMethod: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        val itemsData = items.map { cartItem ->
            mapOf(
                "fruitName" to cartItem.fruit.name,
                "emoji" to cartItem.fruit.emoji,
                "quantity" to cartItem.quantity,
                "pricePerUnit" to cartItem.fruit.price,
                "unit" to cartItem.fruit.unit,
                "itemTotal" to cartItem.totalPrice
            )
        }

        val orderData = hashMapOf(
            "customerName" to customerName,
            "customerPhone" to customerPhone,
            "customerAddress" to customerAddress,
            "items" to itemsData,
            "totalAmount" to totalAmount,
            "paymentMethod" to paymentMethod,
            "status" to "New",
            "timestamp" to com.google.firebase.Timestamp.now()
        )

        db.collection("orders")
            .add(orderData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }

    /**
     * Saves/updates customer profile data separately too, keyed by phone number,
     * so the same customer's repeat visits update one record instead of duplicating.
     */
    fun saveCustomerProfile(
        name: String,
        username: String,
        phone: String,
        address: String,
        dob: String,
        gender: String,
        onSuccess: () -> Unit = {},
        onFailure: (Exception) -> Unit = {}
    ) {
        if (phone.isBlank()) {
            onFailure(IllegalArgumentException("Phone number required"))
            return
        }

        val customerData = hashMapOf(
            "name" to name,
            "username" to username,
            "phone" to phone,
            "address" to address,
            "dob" to dob,
            "gender" to gender,
            "lastUpdated" to com.google.firebase.Timestamp.now()
        )

        db.collection("customers")
            .document(phone) // phone number as unique document ID
            .set(customerData)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { e -> onFailure(e) }
    }
}
