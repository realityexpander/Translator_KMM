package com.realityexpander.translator_kmm.translate.data.local

import com.realityexpander.translator_kmm.core.domain.util.CommonFlow
import com.realityexpander.translator_kmm.core.domain.util.toCommonFlow
import com.realityexpander.translator_kmm.translate.domain.history.IHistoryRepository
import com.realityexpander.translator_kmm.translate.domain.history.HistoryItem
import kotlinx.coroutines.flow.MutableStateFlow

class HistoryRepositoryFakeImpl: IHistoryRepository {

    private val _data = MutableStateFlow<List<HistoryItem>>(emptyList())

    override fun getHistory(): CommonFlow<List<HistoryItem>> {
        return _data.toCommonFlow()
    }

    override suspend fun insertHistoryItem(item: HistoryItem) {
        _data.value += item
    }

    override suspend fun deleteHistoryItem(id: Long) {
        _data.value = _data.value.filter { it.id != id }
    }
}