package com.fruitapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fruitapp.data.FirebaseOrderRepository
import com.fruitapp.data.FruitRepository
import com.fruitapp.ui.Routes
import com.fruitapp.ui.screens.*
import com.fruitapp.ui.theme.FruitAppTheme
import com.fruitapp.viewmodel.CartViewModel
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject

class MainActivity : ComponentActivity(), PaymentResultListener {

    private val cartViewModel: CartViewModel by viewModels()

    private val razorpayKeyId = "rzp_test_XXXXXXXXXXXX"

    private var onPaymentSuccess: (() -> Unit)? = null
    private var onPaymentFailure: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            Checkout.preload(applicationContext)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        setContent {
            FruitAppTheme {
                val navController = rememberNavController()
                AppNavGraph(
                    navController = navController,
                    viewModel = cartViewModel,
                    launchRazorpay = { amountInPaise -> startRazorpayCheckout(amountInPaise, navController) },
                    onOrderPlaced = { saveOrderToFirebase() }
                )
            }
        }
    }

    private fun saveOrderToFirebase() {
        FirebaseOrderRepository.saveOrder(
            customerName = cartViewModel.deliveryName,
            customerPhone = cartViewModel.deliveryPhone,
            customerAddress = cartViewModel.deliveryAddress,
            items = cartViewModel.cartItems,
            totalAmount = cartViewModel.cartTotal,
            paymentMethod = cartViewModel.selectedPaymentMethod.name,
            onFailure = { e -> e.printStackTrace() }
        )
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
                put("amount", amountInPaise)
                put("prefill", JSONObject().apply {
                    put("contact", cartViewModel.deliveryPhone)
                })
            }
            checkout.open(this, options)
        } catch (e: Exception) {
            onPaymentFailure?.invoke()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        saveOrderToFirebase()
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
    launchRazorpay: (Int) -> Unit,
    onOrderPlaced: () -> Unit
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
                onCartClick = { navController.navigate(Routes.CART) },
                onProfileClick = { navController.navigate(Routes.PROFILE) }
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
                    onOrderPlaced()
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

        composable(Routes.PROFILE) {
            ProfileScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
