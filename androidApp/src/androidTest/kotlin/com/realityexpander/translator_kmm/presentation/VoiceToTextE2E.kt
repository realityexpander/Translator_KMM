package com.realityexpander.translator_kmm.presentation

import android.Manifest
import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.GrantPermissionRule
import com.realityexpander.translator_kmm.android.MainActivity
import com.realityexpander.translator_kmm.android.R
import com.realityexpander.translator_kmm.android.di.AppModule
import com.realityexpander.translator_kmm.android.voice_to_text.di.VoiceToTextModule
import com.realityexpander.translator_kmm.translate.data.remote.FakeTranslateClient
import com.realityexpander.translator_kmm.translate.domain.translate.ITranslateClient
import com.realityexpander.translator_kmm.voice_to_text.data.FakeVoiceToTextProcessor
import com.realityexpander.translator_kmm.voice_to_text.domain.IVoiceToTextProcessor
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(AppModule::class, VoiceToTextModule::class)
class VoiceToTextE2E {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val permissionRule = GrantPermissionRule.grant(
        Manifest.permission.RECORD_AUDIO
    )

    @Inject
    lateinit var fakeVoiceParser: IVoiceToTextProcessor

    @Inject
    lateinit var fakeClient: ITranslateClient

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun recordAndTranslate() = runBlocking<Unit> {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val vttProcessor = fakeVoiceParser as FakeVoiceToTextProcessor
        val client = fakeClient as FakeTranslateClient

        composeRule
            .onNodeWithContentDescription(context.getString(R.string.record_audio))
            .performClick()

        composeRule
            .onNodeWithContentDescription(context.getString(R.string.record_audio))
            .performClick()

        composeRule
            .onNodeWithContentDescription(context.getString(R.string.stop_recording))
            .performClick()

        composeRule
            .onNodeWithText(vttProcessor.voiceResult)
            .assertIsDisplayed()

        composeRule
            .onNodeWithContentDescription(context.getString(R.string.apply))
            .performClick()

        composeRule
            .onNodeWithText(vttProcessor.voiceResult)
            .assertIsDisplayed()

        // Not needed bc it automatically translates after returning from the voice to text screen
//        composeRule
//            .onNodeWithText(context.getString(R.string.translate), ignoreCase = true)
//            .performClick()

        composeRule
            .onNodeWithText(vttProcessor.voiceResult)
            .assertIsDisplayed()

        composeRule
            .onNodeWithText(client.translatedText)
            .assertIsDisplayed()
    }
}