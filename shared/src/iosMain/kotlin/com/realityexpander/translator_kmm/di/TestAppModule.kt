package com.realityexpander.translator_kmm.di

import com.realityexpander.translator_kmm.testing.FakeHistoryDataSource
import com.realityexpander.translator_kmm.testing.FakeTranslateClient
import com.realityexpander.translator_kmm.testing.FakeVoiceToTextParser
import com.realityexpander.translator_kmm.translate.domain.translate.Translate

class TestAppModule: AppModule {
    override val historyDataSource = FakeHistoryDataSource()
    override val client = FakeTranslateClient()
    override val translateUseCase = Translate(client, historyDataSource)
    override val voiceParser = FakeVoiceToTextParser()
}