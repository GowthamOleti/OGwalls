# OGWalls - Premium Wallpaper App

A beautiful, dark-themed wallpaper application built with Jetpack Compose and Material 3. Features a stunning onboarding experience with static card stacking and comprehensive wallpaper filtering capabilities.

## ✨ Features

### 🎨 **Premium Dark Design**
- Pure black backgrounds throughout the app
- SF Pro Display typography for crisp, modern text
- Consistent dark theme with blue accent colors
- Material 3 design principles

### 📱 **Stunning Onboarding**
- Static 3D card stack showcasing wallpapers
- Beautiful gradient backgrounds
- Smooth transitions and animations
- Configurable content and wallpapers

### 🖼️ **Wallpaper Management**
- High-quality wallpaper carousel with smooth paging
- Real-time filter application (brightness, contrast, saturation, etc.)
- Set wallpapers for home screen, lock screen, or both
- Loading states and error handling

### 🎛️ **Advanced Filtering**
- 10+ professional filters including:
  - Brightness, Contrast, Saturation
  - Sepia, Vintage, Black & White
  - Cool/Warm tones, Blur, Vignette
- Real-time preview with live updates
- Intuitive slider controls

## 🏗️ Architecture

### **Tech Stack**
- **Kotlin** - Modern Android development
- **Jetpack Compose** - Declarative UI framework
- **Material 3** - Latest Material Design
- **Navigation Compose** - Type-safe navigation
- **Coil** - Image loading and caching
- **Coroutines & Flow** - Asynchronous programming

### **Project Structure**
```
app/src/main/java/com/example/ogwalls/
├── data/
│   ├── model/              # Data models (Wallpaper, FilterState, etc.)
│   ├── repository/         # Data repository with sample wallpapers
│   └── OnboardingConfig.kt # Configuration for onboarding wallpapers
├── ui/
│   ├── component/          # Reusable UI components
│   │   ├── WallpaperCarousel.kt
│   │   ├── FilterControlPanel.kt
│   │   └── WallpaperPreview.kt
│   ├── screen/             # App screens
│   │   ├── OnboardingScreen.kt
│   │   ├── WallpaperScreen.kt
│   │   └── WallpaperDetailScreen.kt
│   └── theme/              # App theming
│       ├── Color.kt        # Dark theme color palette
│       ├── Theme.kt        # Material 3 theme setup
│       └── Type.kt         # SF Pro Display typography
├── utils/
│   └── WallpaperUtils.kt   # Wallpaper setting utilities
└── MainActivity.kt         # Main activity with navigation
```

## 🎨 Design System

### **Color Palette**
- **Background**: Pure Black (`#000000`)
- **Surface**: Dark Gray (`#111111`)
- **Cards**: Darker Gray (`#1A1A1A`)
- **Primary**: Accent Blue (`#4A90E2`)
- **Secondary**: Accent Orange (`#FF6B35`)
- **Text**: Pure White with opacity variants

### **Typography**
- **Font Family**: SF Pro Display (with system fallbacks)
- **Weights**: Light, Normal, Medium, SemiBold, Bold, ExtraBold
- **Responsive sizing** for different screen densities

## 🔧 Customization

### **Update Onboarding Wallpapers**
Edit `app/src/main/java/com/example/ogwalls/data/OnboardingConfig.kt`:

```kotlin
val featuredWallpapers = listOf(
    Wallpaper(
        id = "your_wallpaper",
        title = "Your Title",
        subtitle = "Your Collection",
        imageUrl = "https://your-image-url.jpg",
        // ... other properties
    )
)
```

### **Customize Text Content**
```kotlin
object Text {
    const val MAIN_TITLE = "Your Custom Title"
    const val SUBTITLE = "Your custom subtitle"
    const val BUTTON_TEXT = "Get Started"
}
```

### **Adjust Card Stack**
```kotlin
object CardStack {
    val FRONT_CARD_SIZE = Pair(240, 420)    // width, height in dp
    val MIDDLE_CARD_SIZE = Pair(220, 380)
    val BACK_CARD_SIZE = Pair(200, 350)
}
```

## 🚀 Getting Started

### **Prerequisites**
- Android Studio Arctic Fox or later
- Android SDK 21+ (Android 5.0+)
- Kotlin 1.8+

### **Setup**
1. Clone the repository
2. Open in Android Studio
3. Sync project with Gradle files
4. Run on device or emulator

### **Building**
```bash
./gradlew assembleDebug    # Debug build
./gradlew assembleRelease  # Release build
```

## 📱 App Flow

1. **Onboarding Screen**
   - Beautiful static card stack
   - Three layered phone mockups
   - Configurable wallpapers and text
   - "Get Started" button

2. **Wallpaper List**
   - Horizontal paging carousel
   - Page indicators and wallpaper info
   - Tap to open detail view

3. **Wallpaper Detail**
   - Full-screen wallpaper preview
   - Filter controls with live preview
   - Set wallpaper options (Home/Lock/Both)

## 🎯 Features in Detail

### **Static Card Stack**
- Three layered cards with depth and rotation
- Each card shows a different wallpaper
- Smooth scaling and opacity effects
- No device sensors required

### **Filter System**
- Tab-based filter selection
- Real-time value display (percentage)
- Smooth slider interactions
- Immediate preview updates

### **Wallpaper Setting**
- Android WallpaperManager integration
- Options for home screen, lock screen, or both
- Loading states during wallpaper setting
- Success/error feedback

## 🎨 Visual Highlights

- **Gradient Backgrounds**: Subtle radial gradients
- **Rounded Corners**: Consistent 16-28dp radius
- **Elevation**: Strategic use of shadows and depth
- **Typography**: Clean, readable SF Pro Display
- **Spacing**: Generous padding and margins
- **Colors**: High contrast for accessibility

## 🔮 Future Enhancements

- [ ] Remote wallpaper management via JSON API
- [ ] User favorites and collections
- [ ] Search and category filtering
- [ ] Social sharing capabilities
- [ ] Offline wallpaper downloads
- [ ] Custom wallpaper uploads

## 📄 License

This project is open source and available under the [MIT License](LICENSE).

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

---

**Built with ❤️ using Jetpack Compose and Material 3** 