package com.realityexpander.translator_kmm.android.voice_to_text.data

import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.SpeechRecognizer.*
import com.realityexpander.translator_kmm.android.R
import com.realityexpander.translator_kmm.core.domain.util.CommonStateFlow
import com.realityexpander.translator_kmm.core.domain.util.toCommonStateFlow
import com.realityexpander.translator_kmm.voice_to_text.domain.IVoiceToTextProcessor
import com.realityexpander.translator_kmm.voice_to_text.domain.VoiceToTextProcessorState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class VoiceToTextProcessorAndroidImpl(
    private val app: Application
): IVoiceToTextProcessor, RecognitionListener {

    private val recognizer = SpeechRecognizer.createSpeechRecognizer(app)

    private val _state = MutableStateFlow(VoiceToTextProcessorState())
    override val state: CommonStateFlow<VoiceToTextProcessorState> =
        _state.toCommonStateFlow()

    ///////////////////////////////////////////////////////////////////////
    ////////////////// IVoiceToTextParser Implementation //////////////////

    override fun startListening(languageCode: String) {
        _state.update { VoiceToTextProcessorState() }

        if(!SpeechRecognizer.isRecognitionAvailable(app)) {
            _state.update { it.copy(
                error = app.getString(R.string.error_speech_recognition_unavailable)
            ) }
            return
        }

        // Build the intent for OS to recognize speech
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
        }
        recognizer.setRecognitionListener(this)
        recognizer.startListening(intent)
        _state.update { it.copy(
            isRecognizerListening = true
        ) }
    }

    override fun stopListening() {
        _state.update { VoiceToTextProcessorState() }
        recognizer.stopListening()
    }

    override fun cancel() {
        recognizer.cancel()
    }

    override fun reset() {
        _state.value = VoiceToTextProcessorState() // prevents a race condition by not using `.update{}`
    }

    //////////////////////////////////////////////////////////////////////
    ///////////////// RecognitionListener Implementation /////////////////

    override fun onReadyForSpeech(p0: Bundle?) {
        _state.update { it.copy(error = null) }
    }

    override fun onBeginningOfSpeech() = Unit

    override fun onRmsChanged(rmsDb: Float) {
        _state.update { it.copy(
            powerRatio = rmsDb * (1f / (12f - (-2f))) // convert -2dB->12dB to 0.0->1.0
        ) }
    }

    override fun onBufferReceived(p0: ByteArray?) = Unit

    override fun onEndOfSpeech() {
        _state.update { it.copy(isRecognizerListening = false) }
    }

    override fun onError(code: Int) {
        if(code == ERROR_CLIENT) {
            return
        }

        val appendErrorStr = when(code) {
            ERROR_NETWORK -> app.getString(R.string.error_speechrecognizer_network_error)
            ERROR_CLIENT -> app.getString(R.string.error_speech_recognition_unavailable)
            ERROR_NO_MATCH -> app.getString(R.string.error_speech_no_match)
            else -> app.getString(R.string.error_speech_recognition_unavailable)
        }

        _state.update { it.copy(error = "Error: $code:$appendErrorStr") }
    }

    override fun onResults(result: Bundle?) {
        result
            ?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.getOrNull(0)
            ?.let { text ->
                _state.update { it.copy(
                    result = text
                ) }
            }
    }

    override fun onPartialResults(p0: Bundle?) = Unit

    override fun onEvent(p0: Int, p1: Bundle?) = Unit
}