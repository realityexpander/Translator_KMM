package com.realityexpander.translator_kmm.di

import com.realityexpander.translator_kmm.database.TranslateDatabase
import com.realityexpander.translator_kmm.translate.data.history.HistoryRepositorySqlDelightImpl
import com.realityexpander.translator_kmm.translate.data.local.DatabaseDriverFactory
import com.realityexpander.translator_kmm.translate.data.remote.HttpClientFactory
import com.realityexpander.translator_kmm.translate.data.translate.TranslateClientKtorImpl
import com.realityexpander.translator_kmm.translate.domain.history.IHistoryRepository
import com.realityexpander.translator_kmm.translate.domain.translate.TranslateUseCase
import com.realityexpander.translator_kmm.translate.domain.translate.ITranslateClient
import com.realityexpander.translator_kmm.voice_to_text.domain.IVoiceToTextParser

interface AppModule {
    val historyRepo: IHistoryRepository
    val client: ITranslateClient
    val translateUseCase: TranslateUseCase
    val voiceParser: IVoiceToTextParser
}

class AppModuleImpl( // Used in iOS (simulates dependency injection)
    parser: IVoiceToTextParser
): AppModule {

    override val historyRepo: IHistoryRepository by lazy {
        HistoryRepositorySqlDelightImpl(
            TranslateDatabase(
                DatabaseDriverFactory().create()
            )
        )
    }

    override val client: ITranslateClient by lazy {
        TranslateClientKtorImpl(
            HttpClientFactory().create()
        )
    }

    override val translateUseCase: TranslateUseCase by lazy {
        TranslateUseCase(client, historyRepo)
    }

    override val voiceParser = parser
}