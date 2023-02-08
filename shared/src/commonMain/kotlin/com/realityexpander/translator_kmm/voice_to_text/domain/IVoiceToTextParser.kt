package com.realityexpander.translator_kmm.voice_to_text.domain

import com.realityexpander.translator_kmm.core.domain.util.CommonStateFlow

interface IVoiceToTextParser {
    val state: CommonStateFlow<VoiceToTextParserState>
    fun startListening(languageCode: String)
    fun stopListening()
    fun cancel()
    fun reset()  // only used on iOS
}