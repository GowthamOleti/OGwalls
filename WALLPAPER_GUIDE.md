# 🎨 Adding Your Own Wallpapers to OGWalls

## 📋 **Quick Overview**

There are **3 ways** to add your wallpaper pictures:
1. **Local Images** - Store in app (easiest, offline)
2. **Online URLs** - Host online (flexible, smaller app size)
3. **Hybrid** - Mix of both approaches

---

## 🎯 **Option 1: Local Images (Recommended)**

### **Step 1: Prepare Your Images**
- **Format**: JPG or PNG
- **Resolution**: 1080x1920 (9:16 ratio) or higher
- **Size**: Under 2MB each for best performance
- **Names**: Use lowercase, no spaces (e.g., `mountain_sunset.jpg`)

### **Step 2: Add Images to App**
1. Copy your images to: `app/src/main/res/drawable/`
2. Example files:
   ```
   app/src/main/res/drawable/
   ├── my_wallpaper_1.jpg
   ├── my_wallpaper_2.jpg
   ├── my_wallpaper_3.jpg
   └── ...
   ```

### **Step 3: Update Configuration**

**For Onboarding Screen** (`OnboardingConfig.kt`):
```kotlin
val featuredWallpapers = listOf(
    Wallpaper(
        id = "my_wallpaper_1",
        title = "Your Custom Title",
        subtitle = "Your Collection",
        imageUrl = "android.resource://com.example.ogwalls/drawable/my_wallpaper_1",
        thumbnailUrl = "android.resource://com.example.ogwalls/drawable/my_wallpaper_1",
        category = "Custom",
        photographer = "Your Name",
        resolution = "1080x1920",
        tags = listOf("custom", "personal")
    ),
    // Add more wallpapers...
)
```

**For Main Gallery** (`WallpaperRepository.kt`):
```kotlin
private fun getSampleWallpapers(): List<Wallpaper> {
    return listOf(
        Wallpaper(
            id = "custom_1",
            title = "My Amazing Photo",
            subtitle = "Personal Collection",
            imageUrl = "android.resource://com.example.ogwalls/drawable/my_wallpaper_1",
            thumbnailUrl = "android.resource://com.example.ogwalls/drawable/my_wallpaper_1",
            category = "Personal",
            photographer = "Me",
            resolution = "1080x1920"
        ),
        // Add more...
    )
}
```

---

## 🌐 **Option 2: Online URLs (Most Flexible)**

### **Step 1: Host Your Images**
Upload your wallpapers to any of these **free services**:

- **GitHub** (free, reliable)
- **Imgur** (easy upload)
- **Google Drive** (with public links)
- **Firebase Storage** (professional)
- **Cloudinary** (with optimization)

### **Step 2: Get Direct URLs**
Make sure your URLs are **direct image links** ending in `.jpg` or `.png`

Example:
```
✅ Good: https://i.imgur.com/ABC123.jpg
❌ Bad:  https://imgur.com/ABC123
```

### **Step 3: Update URLs**
Simply replace the Unsplash URLs with your own:

```kotlin
Wallpaper(
    id = "my_photo_1",
    title = "My Awesome Wallpaper",
    subtitle = "Personal Collection", 
    imageUrl = "https://your-image-host.com/wallpaper1.jpg",
    thumbnailUrl = "https://your-image-host.com/wallpaper1_thumb.jpg",
    category = "Personal",
    photographer = "Your Name",
    resolution = "1080x1920"
)
```

---

## 🔄 **Option 3: Hybrid Approach**

Mix local and online images for maximum flexibility:

```kotlin
val featuredWallpapers = listOf(
    // Local image
    Wallpaper(
        id = "local_1",
        title = "Local Wallpaper",
        imageUrl = "android.resource://com.example.ogwalls/drawable/local_image",
        // ... other properties
    ),
    // Online image  
    Wallpaper(
        id = "online_1",
        title = "Online Wallpaper",
        imageUrl = "https://your-host.com/image.jpg",
        // ... other properties
    )
)
```

---

## ⚡ **Quick Start Templates**

### **Template 1: Replace Onboarding Images**
```kotlin
// In OnboardingConfig.kt
val featuredWallpapers = listOf(
    Wallpaper(
        id = "my_wallpaper_1",
        title = "Your Photo Title",
        subtitle = "Your Collection Name",
        imageUrl = "YOUR_IMAGE_URL_HERE", // Replace this
        thumbnailUrl = "YOUR_THUMBNAIL_URL_HERE", // Replace this
        category = "Personal",
        photographer = "Your Name",
        resolution = "1080x1920",
        tags = listOf("personal", "custom", "favorite")
    ),
    // Copy and modify for more wallpapers
)
```

### **Template 2: Replace Gallery Images**
```kotlin
// In WallpaperRepository.kt - getSampleWallpapers() function
return listOf(
    Wallpaper(
        id = "gallery_1",
        title = "Your Gallery Photo 1",
        subtitle = "Personal Collection",
        imageUrl = "YOUR_IMAGE_URL_HERE", // Replace this
        thumbnailUrl = "YOUR_THUMBNAIL_URL_HERE", // Replace this  
        category = "Personal",
        photographer = "Your Name",
        resolution = "1080x1920",
        tags = listOf("personal", "gallery"),
        downloadCount = 0
    ),
    // Add more wallpapers...
)
```

---

## 🎨 **Image Optimization Tips**

### **Best Practices**
- **Aspect Ratio**: 9:16 (portrait) works best for phone wallpapers
- **Resolution**: 1080x1920 minimum, 1440x2560 ideal
- **File Size**: 500KB - 2MB per image
- **Format**: JPG for photos, PNG for graphics with transparency

### **Tools for Optimization**
- **Online**: TinyPNG, Squoosh.app
- **Mac**: ImageOptim, Preview app
- **Mobile**: Any photo editor app

### **Thumbnail Creation**
Create smaller versions (400x711) for faster loading:
- Use same image with lower quality/resolution
- Or crop to show the most interesting part

---

## 🚀 **Example: Adding 3 Custom Wallpapers**

1. **Prepare 3 images**: `sunset.jpg`, `forest.jpg`, `city.jpg`
2. **Copy to**: `app/src/main/res/drawable/`
3. **Update OnboardingConfig.kt**:

```kotlin
val featuredWallpapers = listOf(
    Wallpaper(
        id = "sunset",
        title = "Golden Sunset",
        subtitle = "My Photography", 
        imageUrl = "android.resource://com.example.ogwalls/drawable/sunset",
        thumbnailUrl = "android.resource://com.example.ogwalls/drawable/sunset",
        category = "Nature",
        photographer = "Me",
        resolution = "1080x1920",
        tags = listOf("sunset", "golden", "sky")
    ),
    Wallpaper(
        id = "forest",
        title = "Mystic Forest",
        subtitle = "My Photography",
        imageUrl = "android.resource://com.example.ogwalls/drawable/forest", 
        thumbnailUrl = "android.resource://com.example.ogwalls/drawable/forest",
        category = "Nature",
        photographer = "Me", 
        resolution = "1080x1920",
        tags = listOf("forest", "trees", "green")
    ),
    Wallpaper(
        id = "city",
        title = "Urban Lights",
        subtitle = "My Photography",
        imageUrl = "android.resource://com.example.ogwalls/drawable/city",
        thumbnailUrl = "android.resource://com.example.ogwalls/drawable/city", 
        category = "Urban",
        photographer = "Me",
        resolution = "1080x1920", 
        tags = listOf("city", "lights", "night")
    )
)
```

---

## 🛠️ **Troubleshooting**

### **Images Not Loading**
- Check file names (no spaces, lowercase)
- Verify image format (JPG/PNG)
- Ensure images are in correct folder
- Clean and rebuild project

### **App Size Too Large**
- Compress images before adding
- Use online hosting instead of local storage
- Limit number of high-resolution images

### **Poor Performance**
- Reduce image file sizes
- Use thumbnails for list views
- Consider lazy loading for large galleries

---

## 🎯 **Ready to Add Your Wallpapers?**

Choose your preferred method and I'll help you implement it! Just let me know:
1. Which option you want to use (Local/Online/Hybrid)
2. How many wallpapers you want to add
3. If you need help with image hosting or optimization

Your custom wallpapers will look amazing in the app! 🚀 