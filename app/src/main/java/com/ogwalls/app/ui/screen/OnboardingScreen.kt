package com.ogwalls.app.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.ogwalls.app.data.OnboardingConfig
import com.ogwalls.app.data.model.Wallpaper
import com.ogwalls.app.ui.theme.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.foundation.BorderStroke
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.EaseOutBack
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize


@Composable
fun OnboardingScreen(
    onGetStarted: () -> Unit,
    viewModel: OnboardingViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    
    // Load wallpapers when screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.loadPreviewWallpapers()
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)  // Pitch black background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(100.dp))
            
            // Static Phone Cards Stack
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp), // Provide ample space for the stack
                contentAlignment = Alignment.Center
            ) {
                if (uiState.previewWallpapers.isNotEmpty()) {
                    AnimatedPhoneCardStack(
                        wallpapers = uiState.previewWallpapers.take(3)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Text Content Container - Center aligned
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Main Title
                Text(
                    text = OnboardingConfig.Text.MAIN_TITLE,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 28.sp, // Adjusted for perfect fit
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    ),
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Subtitle - Single line with larger font
                Text(
                    text = OnboardingConfig.Text.SUBTITLE,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 19.sp,
                        lineHeight = 26.sp,
                        fontWeight = FontWeight.Medium
                    ),
                    color = Color.White.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Get Started Button
            Button(
                onClick = onGetStarted,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(30.dp) // Perfect rounding
            ) {
                Text(
                    text = OnboardingConfig.Text.BUTTON_TEXT,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun AnimatedPhoneCardStack(
    wallpapers: List<Wallpaper>
) {
    var isVisible by remember { mutableStateOf(false) }
    var imagesLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        // Start the card animation immediately
        isVisible = true
        // Give images time to preload before showing them
        kotlinx.coroutines.delay(600)
        imagesLoaded = true
    }

    val transition = updateTransition(targetState = isVisible, label = "CardAnimation")

    val card1OffsetY by transition.animateDp(
        transitionSpec = { tween(durationMillis = 800, easing = EaseOutBack) },
        label = "Card1_OffsetY"
    ) { visible -> if (visible) 0.dp else 80.dp }

    val card2OffsetX by transition.animateDp(
        transitionSpec = { tween(durationMillis = 800, delayMillis = 100, easing = EaseOutBack) },
        label = "Card2_OffsetX"
    ) { visible -> if (visible) (-50).dp else 0.dp }
    
    val card2Rotation by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 800, delayMillis = 100, easing = EaseOutBack) },
        label = "Card2_Rotation"
    ) { visible -> if (visible) -15f else 0f }

    val card3OffsetX by transition.animateDp(
        transitionSpec = { tween(durationMillis = 800, delayMillis = 200, easing = EaseOutBack) },
        label = "Card3_OffsetX"
    ) { visible -> if (visible) 50.dp else 0.dp }

    val card3Rotation by transition.animateFloat(
        transitionSpec = { tween(durationMillis = 800, delayMillis = 200, easing = EaseOutBack) },
        label = "Card3_Rotation"
    ) { visible -> if (visible) 15f else 0f }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(450.dp),
        contentAlignment = Alignment.Center
    ) {
        // Card 3
        if (wallpapers.size > 2) {
            PhoneCard(
                wallpaper = wallpapers[2],
                showImage = imagesLoaded,
                modifier = Modifier
                    .offset(x = card3OffsetX, y = 20.dp)
                    .graphicsLayer {
                        scaleX = 0.85f
                        scaleY = 0.85f
                        rotationZ = card3Rotation
                    }
            )
        }

        // Card 2
        if (wallpapers.size > 1) {
            PhoneCard(
                wallpaper = wallpapers[1],
                showImage = imagesLoaded,
                modifier = Modifier
                    .offset(x = card2OffsetX, y = 10.dp)
                    .graphicsLayer {
                        scaleX = 0.9f
                        scaleY = 0.9f
                        rotationZ = card2Rotation
                    }
            )
        }
        
        // Card 1
        if (wallpapers.isNotEmpty()) {
            PhoneCard(
                wallpaper = wallpapers[0],
                showImage = imagesLoaded,
                modifier = Modifier
                    .offset(y = card1OffsetY + 6.dp)
            )
        }
    }
}

@Composable
fun PhoneCard(
    wallpaper: Wallpaper,
    showImage: Boolean = true,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var hasError by remember { mutableStateOf(false) }
    var imageAlpha by remember { mutableStateOf(0f) }
    
    // Animate image alpha when showImage becomes true
    LaunchedEffect(showImage) {
        if (showImage) {
            kotlinx.coroutines.delay(200) // Small delay for smooth transition
            imageAlpha = 1f
        }
    }
    
    Card(
        modifier = modifier
            .size(width = 210.dp, height = 380.dp),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        border = BorderStroke(5.dp, Color.White),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2C2C2E) // Dark gray to show image loading
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(22.dp)), // Clip image inside the border
            contentAlignment = Alignment.Center
        ) {
            // Loading indicator
            if (isLoading && showImage) {
                CircularProgressIndicator(
                    modifier = Modifier.size(32.dp),
                    color = Color.White,
                    strokeWidth = 3.dp
                )
            }
            
            // Error state
            if (hasError && showImage) {
                Text(
                    text = "Image\nFailed",
                    color = Color.White.copy(alpha = 0.6f),
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center
                )
            }
            
            // Image with alpha animation
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(wallpaper.imageUrl)
    
                    .build(),
                contentDescription = wallpaper.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(imageAlpha),
                onLoading = { isLoading = true },
                onSuccess = { isLoading = false },
                onError = { 
                    isLoading = false
                    hasError = true
                }
            )
        }
    }
}



// Onboarding UI State
data class OnboardingUiState(
    val previewWallpapers: List<Wallpaper> = emptyList(),
    val isLoading: Boolean = false
)

// Simplified Onboarding ViewModel (no sensors)
class OnboardingViewModel : androidx.lifecycle.ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()
    
    fun loadPreviewWallpapers() {
        _uiState.value = _uiState.value.copy(
            previewWallpapers = OnboardingConfig.getOnboardingWallpapers(),
            isLoading = false
        )
    }
} 