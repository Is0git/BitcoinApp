package com.is0git.bitcoin.viewmodels.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(private val repo: HomeRepository, @Assisted var savedStateHandle: SavedStateHandle) : ViewModel() {
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

    companion object{
        // keys for bundle(restore after process kill)
        const val SELECTED_CURRENCY_CODE = "SelectedCurrencyCode"
        const val MOTION_TRANSITION_ID = "MotionTransitionId"
        const val FROM_BITCOIN_EDIT_TEXT = "FromBitcoinEditText"
        const val TO_BITCOIN_EDIT_TEXT = "ToBitcoinEditText"
        const val IS_BITCOIN_EDIT_FOCUSED = "IsBitcoinEditFocused"
        const val IS_CURRENCY_EDIT_FOCUSED = "IsCurrencyEditFocused"
        const val HAS_BPI_LIST_ANIMATION_PLAYED = "HasBpiListAnimationPlayed"
    }
}