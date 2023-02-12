package com.realityexpander.translator_kmm.voice_to_text.domain

import com.realityexpander.translator_kmm.core.domain.util.CommonStateFlow
import com.realityexpander.translator_kmm.core.domain.util.toCommonStateFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class VoiceToTextProcessorFakeImpl: IVoiceToTextProcessor {

    private val _state = MutableStateFlow(VoiceToTextProcessorState())
    override val state: CommonStateFlow<VoiceToTextProcessorState>
        get() = _state.toCommonStateFlow()

    var voiceResult = "test result"

    override fun startListening(languageCode: String) {
        _state.update { it.copy(
            result = "",
            isRecognizerListening = true
        ) }
    }

    override fun stopListening() {
        _state.update { it.copy(
            result =  voiceResult,
            isRecognizerListening = false
        ) }
    }

    override fun cancel() = Unit

    override fun reset() {
        _state.update { VoiceToTextProcessorState() }
    }
}