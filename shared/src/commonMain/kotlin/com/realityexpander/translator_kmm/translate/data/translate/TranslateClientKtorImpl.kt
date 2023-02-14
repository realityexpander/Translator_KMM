package com.realityexpander.translator_kmm.translate.data.translate

import com.realityexpander.translator_kmm.NetworkConstants
import com.realityexpander.translator_kmm.core.domain.language.Language
import com.realityexpander.translator_kmm.translate.domain.translate.ITranslateClient
import com.realityexpander.translator_kmm.translate.domain.translate.TranslateErrorEnum
import com.realityexpander.translator_kmm.translate.domain.translate.TranslateException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.utils.io.errors.*

class TranslateClientKtorImpl(  // was KtorTranslateClient
    private val httpClient: HttpClient
): ITranslateClient {

    override suspend fun translate(
        fromLanguage: Language,
        fromText: String,
        toLanguage: Language
    ): String {
        val result = try {
            httpClient.post {

                println("TranslateClientKtorImpl.translate() - fromLanguage: $fromLanguage")
                println("TranslateClientKtorImpl.translate() - fromText: $fromText")
                println("TranslateClientKtorImpl.translate() - toLanguage: $toLanguage")

                url(NetworkConstants.BASE_URL + "/translate")
                contentType(ContentType.Application.Json)
                setBody(
                    TranslateRequestDto(
                        textToTranslate = fromText,
                        sourceLanguageCode = fromLanguage.langCode,
                        targetLanguageCode = toLanguage.langCode
                    )
                )
            }
        } catch(e: IOException) {
            throw TranslateException(TranslateErrorEnum.SERVICE_UNAVAILABLE)
        }

        when(result.status.value) {
            in 200..299 -> Unit
            500 -> throw TranslateException(TranslateErrorEnum.SERVER_ERROR)
            in 400..499 -> throw TranslateException(TranslateErrorEnum.CLIENT_ERROR)
            else -> throw TranslateException(TranslateErrorEnum.UNKNOWN_ERROR)
        }

        return try {
            val res = result.body<TranslateResponseDto>().translatedText
            println("TranslateClientKtorImpl.translate() - res: $res")
            res
        } catch(e: Exception) {
            throw TranslateException(TranslateErrorEnum.SERVER_ERROR)
        }
    }
}