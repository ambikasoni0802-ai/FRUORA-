package com.fruitapp.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.fruitapp.ui.theme.BgTop
import com.fruitapp.ui.theme.PinkMid
import com.fruitapp.ui.theme.TextBrown
import com.fruitapp.viewmodel.CartViewModel

@Composable
fun ProfileScreen(
    viewModel: CartViewModel,
    onBack: () -> Unit
) {
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            viewModel.onProfilePhotoChange(uri.toString())
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgTop)
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.ArrowBack,
                contentDescription = "Back",
                tint = TextBrown,
                modifier = Modifier.clickable { onBack() }
            )
            Spacer(Modifier.width(12.dp))
            Text("My Profile", fontWeight = FontWeight.Bold, color = TextBrown, fontSize = 20.sp)
        }

        Spacer(Modifier.height(24.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Surface(
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier
                    .size(110.dp)
                    .clickable { imagePicker.launch("image/*") }
            ) {
                if (viewModel.profilePhotoUri != null) {
                    AsyncImage(
                        model = viewModel.profilePhotoUri,
                        contentDescription = "Profile Photo",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize().clip(CircleShape)
                    )
                } else {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        Text("🧑", fontSize = 48.sp)
                    }
                }
            }
        }
        Spacer(Modifier.height(10.dp))
        Text(
            "Tap photo to change",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(Modifier.height(28.dp))
        SectionLabel("Personal Details")

        OutlinedTextField(
            value = viewModel.profileName,
            onValueChange = { viewModel.onProfileNameChange(it) },
            label = { Text("Full Name") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
        )
        OutlinedTextField(
            value = viewModel.profileUsername,
            onValueChange = { viewModel.onProfileUsernameChange(it) },
            label = { Text("Username") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
        )
        OutlinedTextField(
            value = viewModel.profilePhone,
            onValueChange = { viewModel.onProfilePhoneChange(it.filter { c -> c.isDigit() }.take(10)) },
            label = { Text("Phone Number") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
        )
        OutlinedTextField(
            value = viewModel.profileAddress,
            onValueChange = { viewModel.onProfileAddressChange(it) },
            label = { Text("Address") },
            minLines = 2,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
        )
        OutlinedTextField(
            value = viewModel.profileDob,
            onValueChange = { viewModel.onProfileDobChange(it) },
            label = { Text("Date of Birth (DD/MM/YYYY)") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
        )

        Spacer(Modifier.height(6.dp))
        Text("Gender", fontWeight = FontWeight.Medium, color = TextBrown, modifier = Modifier.padding(top = 12.dp, bottom = 6.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Male", "Female", "Other").forEach { option ->
                ChipOption(
                    label = option,
                    selected = viewModel.profileGender == option,
                    onClick = { viewModel.onProfileGenderChange(option) }
                )
            }
        }

        Spacer(Modifier.height(28.dp))
        SectionLabel("Quick Survey")
        Text(
            "Help us serve you better — 5 quick questions",
            color = Color.Gray,
            fontSize = 12.sp,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        SurveyQuestion(
            question = "1. Why did you download this app?",
            options = listOf("Grocery Shopping", "Health & Fitness", "Gifting", "Business", "Other"),
            selected = viewModel.surveyReason,
            onSelect = { viewModel.onSurveyReasonChange(it) }
        )
        SurveyQuestion(
            question = "2. How often do you plan to order fruits?",
            options = listOf("Daily", "Weekly", "Monthly", "Occasionally"),
            selected = viewModel.surveyFrequency,
            onSelect = { viewModel.onSurveyFrequencyChange(it) }
        )
        SurveyQuestion(
            question = "3. What type of fruits do you prefer?",
            options = listOf("Seasonal", "Exotic", "Cut Fruits", "Mixed"),
            selected = viewModel.surveyPreference,
            onSelect = { viewModel.onSurveyPreferenceChange(it) }
        )
        SurveyQuestion(
            question = "4. How did you hear about us?",
            options = listOf("Friend", "Social Media", "Advertisement", "Other"),
            selected = viewModel.surveyHeardFrom,
            onSelect = { viewModel.onSurveyHeardFromChange(it) }
        )
        SurveyQuestion(
            question = "5. Preferred delivery time?",
            options = listOf("Morning", "Afternoon", "Evening", "Anytime"),
            selected = viewModel.surveyDeliveryTime,
            onSelect = { viewModel.onSurveyDeliveryTimeChange(it) }
        )

        Spacer(Modifier.height(24.dp))

        Button(
            onClick = onBack,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PinkMid),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Save Profile", fontSize = 16.sp)
        }

        Spacer(Modifier.height(40.dp))
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(text, fontWeight = FontWeight.Bold, color = TextBrown, fontSize = 16.sp)
}

@Composable
private fun ChipOption(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (selected) PinkMid else Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            label,
            color = if (selected) Color.White else TextBrown,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun SurveyQuestion(
    question: String,
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Text(question, fontWeight = FontWeight.Medium, color = TextBrown, fontSize = 13.sp)
        Spacer(Modifier.height(6.dp))
        FlowRowSimple(options = options, selected = selected, onSelect = onSelect)
    }
}

@Composable
private fun FlowRowSimple(options: List<String>, selected: String, onSelect: (String) -> Unit) {
    Column {
        var i = 0
        while (i < options.size) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(bottom = 8.dp)) {
                for (j in i until minOf(i + 2, options.size)) {
                    ChipOption(
                        label = options[j],
                        selected = selected == options[j],
                        onClick = { onSelect(options[j]) }
                    )
                }
            }
            i += 2
        }
    }
}
