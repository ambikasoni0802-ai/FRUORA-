package com.fruitapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fruitapp.data.PaymentMethod
import com.fruitapp.ui.theme.BgTop
import com.fruitapp.ui.theme.PinkMid
import com.fruitapp.ui.theme.TextBrown
import com.fruitapp.viewmodel.CartViewModel

@Composable
fun CheckoutScreen(
    viewModel: CartViewModel,
    onBack: () -> Unit,
    onPlaceOrderCOD: () -> Unit,
    onPayOnline: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().background(BgTop).padding(16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back", tint = TextBrown,
                modifier = Modifier.clickable { onBack() })
            Spacer(Modifier.width(12.dp))
            Text("Checkout", fontWeight = FontWeight.Bold, color = TextBrown, fontSize = 18.sp)
        }

        Spacer(Modifier.height(20.dp))
        Text("Delivery Details", fontWeight = FontWeight.Bold, color = TextBrown)
        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = viewModel.deliveryName,
            onValueChange = { viewModel.onDeliveryNameChange(it) },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        )
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(
            value = viewModel.deliveryAddress,
            onValueChange = { viewModel.onAddressChange(it) },
            label = { Text("Delivery Address") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        )
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(
            value = viewModel.deliveryPhone,
            onValueChange = { viewModel.onPhoneChange(it) },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(Modifier.height(24.dp))
        Text("Order Summary", fontWeight = FontWeight.Bold, color = TextBrown)
        Spacer(Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("${viewModel.cartCount} items", color = Color.Gray)
            Text("₹${viewModel.cartTotal.toInt()}", fontWeight = FontWeight.Bold, color = PinkMid)
        }

        Spacer(Modifier.height(24.dp))
        Text("Payment Method", fontWeight = FontWeight.Bold, color = TextBrown)
        Spacer(Modifier.height(8.dp))

        PaymentOptionRow(
            label = "💵 Cash on Delivery",
            selected = viewModel.selectedPaymentMethod == PaymentMethod.CASH_ON_DELIVERY,
            onSelect = { viewModel.onPaymentMethodSelected(PaymentMethod.CASH_ON_DELIVERY) }
        )
        Spacer(Modifier.height(10.dp))
        PaymentOptionRow(
            label = "💳 Pay Online (UPI / Card / Netbanking)",
            selected = viewModel.selectedPaymentMethod == PaymentMethod.ONLINE_PAYMENT,
            onSelect = { viewModel.onPaymentMethodSelected(PaymentMethod.ONLINE_PAYMENT) }
        )

        Spacer(Modifier.weight(1f))

        val isFormValid = viewModel.deliveryName.isNotBlank() &&
            viewModel.deliveryAddress.isNotBlank() && viewModel.deliveryPhone.isNotBlank()

        Button(
            onClick = {
                if (viewModel.selectedPaymentMethod == PaymentMethod.CASH_ON_DELIVERY) {
                    onPlaceOrderCOD()
                } else {
                    onPayOnline()
                }
            },
            enabled = isFormValid,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PinkMid),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text(
                if (viewModel.selectedPaymentMethod == PaymentMethod.CASH_ON_DELIVERY)
                    "Place Order (COD)"
                else
                    "Proceed to Pay ₹${viewModel.cartTotal.toInt()}",
                fontSize = 16.sp
            )
        }
    }
}

@Composable
private fun PaymentOptionRow(label: String, selected: Boolean, onSelect: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (selected) PinkMid.copy(alpha = 0.15f) else Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth().clickable { onSelect() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontWeight = FontWeight.Medium, color = TextBrown)
            RadioButton(selected = selected, onClick = onSelect, colors = RadioButtonDefaults.colors(selectedColor = PinkMid))
        }
    }
}
