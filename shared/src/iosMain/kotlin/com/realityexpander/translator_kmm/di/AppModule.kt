package com.realityexpander.translator_kmm.di

import com.realityexpander.translator_kmm.database.TranslateDatabase
import com.realityexpander.translator_kmm.translate.data.history.HistoryDataSourceSqlDelightImpl
import com.realityexpander.translator_kmm.translate.data.local.DatabaseDriverFactory
import com.realityexpander.translator_kmm.translate.data.remote.HttpClientFactory
import com.realityexpander.translator_kmm.translate.data.translate.TranslateClientKtorImpl
import com.realityexpander.translator_kmm.translate.domain.history.IHistoryDataSource
import com.realityexpander.translator_kmm.translate.domain.translate.TranslateUseCase
import com.realityexpander.translator_kmm.translate.domain.translate.ITranslateClient
import com.realityexpander.translator_kmm.voice_to_text.domain.IVoiceToTextParser

interface AppModule {
    val historyDataSource: IHistoryDataSource
    val client: ITranslateClient
    val translateUseCase: TranslateUseCase
    val voiceParser: IVoiceToTextParser
}

class AppModuleImpl(
    parser: IVoiceToTextParser
): AppModule {

    override val historyDataSource: IHistoryDataSource by lazy {
        HistoryDataSourceSqlDelightImpl(
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
        TranslateUseCase(client, historyDataSource)
    }

    override val voiceParser = parser
}