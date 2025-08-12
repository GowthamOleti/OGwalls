package com.ogwalls.app.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ogwalls.app.data.model.Wallpaper
import com.ogwalls.app.data.repository.WallpaperRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class WallpaperUiState(
    val wallpapers: List<Wallpaper> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedWallpaper: Wallpaper? = null,
    val filterStates: Map<String, com.ogwalls.app.data.model.FilterState> = emptyMap()
)

class WallpaperViewModel(
    private val repository: WallpaperRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(WallpaperUiState())
    val uiState: StateFlow<WallpaperUiState> = _uiState.asStateFlow()
    
    init {
        loadWallpapers()
    }
    
    private fun loadWallpapers() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                repository.getWallpapers().collect { wallpapers ->
                    _uiState.update {
                        it.copy(
                            wallpapers = wallpapers,
                            isLoading = false
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Failed to load wallpapers: ${e.message}"
                    )
                }
            }
        }
    }
    
    fun toggleFavorite(wallpaperId: String) {
        viewModelScope.launch {
            try {
                val success = repository.toggleFavorite(wallpaperId)
                if (success) {
                    val updatedWallpapers = _uiState.value.wallpapers.map { wallpaper ->
                        if (wallpaper.id == wallpaperId) {
                            wallpaper.copy(isFavorite = !wallpaper.isFavorite)
                        } else {
                            wallpaper
                        }
                    }
                    _uiState.update {
                        it.copy(wallpapers = updatedWallpapers)
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = "Failed to toggle favorite: ${e.message}")
                }
            }
        }
    }
    
    suspend fun getWallpaperById(id: String): Wallpaper? {
        return try {
            repository.getWallpaperById(id)
        } catch (e: Exception) {
            null
        }
    }
    
    fun getWallpaperIndexById(id: String): Int {
        return _uiState.value.wallpapers.indexOfFirst { it.id == id }.coerceAtLeast(0)
    }
    
    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
    
    // Filter state management
    fun getFilterState(wallpaperId: String): com.ogwalls.app.data.model.FilterState {
        return _uiState.value.filterStates[wallpaperId] ?: com.ogwalls.app.data.model.FilterState()
    }
    
    fun updateFilterState(wallpaperId: String, filterState: com.ogwalls.app.data.model.FilterState) {
        _uiState.update { currentState ->
            currentState.copy(
                filterStates = currentState.filterStates + (wallpaperId to filterState)
            )
        }
    }
    
    fun clearFilterState(wallpaperId: String) {
        _uiState.update { currentState ->
            currentState.copy(
                filterStates = currentState.filterStates - wallpaperId
            )
        }
    }
    
    // Like/Unlike functionality
    fun toggleLike(wallpaperId: String) {
        viewModelScope.launch {
            try {
                val success = repository.toggleFavorite(wallpaperId)
                if (success) {
                    val updatedWallpapers = _uiState.value.wallpapers.map { wallpaper ->
                        if (wallpaper.id == wallpaperId) {
                            wallpaper.copy(isFavorite = !wallpaper.isFavorite)
                        } else {
                            wallpaper
                        }
                    }
                    _uiState.update {
                        it.copy(wallpapers = updatedWallpapers)
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(error = "Failed to toggle like: ${e.message}")
                }
            }
        }
    }
    
    fun getLikedWallpapers(): List<Wallpaper> {
        return _uiState.value.wallpapers.filter { it.isFavorite }
    }
} 