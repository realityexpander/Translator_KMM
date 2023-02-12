package com.realityexpander.translator_kmm.di

import com.realityexpander.translator_kmm.translate.domain.history.HistoryRepositoryFakeImpl
import com.realityexpander.translator_kmm.translate.domain.translate.TranslateClientFakeImpl
import com.realityexpander.translator_kmm.translate.domain.translate.TranslateUseCase
import com.realityexpander.translator_kmm.voice_to_text.domain.VoiceToTextProcessorFakeImpl

class TestAppModule: AppModule {
    override val historyRepo = HistoryRepositoryFakeImpl()
    override val client = TranslateClientFakeImpl()
    override val translateUseCase = TranslateUseCase(client, historyRepo)
    override val vttProcessor = VoiceToTextProcessorFakeImpl()
}