package com.realityexpander.translator_kmm.translate.domain.translate

import com.realityexpander.translator_kmm.core.domain.language.Language
import com.realityexpander.translator_kmm.core.domain.util.Resource
import com.realityexpander.translator_kmm.translate.domain.history.IHistoryRepository
import com.realityexpander.translator_kmm.translate.domain.history.HistoryItem

class TranslateUseCase(  // Translate
    private val client: ITranslateClient,
    private val historyRepo: IHistoryRepository
) {

    suspend fun execute(
        fromLanguage: Language,
        fromText: String,
        toLanguage: Language
    ): Resource<String> {
        return try {
            val translatedText = client.translate(
                fromLanguage, fromText, toLanguage
            )

            historyRepo.insertHistoryItem(
                HistoryItem(
                    id = null,
                    fromLanguageCode = fromLanguage.langCode,
                    fromText = fromText,
                    toLanguageCode = toLanguage.langCode,
                    toText = translatedText,
                )
            )

            Resource.Success(translatedText)
        } catch(e: TranslateException) {
            e.printStackTrace()
            Resource.Error(e)
        }
    }
}