package com.astro.rocketapp.rocket.model

sealed interface UiAction {
    data class LoadDetail(val id: String) : UiAction
}
