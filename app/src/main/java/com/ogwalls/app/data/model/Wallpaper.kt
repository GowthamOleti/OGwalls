package com.ogwalls.app.data.model

data class Wallpaper(
    val id: String,
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val thumbnailUrl: String,
    val category: String,
    val photographer: String,
    val resolution: String,
    val isPortrait: Boolean = true,
    val tags: List<String> = emptyList(),
    val downloadCount: Int = 0,
    val isFavorite: Boolean = false
)

data class WallpaperFilter(
    val name: String,
    val intensity: Float = 1.0f,
    val type: FilterType
)

enum class FilterType {
    NONE,
    BRIGHTNESS,
    CONTRAST,
    SATURATION,
    SEPIA,
    BLUR,
    VINTAGE,
    COOL,
    WARM,
    BLACK_AND_WHITE,
    VIGNETTE
}

data class FilterState(
    val brightness: Float = 1.0f,  // 100% - neutral starting point
    val contrast: Float = 1.0f,    // 100% - neutral starting point
    val saturation: Float = 1.0f,  // 100% - neutral starting point
    val sepia: Float = 0.0f,
    val blur: Float = 0.0f,
    val vintage: Float = 0.0f,
    val cool: Float = 0.0f,
    val warm: Float = 0.0f,
    val blackAndWhite: Float = 0.0f,
    val vignette: Float = 0.0f
)

enum class WallpaperType {
    HOME_SCREEN,
    LOCK_SCREEN,
    BOTH
} 