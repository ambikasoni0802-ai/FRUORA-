package com.fruitapp.ui.components

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.fruitapp.ui.theme.PinkMid
import com.fruitapp.ui.theme.TextBrown
import com.google.android.gms.location.LocationServices
import java.util.Locale

@Composable
fun DeliveryDetailsDialog(
    initialName: String,
    initialPhone: String,
    initialAddress: String,
    onConfirm: (name: String, phone: String, address: String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf(initialName) }
    var phone by remember { mutableStateOf(initialPhone) }
    var address by remember { mutableStateOf(initialAddress) }
    var isLocating by remember { mutableStateOf(false) }
    var locationError by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    fun fetchCurrentLocation() {
        isLocating = true
        locationError = null
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    isLocating = false
                    if (location != null) {
                        try {
                            val geocoder = Geocoder(context, Locale.getDefault())
                            @Suppress("DEPRECATION")
                            val results = geocoder.getFromLocation(location.latitude, location.longitude, 1)
                            if (!results.isNullOrEmpty()) {
                                val addr = results[0]
                                val fullAddress = listOfNotNull(
                                    addr.getAddressLine(0)
                                ).joinToString(", ")
                                address = fullAddress.ifBlank {
                                    "Lat: ${location.latitude}, Lng: ${location.longitude}"
                                }
                            } else {
                                address = "Lat: ${location.latitude}, Lng: ${location.longitude}"
                            }
                        } catch (e: Exception) {
                            address = "Lat: ${location.latitude}, Lng: ${location.longitude}"
                        }
                    } else {
                        locationError = "Could not get location. Make sure GPS is on."
                    }
                }
                .addOnFailureListener {
                    isLocating = false
                    locationError = "Location fetch failed. Try again."
                }
        } catch (e: SecurityException) {
            isLocating = false
            locationError = "Location permission needed."
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            fetchCurrentLocation()
        } else {
            locationError = "Location permission denied."
        }
    }

    fun onUseLocationClick() {
        val hasPermission = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            fetchCurrentLocation()
        } else {
            permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    val isValid = name.isNotBlank() && phone.length >= 10 && address.isNotBlank()

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = androidx.compose.ui.graphics.Color.White
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "Delivery Details",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = TextBrown
                )
                Text(
                    "Please fill this once — needed to deliver your order",
                    fontSize = 12.sp,
                    color = androidx.compose.ui.graphics.Color.Gray,
                    modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Full Name") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { if (it.length <= 10) phone = it.filter { c -> c.isDigit() } },
                    label = { Text("Mobile Number") },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(10.dp))

                OutlinedButton(
                    onClick = { onUseLocationClick() },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isLocating) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), strokeWidth = 2.dp)
                        Spacer(Modifier.width(8.dp))
                        Text("Locating...")
                    } else {
                        Text("📍 Use My Current Location")
                    }
                }
                if (locationError != null) {
                    Text(
                        locationError ?: "",
                        color = androidx.compose.ui.graphics.Color.Red,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Spacer(Modifier.height(10.dp))

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Full Address (House, Street, City, Pincode)") },
                    minLines = 3,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(20.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Cancel")
                    }
                    Spacer(Modifier.width(10.dp))
                    Button(
                        onClick = { onConfirm(name, phone, address) },
                        enabled = isValid,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PinkMid),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Confirm")
                    }
                }
            }
        }
    }
}
