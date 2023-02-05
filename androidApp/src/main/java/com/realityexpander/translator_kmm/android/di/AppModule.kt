package com.realityexpander.translator_kmm.android.di

import android.app.Application
import com.realityexpander.translator_kmm.database.TranslateDatabase
import com.realityexpander.translator_kmm.translate.data.history.HistoryDataSourceSqlDelightImpl
import com.realityexpander.translator_kmm.translate.data.local.DatabaseDriverFactory
import com.realityexpander.translator_kmm.translate.data.remote.HttpClientFactory
import com.realityexpander.translator_kmm.translate.data.translate.TranslateClientKtorImpl
import com.realityexpander.translator_kmm.translate.domain.history.IHistoryDataSource
import com.realityexpander.translator_kmm.translate.domain.translate.TranslateUseCase
import com.realityexpander.translator_kmm.translate.domain.translate.ITranslateClient
import com.squareup.sqldelight.db.SqlDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.*
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient {
        return HttpClientFactory().create()
    }

    @Provides
    @Singleton
    fun provideTranslateClient(httpClient: HttpClient): ITranslateClient {
        return TranslateClientKtorImpl(httpClient)
    }

    @Provides
    @Singleton
    fun provideDatabaseDriver(app: Application): SqlDriver {
        return DatabaseDriverFactory(app).create()
    }

    @Provides
    @Singleton
    fun provideHistoryDataSource(driver: SqlDriver): IHistoryDataSource {
        return HistoryDataSourceSqlDelightImpl(TranslateDatabase(driver))
    }

    @Provides
    @Singleton
    fun provideTranslateUseCase(
        client: ITranslateClient,
        dataSource: IHistoryDataSource
    ): TranslateUseCase {
        return TranslateUseCase(client, dataSource)
    }
}