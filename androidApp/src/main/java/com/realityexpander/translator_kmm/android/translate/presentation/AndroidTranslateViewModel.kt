package com.realityexpander.translator_kmm.android.translate.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realityexpander.translator_kmm.translate.data.presentation.TranslateStateAndroidWrapper
import com.realityexpander.translator_kmm.translate.domain.history.IHistoryDataSource
import com.realityexpander.translator_kmm.translate.domain.translate.TranslateUseCase
import com.realityexpander.translator_kmm.translate.presentation.TranslateEvent
import com.realityexpander.translator_kmm.translate.presentation.TranslateViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AndroidTranslateViewModel @Inject constructor(
    private val translate: TranslateUseCase,
    private val historyDataSource: IHistoryDataSource,
    private val savedStateHandle: SavedStateHandle
): ViewModel() {

    private val viewModel by lazy {
        TranslateViewModel(
            translate = translate,
            historyDataSource = historyDataSource,
            coroutineScope = viewModelScope,
            savedState = savedStateHandle.get<TranslateStateAndroidWrapper>("state")?.data
        )
    }

    val state = viewModel.state

    init {
        // We collect state here for savedStateHandle to support process death.
        viewModelScope.launch {
            viewModel.state.collect { translateState ->
                savedStateHandle.set("state", TranslateStateAndroidWrapper(translateState))
            }
        }
    }

    fun onEvent(event: TranslateEvent) {
        viewModel.onEvent(event)
    }
}