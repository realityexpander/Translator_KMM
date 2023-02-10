package com.realityexpander.translator_kmm.translate.presentation

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEmpty
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import com.realityexpander.translator_kmm.core.presentation.UiLanguage
import com.realityexpander.translator_kmm.translate.data.local.HistoryRepositoryFakeImpl
import com.realityexpander.translator_kmm.translate.data.remote.TranslateClientFakeImpl
import com.realityexpander.translator_kmm.translate.domain.history.HistoryItem
import com.realityexpander.translator_kmm.translate.domain.translate.TranslateUseCase
import com.realityexpander.translator_kmm.translate.mappers.toHistoryItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.yield
import kotlin.test.BeforeTest
import kotlin.test.Test

class TranslateViewModelTest {

    private lateinit var viewModel: TranslateViewModel
    private lateinit var client: TranslateClientFakeImpl
    private lateinit var historyRepo: HistoryRepositoryFakeImpl

    @BeforeTest
    fun setUp() {
        client = TranslateClientFakeImpl()
        historyRepo = HistoryRepositoryFakeImpl()

        val translate = TranslateUseCase(
            client = client,
            historyRepo = historyRepo
        )

        viewModel = TranslateViewModel(
            translate = translate,
            historyRepo = historyRepo,
            coroutineScope = CoroutineScope(Dispatchers.Default)
        )
    }

    @Test
    fun `State and history items are properly combined`() = runBlocking {
        viewModel.state.test {
            // Receive and Check for initial state
            val initialState = awaitItem()
            assertThat(initialState).isEqualTo(TranslateState())

            // Arrange
            val item = HistoryItem(
                id = 0,
                fromLanguageCode = "en",
                fromText = "from",
                toLanguageCode = "de",
                toText = "to"
            )
            historyRepo.insertHistoryItem(item)

            // Act
            val state = awaitItem()

            // Assert
            val expected = UiHistoryItem(
                id = item.id!!,
                fromText = item.fromText,
                toText = item.toText,
                fromLanguage = UiLanguage.byCode(item.fromLanguageCode),
                toLanguage = UiLanguage.byCode(item.toLanguageCode)
            )
            assertThat(state.history.first()).isEqualTo(expected)
        }
    }

    @Test
    fun `Translate success - state properly updated`() = runBlocking {
        viewModel.state.test {
            // Receive the initial state
            awaitItem()

            // Arrange
            viewModel.onEvent(TranslateEvent.ChangeTranslationText("test"))
            awaitItem()

            // Act
            viewModel.onEvent(TranslateEvent.Translate)

            // Assert

            // Check for loading state
            val loadingState = awaitItem()
            assertThat(loadingState.isTranslating).isTrue()

            // Check for correct result state
            val resultState = awaitItem()
            assertThat(resultState.isTranslating).isFalse()
            assertThat(resultState.toText).isEqualTo(client.expectedTranslatedText)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Translate success - state properly updated2`() = runBlocking {
        viewModel.state.test {
            // Receive the initial state
            awaitItem()

            // Arrange
            viewModel.onEvent(TranslateEvent.ChangeTranslationText("test"))
            awaitItem()

            // Act
            viewModel.onEvent(TranslateEvent.Translate)

            // Assert

            // Check for loading state
            val loadingState = awaitItem()
            assertThat(loadingState.isTranslating).isTrue()

            // Check for correct result state
            val resultState = awaitItem()
            assertThat(resultState.isTranslating).isFalse()
            assertThat(resultState.toText).isEqualTo(client.expectedTranslatedText)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Delete History Item success - state properly updated`() = runBlocking {
        viewModel.state.test {
            // Receive the initial state
            awaitItem()

            // Arrange
            val item = UiHistoryItem(
                id = 0,
                fromLanguage = UiLanguage.byCode("en"),
                fromText = "from",
                toLanguage = UiLanguage.byCode("de"),
                toText = "to"
            )
            historyRepo.insertHistoryItem(item.toHistoryItem())

            awaitItem() // allow the insert to complete

            // Act
            viewModel.onEvent(TranslateEvent.DeleteHistoryItem(item))

            // Assert
            val state = awaitItem()
            assertThat(state.history).isEmpty()
        }
    }
}