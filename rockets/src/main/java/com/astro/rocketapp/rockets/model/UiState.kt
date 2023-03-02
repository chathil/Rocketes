package com.astro.rocketapp.rockets.model

import com.astro.rocketapp.data.model.domain.RocketModel

data class UiState(
    val isLoading: Boolean,
    private val rockets: List<RocketModel>,
    val query: String,
    val error: Throwable?
) {

    val filteredRockets: List<RocketModel> = rockets.filter { rocket ->
        rocket.name.contains(query)
    }

    companion object {
        val initial = UiState(isLoading = false, rockets = emptyList(), query = "", error = null)
    }
}