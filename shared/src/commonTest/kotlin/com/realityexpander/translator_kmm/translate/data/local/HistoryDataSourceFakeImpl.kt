package com.realityexpander.translator_kmm.translate.data.local

import com.realityexpander.translator_kmm.core.domain.util.CommonFlow
import com.realityexpander.translator_kmm.core.domain.util.toCommonFlow
import com.realityexpander.translator_kmm.core.domain.util.toCommonMutableStateFlow
import com.realityexpander.translator_kmm.translate.domain.history.IHistoryDataSource
import com.realityexpander.translator_kmm.translate.domain.history.HistoryItem
import kotlinx.coroutines.flow.MutableStateFlow

class HistoryDataSourceFakeImpl: IHistoryDataSource {

    private val _data = MutableStateFlow<List<HistoryItem>>(emptyList()).toCommonMutableStateFlow()

    override fun getHistory(): CommonFlow<List<HistoryItem>> {
        return _data.toCommonFlow()
    }

    override suspend fun insertHistoryItem(item: HistoryItem) {
        _data.value += item
    }
}