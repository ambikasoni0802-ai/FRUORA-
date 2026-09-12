package com.fruitapp.ui

object Routes {
    const val SPLASH = "splash"
    const val HOME = "home"
    const val PRODUCT_DETAIL = "product_detail/{fruitId}"
    fun productDetail(fruitId: Int) = "product_detail/$fruitId"
    const val CART = "cart"
    const val CHECKOUT = "checkout"
    const val PAYMENT = "payment"
    const val ORDER_SUCCESS = "order_success"
}
