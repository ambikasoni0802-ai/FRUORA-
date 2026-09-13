package com.fruitapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import com.fruitapp.ui.theme.BgTop
import com.fruitapp.ui.theme.PinkMid
import com.fruitapp.ui.theme.TextBrown
import com.fruitapp.viewmodel.CartViewModel

@Composable
fun ProfileScreen(
    viewModel: CartViewModel,
    onBack: () -> Unit
) {
    val emojiOptions = listOf("🧑", "👩", "👨", "🧔", "👩‍🦱", "👨‍🦱", "🧑‍🦰", "🧓")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BgTop)
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
            Text("My Profile", fontWeight = FontWeight.Bold, color = TextBrown, fontSize = 18.sp)
        }

        Spacer(Modifier.height(24.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 6.dp,
                modifier = Modifier.size(100.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(viewModel.profilePhotoEmoji, fontSize = 48.sp)
                }
            }
        }

        Spacer(Modifier.height(12.dp))
        Text(
            "Tap an avatar to change:",
            color = TextBrown,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(Modifier.height(8.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            items(emojiOptions) { emoji ->
                Surface(
                    shape = CircleShape,
                    color = if (emoji == viewModel.profilePhotoEmoji) PinkMid.copy(alpha = 0.2f) else Color.White,
                    shadowElevation = 2.dp,
                    modifier = Modifier
                        .size(46.dp)
                        .clickable { viewModel.onProfilePhotoChange(emoji) }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(emoji, fontSize = 22.sp)
                    }
                }
            }
        }

        Spacer(Modifier.height(28.dp))

        OutlinedTextField(
            value = viewModel.profileName,
            onValueChange = { viewModel.onProfileNameChange(it) },
            label = { Text("Full Name") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = viewModel.profileUsername,
            onValueChange = { viewModel.onProfileUsernameChange(it) },
            label = { Text("Username") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = viewModel.profilePhone,
            onValueChange = { viewModel.onProfilePhoneChange(it) },
            label = { Text("Phone Number") },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(28.dp))

        Button(
            onClick = onBack,
            shape = RoundedCornerShape(20.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PinkMid),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Save Profile", fontSize = 16.sp)
        }
    }
}
