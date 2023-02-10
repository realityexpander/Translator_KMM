package com.realityexpander.translator_kmm.translate.presentation

import com.realityexpander.translator_kmm.core.domain.util.Resource
import com.realityexpander.translator_kmm.core.domain.util.toCommonStateFlow
import com.realityexpander.translator_kmm.core.presentation.UiLanguage
import com.realityexpander.translator_kmm.translate.domain.history.IHistoryRepository
import com.realityexpander.translator_kmm.translate.domain.translate.TranslateUseCase
import com.realityexpander.translator_kmm.translate.domain.translate.TranslateException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TranslateViewModel(
    private val translate: TranslateUseCase,
    private val historyRepo: IHistoryRepository,
    private val coroutineScope: CoroutineScope?,
    private val savedState: TranslateState? = null
) {

    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(savedState ?: TranslateState())
    val state = combine(
        _state,
        historyRepo.getHistory()
    ) { state, history ->

        // Check for new history items
        // deep check: val isHistoryDifferent = state.history != history.map { UiHistoryItem(it.id?.toLong() ?: 0, it.fromText, it.toText, UiLanguage.byCode(it.fromLanguageCode), UiLanguage.byCode(it.toLanguageCode)) }
        val hasHistoryChanged = state.history.size != history.size
        if(hasHistoryChanged) {
            // Map database history items to UI model
            _state.update {
                it.copy(
                    history = history.mapNotNull { item ->
                        UiHistoryItem(
                            id = item.id ?: return@mapNotNull null,
                            fromText = item.fromText,
                            toText = item.toText,
                            fromLanguage = UiLanguage.byCode(item.fromLanguageCode),
                            toLanguage = UiLanguage.byCode(item.toLanguageCode)
                        )
                    }
                )
            }
            state.copy(history = _state.value.history)
        } else
            state
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), savedState ?: TranslateState())
    .toCommonStateFlow()

    private var translateJob: Job? = null

    fun onEvent(event: TranslateEvent) {
        when(event) {
            is TranslateEvent.ChangeTranslationText -> {
                _state.update { it.copy(
                    fromText = event.text
                ) }
            }
            is TranslateEvent.ChooseFromLanguage -> {
                _state.update { it.copy(
                    isChoosingFromLanguage = false,
                    fromLanguage = event.language
                ) }
            }
            is TranslateEvent.ChooseToLanguage -> {
                val newState = _state.updateAndGet { it.copy(
                    isChoosingToLanguage = false,
                    toLanguage = event.language
                ) }
                translate(newState)
            }
            TranslateEvent.CloseTranslation -> {
                _state.update { it.copy(
                    isTranslating = false,
                    fromText = "",
                    toText = null
                ) }
            }
            TranslateEvent.EditTranslation -> {
                if(state.value.toText != null) {
                    _state.update { it.copy(
                        toText = null,
                        isTranslating = false
                    ) }
                }
            }
            TranslateEvent.OnErrorSeen -> {
                _state.update { it.copy(error = null) }
            }
            TranslateEvent.OpenFromLanguageDropDown -> {  // used only on Android
                _state.update { it.copy(
                    isChoosingFromLanguage = true
                ) }
            }
            TranslateEvent.OpenToLanguageDropDown -> {  // used only on Android
                _state.update { it.copy(
                    isChoosingToLanguage = true
                ) }
            }
            is TranslateEvent.SelectHistoryItem -> {
                translateJob?.cancel()
                _state.update { it.copy(
                    fromText = event.item.fromText,
                    toText = event.item.toText,
                    isTranslating = false,
                    fromLanguage = event.item.fromLanguage,
                    toLanguage = event.item.toLanguage
                ) }
            }
            is TranslateEvent.DeleteHistoryItem -> {
                viewModelScope.launch {
                    historyRepo.deleteHistoryItem(event.item.id)
                }
            }
            TranslateEvent.StopChoosingLanguage -> {  // used only on Android
                _state.update { it.copy(
                    isChoosingFromLanguage = false,
                    isChoosingToLanguage = false
                ) }
            }
            is TranslateEvent.SubmitVoiceResult -> {
                val newState = _state.updateAndGet { it.copy(
                    fromText = event.result ?: it.fromText,
                    isTranslating = if(event.result != null) false else it.isTranslating,
                    toText = if(event.result != null) null else it.toText
                ) }
                translate(newState)
            }
            TranslateEvent.SwapLanguages -> {
                _state.update { it.copy(
                    fromLanguage = it.toLanguage,
                    toLanguage = it.fromLanguage,
                    fromText = it.toText ?: "",
                    toText = if(it.toText != null) it.fromText else null
                ) }
            }
            TranslateEvent.Translate -> translate(state.value)
            else -> Unit
        }
    }

    private fun translate(state: TranslateState) {
        if(state.isTranslating || state.fromText.isBlank()) {
            return
        }

        translateJob = viewModelScope.launch {
            _state.update { it.copy(
                isTranslating = true
            ) }

            val result = translate.execute(
                fromLanguage = state.fromLanguage.language,
                fromText = state.fromText,
                toLanguage = state.toLanguage.language
            )

            when(result) {
                is Resource.Success -> {
                    _state.update { it.copy(
                        isTranslating = false,
                        toText = result.data,
                    ) }
                }
                is Resource.Error -> {
                    _state.update { it.copy(
                        isTranslating = false,
                        error = (result.throwable as? TranslateException)?.error
                    ) }
                }
            }
        }
    }
}