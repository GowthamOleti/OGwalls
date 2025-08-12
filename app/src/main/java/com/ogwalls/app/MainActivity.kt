package com.ogwalls.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.activity.compose.BackHandler
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.compose.ui.platform.LocalContext

import com.ogwalls.app.data.model.Wallpaper
import com.ogwalls.app.data.repository.WallpaperRepository
import com.ogwalls.app.ui.screen.OnboardingScreen
import com.ogwalls.app.ui.screen.LoadingScreen
import com.ogwalls.app.ui.screen.WallpaperDetailScreen
import com.ogwalls.app.ui.screen.WallpaperPreviewScreen
import com.ogwalls.app.ui.screen.WallpaperScreen
import com.ogwalls.app.ui.screen.SuccessAnimationScreen
import com.ogwalls.app.ui.screen.WallpaperViewModel
import com.ogwalls.app.ui.theme.OGWallsTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        // Add back press handling at Activity level
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // The actual handling will be done in the Composable
                // This just prevents the activity from being finished immediately
            }
        })
        
        setContent {
            OGWallsTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.Black // Full black background
                ) {
                    OGWallsApp()
                }
            }
        }
    }
}

@Composable
fun OGWallsApp() {
    val context = LocalContext.current
    val wallpaperViewModel: WallpaperViewModel = viewModel(
        factory = viewModelFactory {
            initializer {
                val repository = WallpaperRepository(context)
                WallpaperViewModel(repository)
            }
        }
    )
    
    // Optimized state-based navigation with memoization
    var currentScreen by remember { mutableStateOf("onboarding") }
    var selectedWallpaperId by remember { mutableStateOf<String?>(null) }
    var selectedWallpaper by remember { mutableStateOf<Wallpaper?>(null) }
    var currentViewType by remember { mutableStateOf("carousel") }
    var carouselPosition by remember { mutableStateOf(0) }
    var previewWallpaper by remember { mutableStateOf<Wallpaper?>(null) }
    var previewColorFilter by remember { mutableStateOf<androidx.compose.ui.graphics.ColorFilter?>(null) }
    var previewColorMatrix by remember { mutableStateOf<androidx.compose.ui.graphics.ColorMatrix?>(null) }
    var showSuccessAnimation by remember { mutableStateOf(false) }
    
    // Handle back navigation
    BackHandler(enabled = currentScreen != "onboarding") {
        when (currentScreen) {
            "wallpaper_preview" -> {
                currentScreen = "wallpaper_detail"
                previewWallpaper = null
                previewColorFilter = null
            }
            "wallpaper_detail" -> {
                currentScreen = "wallpapers"
                selectedWallpaperId = null
                selectedWallpaper = null
            }
            "wallpapers" -> {
                currentScreen = "loading"
            }
            "loading" -> {
                currentScreen = "onboarding"
            }
        }
    }
    
    // Optimized wallpaper loading with memoization
    LaunchedEffect(selectedWallpaperId) {
        selectedWallpaperId?.let { wallpaperId ->
            if (selectedWallpaper?.id != wallpaperId) {
                selectedWallpaper = try {
                    wallpaperViewModel.getWallpaperById(wallpaperId)
                } catch (e: Exception) {
                    // Fallback wallpaper if loading fails
                    Wallpaper(
                        id = wallpaperId,
                        title = "Sample Wallpaper",
                        subtitle = "High Quality",
                        imageUrl = "https://picsum.photos/1080/1920",
                        thumbnailUrl = "https://picsum.photos/300/400",
                        category = "Nature",
                        photographer = "Unknown",
                        resolution = "1080x1920"
                    )
                }
            }
        }
    }
    
    // Optimized screen transitions with faster animations
    Crossfade(
        targetState = currentScreen,
        animationSpec = tween(200),
        label = "screen_transition"
    ) { screen ->
        when (screen) {
            "onboarding" -> {
                OnboardingScreen(
                    onGetStarted = {
                        currentScreen = "loading"
                    }
                )
            }
            "loading" -> {
                LoadingScreen(
                    onLoadingComplete = {
                        currentScreen = "wallpapers"
                    }
                )
            }
            "wallpapers" -> {
                WallpaperScreen(
                    viewModel = wallpaperViewModel, // Pass the central ViewModel
                    initialViewType = currentViewType, // Pass the remembered view type
                    initialCarouselPosition = carouselPosition, // Pass the remembered carousel position
                    onWallpaperClick = { wallpaperId, viewType, position ->
                        currentViewType = viewType // Remember current view type
                        carouselPosition = position // Remember carousel position
                        selectedWallpaperId = wallpaperId
                        currentScreen = "wallpaper_detail"
                    }
                )
            }
            "wallpaper_detail" -> {
                selectedWallpaper?.let { wallpaper ->
                    WallpaperDetailScreen(
                        wallpaper = wallpaper,
                        onDismiss = {
                            currentScreen = "wallpapers"
                            selectedWallpaperId = null
                            selectedWallpaper = null
                            // currentViewType is preserved, so it returns to the same view
                        },
                        onNavigateToPreview = { wallpaper, colorFilter, colorMatrix ->
                            previewWallpaper = wallpaper
                            previewColorFilter = colorFilter
                            previewColorMatrix = colorMatrix
                            currentScreen = "wallpaper_preview"
                        },
                        viewModel = wallpaperViewModel
                    )
                }
            }
            "wallpaper_preview" -> {
                previewWallpaper?.let { wallpaper ->
                    previewColorFilter?.let { colorFilter ->
                        previewColorMatrix?.let { colorMatrix ->
                            WallpaperPreviewScreen(
                                wallpaper = wallpaper,
                                colorFilter = colorFilter,
                                colorMatrix = colorMatrix,
                                onDismiss = {
                                    currentScreen = "wallpaper_detail"
                                    previewWallpaper = null
                                    previewColorFilter = null
                                    previewColorMatrix = null
                                },
                                onWallpaperSet = {
                                    showSuccessAnimation = true
                                    currentScreen = "success_animation"
                                }
                            )
                        }
                    }
                }
            }
            "success_animation" -> {
                SuccessAnimationScreen(
                    onAnimationComplete = {
                        showSuccessAnimation = false
                        currentScreen = "wallpapers"
                        selectedWallpaperId = null
                        selectedWallpaper = null
                    }
                )
            }
        }
    }
}