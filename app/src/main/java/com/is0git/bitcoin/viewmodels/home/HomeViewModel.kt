package com.is0git.bitcoin.viewmodels.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(private val repo: HomeRepository, @Assisted var savedStateHandle: SavedStateHandle) : ViewModel() {
    companion object{
        //keys for bundle(restore after process kill)
        const val SELECTED_CURRENCY_CODE = "SELECTED_CURRENCY_CODE"
        const val MOTION_TRANSITION_ID = "MOTION_TRANSITION_ID"
        const val FROM_BITCOIN_EDIT_TEXT = "FROM_BITCOIN_EDIT_TEXT"
        const val TO_BITCOIN_EDIT_TEXT = "TO_BITCOIN_EDIT_TEXT"
        const val IS_BITCOIN_EDIT_FOCUSED = "IS_BITCOIN_EDIT_FOCUSED"
        const val IS_CURRENCY_EDIT_FOCUSED = "IS_CURRENCY_EDIT_FOCUSED"
        const val HAS_BPI_LIST_ANIMATION_PLAYED = "HAS_BPI_LIST_ANIMATION_PLAYED"
    }

    init {
        viewModelScope.launch {
            getBpi()
        }
    }

    val bpiLiveData = repo.bpiLiveData
    val fromBitcoinConversionLiveData = repo.fromBitcoinConversionLiveData
    val toBitcoinConversionLiveData = repo.toBitcoinConversionLiveData
    var selectedCurrencyCode = savedStateHandle.getLiveData<String>(SELECTED_CURRENCY_CODE)
    val bpiMediatorLiveData = repo.bpiMediatorLiveData

    suspend fun getBpi() {
        repo.getBpi()
    }

    suspend fun convertFromBitcoin(value: String, code: String) {
        repo.convertFromBitcoin(value, code)
    }

    suspend fun convertToBitcoin(value: String, code: String) {
        repo.convertToBitcoin(value, code)
    }
}