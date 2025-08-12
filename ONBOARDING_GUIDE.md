# OGWalls Onboarding Screen Guide

## Overview
Your OGWalls app now features a stunning onboarding screen with dynamic wallpaper cards and gyroscope tilt effects. This guide explains how to customize and manage the onboarding experience.

## Features

### 🎨 Dynamic Wallpaper Cards
- **3D Card Stack**: Three layered phone mockups showing different wallpapers
- **Gyroscope Effect**: Cards tilt and move based on device orientation
- **Easy Updates**: Change wallpapers without touching code

### 📱 Gyroscope Tilt Animation
- **Real-time Response**: Cards respond to phone tilting
- **Smooth Movement**: Parallax effects and 3D rotations
- **Configurable Sensitivity**: Adjust tilt response in configuration

### 🎭 Customizable Content
- **Text Content**: Main title, subtitle, and button text
- **Animation Settings**: Tilt sensitivity, rotation limits, parallax factors
- **Card Configuration**: Sizes, opacity, and layering

## How to Update Wallpapers

### Quick Update
1. Open `app/src/main/java/com/example/ogwalls/data/OnboardingConfig.kt`
2. Replace any wallpaper URLs in the `featuredWallpapers` list
3. Update title, subtitle, category, and photographer as needed
4. Build and run the app

### Example Update:
```kotlin
Wallpaper(
    id = "onboarding_beach",
    title = "Tropical Beach",
    subtitle = "Paradise Collection",
    imageUrl = "YOUR_NEW_WALLPAPER_URL",
    thumbnailUrl = "YOUR_NEW_THUMBNAIL_URL",
    category = "Nature",
    photographer = "Your Name",
    resolution = "1080x1920",
    tags = listOf("beach", "ocean", "tropical", "paradise")
)
```

## Configuration Options

### Text Content
```kotlin
object Text {
    const val MAIN_TITLE = "Your Custom Title"
    const val SUBTITLE = "Your custom subtitle here"
    const val BUTTON_TEXT = "Let's Go!"
}
```

### Animation Settings
```kotlin
object Animation {
    const val TILT_SENSITIVITY = 30f  // Lower = more sensitive
    const val MAX_ROTATION = 25f      // Maximum rotation in degrees
    const val PARALLAX_FACTOR = 50f   // Parallax movement factor
}
```

### Card Stack Settings
```kotlin
object CardStack {
    val FRONT_CARD_SIZE = Pair(240, 420)    // width, height in dp
    val MIDDLE_CARD_SIZE = Pair(220, 380)   // width, height in dp
    val BACK_CARD_SIZE = Pair(200, 350)     // width, height in dp
    
    const val FRONT_CARD_ALPHA = 1.0f      // Front card opacity
    const val MIDDLE_CARD_ALPHA = 0.8f     // Middle card opacity
    const val BACK_CARD_ALPHA = 0.6f       // Back card opacity
}
```

## Helper Functions

### Random Wallpapers
```kotlin
// Get 3 random wallpapers for variety
OnboardingConfig.getRandomWallpapers(3)
```

### Category-based Selection
```kotlin
// Get wallpapers from specific category
OnboardingConfig.getWallpapersByCategory("Nature")
```

## Testing the Gyroscope

### On Real Device
1. Install the app on a physical Android device
2. Open the app (starts with onboarding screen)
3. Slowly tilt your phone in different directions
4. Watch the cards move and rotate in 3D space

### On Emulator
- The gyroscope effect won't work on emulators
- You'll still see the beautiful static card layout
- Text and button functionality will work normally

## Image Requirements

### Best Practices
- **Resolution**: 1080x1920 (portrait orientation)
- **Format**: JPG or PNG
- **Size**: Keep under 2MB for fast loading
- **Aspect Ratio**: 9:16 (portrait)

### Recommended Sources
- **Unsplash**: Free high-quality images
- **Pexels**: Free stock photos
- **Your Own**: Custom wallpapers you create

## Navigation Flow

1. **Onboarding Screen**: First screen with 3D cards
2. **Get Started Button**: Navigates to main wallpaper list
3. **No Going Back**: Onboarding is removed from navigation stack

## Troubleshooting

### Cards Not Moving
- Ensure you're testing on a real device (not emulator)
- Check that sensor permissions are granted
- Verify device has accelerometer and magnetometer

### Images Not Loading
- Check internet connection
- Verify image URLs are accessible
- Ensure URLs use HTTPS protocol

### Performance Issues
- Reduce image sizes if loading slowly
- Lower the `TILT_SENSITIVITY` for smoother animation
- Reduce `PARALLAX_FACTOR` for less movement

## Future Enhancements

### Planned Features
- **Remote Configuration**: Load wallpapers from online JSON
- **User Preferences**: Remember if user has seen onboarding
- **Swipe Gestures**: Allow manual card switching
- **Loading States**: Show progress while images load

### Easy Additions
- More wallpapers in the configuration
- Different card layouts (circular, diamond, etc.)
- Custom gradient backgrounds
- Sound effects for interactions

## File Structure
```
app/src/main/java/com/example/ogwalls/
├── data/
│   └── OnboardingConfig.kt          # Main configuration file
├── ui/screen/
│   └── OnboardingScreen.kt          # Onboarding screen implementation
└── MainActivity.kt                   # Navigation setup
```

---

**Need help?** The configuration file is well-documented with comments explaining each setting. Start there for any customizations! 