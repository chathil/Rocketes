package com.chathil.rocketes.rocket.model

sealed interface UiAction {
    data class LoadDetail(val id: String) : UiAction
}
