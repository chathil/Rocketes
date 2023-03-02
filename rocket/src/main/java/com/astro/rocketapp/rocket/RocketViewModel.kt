package com.astro.rocketapp.rocket

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astro.rocketapp.data.model.domain.RocketDetailModel
import com.astro.rocketapp.data.repository.RocketRepository
import com.astro.rocketapp.data.vo.Resource
import com.astro.rocketapp.rocket.model.UiAction
import com.astro.rocketapp.rocket.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RocketViewModel @Inject constructor(
    private val repository: RocketRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState.initial)
    val uiState: StateFlow<UiState> = _uiState

    val accept: (UiAction) -> Unit

    init {

        val actionStateFlow = MutableSharedFlow<UiAction>()

        actionStateFlow.observeDetail().launchIn(viewModelScope)

        accept = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }

        savedStateHandle.get<String>(DetailRouteId)?.takeIf(String::isNotBlank)?.let { id ->
            _uiState.value = uiState.value.copy(rocketId = id)
            accept(UiAction.LoadDetail(id))
        }
    }

    private fun MutableSharedFlow<UiAction>.observeDetail(): Flow<Unit> {
        return filterIsInstance<UiAction.LoadDetail>().flatMapLatest { (id) ->
            repository.fetchRocket(id)
        }.map { res ->
            _uiState.value = when (res) {
                is Resource.Loading -> _uiState.value.copy(
                    isLoading = true,
                    rocket = RocketDetailModel.empty,
                    error = null
                )
                is Resource.Success -> _uiState.value.copy(isLoading = false, rocket = res.data)
                is Resource.Error -> _uiState.value.copy(isLoading = false, error = res.error)
            }
        }
    }
}