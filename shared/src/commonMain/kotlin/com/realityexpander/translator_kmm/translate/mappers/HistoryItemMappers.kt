package com.realityexpander.translator_kmm.translate.mappers

import com.realityexpander.translator_kmm.core.presentation.UiLanguage
import com.realityexpander.translator_kmm.translate.domain.history.HistoryItem
import com.realityexpander.translator_kmm.translate.presentation.UiHistoryItem

fun UiHistoryItem.toHistoryItem(): HistoryItem {
    return HistoryItem(
        id,
        fromText = fromText,
        toText = toText,
        fromLanguageCode = fromLanguage.language.langCode,
        toLanguageCode = toLanguage.language.langCode
    )
}


fun HistoryItem.toUiHistoryItem(): UiHistoryItem {
    return UiHistoryItem(
        id ?: 0,
        fromText,
        toText,
        UiLanguage.byCode(fromLanguageCode),
        UiLanguage.byCode(toLanguageCode)
    )
}