package com.realityexpander.translator_kmm.voice_to_text.presentation

import com.realityexpander.translator_kmm.core.domain.util.toCommonStateFlow
import com.realityexpander.translator_kmm.voice_to_text.domain.IVoiceToTextProcessor
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class VoiceToTextViewModel(
    private val vttProcessor: IVoiceToTextProcessor,
    coroutineScope: CoroutineScope? = null
) {
    private val viewModelScope = coroutineScope ?: CoroutineScope(Dispatchers.Main)

    private val _state = MutableStateFlow(VoiceToTextState())
    val state =
        _state.combine(vttProcessor.state) { state, voiceResult ->
            val isError = !state.isRecordPermissionGranted || voiceResult.error != null
            val isResultVisible = voiceResult.result.isNotBlank() && !voiceResult.isRecognizerListening

            state.copy(
                spokenText = voiceResult.result,
                recordError = if (state.isRecordPermissionGranted) {
                        voiceResult.error
                    } else {
                        "Can't record without permission"
                    },
                displayState = when {
                    isError ->
                        DisplayState.ERROR
                    isResultVisible ->
                        DisplayState.RESULT_VISIBLE
                    voiceResult.isRecognizerListening ->
                        DisplayState.LISTENING_ACTIVE
                    else ->
                        DisplayState.WAITING_TO_LISTEN
                }
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), VoiceToTextState())
        .toCommonStateFlow()

    init {

        // Update the power ratio every 50ms for the "waveform" audio-level animation
        viewModelScope.launch {
            while (true) {
                if (state.value.displayState == DisplayState.LISTENING_ACTIVE) {
                    _state.update {
                        it.copy(
                            powerRatios = it.powerRatios + vttProcessor.state.value.powerRatio
                        )
                    }
                }
                delay(50L)
            }
        }
    }

    fun onEvent(event: VoiceToTextEvent) {
        when (event) {
            is VoiceToTextEvent.PermissionResult -> {
                _state.update {
                    it.copy(
                        isRecordPermissionGranted = event.isGranted,
                        recordError = if (event.isGranted) {
                                null
                            } else {
                                "Can't record without permission"
                            }
                    )
                }
            }
            VoiceToTextEvent.Reset -> {
                vttProcessor.reset()
                _state.update {
                    VoiceToTextState()
                        .copy(isRecordPermissionGranted = it.isRecordPermissionGranted) // keep the permission state
                }
            }
            is VoiceToTextEvent.ToggleRecording ->
                toggleRecording(event.languageCode)
            else -> Unit
        }
    }

    private fun toggleRecording(languageCode: String) {
        _state.update { it.copy(powerRatios = emptyList()) }

        vttProcessor.cancel()
        if (state.value.displayState == DisplayState.LISTENING_ACTIVE) {
            vttProcessor.stopListening()
        } else {
            vttProcessor.startListening(languageCode)
        }
    }
}
