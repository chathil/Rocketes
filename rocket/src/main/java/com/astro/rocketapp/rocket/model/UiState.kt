package com.astro.rocketapp.rocket.model

import com.astro.rocketapp.data.model.domain.RocketDetailModel

data class UiState(
    val isLoading: Boolean,
    val rocketId: String,
    val rocket: RocketDetailModel,
    val error: Throwable?
) {
    companion object {
        val initial = UiState(
            isLoading = false,
            rocketId = "",
            rocket = RocketDetailModel.empty,
            error = null
        )
    }
}
