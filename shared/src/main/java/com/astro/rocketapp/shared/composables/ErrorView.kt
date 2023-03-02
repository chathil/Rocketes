package com.astro.rocketapp.shared.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.astro.rocketapp.shared.R
import com.astro.rocketapp.shared.theme.RocketesTheme

@Composable
fun ErrorView(
    modifier: Modifier = Modifier,
    error: Throwable,
    onRetry: () -> Unit = {}
) {
    Column(
        modifier = modifier.padding(RocketesTheme.dimensions.spacingLarge),
        verticalArrangement = Arrangement.spacedBy(RocketesTheme.dimensions.spacingMedium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Something went wrong: ${error.message}", style = MaterialTheme.typography.h6)
        Button(onClick = onRetry) {
            Text(text = stringResource(id = R.string.retry))
        }
    }
}
