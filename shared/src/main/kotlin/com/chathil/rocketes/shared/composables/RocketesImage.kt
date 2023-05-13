package com.chathil.rocketes.shared.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest

@Composable
fun RocketesImage(
    modifier: Modifier = Modifier,
    url: String?,
    shape: Shape = MaterialTheme.shapes.medium,
    size: DpSize? = DpSize(68.dp, 68.dp),
    placeholder: @Composable () -> Unit = {
        Box(
            modifier = Modifier
                .size(size ?: DpSize(68.dp, 68.dp))
                .background(MaterialTheme.colors.primary)
        )
    },
    contentScale: ContentScale = ContentScale.Crop,
    contentDescription: String = ""
) {
    SubcomposeAsyncImage(
        modifier = modifier
            .then(if (size != null) Modifier.size(size) else Modifier)
            .clip(shape),
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .crossfade(true)
            .build(),
        contentDescription = contentDescription,
        loading = { placeholder() },
        error = { placeholder() },
        contentScale = contentScale,
    )
}
