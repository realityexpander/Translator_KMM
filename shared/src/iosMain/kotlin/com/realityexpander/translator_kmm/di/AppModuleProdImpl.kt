package com.realityexpander.translator_kmm.di

import com.realityexpander.translator_kmm.database.TranslateDatabase
import com.realityexpander.translator_kmm.translate.data.history.HistoryRepositorySqlDelightImpl
import com.realityexpander.translator_kmm.translate.data.local.DatabaseDriverFactory
import com.realityexpander.translator_kmm.translate.data.remote.HttpClientFactory
import com.realityexpander.translator_kmm.translate.data.translate.TranslateClientKtorImpl
import com.realityexpander.translator_kmm.translate.domain.history.IHistoryRepository
import com.realityexpander.translator_kmm.translate.domain.translate.ITranslateClient
import com.realityexpander.translator_kmm.translate.domain.translate.TranslateUseCase
import com.realityexpander.translator_kmm.voice_to_text.domain.IVoiceToTextProcessor

class AppModuleProdImpl( // Used in iOS (simulates dependency injection)
    override val vttProcessor: IVoiceToTextProcessor // passed in from iOS when creating the app.
): IAppModule {

    ////////////////////////
    // data/remote

    override val client: ITranslateClient by lazy {
        TranslateClientKtorImpl(
            HttpClientFactory().create()  // uses iOS's native networking
        )
    }

    ////////////////////////
    // data/local

    override val historyRepo: IHistoryRepository by lazy {
        HistoryRepositorySqlDelightImpl(
            TranslateDatabase(
                DatabaseDriverFactory().create()
            )
        )
    }

    ////////////////////////
    // domain

    override val translateUseCase: TranslateUseCase by lazy {
        TranslateUseCase(client, historyRepo)
    }
}