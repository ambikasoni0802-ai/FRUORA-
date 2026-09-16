package com.fruitapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.fruitapp.data.CartItem
import com.fruitapp.data.Fruit
import com.fruitapp.data.FruitRepository
import com.fruitapp.data.Order
import com.fruitapp.data.PaymentMethod

class CartViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("fruit_app_profile", Context.MODE_PRIVATE)

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
        searchQuery = ""
    }

    private val _cartItems = mutableStateOf<List<CartItem>>(emptyList())
    val cartItems: List<CartItem> get() = _cartItems.value

    val cartTotal: Double
        get() = cartItems.sumOf { it.totalPrice }

    val cartCount: Int
        get() = cartItems.sumOf { it.quantity }

    var deliveryName by mutableStateOf("")
        private set

    var deliveryAddress by mutableStateOf("")
        private set

    var deliveryPhone by mutableStateOf("")
        private set

    fun onAddressChange(value: String) { deliveryAddress = value }
    fun onPhoneChange(value: String) { deliveryPhone = value }
    fun onDeliveryNameChange(value: String) { deliveryName = value }

    val hasDeliveryDetails: Boolean
        get() = deliveryName.isNotBlank() && deliveryPhone.isNotBlank() && deliveryAddress.isNotBlank()

    var pendingFruit by mutableStateOf<Fruit?>(null)
        private set

    var pendingQuantity by mutableStateOf(1)
        private set

    fun requestAddToCart(fruit: Fruit, quantity: Int = 1) {
        if (hasDeliveryDetails) {
            repeat(quantity) { addToCart(fruit) }
        } else {
            pendingFruit = fruit
            pendingQuantity = quantity
        }
    }

    fun confirmDeliveryDetailsAndAddToCart(name: String, phone: String, address: String) {
        deliveryName = name
        deliveryPhone = phone
        deliveryAddress = address
        val fruit = pendingFruit
        val qty = pendingQuantity
        pendingFruit = null
        if (fruit != null) {
            repeat(qty) { addToCart(fruit) }
        }
    }

    fun cancelPendingAddToCart() {
        pendingFruit = null
        pendingQuantity = 1
    }

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

    var selectedPaymentMethod by mutableStateOf(PaymentMethod.CASH_ON_DELIVERY)
        private set

    fun onPaymentMethodSelected(method: PaymentMethod) {
        selectedPaymentMethod = method
    }

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

    // ---- Profile state (persisted to disk so it survives app restarts) ----
    var profileName by mutableStateOf("")
        private set

    var profileUsername by mutableStateOf("")
        private set

    var profilePhone by mutableStateOf("")
        private set

    var profileAddress by mutableStateOf("")
        private set

    var profileDob by mutableStateOf("")
        private set

    var profileGender by mutableStateOf("")
        private set

    var profilePhotoUri by mutableStateOf<String?>(null)
        private set

    var isLoggedIn by mutableStateOf(false)
        private set

    init {
        loadProfileFromDisk()
    }

    private fun loadProfileFromDisk() {
        profileName = prefs.getString("name", "") ?: ""
        profileUsername = prefs.getString("username", "") ?: ""
        profilePhone = prefs.getString("phone", "") ?: ""
        profileAddress = prefs.getString("address", "") ?: ""
        profileDob = prefs.getString("dob", "") ?: ""
        profileGender = prefs.getString("gender", "") ?: ""
        profilePhotoUri = prefs.getString("photoUri", null)
        isLoggedIn = prefs.getBoolean("isLoggedIn", false)
    }

    private fun saveProfileToDisk() {
        prefs.edit()
            .putString("name", profileName)
            .putString("username", profileUsername)
            .putString("phone", profilePhone)
            .putString("address", profileAddress)
            .putString("dob", profileDob)
            .putString("gender", profileGender)
            .putString("photoUri", profilePhotoUri)
            .putBoolean("isLoggedIn", isLoggedIn)
            .apply()
    }

    fun onProfileNameChange(value: String) { profileName = value; isLoggedIn = true; saveProfileToDisk() }
    fun onProfileUsernameChange(value: String) { profileUsername = value; isLoggedIn = true; saveProfileToDisk() }
    fun onProfilePhoneChange(value: String) { profilePhone = value; isLoggedIn = true; saveProfileToDisk() }
    fun onProfileAddressChange(value: String) { profileAddress = value; isLoggedIn = true; saveProfileToDisk() }
    fun onProfileDobChange(value: String) { profileDob = value; isLoggedIn = true; saveProfileToDisk() }
    fun onProfileGenderChange(value: String) { profileGender = value; isLoggedIn = true; saveProfileToDisk() }
    fun onProfilePhotoChange(uri: String?) { profilePhotoUri = uri; isLoggedIn = true; saveProfileToDisk() }

    /** Explicit logout — the ONLY way profile data gets cleared. */
    fun logout() {
        profileName = ""
        profileUsername = ""
        profilePhone = ""
        profileAddress = ""
        profileDob = ""
        profileGender = ""
        profilePhotoUri = null
        isLoggedIn = false
        prefs.edit().clear().apply()
    }

    // ---- Onboarding survey (5 questions) ----
    var surveyReason by mutableStateOf("")
        private set
    var surveyFrequency by mutableStateOf("")
        private set
    var surveyPreference by mutableStateOf("")
        private set
    var surveyHeardFrom by mutableStateOf("")
        private set
    var surveyDeliveryTime by mutableStateOf("")
        private set

    fun onSurveyReasonChange(value: String) { surveyReason = value }
    fun onSurveyFrequencyChange(value: String) { surveyFrequency = value }
    fun onSurveyPreferenceChange(value: String) { surveyPreference = value }
    fun onSurveyHeardFromChange(value: String) { surveyHeardFrom = value }
    fun onSurveyDeliveryTimeChange(value: String) { surveyDeliveryTime = value }
}
