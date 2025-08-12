package com.ogwalls.app.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.ogwalls.app.data.model.Wallpaper
import com.ogwalls.app.data.MyWallpapers // 🎨 Using your custom wallpapers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WallpaperRepository(private val context: Context) {
    
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("wallpaper_prefs", Context.MODE_PRIVATE)
    private val likedWallpapersKey = "liked_wallpapers"
    
    fun getWallpapers(): Flow<List<Wallpaper>> = flow {
        // Simulate network delay
        delay(1000)
        val wallpapers = getSampleWallpapers()
        val likedWallpapers = getLikedWallpaperIds()
        
        // Update isFavorite status based on saved preferences
        val updatedWallpapers = wallpapers.map { wallpaper ->
            wallpaper.copy(isFavorite = likedWallpapers.contains(wallpaper.id))
        }
        
        emit(updatedWallpapers)
    }
    
    suspend fun getWallpaperById(id: String): Wallpaper? {
        delay(500)
        val wallpaper = getSampleWallpapers().find { it.id == id }
        val likedWallpapers = getLikedWallpaperIds()
        
        return wallpaper?.copy(isFavorite = likedWallpapers.contains(id))
    }
    
    suspend fun toggleFavorite(wallpaperId: String): Boolean {
        try {
            val likedWallpapers = getLikedWallpaperIds().toMutableSet()
            
            if (likedWallpapers.contains(wallpaperId)) {
                likedWallpapers.remove(wallpaperId)
            } else {
                likedWallpapers.add(wallpaperId)
            }
            
            // Save to SharedPreferences
            val editor = sharedPreferences.edit()
            editor.putStringSet(likedWallpapersKey, likedWallpapers)
            editor.apply()
            
        return true
        } catch (e: Exception) {
            return false
        }
    }
    
    private fun getLikedWallpaperIds(): Set<String> {
        return sharedPreferences.getStringSet(likedWallpapersKey, emptySet()) ?: emptySet()
    }
    
    private fun getSampleWallpapers(): List<Wallpaper> {
        // 🎨 TO USE YOUR OWN WALLPAPERS:
        // 1. Edit MyWallpapers.kt file
        // 2. Uncomment the line below to use your custom wallpapers
        // 3. Comment out the default return statement
        
        return MyWallpapers.wallpapers
        
        // Default wallpapers (commented out - using custom wallpapers)
        // return getDefaultWallpapers()
    }
    
    private fun getDefaultWallpapers(): List<Wallpaper> {
        return listOf(
            Wallpaper(
                id = "1",
                title = "Mountain Peak",
                subtitle = "Majestic alpine landscape with snow-capped peaks",
                imageUrl = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1080&q=80",
                thumbnailUrl = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=400&q=80",
                category = "Nature",
                photographer = "John Doe",
                resolution = "1080x1920",
                tags = listOf("mountains", "nature", "landscape"),
                downloadCount = 1250
            ),
            Wallpaper(
                id = "2",
                title = "Ocean Waves",
                subtitle = "Peaceful blue waters meeting the shore",
                imageUrl = "https://images.unsplash.com/photo-1505142468610-359e7d316be0?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1080&q=80",
                thumbnailUrl = "https://images.unsplash.com/photo-1505142468610-359e7d316be0?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=400&q=80",
                category = "Nature",
                photographer = "Jane Smith",
                resolution = "1080x1920",
                tags = listOf("ocean", "waves", "blue"),
                downloadCount = 890
            ),
            Wallpaper(
                id = "3",
                title = "Desert Dunes",
                subtitle = "Golden sand formations under clear skies",
                imageUrl = "https://images.unsplash.com/photo-1519501025264-65ba15a82390?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1080&q=80",
                thumbnailUrl = "https://images.unsplash.com/photo-1519501025264-65ba15a82390?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=400&q=80",
                category = "Nature",
                photographer = "Alex Johnson",
                resolution = "1080x1920",
                tags = listOf("desert", "sand", "landscape"),
                downloadCount = 1520
            ),
            Wallpaper(
                id = "4",
                title = "Forest Trail",
                subtitle = "Mystical path through ancient woods",
                imageUrl = "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1080&q=80",
                thumbnailUrl = "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=400&q=80",
                category = "Nature",
                photographer = "Emma Wilson",
                resolution = "1080x1920",
                tags = listOf("forest", "trees", "path"),
                downloadCount = 675
            ),
            Wallpaper(
                id = "5",
                title = "Northern Lights",
                subtitle = "Aurora borealis dancing across the night sky",
                imageUrl = "https://images.unsplash.com/photo-1557672172-298e090bd0f1?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1080&q=80",
                thumbnailUrl = "https://images.unsplash.com/photo-1557672172-298e090bd0f1?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=400&q=80",
                category = "Space",
                photographer = "Michael Chen",
                resolution = "1080x1920",
                tags = listOf("aurora", "lights", "sky"),
                downloadCount = 2100
            ),
            Wallpaper(
                id = "6",
                title = "Mountain Reflection",
                subtitle = "Perfect mirror image in crystal clear lake",
                imageUrl = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1080&q=80",
                thumbnailUrl = "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=400&q=80",
                category = "Nature",
                photographer = "Sarah Davis",
                resolution = "1080x1920",
                tags = listOf("mountains", "reflection", "lake"),
                downloadCount = 1340
            )
        )
    }
} 