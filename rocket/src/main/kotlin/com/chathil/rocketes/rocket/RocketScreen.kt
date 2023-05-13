package com.chathil.rocketes.rocket

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.chathil.rocketes.data.model.domain.RocketDetailModel
import com.chathil.rocketes.rocket.model.UiAction
import com.chathil.rocketes.rocket.model.UiState
import com.chathil.rocketes.rocket.util.toFormattedString
import com.chathil.rocketes.rocket.util.toMoneyString
import com.chathil.rocketes.shared.composables.ErrorView
import com.chathil.rocketes.shared.composables.RocketesImage
import com.chathil.rocketes.shared.theme.RocketesTheme
import java.util.Date

@Composable
fun RocketScreen(
    state: UiState = UiState.initial,
    dispatcher: (UiAction) -> Unit = {},
    onBackPressed: () -> Unit = {}
) {

    Scaffold(
        topBar = { DetailTopAppBar(onBackPressed) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            if (state.isLoading) {
                DetailPlaceholder()
            }

            if (state.rocket != RocketDetailModel.empty) {
                Detail(rocket = state.rocket)
            }

            state.error?.let { error ->
                ErrorView(
                    error = error,
                    onRetry = { dispatcher(UiAction.LoadDetail(state.rocketId)) }
                )
            }
        }
    }
}

const val DetailRouteId = "id"
const val DetailRoute = "detail/"

fun NavGraphBuilder.detailScreen(onBackPressed: () -> Unit = {}) {
    composable(
        route = "$DetailRoute{${DetailRouteId}}",
        arguments = listOf(
            navArgument(DetailRouteId) {
                defaultValue = ""
                type = NavType.StringType
            }
        )
    ) {
        val viewModel: RocketViewModel = hiltViewModel()
        val state by viewModel.uiState.collectAsStateWithLifecycle()

        RocketScreen(state, viewModel.accept, onBackPressed)
    }
}

fun NavController.navigateToDetail(id: String) {
    navigate(route = DetailRoute + id)
}

@Composable
private fun Detail(
    modifier: Modifier = Modifier,
    rocket: RocketDetailModel
) {
    Column(modifier = modifier.padding(RocketesTheme.dimensions.spacingMedium)) {
        RocketesImage(
            url = rocket.img, size = null, modifier = Modifier
                .fillMaxWidth()
                .height(ImageHeight)
        )
        Spacer(modifier = Modifier.height(RocketesTheme.dimensions.spacingXXL))
        Text(rocket.name, style = MaterialTheme.typography.h6)
        Spacer(modifier = Modifier.height(RocketesTheme.dimensions.spacingXS))
        Text(
            rocket.desc,
            style = MaterialTheme.typography.body2,
        )
        Spacer(modifier = Modifier.height(RocketesTheme.dimensions.spacingMedium))
        DetailSection(
            title = stringResource(R.string.cost_per_launch),
            subtitle = rocket.costPerLaunch.toMoneyString()
        )
        DetailSection(title = stringResource(R.string.country), subtitle = rocket.country)
        DetailSection(
            title = stringResource(R.string.first_flight),
            subtitle = rocket.firstFlight.toFormattedString()
        )
    }
}

@Composable
private fun DetailSection(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String
) {
    Column(modifier = modifier) {
        Text(title, style = MaterialTheme.typography.subtitle2)
        Text(
            subtitle,
            style = MaterialTheme.typography.body2,
        )
        Spacer(modifier = Modifier.height(RocketesTheme.dimensions.spacingSmall))
    }
}

@Composable
private fun DetailPlaceholder(modifier: Modifier = Modifier) {
    val placeholderColor = MaterialTheme.colors.primary.copy(PlaceHolderAlpha)
    Column(modifier = modifier.padding(RocketesTheme.dimensions.spacingMedium)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(ImageHeight)
                .background(placeholderColor, MaterialTheme.shapes.medium)
        )
        Spacer(modifier = Modifier.height(RocketesTheme.dimensions.spacingXXL))
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
        Spacer(modifier = Modifier.height(RocketesTheme.dimensions.spacingMedium))
    }
}

@Composable
private fun DetailTopAppBar(onBackPressed: () -> Unit) {
    TopAppBar(
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.go_back)
                )
            }
        },
        title = { Text(stringResource(R.string.rocket_detail)) }
    )
}

private val ImageHeight = 176.dp
private val TitlePlaceHolderSize = DpSize(116.dp, 26.dp)
private val SubTitlePlaceHolderSize = DpSize(224.dp, 12.dp)
private const val PlaceHolderAlpha = .1f
private const val SubtitlePlaceHolderCount = 2

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun DetailPreview() {
    val preview = RocketDetailModel(
        id = "preview",
        name = "Starship",
        desc = LoremIpsum(10).values.joinToString(" "),
        img = "",
        country = "Indonesia",
        costPerLaunch = 10000,
        firstFlight = Date()
    )
    RocketesTheme {
        Detail(rocket = preview)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
private fun DetailPlaceholderPreview() {
    RocketesTheme {
        DetailPlaceholder()
    }
}

@Preview
@Composable
private fun RocketScreenPreview() {
    val state = UiState.initial.copy(
        rocket = RocketDetailModel(
            id = "preview",
            name = "Starship",
            desc = LoremIpsum(10).values.joinToString(" "),
            img = "",
            country = "Indonesia",
            costPerLaunch = 10000,
            firstFlight = Date()
        )
    )
    RocketesTheme {
        RocketScreen(state = state)
    }
}