package com.fruitapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fruitapp.data.FruitRepository
import com.fruitapp.data.PaymentMethod
import com.fruitapp.ui.Routes
import com.fruitapp.ui.screens.*
import com.fruitapp.ui.theme.FruitAppTheme
import com.fruitapp.viewmodel.CartViewModel
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class MainActivity : ComponentActivity(), PaymentResultListener {

    private val cartViewModel: CartViewModel by viewModels()

    // Set these to your real Razorpay key before release.
    // Test key placeholder — replace with your own from the Razorpay dashboard.
    private val razorpayKeyId = "rzp_test_XXXXXXXXXXXX"

    // Bridges Razorpay's static callback back into Compose navigation
    private var onPaymentSuccess: (() -> Unit)? = null
    private var onPaymentFailure: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Checkout.preload(applicationContext)

        setContent {
            FruitAppTheme {
                val navController = rememberNavController()
                AppNavGraph(
                    navController = navController,
                    viewModel = cartViewModel,
                    launchRazorpay = { amountInPaise -> startRazorpayCheckout(amountInPaise, navController) }
                )
            }
        }
    }

    private fun startRazorpayCheckout(amountInPaise: Int, navController: NavHostController) {
        onPaymentSuccess = {
            navController.navigate(Routes.ORDER_SUCCESS) {
                popUpTo(Routes.HOME)
            }
        }
        onPaymentFailure = {
            Toast.makeText(this, "Payment failed or cancelled. Please try again.", Toast.LENGTH_LONG).show()
            navController.popBackStack(Routes.CHECKOUT, inclusive = false)
        }

        val checkout = Checkout()
        checkout.setKeyID(razorpayKeyId)

        try {
            val options = JSONObject().apply {
                put("name", "Fruit App")
                put("description", "Fruit order payment")
                put("currency", "INR")
                put("amount", amountInPaise) // amount in paise
                put("prefill", JSONObject().apply {
                    put("contact", cartViewModel.deliveryPhone)
                })
            }
            checkout.open(this, options)
        } catch (e: Exception) {
            onPaymentFailure?.invoke()
        }
    }

    // ---- Razorpay PaymentResultListener callbacks ----
    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        cartViewModel.placeOrder()
        cartViewModel.onOrderCompleted()
        onPaymentSuccess?.invoke()
    }

    override fun onPaymentError(code: Int, response: String?) {
        onPaymentFailure?.invoke()
    }
}

@androidx.compose.runtime.Composable
fun AppNavGraph(
    navController: NavHostController,
    viewModel: CartViewModel,
    launchRazorpay: (Int) -> Unit
) {
    NavHost(navController = navController, startDestination = Routes.SPLASH) {

        composable(Routes.SPLASH) {
            SplashScreen(onFinished = {
                navController.navigate(Routes.HOME) { popUpTo(Routes.SPLASH) { inclusive = true } }
            })
        }

        composable(Routes.HOME) {
            HomeScreen(
                viewModel = viewModel,
                onFruitClick = { fruit -> navController.navigate(Routes.productDetail(fruit.id)) },
                onCartClick = { navController.navigate(Routes.CART) }
            )
        }

        composable(Routes.PRODUCT_DETAIL) { backStackEntry ->
            val fruitId = backStackEntry.arguments?.getString("fruitId")?.toIntOrNull()
            val fruit = FruitRepository.allFruits.find { it.id == fruitId }
            if (fruit != null) {
                ProductDetailScreen(
                    fruit = fruit,
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onGoToCart = { navController.navigate(Routes.CART) }
                )
            }
        }

        composable(Routes.CART) {
            CartScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onCheckout = { navController.navigate(Routes.CHECKOUT) }
            )
        }

        composable(Routes.CHECKOUT) {
            CheckoutScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onPlaceOrderCOD = {
                    viewModel.placeOrder()
                    viewModel.onOrderCompleted()
                    navController.navigate(Routes.ORDER_SUCCESS) { popUpTo(Routes.HOME) }
                },
                onPayOnline = { navController.navigate(Routes.PAYMENT) }
            )
        }

        composable(Routes.PAYMENT) {
            PaymentScreen(viewModel = viewModel, launchRazorpay = launchRazorpay)
        }

        composable(Routes.ORDER_SUCCESS) {
            OrderSuccessScreen(
                paymentMethod = viewModel.selectedPaymentMethod,
                totalAmount = viewModel.lastOrder?.totalAmount ?: 0.0,
                onBackToHome = {
                    navController.navigate(Routes.HOME) { popUpTo(Routes.HOME) { inclusive = true } }
                }
            )
        }
    }
}
