package com.chathil.rocketes.rocket.model

import com.chathil.rocketes.data.model.domain.RocketDetailModel

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
