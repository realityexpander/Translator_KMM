package com.realityexpander.translator_kmm.di

import com.realityexpander.translator_kmm.testing.HistoryDataSourceFakeImpl
import com.realityexpander.translator_kmm.testing.TranslateClientFakeImpl
import com.realityexpander.translator_kmm.testing.VoiceToTextParserFakeImpl
import com.realityexpander.translator_kmm.translate.domain.translate.TranslateUseCase

class TestAppModule: AppModule {
    override val historyDataSource = HistoryDataSourceFakeImpl()
    override val client = TranslateClientFakeImpl()
    override val translateUseCase = TranslateUseCase(client, historyDataSource)
    override val voiceParser = VoiceToTextParserFakeImpl()
}