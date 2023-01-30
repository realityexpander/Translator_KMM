package com.realityexpander.translator_kmm.di

import com.realityexpander.translator_kmm.translate.data.local.FakeHistoryDataSource
import com.realityexpander.translator_kmm.translate.data.remote.FakeTranslateClient
import com.realityexpander.translator_kmm.translate.domain.history.HistoryDataSource
import com.realityexpander.translator_kmm.translate.domain.translate.Translate
import com.realityexpander.translator_kmm.translate.domain.translate.TranslateClient
import com.realityexpander.translator_kmm.voice_to_text.data.FakeVoiceToTextParser
import com.realityexpander.translator_kmm.voice_to_text.domain.VoiceToTextParser
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
    fun provideFakeTranslateClient(): TranslateClient {
        return FakeTranslateClient()
    }

    @Provides
    @Singleton
    fun provideFakeHistoryDataSource(): HistoryDataSource {
        return FakeHistoryDataSource()
    }

    @Provides
    @Singleton
    fun provideTranslateUseCase(
        client: TranslateClient,
        dataSource: HistoryDataSource
    ): Translate {
        return Translate(client, dataSource)
    }

    @Provides
    @Singleton
    fun provideFakeVoiceToTextParser(): VoiceToTextParser {
        return FakeVoiceToTextParser()
    }
}