package com.realityexpander.translator_kmm.translate.domain.translate

import com.realityexpander.translator_kmm.core.domain.language.Language

interface ITranslateClient {
    suspend fun translate(
        fromLanguage: Language,
        fromText: String,
        toLanguage: Language
    ): String
}