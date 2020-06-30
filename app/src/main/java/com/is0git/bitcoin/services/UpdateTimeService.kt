package com.is0git.bitcoin.services

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import com.is0git.bitcoin.R
import com.is0git.bitcoin.utils.HtmlParser
import com.is0git.bitcoin.utils.TimeResolver
import kotlinx.coroutines.*

const val UPDATE_TIME_SERVICE_TAG = "TIME_SERVICE_TAG"

class UpdateTimeService : Service(){

    lateinit var updateTimeJob: Job
    var onTimeUpdateListener: OnTimeUpdateListener? = null
    var binder = UpdateServiceBinder()
    var updatePeriodTime = 10 * 1000L
    var time: String? = null
    var isBound: Boolean = false

    override fun onBind(intent: Intent?): IBinder? {
        Log.i(UPDATE_TIME_SERVICE_TAG, "onBind started")
        isBound = true
        updateTimeJob = CoroutineScope(Dispatchers.Default).launch {
            while (isBound) {
                delay(updatePeriodTime)
                if (time != null) {
                    val mTime = getString(
                        R.string.last_updated,
                        TimeResolver.getLastUpdatedTime(time, applicationContext)
                    )
                    val timeFromHtml = HtmlParser.getStringFromHtml(mTime, applicationContext)
                    withContext(Dispatchers.Main) { onTimeUpdateListener?.onTimeUpdate(timeFromHtml)}
                }
            }
        }
        return binder
    }

    inner class UpdateServiceBinder: Binder() {
        fun getService() : UpdateTimeService = this@UpdateTimeService
    }

    override fun onUnbind(intent: Intent?): Boolean {
        isBound = false
        updateTimeJob.cancel("service was unbound")
        onTimeUpdateListener = null
        Log.i(UPDATE_TIME_SERVICE_TAG, "service is unbound and job active?: ${updateTimeJob.isActive}")
        return super.onUnbind(intent)
    }

    interface OnTimeUpdateListener {
        fun onTimeUpdate(lastUpdated: CharSequence?)
    }
}