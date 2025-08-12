package com.ogwalls.app.ui.screen


import android.graphics.BitmapFactory
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest as CoilImageRequest
import com.ogwalls.app.data.model.Wallpaper
import kotlinx.coroutines.launch
import java.net.URL
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PhoneAndroid
import com.ogwalls.app.ui.theme.AccentBlue
import com.ogwalls.app.data.model.FilterState
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState

import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.widget.Toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.filled.Tune
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.IconButton
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.SliderDefaults
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.input.pointer.pointerInput
import com.ogwalls.app.utils.applyTransformationsToBitmap
import com.ogwalls.app.ui.component.UnifiedSetWallpaperButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.filled.Contrast
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.FilterVintage
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.FilterBAndW
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyRow

// Performance optimizations
private val defaultFilterState = FilterState(
    brightness = 1.0f,  // 100% - neutral
    contrast = 1.0f,    // 100% - neutral  
    saturation = 1.0f   // 100% - neutral
)
private val defaultColorMatrix = ColorMatrix()

// Data class for filter items
data class FilterItem(
    val name: String,
    val value: Float,
    val range: ClosedFloatingPointRange<Float>,
    val onValueChange: (Float) -> Unit
)

// Memoized filter items to prevent recreation
@Composable
private fun rememberFilterItems(filterState: FilterState, onFilterStateChange: (FilterState) -> Unit): List<FilterItem> {
    return remember(filterState) {
        listOf(
            FilterItem("Brightness", filterState.brightness, 0.3f..3.0f) { value -> 
                onFilterStateChange(filterState.copy(brightness = value)) 
            },
            FilterItem("Contrast", filterState.contrast, 0.3f..3.0f) { value -> 
                onFilterStateChange(filterState.copy(contrast = value)) 
            },
            FilterItem("Saturation", filterState.saturation, 0.0f..3.0f) { value -> 
                onFilterStateChange(filterState.copy(saturation = value)) 
            },
            FilterItem("Sepia", filterState.sepia, 0.0f..1.0f) { value -> 
                onFilterStateChange(filterState.copy(sepia = value)) 
            },
            FilterItem("Vintage", filterState.vintage, 0.0f..1.0f) { value -> 
                onFilterStateChange(filterState.copy(vintage = value)) 
            },
            FilterItem("Cool Tone", filterState.cool, 0.0f..1.0f) { value -> 
                onFilterStateChange(filterState.copy(cool = value)) 
            },
            FilterItem("Warm Tone", filterState.warm, 0.0f..1.0f) { value -> 
                onFilterStateChange(filterState.copy(warm = value)) 
            },
            FilterItem("Black & White", filterState.blackAndWhite, 0.0f..1.0f) { value -> 
                onFilterStateChange(filterState.copy(blackAndWhite = value)) 
            },
            FilterItem("Vignette", filterState.vignette, 0.0f..1.0f) { value -> 
                onFilterStateChange(filterState.copy(vignette = value)) 
            }
        )
    }
}

// Memoized color matrix calculation
@Composable
private fun rememberColorMatrix(filterState: FilterState): ColorMatrix {
    return remember(filterState) {
        createCustomColorMatrix(filterState)
    }
}

// Global filter state storage to persist across navigation
private val filterStateMap = mutableMapOf<String, FilterState>()

// Cleanup function to remove old filter states (call this when app is backgrounded)
fun clearFilterStates() {
    filterStateMap.clear()
}

// Optimized filter state holder with persistence
@Composable
private fun rememberFilterStateHolder(wallpaperId: String): MutableState<FilterState> {
    return remember(wallpaperId) { 
        mutableStateOf(filterStateMap[wallpaperId] ?: defaultFilterState)
    }.also { state ->
        // Update the map whenever the state changes
        LaunchedEffect(state.value) {
            filterStateMap[wallpaperId] = state.value
        }
    }
}

// Helper functions defined first to avoid unresolved reference errors
@Composable
fun FilterOption(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    imageUrl: String,
    colorFilter: ColorFilter?,
    icon: ImageVector?,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val animatedTextAlpha by animateFloatAsState(
        targetValue = if (isSelected) 1f else 0.7f,
        animationSpec = tween(200),
        label = "text_alpha"
    )
    
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Surface(
            shape = CircleShape,
            color = if (icon != null) Color.Black else Color.Transparent,
            border = when {
                isSelected -> BorderStroke(2.dp, Color.White)
                icon != null -> BorderStroke(1.dp, Color.White)
                else -> null
            },
            modifier = Modifier
                .size(56.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick
                )
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                if (icon != null) {
                    Icon(
                        imageVector = icon,
                        contentDescription = name,
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                } else {
                    AsyncImage(
                        model = CoilImageRequest.Builder(LocalContext.current)
                            .data(imageUrl)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        colorFilter = colorFilter,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.White.copy(alpha = if (isSelected) 0.2f else 0f))
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.bodySmall,
            color = Color.White.copy(alpha = animatedTextAlpha),
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun CustomizeFiltersBottomSheet(
    filterState: FilterState,
    onFilterStateChange: (FilterState) -> Unit,
    onDoneClick: () -> Unit,
    onResetClick: () -> Unit,
    onSetWallpaper: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Black.copy(alpha = 0.4f),
                        Color.Black.copy(alpha = 0.8f)
                    )
                )
            )
            .padding(horizontal = 24.dp, vertical = 24.dp)
    ) {
        // Enhanced header
        Text(
            text = "Customize",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        
        // Filter dropdown and slider
        data class FilterItem(
            val name: String,
            val value: Float,
            val range: ClosedFloatingPointRange<Float>,
            val onValueChange: (Float) -> Unit
        )
        
        val filterItems = rememberFilterItems(filterState, onFilterStateChange)
        
        var selectedFilterIndex by remember { mutableStateOf(0) }
        var expanded by remember { mutableStateOf(false) }
        
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Google Photos-style filter carousel with slider above
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                // Slider positioned above the carousel
                if (selectedFilterIndex < filterItems.size) {
                    val currentFilter = filterItems[selectedFilterIndex]
                    val currentValue = when (currentFilter.name) {
                        "Brightness" -> filterState.brightness
                        "Contrast" -> filterState.contrast
                        "Saturation" -> filterState.saturation
                        "Sepia" -> filterState.sepia
                        "Vintage" -> filterState.vintage
                        "Cool Tone" -> filterState.cool
                        "Warm Tone" -> filterState.warm
                        "Black & White" -> filterState.blackAndWhite
                        "Vignette" -> filterState.vignette
                        else -> 0.5f
                    }
                    
                    // Filter name and value
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = currentFilter.name,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Medium,
                            color = Color.White,
                            letterSpacing = 0.2.sp
                        )
                        
                        Text(
                            text = "${(currentValue * 100).toInt()}%",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Normal,
                            color = Color.White.copy(alpha = 0.7f),
                            letterSpacing = 0.1.sp
                        )
                    }
                    
                    // Standard slider with haptic feedback
                    val hapticFeedback = LocalHapticFeedback.current
                    
                    Slider(
                        value = currentValue,
                        onValueChange = { value ->
                            hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                            
                            when (currentFilter.name) {
                                "Brightness" -> onFilterStateChange(filterState.copy(brightness = value))
                                "Contrast" -> onFilterStateChange(filterState.copy(contrast = value))
                                "Saturation" -> onFilterStateChange(filterState.copy(saturation = value))
                                "Sepia" -> onFilterStateChange(filterState.copy(sepia = value))
                                "Vintage" -> onFilterStateChange(filterState.copy(vintage = value))
                                "Cool Tone" -> onFilterStateChange(filterState.copy(cool = value))
                                "Warm Tone" -> onFilterStateChange(filterState.copy(warm = value))
                                "Black & White" -> onFilterStateChange(filterState.copy(blackAndWhite = value))
                                "Vignette" -> onFilterStateChange(filterState.copy(vignette = value))
                            }
                        },
                        valueRange = when (currentFilter.name) {
                            "Brightness", "Contrast" -> 0.3f..3.0f
                            "Saturation" -> 0.0f..3.0f
                            else -> 0f..1f
                        },
                        colors = SliderDefaults.colors(
                            thumbColor = Color.White,
                            activeTrackColor = Color.White.copy(alpha = 0.9f),
                            inactiveTrackColor = Color.White.copy(alpha = 0.2f)
                            ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            // Horizontal carousel of filter icons
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                items(filterItems.size) { index ->
                    val filterItem = filterItems[index]
                    val isSelected = selectedFilterIndex == index
                    
                    val animatedScale by animateFloatAsState(
                        targetValue = if (isSelected) 1.1f else 1f,
                        animationSpec = tween(200),
                        label = "icon_scale"
                    )
                    
                    val animatedBorderAlpha by animateFloatAsState(
                        targetValue = if (isSelected) 1f else 0f,
                        animationSpec = tween(200),
                        label = "border_alpha"
                    )
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .graphicsLayer {
                                scaleX = animatedScale
                                scaleY = animatedScale
                            }
                            .clickable {
                                selectedFilterIndex = index
                            }
                    ) {
                        // Circular icon container
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .background(
                                    if (isSelected) Color.White.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.08f),
                                    CircleShape
                                )
                                .border(
                                    width = if (isSelected) 2.dp else 0.dp,
                                    color = Color.White.copy(alpha = animatedBorderAlpha),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = when (filterItem.name) {
                                    "Brightness" -> Icons.Default.WbSunny
                                    "Contrast" -> Icons.Default.Contrast
                                    "Saturation" -> Icons.Default.Palette
                                    "Sepia" -> Icons.Default.FilterVintage
                                    "Vintage" -> Icons.Default.CameraAlt
                                    "Cool Tone" -> Icons.Default.AcUnit
                                    "Warm Tone" -> Icons.Default.LocalFireDepartment
                                    "Black & White" -> Icons.Default.FilterBAndW
                                    "Vignette" -> Icons.Default.CenterFocusStrong
                                    else -> Icons.Default.Tune
                                },
                                contentDescription = filterItem.name,
                                tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.8f),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Filter name
                        Text(
                            text = filterItem.name,
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal,
                            color = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f),
                            letterSpacing = 0.1.sp
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Enhanced bottom buttons with better design
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Reset Button - Enhanced outlined style
            OutlinedButton(
                onClick = onResetClick,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .shadow(4.dp, RoundedCornerShape(28.dp)),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.White,
                    containerColor = Color.Transparent
                ),
                border = BorderStroke(1.5.dp, Color.White.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Reset", 
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            // Apply Button - Enhanced white style
            Button(
                onClick = onDoneClick,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .shadow(8.dp, RoundedCornerShape(28.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                shape = RoundedCornerShape(28.dp)
            ) {
                Text(
                    text = "Apply", 
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

// Utility functions
fun createCustomColorMatrix(filterState: FilterState): ColorMatrix {
    val resultMatrix = ColorMatrix()

    // --- Saturation ---
    val saturationMatrix = ColorMatrix().apply {
        setToSaturation(filterState.saturation)
    }

    // --- Brightness ---
    // Values below 1 decrease brightness, above 1 increase it
    val brightnessMatrix = ColorMatrix(
        floatArrayOf(
            1f, 0f, 0f, 0f, (filterState.brightness - 1f) * 255f,
            0f, 1f, 0f, 0f, (filterState.brightness - 1f) * 255f,
            0f, 0f, 1f, 0f, (filterState.brightness - 1f) * 255f,
            0f, 0f, 0f, 1f, 0f
        )
    )

    // --- Contrast ---
    val c = filterState.contrast
    val offset = 128f * (1f - c)
    val contrastMatrix = ColorMatrix(
        floatArrayOf(
            c, 0f, 0f, 0f, offset,
            0f, c, 0f, 0f, offset,
            0f, 0f, c, 0f, offset,
            0f, 0f, 0f, 1f, 0f
        )
    )
    
    // --- Special Effects ---
    val effectsMatrix = ColorMatrix()
    if (filterState.blackAndWhite > 0f) {
        val bwMatrix = ColorMatrix().apply { setToSaturation(1f - filterState.blackAndWhite) }
        effectsMatrix.timesAssign(bwMatrix)
    }
    if (filterState.sepia > 0f) {
        val sepiaMatrix = ColorMatrix(
            floatArrayOf(
                0.393f, 0.769f, 0.189f, 0f, 0f,
                0.349f, 0.686f, 0.168f, 0f, 0f,
                0.272f, 0.534f, 0.131f, 0f, 0f,
                0f, 0f, 0f, 1f, 0f
            )
        )
        // Lerp between identity and sepia
        for (i in 0..19) {
            sepiaMatrix.values[i] = lerp(ColorMatrix().values[i], sepiaMatrix.values[i], filterState.sepia)
        }
        effectsMatrix.timesAssign(sepiaMatrix)
    }
    if (filterState.vintage > 0f) {
        val vintageMatrix = ColorMatrix().apply {
            set(0, 0, 1.1f)
            set(1, 1, 1.0f)
            set(2, 2, 0.8f)
        }
        for (i in 0..19) {
            vintageMatrix.values[i] = lerp(ColorMatrix().values[i], vintageMatrix.values[i], filterState.vintage)
        }
        effectsMatrix.timesAssign(vintageMatrix)
    }
    if (filterState.cool > 0f) {
        val coolMatrix = ColorMatrix().apply {
            set(2, 2, 1.2f) // More blue
        }
         for (i in 0..19) {
            coolMatrix.values[i] = lerp(ColorMatrix().values[i], coolMatrix.values[i], filterState.cool)
        }
        effectsMatrix.timesAssign(coolMatrix)
    }
    if (filterState.warm > 0f) {
        val warmMatrix = ColorMatrix().apply {
            set(0, 0, 1.2f) // More red
        }
         for (i in 0..19) {
            warmMatrix.values[i] = lerp(ColorMatrix().values[i], warmMatrix.values[i], filterState.warm)
        }
        effectsMatrix.timesAssign(warmMatrix)
    }

    // Combine all matrices in order
    resultMatrix.timesAssign(saturationMatrix)
    resultMatrix.timesAssign(brightnessMatrix)
    resultMatrix.timesAssign(contrastMatrix)
    resultMatrix.timesAssign(effectsMatrix)
    
    return resultMatrix
}


fun applyColorMatrixToBitmap(originalBitmap: Bitmap, colorMatrix: androidx.compose.ui.graphics.ColorMatrix): Bitmap {
    val filteredBitmap = Bitmap.createBitmap(
        originalBitmap.width,
        originalBitmap.height,
        Bitmap.Config.ARGB_8888
    )
    
    val canvas = Canvas(filteredBitmap)
    val paint = Paint().apply {
        colorFilter = android.graphics.ColorMatrixColorFilter(
            android.graphics.ColorMatrix(colorMatrix.values)
        )
    }
    
    canvas.drawBitmap(originalBitmap, 0f, 0f, paint)
    return filteredBitmap
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperDetailScreen(
    wallpaper: Wallpaper,
    onDismiss: () -> Unit,
    onNavigateToPreview: (Wallpaper, ColorFilter, ColorMatrix) -> Unit,
    viewModel: WallpaperViewModel
) {
    // Get filter state from ViewModel reactively
    val uiState by viewModel.uiState.collectAsState()
    val filterState = uiState.filterStates[wallpaper.id] ?: com.ogwalls.app.data.model.FilterState()
    
    // Get current wallpaper state from ViewModel to ensure like state is reactive
    val currentWallpaper = uiState.wallpapers.find { it.id == wallpaper.id } ?: wallpaper
    
    // Debug: Log when filter state is restored
    LaunchedEffect(wallpaper.id) {
        val savedState = uiState.filterStates[wallpaper.id]
        if (savedState != null) {
            println("Restored filter state for ${wallpaper.id}: $savedState")
        }
    }
    
    // Filter state change handler that updates ViewModel
    val onFilterStateChange = { newFilterState: com.ogwalls.app.data.model.FilterState ->
        viewModel.updateFilterState(wallpaper.id, newFilterState)
    }
    var selectedFilter by remember { mutableStateOf(0) }
    var isCustomizing by remember { mutableStateOf(false) }
    var isSettingWallpaper by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val hapticFeedback = LocalHapticFeedback.current
    
    // Simple entrance - show filters immediately
    var showFilters by remember { mutableStateOf(true) }

    // Memoized filters with optimized color matrix calculation
    val customMatrix = rememberColorMatrix(filterState)
    val filters = remember(filterState) {
        listOf(
            "Original" to defaultColorMatrix,
            "Noir" to ColorMatrix().apply {
                setToSaturation(0f)
                set(0, 0, 1.2f)
            },
            "Black and White" to ColorMatrix().apply {
                setToSaturation(0f)
            },
            "Warm" to ColorMatrix().apply {
                set(0, 0, 1.1f)
                set(1, 1, 1.05f)
                set(2, 2, 0.9f)
            },
            "Customize" to customMatrix
        )
    }

    val scaffoldState = rememberBottomSheetScaffoldState()
    


    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            if (isCustomizing) {
                CustomizeFiltersBottomSheet(
                    filterState = filterState,
                    onFilterStateChange = onFilterStateChange,
                    onDoneClick = { 
                        // Save the customized filter and close bottom sheet
                        isCustomizing = false
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    },
                    onResetClick = {
                        // Reset all filter values to default
                        viewModel.updateFilterState(wallpaper.id, com.ogwalls.app.data.model.FilterState())
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    },
                    onSetWallpaper = {
                        onNavigateToPreview(wallpaper, ColorFilter.colorMatrix(createCustomColorMatrix(filterState)), createCustomColorMatrix(filterState))
                    }
                )
            } else {
                Box(modifier = Modifier.height(1.dp))
            }
        },
        sheetPeekHeight = if (isCustomizing) 400.dp else 0.dp,
        containerColor = Color.Black,
        contentColor = Color.White,
        sheetDragHandle = {
            if (isCustomizing) {
                // Custom drag handle that matches app design
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .background(
                            Color.White.copy(alpha = 0.3f),
                            RoundedCornerShape(2.dp)
                        )
                )
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
        ) {

            // Main content
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .padding(bottom = if (isCustomizing) 120.dp else 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    AsyncImage(
                        model = CoilImageRequest.Builder(context)
                            .data(wallpaper.imageUrl)
                            .build(),
                        contentDescription = wallpaper.title,
                        contentScale = ContentScale.Crop,
                        colorFilter = ColorFilter.colorMatrix(filters[selectedFilter].second),
                        modifier = Modifier.fillMaxSize()
                    )

                    // Vignette Overlay
                    if (filterState.vignette > 0f) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = filterState.vignette * 0.8f)
                                        ),
                                        radius = 2200f // Large radius to keep the center clear
                                    )
                                )
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black),
                                    startY = 600f,
                                    endY = Float.POSITIVE_INFINITY
                                )
                            )
                    )
                }

                // Optimized filter options with memoization
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    filters.forEachIndexed { index, (name, matrix) ->
                        val isSelected = selectedFilter == index
                        val colorFilter = remember(name, matrix) {
                            if (name == "Customize") null else ColorFilter.colorMatrix(matrix)
                        }
                        
                        FilterOption(
                            name = name,
                            isSelected = isSelected,
                            onClick = { 
                                hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                if (name == "Customize") {
                                    selectedFilter = index
                                    scope.launch {
                                        isCustomizing = true
                                        scaffoldState.bottomSheetState.expand()
                                    }
                                } else {
                                    selectedFilter = index
                                    if (isCustomizing) {
                                        scope.launch {
                                            scaffoldState.bottomSheetState.partialExpand()
                                            isCustomizing = false
                                        }
                                    }
                                }
                            },
                            imageUrl = wallpaper.imageUrl,
                            colorFilter = colorFilter,
                            icon = if (name == "Customize") Icons.Default.Tune else null,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                // Unified Set Wallpaper Button
                UnifiedSetWallpaperButton(
                    onClick = {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                        onNavigateToPreview(wallpaper, ColorFilter.colorMatrix(filters[selectedFilter].second), filters[selectedFilter].second)
                    },
                    enabled = !isSettingWallpaper,
                    isLoading = isSettingWallpaper,
                    loadingText = "Setting...",
                    buttonText = "Set Wallpaper",
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(horizontal = 32.dp, vertical = 16.dp),
                    showIcon = true
                )
            }
            
            // Top buttons row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Back button - Material 3 style positioned on top left
                Surface(
                    onClick = onDismiss,
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.7f),
                    contentColor = Color.White,
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                    shadowElevation = 8.dp
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
                
                // Like button - Material 3 style positioned on top right
                Surface(
                    onClick = {
                        viewModel.toggleLike(currentWallpaper.id)
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                    },
                    modifier = Modifier.size(48.dp),
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.7f),
                    contentColor = Color.White,
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f)),
                    shadowElevation = 8.dp
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (currentWallpaper.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = if (currentWallpaper.isFavorite) "Unlike" else "Like",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FilterSliderItem(
    name: String,
    value: Float,
    onValueChange: (Float) -> Unit,
    range: ClosedFloatingPointRange<Float>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Color.White.copy(alpha = 0.08f),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp)
    ) {
        // Google Photos-style header with value display
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
        Text(
            text = name,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
            color = Color.White,
                letterSpacing = 0.2.sp
            )
            
            Text(
                text = "${(value * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.7f),
                letterSpacing = 0.1.sp
            )
        }
        
        Spacer(modifier = Modifier.height(12.dp))
        
        // Google Photos-style slider
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = range,
            colors = SliderDefaults.colors(
                thumbColor = Color.White,
                activeTrackColor = Color.White.copy(alpha = 0.9f),
                inactiveTrackColor = Color.White.copy(alpha = 0.2f)
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

