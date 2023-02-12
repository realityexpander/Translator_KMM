package com.realityexpander.translator_kmm.translate.domain.translate

import com.realityexpander.translator_kmm.core.domain.language.Language
import com.realityexpander.translator_kmm.translate.domain.translate.ITranslateClient

class TranslateClientFakeImpl: ITranslateClient {

    var expectedTranslatedText = "test translation"

    override suspend fun translate(
        fromLanguage: Language,
        fromText: String,
        toLanguage: Language
    ): String {
        return expectedTranslatedText
    }
}