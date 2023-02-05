package com.realityexpander.translator_kmm.di

import com.realityexpander.translator_kmm.testing.FakeHistoryDataSourceImpl
import com.realityexpander.translator_kmm.testing.FakeTranslateClientImpl
import com.realityexpander.translator_kmm.testing.FakeVoiceToTextParserImpl
import com.realityexpander.translator_kmm.translate.domain.translate.TranslateUseCase

class TestAppModule: AppModule {
    override val historyDataSource = FakeHistoryDataSourceImpl()
    override val client = FakeTranslateClientImpl()
    override val translateUseCase = TranslateUseCase(client, historyDataSource)
    override val voiceParser = FakeVoiceToTextParserImpl()
}