package com.realityexpander.translator_kmm.android.voice_to_text.di

import android.app.Application
import com.realityexpander.translator_kmm.android.voice_to_text.data.VoiceToTextProcessorAndroidImpl
import com.realityexpander.translator_kmm.voice_to_text.domain.IVoiceToTextProcessor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

// Android needs to scope the processor to the ViewModel, so that it can be
// destroyed when the ViewModel is destroyed. This is because the processor
// uses a CoroutineScope that is tied to the ViewModel's lifecycle.

@Module
@InstallIn(ViewModelComponent::class)
object VoiceToTextModule {

    @Provides
    @ViewModelScoped
    fun provideVTTProcessor(app: Application): IVoiceToTextProcessor {
        return VoiceToTextProcessorAndroidImpl(app)
    }
}