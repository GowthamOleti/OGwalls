# 🚀 Quick Guide: Add Your Wallpapers

## 📁 **What You Have Now**

1. **MyWallpapers.kt** - Template file to add your wallpapers  
2. **OnboardingConfig.kt** - Controls the 3 cards on first screen
3. **WallpaperRepository.kt** - Controls the main wallpaper gallery
4. **drawable/** folder - For storing local images

---

## ⚡ **Quick Steps (5 Minutes)**

### **Option A: Use Online Images (Easiest)**

1. **Upload your images** to any free service:
   - **Imgur** (imgur.com) - Just drag & drop
   - **GitHub** - Create a repo, upload images
   - **Google Drive** - Upload, get shareable link

2. **Get direct image URLs** (must end in .jpg or .png)
   ```
   ✅ Good: https://i.imgur.com/ABC123.jpg
   ❌ Bad:  https://imgur.com/ABC123
   ```

3. **Edit MyWallpapers.kt**:
   - Replace `"YOUR_IMAGE_URL_HERE"` with your image URLs
   - Update titles, photographer names, etc.

4. **Activate your wallpapers**:
   - **OnboardingConfig.kt**: Uncomment line 4 and 10
   - **WallpaperRepository.kt**: Uncomment line 3 and 8

### **Option B: Use Local Images**

1. **Copy your images** to: `app/src/main/res/drawable/`
   - Name files like: `my_photo_1.jpg`, `sunset.jpg`, etc.
   - No spaces, lowercase only

2. **Edit MyWallpapers.kt**:
   - Replace URLs with: `"android.resource://com.example.ogwalls/drawable/your_file_name"`
   - Update titles, photographer names, etc.

3. **Activate your wallpapers** (same as Option A)

---

## 🎯 **Example: Add 3 Wallpapers in 2 Minutes**

Let's say you have 3 photos uploaded to Imgur:
- `https://i.imgur.com/photo1.jpg`
- `https://i.imgur.com/photo2.jpg` 
- `https://i.imgur.com/photo3.jpg`

**Edit MyWallpapers.kt:**
```kotlin
val onboardingWallpapers = listOf(
    Wallpaper(
        id = "my_photo_1",
        title = "Sunset at Beach",
        subtitle = "My Photography",
        imageUrl = "https://i.imgur.com/photo1.jpg",
        thumbnailUrl = "https://i.imgur.com/photo1.jpg",
        category = "Personal",
        photographer = "Your Name",
        resolution = "1080x1920",
        tags = listOf("sunset", "beach")
    ),
    // Copy pattern for photo2 and photo3...
)
```

**Activate in OnboardingConfig.kt:**
```kotlin
// Uncomment these 2 lines:
import com.example.ogwalls.data.MyWallpapers
val featuredWallpapers = MyWallpapers.onboardingWallpapers
```

**Activate in WallpaperRepository.kt:**
```kotlin
// Uncomment these 2 lines:
import com.example.ogwalls.data.MyWallpapers  
return MyWallpapers.galleryWallpapers
```

Done! Your wallpapers are now in the app! 🎉

---

## 🔧 **Files to Edit**

| File | Purpose | What to Change |
|------|---------|----------------|
| `MyWallpapers.kt` | Your wallpaper data | Replace URLs, titles, names |
| `OnboardingConfig.kt` | First screen cards | Uncomment 2 lines |
| `WallpaperRepository.kt` | Main gallery | Uncomment 2 lines |

---

## 🎨 **Image Tips**

- **Size**: 1080x1920 or higher (portrait)
- **Format**: JPG or PNG
- **File size**: Under 2MB each
- **Quality**: High resolution looks best

---

## 🚨 **Troubleshooting**

### Images not showing?
- Check URLs are direct links ending in .jpg/.png
- Verify internet connection
- Make sure you uncommented the import lines

### App won't build?
- Check file names have no spaces or special characters
- Ensure all commas and quotes are correct in MyWallpapers.kt

### Want to mix default + custom wallpapers?
- Edit MyWallpapers.kt to include both
- Or modify the default lists directly

---

## 🎯 **What's Next?**

1. **Add your wallpapers** using steps above
2. **Build and test** the app in Android Studio
3. **Enjoy your custom wallpaper app!** 

Your wallpapers will look amazing with the dark theme and smooth animations! 🌟 