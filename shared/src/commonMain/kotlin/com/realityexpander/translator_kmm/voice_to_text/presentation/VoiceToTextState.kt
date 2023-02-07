package com.realityexpander.translator_kmm.voice_to_text.presentation

data class VoiceToTextState(
    val powerRatios: List<Float> = emptyList(),
    val spokenText: String = "",
    val isRecordPermissionGranted: Boolean = false,
    val recordError: String? = null,
    val displayState: DisplayState? = null
)

enum class DisplayState {
    WAITING_TO_SPEAK,
    SPEAKING,
    RESULT_VISIBLE,
    ERROR
}
