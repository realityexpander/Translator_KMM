package com.realityexpander.translator_kmm.translate.presentation

import com.realityexpander.translator_kmm.core.presentation.UiLanguage
import com.realityexpander.translator_kmm.translate.domain.translate.TranslateErrorEnum

data class TranslateState(
    val fromText: String = "",
    val toText: String? = null,
    val isTranslating: Boolean = false,
    val fromLanguage: UiLanguage = UiLanguage.byCode("en"),
    val toLanguage: UiLanguage = UiLanguage.byCode("de"),
    val isChoosingFromLanguage: Boolean = false,
    val isChoosingToLanguage: Boolean = false,
    val error: TranslateErrorEnum? = null,
    val history: List<UiHistoryItem> = emptyList()
)
