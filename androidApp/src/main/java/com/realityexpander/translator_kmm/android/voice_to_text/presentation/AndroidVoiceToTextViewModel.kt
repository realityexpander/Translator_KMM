package com.realityexpander.translator_kmm.android.voice_to_text.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.realityexpander.translator_kmm.voice_to_text.domain.IVoiceToTextProcessor
import com.realityexpander.translator_kmm.voice_to_text.presentation.VoiceToTextEvent
import com.realityexpander.translator_kmm.voice_to_text.presentation.VoiceToTextViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

// Wrapper around VoiceToTextViewModel to make it compatible with Hilt

@HiltViewModel
class AndroidVoiceToTextViewModel @Inject constructor(
    private val vttProcessor: IVoiceToTextProcessor
): ViewModel() {

    private val viewModel by lazy {
        VoiceToTextViewModel(
            vttProcessor = vttProcessor,
            coroutineScope = viewModelScope
        )
    }

    val state = viewModel.state

    fun onEvent(event: VoiceToTextEvent) {
        viewModel.onEvent(event)
    }
}