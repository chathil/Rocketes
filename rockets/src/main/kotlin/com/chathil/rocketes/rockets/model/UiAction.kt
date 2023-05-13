package com.chathil.rocketes.rockets.model

sealed interface UiAction {

    object LoadRockets : UiAction

    data class SearchRocket(val query: String) : UiAction
}