package com.realityexpander.translator_kmm.translate.domain.history

import com.realityexpander.translator_kmm.core.domain.util.CommonFlow

interface HistoryDataSource {
    fun getHistory(): CommonFlow<List<HistoryItem>>
    suspend fun insertHistoryItem(item: HistoryItem)
}