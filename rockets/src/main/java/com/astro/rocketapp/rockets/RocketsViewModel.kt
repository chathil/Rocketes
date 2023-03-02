package com.astro.rocketapp.rockets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astro.rocketapp.data.repository.RocketRepository
import com.astro.rocketapp.data.vo.Resource
import com.astro.rocketapp.rockets.model.UiAction
import com.astro.rocketapp.rockets.model.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RocketsViewModel @Inject constructor(
    private val repository: RocketRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState.initial)
    val uiState: StateFlow<UiState> = _uiState

    val accept: (UiAction) -> Unit

    init {
        val actionStateFlow = MutableSharedFlow<UiAction>()

        merge(
            actionStateFlow.observeRockets(),
            actionStateFlow.observeSearch()
        ).launchIn(viewModelScope)

        accept = { action ->
            viewModelScope.launch { actionStateFlow.emit(action) }
        }

        accept(UiAction.LoadRockets)
    }

    private fun MutableSharedFlow<UiAction>.observeRockets(): Flow<Unit> {
        return filterIsInstance<UiAction.LoadRockets>().flatMapLatest {
            repository.fetchRockets()
        }.map { res ->
            _uiState.value = when (res) {
                is Resource.Loading -> _uiState.value.copy(
                    isLoading = true,
                    rockets = emptyList(),
                    error = null
                )
                is Resource.Success -> _uiState.value.copy(isLoading = false, rockets = res.data)
                is Resource.Error -> _uiState.value.copy(isLoading = false, error = res.error)
            }
        }
    }

    private fun MutableSharedFlow<UiAction>.observeSearch(): Flow<Unit> {
        return filterIsInstance<UiAction.SearchRocket>().map { (query) ->
            _uiState.value = _uiState.value.copy(query = query)
        }
    }
}
