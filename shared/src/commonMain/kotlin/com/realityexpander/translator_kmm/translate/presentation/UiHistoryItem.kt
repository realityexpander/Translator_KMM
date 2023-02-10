package com.realityexpander.translator_kmm.translate.presentation

import com.realityexpander.translator_kmm.core.domain.language.Language
import com.realityexpander.translator_kmm.core.presentation.UiLanguage
import com.realityexpander.translator_kmm.translate.domain.history.HistoryItem

data class UiHistoryItem(
    val id: Long,
    val fromText: String,
    val toText: String,
    val fromLanguage: UiLanguage,
    val toLanguage: UiLanguage
) {

    // Convert a `print stringified` UiHistoryItem to a UiHistoryItem -  maybe json would be better but this is fast and no need to use serialization
    // UiHistoryItem(id=2, fromText=no match, toText=no hay coincidencia, fromLanguage=UiLanguage(language=ENGLISH, drawableRes=2131099657),
    //   toLanguage=UiLanguage(language=SPANISH, drawableRes=2131099658))
    // Note: this is for handling the parcelable for process death on Android.  It's not used on iOS.
    companion object {
        fun String.convertParceledStringToUiHistoryItem(): UiHistoryItem {
            val string = this

            val id = string.substringAfter("id=").substringBefore(",").toLong()
            val fromText = string.substringAfter("fromText=").substringBefore(",").trim()
            val toText = string.substringAfter("toText=").substringBefore(",").trim()

            val fromLanguage = string.substringAfter("fromLanguage=")
                .substringAfter("language=")
                .substringBefore(",").trim()
            val fromLanguageEnum = Language.valueOf(fromLanguage).langCode

            val toLanguage = string.substringAfter("toLanguage=")
                .substringAfter("language=")
                .substringBefore(",").trim()
            val toLanguageEnum = Language.valueOf(toLanguage).langCode

            return UiHistoryItem(
                id,
                fromText,
                toText,
                UiLanguage.byCode(fromLanguageEnum),
                UiLanguage.byCode(toLanguageEnum)
            )
        }
    }
}