package com.realityexpander.translator_kmm.translate.data.translate

import kotlinx.serialization.Serializable

@Serializable
data class TranslateResponseDto(
    val translatedText: String
)
