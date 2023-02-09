package com.realityexpander.translator_kmm.di

import com.realityexpander.translator_kmm.translate.data.local.HistoryRepositoryFakeImpl
import com.realityexpander.translator_kmm.translate.data.remote.FakeTranslateClient
import com.realityexpander.translator_kmm.translate.domain.history.IHistoryRepository
import com.realityexpander.translator_kmm.translate.domain.translate.TranslateUseCase
import com.realityexpander.translator_kmm.translate.domain.translate.ITranslateClient
import com.realityexpander.translator_kmm.voice_to_text.data.FakeVoiceToTextProcessor
import com.realityexpander.translator_kmm.voice_to_text.domain.IVoiceToTextProcessor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Singleton
    fun provideFakeTranslateClient(): ITranslateClient {
        return FakeTranslateClient()
    }

    @Provides
    @Singleton
    fun provideFakeHistoryRepository(): IHistoryRepository {
        return HistoryRepositoryFakeImpl()
    }

    @Provides
    @Singleton
    fun provideTranslateUseCase(
        client: ITranslateClient,
        dataSource: IHistoryRepository
    ): TranslateUseCase {
        return TranslateUseCase(client, dataSource)
    }

    @Provides
    @Singleton
    fun provideFakeVoiceToTextParser(): IVoiceToTextProcessor {
        return FakeVoiceToTextProcessor()
    }
}