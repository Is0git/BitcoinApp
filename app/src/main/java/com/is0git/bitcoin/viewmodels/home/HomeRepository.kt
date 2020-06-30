package com.is0git.bitcoin.viewmodels.home

import android.util.Log
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.is0git.bitcoin.data.dao.BpiDao
import com.is0git.bitcoin.data.entities.BpiWithData
import com.is0git.bitcoin.data.entities.RoomBpi
import com.is0git.bitcoin.data.entities.RoomBpiData
import com.is0git.bitcoin.network.models.BpiResponse
import com.is0git.bitcoin.network.services.BpiService
import com.is0git.bitcoinexchangecalculator.data.BitcoinCurrency
import com.is0git.bitcoinexchangecalculator.data.ConversionResult
import com.is0git.bitcoinexchangecalculator.exchange_calculator.BitcoinExchangeCalculator
import com.is0git.bitcoinexchangecalculator.listeners.ExchangeListener
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject


class HomeRepository @Inject constructor(
    private val bpiService: BpiService,
    private val bpiDao: BpiDao,
    private val bpiExchangeCalculator: BitcoinExchangeCalculator
) : ExchangeListener<BitcoinCurrency> {
    companion object {
        const val HOME_REPOSITORY_TAG = "HOME_REPOSITORY_TAG"
        const val THROTTLE_TIME = 60 * 1000L
    }

    init {
        bpiExchangeCalculator.exchangeListener = this
    }

    val fromBitcoinConversionLiveData = MutableLiveData<ConversionResult<BitcoinCurrency>>()
    val toBitcoinConversionLiveData = MutableLiveData<ConversionResult<BitcoinCurrency>>()
    val bpiLiveData = bpiDao.getBpiWithDataLiveData("Bitcoin")
    val bpiMediatorLiveData = MediatorLiveData<BpiWithData?>().apply {
        addSource(bpiLiveData) { bpiWithData ->
            if (bpiWithData == null) return@addSource
            bpiExchangeCalculator.updateCurrencies(
                bpiWithData.bpiData.map {
                    BitcoinCurrency(
                        it.code,
                        it.rate,
                        it.rateFloat,
                        it.description,
                        it.symbol
                    )
                }.associateBy { it.code!! }
            )
            this.postValue(bpiWithData)
        }
    }

    suspend fun getBpi() {
        coroutineScope {
            try {
                val timeUpdated = bpiDao.getLastUpdatedTime("Bitcoin")
                if (timeUpdated != null) {
                    val diffTime = System.currentTimeMillis() - timeUpdated
                    if (diffTime < THROTTLE_TIME) throw CancellationException("throttle has not ended, throttle time: $THROTTLE_TIME ms, throttle time left: $diffTime ms")
                }
                val response = bpiService.getBpi()
                if (response.isSuccessful && response.body() != null) {
                    launch { mapBpiInDatabase(response.body()!!) }
                } else {
                    Log.e(HOME_REPOSITORY_TAG, "bpi network response failed")
                    throw CancellationException("bpi network response failed")
                }
            } catch (ex: Exception) {
                when (ex) {
                    is IOException -> Log.e(
                        HOME_REPOSITORY_TAG,
                        "Exception while reading/writing: $ex"
                    )
                    else -> Log.e(HOME_REPOSITORY_TAG, "Not handled exception: $ex")
                }
            }
        }
    }

    private suspend fun mapBpiInDatabase(bpiResponse: BpiResponse) {
        val mBpi = RoomBpi(bpiResponse.chartName ?: "", bpiResponse.disclaimer, bpiResponse.time, System.currentTimeMillis())
        val flow = flow {
            bpiResponse.bpi.let { bpiMap ->
                for ((_, b) in bpiMap) {
                    emit(b)
                }
            }
        }
        val roomBpiData = mutableListOf<RoomBpiData>()
        flow.map {
            RoomBpiData(
                bpiResponse.chartName,
                it.symbol,
                it.rateFloat,
                it.code!!,
                it.rate,
                it.description
            )
        }.collect { roomBpiData.add(it) }
        val bpiWitData = BpiWithData(mBpi, roomBpiData)
        bpiDao.insertBpiWithData(bpiWitData)
    }

    suspend fun convertFromBitcoin(value: String, code: String) {
        bpiExchangeCalculator.convertFromBtc(value, code)
    }

    suspend fun convertToBitcoin(value: String, code: String) {
        bpiExchangeCalculator.convertToBtc(value, code)
    }

    override fun onExchangeFailed(throwable: Throwable?) {
        Log.e(HOME_REPOSITORY_TAG, "failed to exchange: $throwable")
    }

    override fun onExchangeComplete(conversionResult: ConversionResult<BitcoinCurrency>) {
        if (conversionResult.flags == ConversionResult.FLAG_REVERSE_CONVERSION) {
            toBitcoinConversionLiveData.value = conversionResult
        } else {
            fromBitcoinConversionLiveData.value = conversionResult
        }
    }
}