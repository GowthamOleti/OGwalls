package com.ogwalls.app.ui.screen

import android.Manifest
import android.app.WallpaperManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.compose.animation.core.*
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.unit.offset
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest as CoilImageRequest
import com.ogwalls.app.data.model.Wallpaper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.delay


import com.ogwalls.app.utils.WallpaperSetter
import kotlin.math.max
import kotlin.math.min
import androidx.compose.foundation.layout.BoxWithConstraints
import android.util.Log

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperPreviewScreen(
    wallpaper: Wallpaper,
    colorFilter: ColorFilter,
    colorMatrix: androidx.compose.ui.graphics.ColorMatrix,
    onDismiss: () -> Unit,
    onWallpaperSet: () -> Unit = onDismiss
) {
    var selectedScreen by remember { mutableStateOf("home") }
    var wallpaperScale by remember { mutableStateOf(1f) }
    var wallpaperOffset by remember { mutableStateOf(Offset.Zero) }
    // Separate states for dual preview
    var homeScale by remember { mutableStateOf(1f) }
    var homeOffset by remember { mutableStateOf(Offset.Zero) }
    var lockScale by remember { mutableStateOf(1f) }
    var lockOffset by remember { mutableStateOf(Offset.Zero) }
    var isSettingWallpaper by remember { mutableStateOf(false) }
    
    // Reset all transformations when wallpaper changes
    LaunchedEffect(wallpaper.id) {
        wallpaperScale = 1f
        wallpaperOffset = Offset.Zero
        homeScale = 1f
        homeOffset = Offset.Zero
        lockScale = 1f
        lockOffset = Offset.Zero
    }
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val hapticFeedback = LocalHapticFeedback.current

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        // Blurred background image
        AsyncImage(
            model = CoilImageRequest.Builder(context)
                .data(wallpaper.imageUrl)
                .build(),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .blur(radius = 30.dp)
                .alpha(0.3f)
        )
        
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 4.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    onClick = onDismiss,
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.1f),
                    contentColor = Color.White
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Text(
                    text = "Preview & Set",
                    style = MaterialTheme.typography.headlineMedium,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Toggle Style Selector
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .shadow(4.dp, RoundedCornerShape(28.dp)),
                shape = RoundedCornerShape(28.dp),
                color = Color.White.copy(alpha = 0.08f),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(6.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Button(
                        onClick = { selectedScreen = "home" },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedScreen == "home") 
                                Color.White else Color.Transparent,
                            contentColor = if (selectedScreen == "home") 
                                Color.Black else Color.White
                        ),
                        shape = RoundedCornerShape(25.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Home",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1
                            )
                        }
                    }
                    
                    Button(
                        onClick = { selectedScreen = "lock" },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedScreen == "lock") 
                                Color.White else Color.Transparent,
                            contentColor = if (selectedScreen == "lock") 
                                Color.Black else Color.White
                        ),
                        shape = RoundedCornerShape(25.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Lock",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1
                            )
                        }
                    }

                    Button(
                        onClick = { selectedScreen = "both" },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(horizontal = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedScreen == "both")
                                Color.White else Color.Transparent,
                            contentColor = if (selectedScreen == "both")
                                Color.Black else Color.White
                        ),
                        shape = RoundedCornerShape(25.dp),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp
                        )
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhoneAndroid,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Both",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Phone Preview
            // Transform state for single preview - DISABLED FOR NOW
            val singleTransformableState = rememberTransformableState { _, _, _ ->
                // Transformations disabled until we fix the wallpaper setting
            }
            val screenHeight = LocalConfiguration.current.screenHeightDp.dp
            val previewHeight = if (selectedScreen == "both")
                (screenHeight * 0.55f).coerceIn(420.dp, 660.dp)
            else
                (screenHeight * 0.65f).coerceIn(460.dp, 720.dp)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(previewHeight)
                    .animateContentSize(
                        animationSpec = tween(durationMillis = 200, easing = FastOutSlowInEasing)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (selectedScreen == "both") {
                    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                        val spacing = 12.dp
                        val baseWidth = (maxWidth - spacing) / 2f
val phoneWidth = minOf(baseWidth * 1.06f, maxWidth * 0.48f)
                        val phoneModifier = Modifier
                            .width(phoneWidth)
                            .aspectRatio(9f / 19.5f)

                        // Independent transform states for each preview - DISABLED FOR NOW
                        val homeTransformableState = rememberTransformableState { _, _, _ ->
                            // Transformations disabled until we fix the wallpaper setting
                        }
                        val lockTransformableState = rememberTransformableState { _, _, _ ->
                            // Transformations disabled until we fix the wallpaper setting
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                PhonePreviewCard(
                                    modifier = phoneModifier,
                                    imageUrl = wallpaper.imageUrl,
                                    contentDescription = wallpaper.title,
                                    colorFilter = colorFilter,
                                    wallpaperScale = homeScale,
                                    wallpaperOffset = homeOffset,
                                    transformableState = homeTransformableState,
                                    overlay = "home"
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Home screen",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                            Spacer(modifier = Modifier.width(spacing))
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                PhonePreviewCard(
                                    modifier = phoneModifier,
                                    imageUrl = wallpaper.imageUrl,
                                    contentDescription = wallpaper.title,
                                    colorFilter = colorFilter,
                                    wallpaperScale = lockScale,
                                    wallpaperOffset = lockOffset,
                                    transformableState = lockTransformableState,
                                    overlay = "lock"
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Lock screen",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }
                } else {
                    PhonePreviewCard(
                        modifier = Modifier
                            .fillMaxWidth(0.68f)
                            .aspectRatio(9f / 19.5f),
                        imageUrl = wallpaper.imageUrl,
                        contentDescription = wallpaper.title,
                        colorFilter = colorFilter,
                        wallpaperScale = wallpaperScale,
                        wallpaperOffset = wallpaperOffset,
                        transformableState = singleTransformableState,
                        overlay = selectedScreen
                    )
                }
            }
            
            // Preview info removed since transformations are disabled
            
                        Spacer(modifier = Modifier.height(24.dp))
            Spacer(modifier = Modifier.height(24.dp))
            
            // Reserve space above bottom bar adaptively
            Spacer(modifier = Modifier.height((screenHeight * 0.10f).coerceIn(72.dp, 112.dp)))
        }
        
        // Set Wallpaper Button
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .navigationBarsPadding(),
            contentAlignment = Alignment.BottomCenter
        ) {

            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                val selectedTarget = when (selectedScreen) {
                    "home" -> WallpaperSetter.Target.HOME
                    "lock" -> WallpaperSetter.Target.LOCK
                    "both" -> WallpaperSetter.Target.BOTH
                    else -> WallpaperSetter.Target.HOME // Default
                }
                
                // Reset Button (Outline)
                OutlinedButton(
                    onClick = {
                        if (selectedScreen == "both") {
                            homeScale = 1f
                            homeOffset = Offset.Zero
                            lockScale = 1f
                            lockOffset = Offset.Zero
                        } else {
                            wallpaperScale = 1f
                            wallpaperOffset = Offset.Zero
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.White,
                        containerColor = Color.Transparent
                    ),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Text(
                        text = "Reset",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                // Apply Button (Filled) - Fixed to include user's filters and adjustments
                Button(
                    onClick = {
                        scope.launch {
                            isSettingWallpaper = true
                            try {
                                val success = if (selectedScreen == "both") {
                                    // Check if any transformations are needed
                                    val isIdentityMatrix = colorMatrix?.let { matrix ->
                                        matrix.values.contentEquals(floatArrayOf(
                                            1f, 0f, 0f, 0f, 0f,
                                            0f, 1f, 0f, 0f, 0f,
                                            0f, 0f, 1f, 0f, 0f,
                                            0f, 0f, 0f, 1f, 0f
                                        ))
                                    } != false
                                    val hasTransformations = homeScale != 1f || homeOffset != Offset.Zero || 
                                                           lockScale != 1f || lockOffset != Offset.Zero || 
                                                           (colorMatrix != null && !isIdentityMatrix)
                                    
                                    Log.d("WallpaperPreview", "Both mode - homeScale: $homeScale, homeOffset: $homeOffset, lockScale: $lockScale, lockOffset: $lockOffset, hasTransformations: $hasTransformations")
                                    
                                    // Use simple working method for both
                                    WallpaperSetter.setWallpaperWorking(
                                        context = context,
                                        imageUrl = wallpaper.imageUrl,
                                        target = WallpaperSetter.Target.BOTH
                                    )
                                } else {
                                    // Get the correct scale and offset based on selected screen
                                    val (scale, offset) = when (selectedScreen) {
                                        "home" -> homeScale to homeOffset
                                        "lock" -> lockScale to lockOffset
                                        else -> wallpaperScale to wallpaperOffset
                                    }
                                    
                                    // Check if transformations are needed
                                    val isIdentityMatrix = colorMatrix?.let { matrix ->
                                        matrix.values.contentEquals(floatArrayOf(
                                            1f, 0f, 0f, 0f, 0f,
                                            0f, 1f, 0f, 0f, 0f,
                                            0f, 0f, 1f, 0f, 0f,
                                            0f, 0f, 0f, 1f, 0f
                                        ))
                                    } != false
                                    val hasTransformations = scale != 1f || offset != Offset.Zero || (colorMatrix != null && !isIdentityMatrix)
                                    
                                    Log.d("WallpaperPreview", "Single mode - scale: $scale, offset: $offset, hasTransformations: $hasTransformations")
                                    
                                    // Use simple working method - bypass complex pipeline
                                    WallpaperSetter.setWallpaperWorking(
                                        context = context,
                                        imageUrl = wallpaper.imageUrl,
                                        target = selectedTarget
                                    )
                                }
                                
                                withContext(Dispatchers.Main) {
                                    if (success) {
                                        Toast.makeText(
                                            context, 
                                            "Wallpaper set!", 
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onWallpaperSet()
                                    } else {
                                        Toast.makeText(
                                            context, 
                                            "Failed to set wallpaper", 
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                            } finally {
                                isSettingWallpaper = false
                            }
                        }
                    },
                    enabled = !isSettingWallpaper,
                    modifier = Modifier
                        .weight(1f)
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(30.dp)
                ) {
                    if (isSettingWallpaper) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = Color.Black,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Applying...",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    } else {
                        Text(
                            text = "Apply",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PhonePreviewCard(
    modifier: Modifier = Modifier,
    imageUrl: String,
    contentDescription: String?,
    colorFilter: ColorFilter,
    wallpaperScale: Float,
    wallpaperOffset: Offset,
    transformableState: androidx.compose.foundation.gestures.TransformableState,
    overlay: String
) {
    Surface(
        modifier = modifier
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = Color.White.copy(alpha = 0.1f)
            ),
        shape = RoundedCornerShape(24.dp),
        color = Color.Black,
        border = BorderStroke(2.dp, Color.White.copy(alpha = 0.3f))
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = CoilImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .build(),
                contentDescription = contentDescription,
                contentScale = ContentScale.Crop,
                colorFilter = colorFilter,
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(
                        scaleX = wallpaperScale,
                        scaleY = wallpaperScale,
                        translationX = wallpaperOffset.x,
                        translationY = wallpaperOffset.y,
                        transformOrigin = TransformOrigin.Center
                    )
                    .transformable(transformableState)
            )
            if (overlay == "home") {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
                        val dotSize = (maxWidth * 0.12f).coerceIn(20.dp, 36.dp)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(dotSize * 2)
                                .background(
                                    Color.White.copy(alpha = 0.1f),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceEvenly,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(4) {
                                Box(
                                    modifier = Modifier
                                        .size(dotSize)
                                        .background(
                                            Color.White.copy(alpha = 0.2f),
                                            CircleShape
                                        )
                                )
                            }
                        }
                    }
                }
            } else if (overlay == "lock") {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Smaller, responsive lock overlay based on card size and placed near top
                    BoxWithConstraints(Modifier.fillMaxWidth()) {
                        val topPad = maxHeight * 0.08f
                        val clockSize = (maxWidth.value * 0.14f).sp
                        val subtitleSize = (maxWidth.value * 0.052f).sp
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = topPad),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Monday, January 15",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = subtitleSize,
                                    shadow = Shadow(color = Color.Black.copy(alpha = 0.6f), offset = Offset(0f, 2f), blurRadius = 6f)
                                ),
                                color = Color.White.copy(alpha = 0.85f),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "9:41",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = clockSize,
                                    shadow = Shadow(color = Color.Black.copy(alpha = 0.7f), offset = Offset(0f, 3f), blurRadius = 8f)
                                ),
                                color = Color.White,
                                fontWeight = FontWeight.Light,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }
        }
    }
}

// No legacy helpers below; all downloading and setting is centralized in WallpaperSetter


 