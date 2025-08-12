package com.ogwalls.app.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.ogwalls.app.data.model.FilterState
import com.ogwalls.app.data.model.Wallpaper

@Composable
fun WallpaperPreview(
    wallpaper: Wallpaper,
    filterState: FilterState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    // Create color matrix based on filter state
    val colorMatrix = remember(filterState) {
        createColorMatrix(filterState)
    }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(wallpaper.imageUrl)
    
                    .build(),
                contentDescription = wallpaper.title,
                contentScale = ContentScale.Crop,
                colorFilter = ColorFilter.colorMatrix(colorMatrix),
                modifier = Modifier.fillMaxSize()
            )
            
            // Apply vignette effect if needed
            if (filterState.vignette > 0f) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            androidx.compose.ui.graphics.Brush.radialGradient(
                                colors = listOf(
                                    Color.Transparent,
                                    Color.Black.copy(alpha = filterState.vignette)
                                ),
                                radius = 800f
                            )
                        )
                )
            }
            
            // Wallpaper info overlay
            Card(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.Black.copy(alpha = 0.7f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = wallpaper.title,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                    Text(
                        text = wallpaper.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}

private fun createColorMatrix(filterState: FilterState): ColorMatrix {
    // Create a simple color matrix that combines basic effects
    val brightness = filterState.brightness
    val contrast = filterState.contrast
    val saturation = filterState.saturation
    
    // Apply saturation and brightness/contrast effects
    val matrix = when {
        filterState.blackAndWhite > 0f -> {
            // Black and white effect
            val bwIntensity = filterState.blackAndWhite
            ColorMatrix(
                floatArrayOf(
                    0.299f * bwIntensity + brightness * (1 - bwIntensity), 0.587f * bwIntensity, 0.114f * bwIntensity, 0f, 0f,
                    0.299f * bwIntensity, 0.587f * bwIntensity + brightness * (1 - bwIntensity), 0.114f * bwIntensity, 0f, 0f,
                    0.299f * bwIntensity, 0.587f * bwIntensity, 0.114f * bwIntensity + brightness * (1 - bwIntensity), 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }
        filterState.sepia > 0f -> {
            // Sepia effect
            val sepiaIntensity = filterState.sepia
            ColorMatrix(
                floatArrayOf(
                    0.393f * sepiaIntensity + brightness * (1 - sepiaIntensity), 0.769f * sepiaIntensity, 0.189f * sepiaIntensity, 0f, 0f,
                    0.349f * sepiaIntensity, 0.686f * sepiaIntensity + brightness * (1 - sepiaIntensity), 0.168f * sepiaIntensity, 0f, 0f,
                    0.272f * sepiaIntensity, 0.534f * sepiaIntensity, 0.131f * sepiaIntensity + brightness * (1 - sepiaIntensity), 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }
        filterState.vintage > 0f -> {
            // Vintage effect
            val vintageIntensity = filterState.vintage
            ColorMatrix(
                floatArrayOf(
                    0.6f * vintageIntensity + brightness * (1 - vintageIntensity), 0.3f * vintageIntensity, 0.1f * vintageIntensity, 0f, 0f,
                    0.2f * vintageIntensity, 0.7f * vintageIntensity + brightness * (1 - vintageIntensity), 0.1f * vintageIntensity, 0f, 0f,
                    0.2f * vintageIntensity, 0.3f * vintageIntensity, 0.5f * vintageIntensity + brightness * (1 - vintageIntensity), 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }
        filterState.cool > 0f -> {
            // Cool tone effect
            val coolIntensity = filterState.cool
            ColorMatrix(
                floatArrayOf(
                    0.8f * coolIntensity + brightness * (1 - coolIntensity), 0.1f * coolIntensity, 0.1f * coolIntensity, 0f, 0f,
                    0.1f * coolIntensity, 0.9f * coolIntensity + brightness * (1 - coolIntensity), 0.1f * coolIntensity, 0f, 0f,
                    0.2f * coolIntensity, 0.2f * coolIntensity, 1.2f * coolIntensity + brightness * (1 - coolIntensity), 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }
        filterState.warm > 0f -> {
            // Warm tone effect
            val warmIntensity = filterState.warm
            ColorMatrix(
                floatArrayOf(
                    1.2f * warmIntensity + brightness * (1 - warmIntensity), 0.1f * warmIntensity, 0.1f * warmIntensity, 0f, 0f,
                    0.1f * warmIntensity, 1.1f * warmIntensity + brightness * (1 - warmIntensity), 0.1f * warmIntensity, 0f, 0f,
                    0.1f * warmIntensity, 0.1f * warmIntensity, 0.7f * warmIntensity + brightness * (1 - warmIntensity), 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }
        else -> {
            // Basic brightness, contrast, and saturation
            val sat = saturation
            val lumR = 0.3086f
            val lumG = 0.6094f
            val lumB = 0.0820f
            
            ColorMatrix(
                floatArrayOf(
                    (lumR * (1 - sat) + sat) * brightness * contrast, lumG * (1 - sat), lumB * (1 - sat), 0f, 0f,
                    lumR * (1 - sat), (lumG * (1 - sat) + sat) * brightness * contrast, lumB * (1 - sat), 0f, 0f,
                    lumR * (1 - sat), lumG * (1 - sat), (lumB * (1 - sat) + sat) * brightness * contrast, 0f, 0f,
                    0f, 0f, 0f, 1f, 0f
                )
            )
        }
    }
    
    return matrix
} 