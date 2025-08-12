package com.ogwalls.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.*
import kotlinx.coroutines.delay

@Composable
fun SuccessAnimationScreen(
    onAnimationComplete: () -> Unit
) {
    LaunchedEffect(Unit) {
        delay(3000) // Wait for 3 seconds
        onAnimationComplete()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val composition by rememberLottieComposition(
                LottieCompositionSpec.Url("https://lottie.host/1848c1e6-123c-434c-9ae6-59b97f780901/KTAtIeaBMj.lottie")
            )
            
            if (composition != null) {
                LottieAnimation(
                    composition = composition,
                    isPlaying = true,
                    iterations = 1, // Play only once
                    speed = 3f,
                    modifier = Modifier.size(200.dp)
                )
            } else {
                // Fallback: Show a simple checkmark if Lottie fails to load
                Text(
                    text = "✅",
                    color = Color.White,
                    style = MaterialTheme.typography.displayLarge
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Wallpaper set!",
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
} 