package com.astro.rocketapp.rockets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.astro.rocketapp.data.model.domain.RocketModel
import com.astro.rocketapp.rockets.model.UiAction
import com.astro.rocketapp.rockets.model.UiState
import com.astro.rocketapp.shared.composables.ErrorView
import com.astro.rocketapp.shared.composables.RocketesImage
import com.astro.rocketapp.shared.theme.RocketesTheme

@Composable
fun RocketsScreen(
    state: UiState = UiState.initial,
    dispatcher: (UiAction) -> Unit = {},
    goToDetail: (id: String) -> Unit = {}
) {
    Scaffold(topBar = { RocketsTopAppBar() }) { padding ->

        LazyColumn(modifier = Modifier.padding(padding)) {
            item {
                SearchBar(query = state.query, dispatcher = dispatcher)
            }

            if (state.isLoading) {
                items(count = PlaceholderCount) {
                    RocketItemPlaceHolder()
                }
            }

            if (state.filteredRockets.isNotEmpty() && state.error == null) {
                items(items = state.filteredRockets, key = { rocket -> rocket.id }) { rocket ->
                    RocketItem(rocket = rocket, onTapped = { goToDetail(rocket.id) })
                }
            }

            if (state.filteredRockets.isEmpty() && state.query.isNotBlank()) {
                item {
                    Text(
                        stringResource(R.string.empty_search),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(RocketesTheme.dimensions.spacingMedium),
                        textAlign = TextAlign.Center
                    )
                }
            }

            if (state.filteredRockets.isEmpty() && state.query.isBlank()) {
                item {
                    Text(
                        stringResource(R.string.no_rockets),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(RocketesTheme.dimensions.spacingMedium),
                        textAlign = TextAlign.Center
                    )
                }
            }

            state.error?.let { error ->
                item {
                    ErrorView(error = error, onRetry = { dispatcher(UiAction.LoadRockets) })
                }
            }
        }
    }
}

const val ListRoute = "list"

fun NavGraphBuilder.listScreen(
    goToDetail: (id: String) -> Unit = {}
) {
    composable(ListRoute) {
        val viewModel: RocketsViewModel = hiltViewModel()
        val state by viewModel.uiState.collectAsStateWithLifecycle()
        RocketsScreen(state = state, dispatcher = viewModel.accept, goToDetail = goToDetail)
    }
}

@Composable
private fun RocketsTopAppBar() {
    TopAppBar(title = { Text(stringResource(id = R.string.rocket_list)) })
}

@Composable
private fun SearchBar(
    query: String, dispatcher: (UiAction) -> Unit
) {
    TextField(
        value = query,
        onValueChange = { str -> dispatcher(UiAction.SearchRocket(str)) },
        placeholder = { Text(stringResource(R.string.find_rocket)) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(RocketesTheme.dimensions.spacingMedium)
    )
}

@Composable
private fun RocketItem(
    modifier: Modifier = Modifier, rocket: RocketModel, onTapped: () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(
                onClick = onTapped,
                interactionSource = remember { MutableInteractionSource() },
                role = Role.Button,
                indication = rememberRipple(bounded = true),
            )
            .padding(
                horizontal = RocketesTheme.dimensions.spacingMedium,
                vertical = RocketesTheme.dimensions.spacingMedium
            ), verticalAlignment = Alignment.CenterVertically
    ) {
        RocketesImage(
            shape = CircleShape,
            url = rocket.img,
            contentDescription = stringResource(R.string.image_of, rocket.name),
        )
        Spacer(modifier = Modifier.width(RocketesTheme.dimensions.spacingSmall))
        Column(modifier = Modifier.weight(1f)) {
            Text(rocket.name, style = MaterialTheme.typography.h6)
            Spacer(modifier = Modifier.height(RocketesTheme.dimensions.spacingXS))
            Text(
                rocket.desc,
                style = MaterialTheme.typography.body2,
                maxLines = DescriptionMaxLines,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.width(RocketesTheme.dimensions.spacingSmall))
        Icon(
            imageVector = Icons.Default.KeyboardArrowRight,
            contentDescription = stringResource(R.string.open_rocket_detail, rocket.name)
        )
    }
}

@Composable
private fun RocketItemPlaceHolder(
    modifier: Modifier = Modifier
) {
    val placeholderColor = MaterialTheme.colors.primary.copy(PlaceholderAlpha)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = RocketesTheme.dimensions.spacingMedium,
                vertical = RocketesTheme.dimensions.spacingMedium
            ), verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(ImgPlaceholderSize)
                .background(placeholderColor, CircleShape)
        )
        Spacer(modifier = Modifier.width(RocketesTheme.dimensions.spacingSmall))
        Column(modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .size(TitlePlaceHolderSize)
                    .background(placeholderColor, MaterialTheme.shapes.medium)
            )
            Spacer(modifier = Modifier.height(RocketesTheme.dimensions.spacingMedium))
            repeat(SubtitlePlaceHolderCount) {
                Box(
                    modifier = Modifier
                        .size(SubTitlePlaceHolderSize)
                        .background(placeholderColor, MaterialTheme.shapes.medium)
                )
                Spacer(modifier = Modifier.height(RocketesTheme.dimensions.spacingXS))
            }
        }
        Spacer(modifier = Modifier.width(RocketesTheme.dimensions.spacingSmall))
        Box(
            modifier = Modifier
                .size(NavIconPlaceholderSize)
                .background(placeholderColor, CircleShape)
        )
    }
}

private const val PlaceholderAlpha = .1f
private const val PlaceholderCount = 5
private const val DescriptionMaxLines = 3
private const val SubtitlePlaceHolderCount = 2

private val TitlePlaceHolderSize = DpSize(116.dp, 26.dp)
private val SubTitlePlaceHolderSize = DpSize(224.dp, 12.dp)
private val ImgPlaceholderSize = 68.dp
private val NavIconPlaceholderSize = 24.dp

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun RocketItemPreview() {
    val preview = RocketModel(
        id = "preview", name = "Starship", desc = LoremIpsum(10).values.joinToString(" "), img = ""
    )

    RocketesTheme {
        RocketItem(rocket = preview)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun RocketItemPlaceholderPreview() {
    RocketesTheme {
        RocketItemPlaceHolder()
    }
}

@Preview
@Composable
private fun RocketScreenPreview() {
    val preview = RocketModel(
        id = "0", name = "Starship", desc = LoremIpsum(10).values.joinToString(" "), img = ""
    )
    val rockets = listOf(
        preview,
        preview.copy(id = "1"),
        preview.copy(id = "2"),
        preview.copy(id = "3"),
        preview.copy(id = "4")
    )
    val state = UiState.initial.copy(
        rockets = rockets
    )
    RocketesTheme {
        RocketsScreen(state = state)
    }
}