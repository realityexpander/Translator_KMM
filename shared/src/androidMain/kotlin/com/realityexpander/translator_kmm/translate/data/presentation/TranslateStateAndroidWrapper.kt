package com.realityexpander.translator_kmm.translate.data.presentation

import android.os.Parcel
import android.os.Parcelable
import com.realityexpander.translator_kmm.core.presentation.UiLanguage
import com.realityexpander.translator_kmm.translate.domain.translate.TranslateErrorEnum
import com.realityexpander.translator_kmm.translate.presentation.TranslateState
import com.realityexpander.translator_kmm.translate.presentation.UiHistoryItem.Companion.convertParceledStringToUiHistoryItem
import kotlinx.parcelize.RawValue

//@Parcelize - using this annotation requires all the data classes to be parcelable, which is not the case.
data class TranslateStateAndroidWrapper(
    val data: @RawValue TranslateState
) : Parcelable {
    constructor(parcel: Parcel) : this(data = TranslateState(
        fromText = parcel.readString() ?: "",
        toText = parcel.readString(),
        isTranslating = parcel.readByte() != 0.toByte(),
        fromLanguage = UiLanguage.byCode(parcel.readString() ?: "en"),
        toLanguage = UiLanguage.byCode(parcel.readString() ?: "de"),
        isChoosingFromLanguage = parcel.readByte() != 0.toByte(),
        isChoosingToLanguage = parcel.readByte() != 0.toByte(),
        error = when(val err= parcel.readString()) { null -> null; else -> TranslateErrorEnum.valueOf(err) },

        // Instead of using JSON.parse, custom parse the string to UiHistoryItem using an extension func.
        history = parcel.createStringArray()?.map {
                it.convertParceledStringToUiHistoryItem()
            } ?: emptyList()
    )
    )

    override fun describeContents(): Int {
        return Parcelable.CONTENTS_FILE_DESCRIPTOR
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(data.fromText)
        dest.writeString(data.toText)
        dest.writeByte(if (data.isTranslating) 1 else 0)
        dest.writeString(data.fromLanguage.language.langCode)
        dest.writeString(data.toLanguage.language.langCode)
        dest.writeByte(if (data.isChoosingFromLanguage) 1 else 0)
        dest.writeByte(if (data.isChoosingToLanguage) 1 else 0)
        dest.writeString(data.error?.name)

        // Instead of using JSON.stringify, we simply use the toString() method of the data class.
        dest.writeStringArray(data.history.map { it.toString() }.toTypedArray())
    }

    companion object CREATOR : Parcelable.Creator<TranslateStateAndroidWrapper> {
        override fun createFromParcel(parcel: Parcel): TranslateStateAndroidWrapper {
            return TranslateStateAndroidWrapper(parcel)
        }

        override fun newArray(size: Int): Array<TranslateStateAndroidWrapper?> {
            return arrayOfNulls(size)
        }
    }
}
