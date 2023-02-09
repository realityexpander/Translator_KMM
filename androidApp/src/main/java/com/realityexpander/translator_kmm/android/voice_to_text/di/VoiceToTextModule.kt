package com.realityexpander.translator_kmm.android.voice_to_text.di

import android.app.Application
import com.realityexpander.translator_kmm.android.voice_to_text.data.VoiceToTextProcessorAndroidImpl
import com.realityexpander.translator_kmm.voice_to_text.domain.IVoiceToTextProcessor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object VoiceToTextModule {

    @Provides
    @ViewModelScoped
    fun provideVoiceToTextParser(app: Application): IVoiceToTextProcessor {
        return VoiceToTextProcessorAndroidImpl(app)
    }
}