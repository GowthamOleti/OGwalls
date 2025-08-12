package com.ogwalls.app.data

import com.ogwalls.app.data.model.Wallpaper
import com.ogwalls.app.data.MyWallpapers

/**
 * Configuration for onboarding screen wallpapers
 * Update this file to change the wallpapers shown in the onboarding cards
 */
object OnboardingConfig {
    
    /**
     * Provides three reliable wallpapers from your Wix gallery for the onboarding screen.
     * Uses a curated selection of the most reliable URLs from your collection.
     */
    fun getOnboardingWallpapers(): List<Wallpaper> {
        // Curated list of the most reliable Wix URLs from your collection
        val reliableWallpapers = listOf(
            // These are the most reliable ones based on URL structure
            MyWallpapers.wallpapers.find { it.id == "1" }, // Abstract Gradient
            MyWallpapers.wallpapers.find { it.id == "2" }, // Geometric Pattern
            MyWallpapers.wallpapers.find { it.id == "3" }, // Nature Landscape
            MyWallpapers.wallpapers.find { it.id == "4" }, // Minimal Design
            MyWallpapers.wallpapers.find { it.id == "5" }, // Colorful Abstract
            MyWallpapers.wallpapers.find { it.id == "6" }, // Dark Elegance
            MyWallpapers.wallpapers.find { it.id == "7" }, // Light Patterns
            MyWallpapers.wallpapers.find { it.id == "8" }, // Ocean Waves
            MyWallpapers.wallpapers.find { it.id == "9" }, // Mountain Vista
            MyWallpapers.wallpapers.find { it.id == "10" } // Urban Night
        ).filterNotNull()
        
        return reliableWallpapers.shuffled().take(3)
    }
    
    /**
     * Onboarding text content
     * Customize the main title and subtitle
     */
    object Text {
        const val MAIN_TITLE = "From my camera roll"
        const val SUBTITLE = "Snapped, saved, and shared from my journey."
        const val BUTTON_TEXT = "Explore wallpapers"
    }
    
    /**
     * Animation settings for the gyroscope effect
     */
    object Animation {
        const val TILT_SENSITIVITY = 30f  // Lower = more sensitive
        const val MAX_ROTATION = 25f      // Maximum rotation in degrees
        const val PARALLAX_FACTOR = 50f   // Parallax movement factor
    }
    
    /**
     * Card stack configuration
     */
    object CardStack {
        val FRONT_CARD_SIZE = Pair(240, 420)    // width, height in dp
        val MIDDLE_CARD_SIZE = Pair(220, 380)   // width, height in dp
        val BACK_CARD_SIZE = Pair(200, 350)     // width, height in dp
        
        const val FRONT_CARD_ALPHA = 1.0f
        const val MIDDLE_CARD_ALPHA = 0.8f
        const val BACK_CARD_ALPHA = 0.6f
    }
} 