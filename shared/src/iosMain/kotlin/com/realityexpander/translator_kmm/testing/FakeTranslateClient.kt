package com.realityexpander.translator_kmm.testing

import com.realityexpander.translator_kmm.core.domain.language.Language
import com.realityexpander.translator_kmm.translate.domain.translate.TranslateClient

class FakeTranslateClient: TranslateClient {

    var translatedText = "test translation"

    override suspend fun translate(
        fromLanguage: Language,
        fromText: String,
        toLanguage: Language
    ): String {
        return translatedText
    }
}