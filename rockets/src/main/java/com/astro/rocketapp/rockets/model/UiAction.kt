package com.astro.rocketapp.rockets.model

sealed interface UiAction {

    object LoadRockets : UiAction

    data class SearchRocket(val query: String) : UiAction
}