package com.ogwalls.app.ui.component

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.pager.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.compose.foundation.Canvas
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.ogwalls.app.data.model.Wallpaper
import com.ogwalls.app.ui.component.ImageWithFallback
import kotlin.math.absoluteValue

@OptIn(ExperimentalFoundationApi::class, ExperimentalAnimationApi::class)
@Composable
fun WallpaperCarousel(
    wallpapers: List<Wallpaper>,
    onWallpaperClick: (String) -> Unit,
    onPositionChange: (Int) -> Unit = {},
    initialPage: Int = 0
) {
    // Use provided initial page for position preservation
    val pagerState = rememberPagerState(
        initialPage = initialPage.coerceIn(0, wallpapers.size - 1),
        pageCount = { wallpapers.size }
    )
    val hapticFeedback = LocalHapticFeedback.current
    
    // Optimized position tracking with haptic feedback for every image
    LaunchedEffect(pagerState.currentPage) {
        onPositionChange(pagerState.currentPage)
        
        // Haptic feedback for every image change
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        // Optimized background - only load current wallpaper for better performance
        val currentWallpaperIndex = pagerState.currentPage
        if (currentWallpaperIndex in wallpapers.indices) {
            val wallpaper = wallpapers[currentWallpaperIndex]
            
            ImageWithFallback(
                imageUrl = wallpaper.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                    .blur(radius = 30.dp) // Further reduced blur for better performance
                    .alpha(0.3f)
                )
        }

        // Main Content - properly spaced with status bar padding
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding() // Add status bar padding
                .navigationBarsPadding() // Add navigation bar padding for 3-button nav
                .padding(
                    top = 20.dp, // Additional spacing below status bar
                    bottom = 140.dp // Increased space for bottom navigation + 3-button nav
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Enhanced Image Carousel with Parallax Effect
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.85f),
                pageSpacing = 32.dp,
                contentPadding = PaddingValues(horizontal = 40.dp)
            ) { page ->
                val wallpaper = wallpapers[page]
                
                // Calculate page offset for parallax effects
                val pageOffset = (
                    (pagerState.currentPage - page) + pagerState
                        .currentPageOffsetFraction
                )
                val pageOffsetAbs = pageOffset.absoluteValue
                
                Box(
                    modifier = Modifier
                        .graphicsLayer {
                            // Optimized parallax effects for better performance
                            val scale = lerp(
                                start = 0.85f,
                                stop = 1f,
                                fraction = 1f - pageOffsetAbs.coerceIn(0f, 1f)
                            )
                            scaleX = scale
                            scaleY = scale

                            // Simplified alpha effect
                            alpha = lerp(
                                start = 0.5f,
                                stop = 1f,
                                fraction = 1f - pageOffsetAbs.coerceIn(0f, 1f)
                            )
                            
                            // Reduced translation for smoother performance
                            translationX = pageOffset * 30f
                            
                            // Reduced rotation for better performance
                            rotationY = pageOffset * 6f
                        }
                ) {
                    // Optimized image container with simplified shadow
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(32.dp))
                            .shadow(
                                elevation = if (pageOffsetAbs < 0.5f) 12.dp else 8.dp,
                                shape = RoundedCornerShape(32.dp),
                                spotColor = Color.White.copy(alpha = 0.1f)
                            )
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color.White.copy(alpha = 0.1f),
                                        Color.Transparent
                                    ),
                                    startY = 0f,
                                    endY = 100f
                                )
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                                onClick = { 
                                    hapticFeedback.performHapticFeedback(HapticFeedbackType.TextHandleMove)
                                    onWallpaperClick(wallpaper.id) 
                                }
                            )
                    ) {
                        // Main wallpaper image
                        ImageWithFallback(
                            imageUrl = wallpaper.imageUrl,
                            contentDescription = wallpaper.title,
                            modifier = Modifier.fillMaxSize()
                        )
                        
                        // Parallax overlay gradient for depth
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.1f)
                                        ),
                                        radius = 300f
                                    )
                                )
                        )
                        
                        // Subtle vignette effect
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            Color.Black.copy(alpha = 0.15f)
                                        ),
                                        radius = 400f
                                    )
                                )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Reduced spacing

            // Title and subtitle with smooth transitions - positioned behind image
            AnimatedContent(
                targetState = pagerState.currentPage,
                transitionSpec = {
                    (slideInVertically { height -> height } + fadeIn()).togetherWith(
                        slideOutVertically { height -> -height } + fadeOut()
                    ).using(SizeTransform(clip = false))
                },
                label = "title_transition",
                modifier = Modifier.zIndex(-1f) // Move text behind the image
            ) { wallpaperIndex ->
                val wallpaper = wallpapers[wallpaperIndex]
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = wallpaper.title,
                        style = MaterialTheme.typography.headlineMedium.copy(
                            letterSpacing = (-1).sp
                        ),
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = wallpaper.photographer,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            letterSpacing = 0.5.sp
                        ),
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Reduced spacing

            // Enhanced Pager Indicator with 3D Parallax Effects
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        // Enhanced 3D parallax for indicator
                        val pageOffset = pagerState.currentPageOffsetFraction
                        translationY = pageOffset * 8f
                        translationX = pageOffset * 12f
                    }
            ) {
                PagerIndicator(
                    pagerState = pagerState,
                    wallpaperCount = wallpapers.size,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PagerIndicator(
    pagerState: PagerState,
    wallpaperCount: Int,
    modifier: Modifier = Modifier
) {
    val dotSize = 8.dp
    val dotSpacing = 10.dp // Reduced from 16.dp to 10.dp
    val activeDotSize = 10.dp
    val density = LocalDensity.current
    
    Box(
        modifier = modifier.height(40.dp),
        contentAlignment = Alignment.Center
    ) {
        // Calculate the sliding offset
        val currentIndex = pagerState.currentPage
        val pageOffset = pagerState.currentPageOffsetFraction
        
        // Calculate the translation offset for sliding effect
        val translationX = with(density) { 
            -pageOffset * (dotSize + dotSpacing).toPx()
        }
        
        Row(
            horizontalArrangement = Arrangement.spacedBy(dotSpacing),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.graphicsLayer {
                // Apply horizontal translation to create sliding effect
                this.translationX = translationX
            }
        ) {
            // Show more dots to create seamless infinite scroll effect
            val visibleDots = 5 // Show 5 dots so sliding looks smooth
            val startIndex = currentIndex - 2
            
            repeat(visibleDots) { index ->
                val dotIndex = (startIndex + index + wallpaperCount * 1000) % wallpaperCount
                val distanceFromCenter = kotlin.math.abs(index - 2) // Center is at index 2
                
                // Calculate size and alpha based on distance from center
                val targetSize = when {
                    distanceFromCenter == 0 -> activeDotSize
                    distanceFromCenter <= 1 -> dotSize
                    else -> dotSize * 0.6f
                }
                
                // More faded and darker for inactive dots
                val targetAlpha = when {
                    distanceFromCenter == 0 -> 1f // Active dot - bright white
                    distanceFromCenter <= 1 -> 0.35f // Adjacent dots - more faded
                    else -> 0.15f // Far dots - very faded
                }
                
                // Use different colors for more contrast
                val dotColor = when {
                    distanceFromCenter == 0 -> Color.White // Active - pure white
                    else -> Color.Gray // Inactive - gray color for darker appearance
                }
                
                // Animate size and alpha
                val animatedSize by animateFloatAsState(
                    targetValue = targetSize.value,
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioMediumBouncy,
                        stiffness = Spring.StiffnessHigh
                    ),
                    label = "dot_size"
                )
                
                val animatedAlpha by animateFloatAsState(
                    targetValue = targetAlpha,
                    animationSpec = tween(200),
                    label = "dot_alpha"
                )
                
                Box(
                    modifier = Modifier
                        .size(animatedSize.dp)
                        .clip(CircleShape)
                        .background(dotColor.copy(alpha = animatedAlpha))
                )
            }
        }
    }
} 