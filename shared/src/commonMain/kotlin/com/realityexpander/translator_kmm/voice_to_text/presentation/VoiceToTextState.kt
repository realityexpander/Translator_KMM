package com.realityexpander.translator_kmm.voice_to_text.presentation

data class VoiceToTextState(
    val powerRatios: List<Float> = emptyList(), // to display a moving waveform
    val spokenText: String = "",
    val isRecordPermissionGranted: Boolean = false,
    val recordError: String? = null,
    val displayState: DisplayState? = null
)

enum class DisplayState {
    WAITING_TO_LISTEN,
    LISTENING,
    RESULT_VISIBLE,
    ERROR
}
