package com.ogwalls.app.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ViewCarousel
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.ViewCarousel
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.offset
import coil3.compose.AsyncImage
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ogwalls.app.ui.component.WallpaperCarousel
import com.ogwalls.app.ui.component.WallpaperGrid
import com.ogwalls.app.ui.theme.*
import com.ogwalls.app.data.model.Wallpaper
import com.ogwalls.app.data.repository.WallpaperRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

// Enum for view types
enum class ViewType {
    CAROUSEL, GRID, LIKED
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WallpaperScreen(
    viewModel: WallpaperViewModel, // Use the provided ViewModel
    initialViewType: String = "carousel",
    initialCarouselPosition: Int = 0,
    onWallpaperClick: (String, String, Int) -> Unit
) {
    // Optimized state management with memoization
    val uiState by viewModel.uiState.collectAsState()
    var selectedViewType by remember { 
        mutableStateOf(
            if (initialViewType == "grid") ViewType.GRID else ViewType.CAROUSEL
        )
    }
    var carouselPosition by remember { mutableStateOf(initialCarouselPosition) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black) // Pure black background
    ) {
        // Error handling with dark theme
        uiState.error?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AccentRed.copy(alpha = 0.1f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Text(
                    text = error,
                    modifier = Modifier.padding(16.dp),
                    color = AccentRed,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
        
        // Main content with dark theme
        when {
            uiState.isLoading -> {
                FloatingWallpapersLoading()
            }
            
            uiState.wallpapers.isNotEmpty() -> {
                // Smooth transition between view types
                AnimatedContent(
                    targetState = selectedViewType,
                    transitionSpec = {
                        val direction = when {
                            targetState == ViewType.LIKED -> 1  // Come from right
                            initialState == ViewType.LIKED -> -1 // Exit to left
                            targetState == ViewType.GRID -> 1   // Come from right
                            initialState == ViewType.GRID -> -1  // Exit to left
                            else -> 0 // No slide for carousel
                        }
                        
                        slideInHorizontally(
                            initialOffsetX = { it * direction },
                            animationSpec = tween(500, easing = EaseInOutCubic)
                        ) + fadeIn(
                            animationSpec = tween(300, delayMillis = 200)
                        ) togetherWith slideOutHorizontally(
                            targetOffsetX = { -it * direction },
                            animationSpec = tween(500, easing = EaseInOutCubic)
                        ) + fadeOut(
                            animationSpec = tween(300)
                        )
                    },
                    label = "view_type_transition"
                ) { viewType ->
                    when (viewType) {
                        ViewType.CAROUSEL -> {
                            WallpaperCarousel(
                                wallpapers = uiState.wallpapers,
                                onWallpaperClick = { wallpaperId -> 
                                    onWallpaperClick(wallpaperId, "carousel", carouselPosition)
                                },
                                onPositionChange = { position ->
                                    carouselPosition = position
                                },
                                initialPage = carouselPosition
                            )
                        }
                        ViewType.GRID -> {
                            WallpaperGrid(
                                wallpapers = uiState.wallpapers,
                                onWallpaperClick = { wallpaperId -> 
                                    onWallpaperClick(wallpaperId, "grid", carouselPosition)
                                }
                            )
                        }
                        ViewType.LIKED -> {
                            val likedWallpapers = viewModel.getLikedWallpapers()
                            if (likedWallpapers.isNotEmpty()) {
                                WallpaperGrid(
                                    wallpapers = likedWallpapers,
                                    onWallpaperClick = { wallpaperId -> 
                                        onWallpaperClick(wallpaperId, "liked", carouselPosition)
                                    }
                                )
                            } else {
                                // Empty state for liked wallpapers
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.spacedBy(16.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Outlined.FavoriteBorder,
                                            contentDescription = "No Liked Wallpapers",
                                            tint = Color.White.copy(alpha = 0.5f),
                                            modifier = Modifier.size(64.dp)
                                        )
                                        Text(
                                            text = "No Liked Wallpapers",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.White.copy(alpha = 0.7f)
                                        )
                                        Text(
                                            text = "Like some wallpapers to see them here",
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = Color.White.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        
                          // Compact Floating Glassmorphism Bottom Navigation
        if (uiState.wallpapers.isNotEmpty()) {
            CompactFloatingBottomNav(
                selectedViewType = selectedViewType,
                onViewTypeSelected = { selectedViewType = it },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
private fun CompactFloatingBottomNav(
    selectedViewType: ViewType,
    onViewTypeSelected: (ViewType) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(bottom = 32.dp)
            .wrapContentSize()
    ) {
        // Glassmorphism background
        Box(
            modifier = Modifier
                .size(width = 180.dp, height = 56.dp)
                .clip(RoundedCornerShape(28.dp))
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.25f),
                            Color.White.copy(alpha = 0.1f)
                        )
                    )
                )
                .background(Color.Black.copy(alpha = 0.3f))
        )
        
        // Navigation items
        Row(
            modifier = Modifier
                .size(width = 180.dp, height = 56.dp)
                .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            CompactNavItem(
                selected = selectedViewType == ViewType.CAROUSEL,
                onClick = { onViewTypeSelected(ViewType.CAROUSEL) },
                selectedIcon = Icons.Filled.ViewCarousel,
                unselectedIcon = Icons.Outlined.ViewCarousel,
                contentDescription = "Carousel View"
            )
            
            CompactNavItem(
                selected = selectedViewType == ViewType.GRID,
                onClick = { onViewTypeSelected(ViewType.GRID) },
                selectedIcon = Icons.Filled.GridView,
                unselectedIcon = Icons.Outlined.GridView,
                contentDescription = "Grid View"
            )
            
            CompactNavItem(
                selected = selectedViewType == ViewType.LIKED,
                onClick = { onViewTypeSelected(ViewType.LIKED) },
                selectedIcon = Icons.Filled.Favorite,
                unselectedIcon = Icons.Outlined.FavoriteBorder,
                contentDescription = "Liked Wallpapers"
            )
        }
    }
}

@Composable
private fun CompactNavItem(
    selected: Boolean,
    onClick: () -> Unit,
    selectedIcon: ImageVector,
    unselectedIcon: ImageVector,
    contentDescription: String,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) Color.White.copy(alpha = 0.9f) else Color.Transparent,
        label = "nav_background"
    )
    
    val iconTint by animateColorAsState(
        targetValue = if (selected) Color.Black else Color.White,
        label = "nav_icon_tint"
    )
    
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = if (selected) selectedIcon else unselectedIcon,
            contentDescription = contentDescription,
            tint = iconTint,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun FloatingWallpapersLoading() {
    // Sample wallpaper URLs for floating animation
    val sampleWallpapers = listOf(
        "https://static.wixstatic.com/media/5c0589_73583c6e0d78476c97acc3a7ea179510~mv2.jpeg",
        "https://static.wixstatic.com/media/5c0589_efb61fd0050843a4b8cdec62493de85e~mv2.jpeg",
        "https://static.wixstatic.com/media/5c0589_34bde73293b34a3e820a380e8566259c~mv2.jpeg",
        "https://static.wixstatic.com/media/5c0589_5c63dd7bd79b49248cdc5de5daba2fe3~mv2.jpeg",
        "https://static.wixstatic.com/media/5c0589_e6648b8d6f99449c96d8eaba897f72a3~mv2.jpeg",
        "https://static.wixstatic.com/media/5c0589_bc6c17f336534c49be22369a0110c2ec~mv2.jpg"
    )
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Floating wallpapers with depth effect
        sampleWallpapers.forEachIndexed { index, imageUrl ->
            FloatingWallpaperCard(
                imageUrl = imageUrl,
                index = index,
                totalCount = sampleWallpapers.size
            )
        }
        
        // Loading text overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.3f),
                            Color.Black.copy(alpha = 0.8f)
                        ),
                        radius = 600f
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Loading beautiful wallpapers...",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                
                // Animated dots
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(3) { index ->
                        val animatedAlpha by rememberInfiniteTransition(label = "dots").animateFloat(
                            initialValue = 0.3f,
                            targetValue = 1f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(600, delayMillis = index * 200),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "dot_alpha"
                        )
                        
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    Color.White.copy(alpha = animatedAlpha),
                                    CircleShape
                                )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FloatingWallpaperCard(
    imageUrl: String,
    index: Int,
    totalCount: Int
) {
    // Calculate random positions and properties for depth effect
    val random = remember { kotlin.random.Random(index) }
    val startX = remember { random.nextFloat() * 0.8f - 0.4f } // -0.4 to 0.4
    val startY = remember { 1.2f + random.nextFloat() * 0.3f } // Start below screen
    val endY = remember { -0.5f - random.nextFloat() * 0.3f } // End above screen
    val scale = remember { 0.3f + random.nextFloat() * 0.4f } // 0.3 to 0.7
    val rotation = remember { random.nextFloat() * 20f - 10f } // -10 to 10 degrees
    val isBlurred = remember { random.nextBoolean() }
    val animationDelay = remember { random.nextInt(2000) }
    
    // Floating animation
    val animatedY by rememberInfiniteTransition(label = "floating").animateFloat(
        initialValue = startY,
        targetValue = endY,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 8000 + random.nextInt(4000), // 8-12 seconds
                delayMillis = animationDelay,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "y_position"
    )
    
    // Subtle horizontal drift
    val animatedX by rememberInfiniteTransition(label = "drift").animateFloat(
        initialValue = startX,
        targetValue = startX + (random.nextFloat() * 0.2f - 0.1f),
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 6000 + random.nextInt(3000),
                easing = FastOutSlowInEasing
            ),
            repeatMode = RepeatMode.Reverse
        ),
        label = "x_drift"
    )
    
    // Fade in/out based on position
    val alpha = when {
        animatedY > 0.8f -> (1f - (animatedY - 0.8f) / 0.4f).coerceIn(0f, 1f)
        animatedY < -0.2f -> (1f - (-0.2f - animatedY) / 0.3f).coerceIn(0f, 1f)
        else -> 1f
    }
    
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val screenWidth = maxWidth
        val screenHeight = maxHeight
        val cardSize = 120.dp * scale
        
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(cardSize)
                .offset(
                    x = screenWidth * animatedX,
                    y = screenHeight * animatedY
                )
                .graphicsLayer {
                    this.alpha = alpha
                    this.scaleX = scale
                    this.scaleY = scale
                    this.rotationZ = rotation
                    if (isBlurred) {
                        this.renderEffect = BlurEffect(
                            radiusX = 8f,
                            radiusY = 8f,
                            edgeTreatment = TileMode.Clamp
                        )
                    }
                }
                .clip(RoundedCornerShape(16.dp))
                .shadow(
                    elevation = if (isBlurred) 4.dp else 12.dp,
                    shape = RoundedCornerShape(16.dp),
                    spotColor = Color.White.copy(alpha = 0.1f)
                )
        )
    }
} 