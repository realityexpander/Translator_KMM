package com.realityexpander.translator_kmm.di

import com.realityexpander.translator_kmm.database.TranslateDatabase
import com.realityexpander.translator_kmm.translate.data.history.HistoryRepositorySqlDelightImpl
import com.realityexpander.translator_kmm.translate.data.local.DatabaseDriverFactory
import com.realityexpander.translator_kmm.translate.data.remote.HttpClientFactory
import com.realityexpander.translator_kmm.translate.data.translate.TranslateClientKtorImpl
import com.realityexpander.translator_kmm.translate.domain.history.IHistoryRepository
import com.realityexpander.translator_kmm.translate.domain.translate.TranslateUseCase
import com.realityexpander.translator_kmm.translate.domain.translate.ITranslateClient
import com.realityexpander.translator_kmm.voice_to_text.domain.IVoiceToTextProcessor

interface IAppModule {
    val historyRepo: IHistoryRepository
    val client: ITranslateClient
    val translateUseCase: TranslateUseCase
    val vttProcessor: IVoiceToTextProcessor
}