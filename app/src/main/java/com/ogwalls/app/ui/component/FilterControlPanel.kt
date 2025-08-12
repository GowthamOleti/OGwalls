package com.ogwalls.app.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ogwalls.app.data.model.FilterState
import com.ogwalls.app.data.model.FilterType
import com.ogwalls.app.ui.theme.*
import coil3.compose.AsyncImage
import androidx.compose.foundation.shape.CircleShape

@Composable
fun FilterControlPanel(
    filterState: FilterState,
    onFilterChanged: (FilterState) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf(FilterType.BRIGHTNESS) }
    
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = DarkCard
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            DarkCard,
                            Color(0xFF222222)
                        )
                    )
                )
                .padding(20.dp)
        ) {
            Text(
                text = "Filters",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Filter tabs
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 20.dp)
            ) {
                items(FilterType.values().filter { it != FilterType.NONE }) { filterType ->
                    FilterTab(
                        filterType = filterType,
                        isSelected = selectedFilter == filterType,
                        onClick = { selectedFilter = filterType },
                        imageUrl = "" // Placeholder for image URL
                    )
                }
            }
            
            // Filter slider
            FilterSlider(
                filterType = selectedFilter,
                value = getFilterValue(filterState, selectedFilter),
                onValueChange = { value ->
                    onFilterChanged(updateFilterState(filterState, selectedFilter, value))
                }
            )
        }
    }
}

@Composable
private fun FilterTab(
    filterType: FilterType,
    isSelected: Boolean,
    onClick: () -> Unit,
    imageUrl: String
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) AccentBlue else Color(0xFF333333),
            contentColor = if (isSelected) Color.White else Color.White.copy(alpha = 0.7f)
        ),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.height(40.dp)
    ) {
        // Add image preview
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.size(24.dp).clip(CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = filterType.name.lowercase().replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
private fun FilterSlider(
    filterType: FilterType,
    value: Float,
    onValueChange: (Float) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = filterType.name.lowercase().replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Text(
                text = "${(value * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium,
                color = AccentBlue,
                fontWeight = FontWeight.SemiBold
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Slider(
            value = value,
            onValueChange = onValueChange,
            valueRange = getFilterRange(filterType),
            colors = SliderDefaults.colors(
                thumbColor = AccentBlue,
                activeTrackColor = AccentBlue,
                inactiveTrackColor = Color(0xFF444444)
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

private fun getFilterValue(filterState: FilterState, filterType: FilterType): Float {
    return when (filterType) {
        FilterType.BRIGHTNESS -> filterState.brightness
        FilterType.CONTRAST -> filterState.contrast
        FilterType.SATURATION -> filterState.saturation
        FilterType.SEPIA -> filterState.sepia
        FilterType.BLUR -> filterState.blur
        FilterType.VINTAGE -> filterState.vintage
        FilterType.COOL -> filterState.cool
        FilterType.WARM -> filterState.warm
        FilterType.BLACK_AND_WHITE -> filterState.blackAndWhite
        FilterType.VIGNETTE -> filterState.vignette
        FilterType.NONE -> 0f
    }
}

private fun updateFilterState(filterState: FilterState, filterType: FilterType, value: Float): FilterState {
    return when (filterType) {
        FilterType.BRIGHTNESS -> filterState.copy(brightness = value)
        FilterType.CONTRAST -> filterState.copy(contrast = value)
        FilterType.SATURATION -> filterState.copy(saturation = value)
        FilterType.SEPIA -> filterState.copy(sepia = value)
        FilterType.BLUR -> filterState.copy(blur = value)
        FilterType.VINTAGE -> filterState.copy(vintage = value)
        FilterType.COOL -> filterState.copy(cool = value)
        FilterType.WARM -> filterState.copy(warm = value)
        FilterType.BLACK_AND_WHITE -> filterState.copy(blackAndWhite = value)
        FilterType.VIGNETTE -> filterState.copy(vignette = value)
        FilterType.NONE -> filterState
    }
}

private fun getFilterRange(filterType: FilterType): ClosedFloatingPointRange<Float> {
    return when (filterType) {
        FilterType.BRIGHTNESS, FilterType.CONTRAST -> 0.3f..3.0f
        FilterType.SATURATION -> 0.0f..3.0f
        else -> 0f..1f
    }
} 