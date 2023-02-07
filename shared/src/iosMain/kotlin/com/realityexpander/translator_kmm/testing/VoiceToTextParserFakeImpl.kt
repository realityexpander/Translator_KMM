package com.realityexpander.translator_kmm.testing

import com.realityexpander.translator_kmm.core.domain.util.CommonStateFlow
import com.realityexpander.translator_kmm.core.domain.util.toCommonStateFlow
import com.realityexpander.translator_kmm.voice_to_text.domain.IVoiceToTextParser
import com.realityexpander.translator_kmm.voice_to_text.domain.VoiceToTextParserState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class VoiceToTextParserFakeImpl: IVoiceToTextParser {

    private val _state = MutableStateFlow(VoiceToTextParserState())
    override val state: CommonStateFlow<VoiceToTextParserState>
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
        _state.update { VoiceToTextParserState() }
    }
}