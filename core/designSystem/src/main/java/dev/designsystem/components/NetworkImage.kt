package dev.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage

@Composable
fun NetworkImage(
    modifier: Modifier = Modifier,
    url: String?,
    imageColor: Color? = null,
    imageAlignment: Alignment = Alignment.Center,
    contentDescription: String? = null,
    onClick: (() -> Unit)? = null,
    placeholder: (@Composable () -> Unit)? = null,
    overlay: (@Composable BoxScope.() -> Unit)? = null,
    contentScale: ContentScale = ContentScale.Crop,
    progressIndicatorColor: Color = MaterialTheme.colorScheme.primary,
    progressIndicatorModifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(50.dp)
            .background(Color.Transparent)
            .clickable(enabled = onClick != null) { onClick?.invoke() },
        contentAlignment = imageAlignment
    ) {
        if (url.isNullOrEmpty()) {
            placeholder?.let { it() } ?: run {
                Icon(Icons.Outlined.BrokenImage, contentDescription = "No Image")
            }
        } else {
            SubcomposeAsyncImage(
                alignment = Alignment.Center,
                modifier = Modifier.fillMaxSize(),
                model = url,
                colorFilter = if (imageColor != null) ColorFilter.tint(imageColor) else null,
                contentDescription = contentDescription,
                contentScale = contentScale,
                loading = {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            strokeWidth = 4.dp,
                            modifier = progressIndicatorModifier
                                .size(50.dp)
                                .padding(6.dp),
                            color = progressIndicatorColor
                        )
                    }
                },
            )
        }
        overlay?.let { it() }
    }
}