package com.realityexpander.translator_kmm.voice_to_text.domain

data class VoiceToTextParserState(
    val result: String = "",
    val error: String? = null,
    val powerRatio: Float = 0f,  // voice volume level rmsDb (decibels)
    val isRecognizerListening: Boolean = false
)
