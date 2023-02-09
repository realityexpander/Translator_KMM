package com.realityexpander.translator_kmm.di

import com.realityexpander.translator_kmm.testing.HistoryRepositoryFakeImpl
import com.realityexpander.translator_kmm.testing.TranslateClientFakeImpl
import com.realityexpander.translator_kmm.testing.VoiceToTextProcessorFakeImpl
import com.realityexpander.translator_kmm.translate.domain.translate.TranslateUseCase

class TestAppModule: AppModule {
    override val historyRepo = HistoryRepositoryFakeImpl()
    override val client = TranslateClientFakeImpl()
    override val translateUseCase = TranslateUseCase(client, historyRepo)
    override val vttProcessor = VoiceToTextProcessorFakeImpl()
}