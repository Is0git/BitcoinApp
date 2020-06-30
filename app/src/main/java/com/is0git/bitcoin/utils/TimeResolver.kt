package com.is0git.bitcoin.utils

import android.content.Context
import android.util.Log
import com.is0git.bitcoin.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


object TimeResolver {
    const val RSS_FORMAT = "MMM dd, yyyy HH:mm:ss z"
    private const val SECOND_MILLIS = 1000
    private const val MINUTE_MILLIS = 60 * SECOND_MILLIS
    private const val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private const val DAY_MILLIS = 24 * HOUR_MILLIS
    private const val WEEK_MILLIS = 7 * DAY_MILLIS
    fun getTimeAgo(timeGiven: Long, context: Context): CharSequence? {
        var time = timeGiven
        if (time < 1000000000000L) { // if timestamp given in seconds, convert to millis
            time *= 1000
        }
        val now = System.currentTimeMillis()
        if (time > now || time <= 0) {
            return null
        }
        val diff = now - time
        val resources = context.resources
        return when {
            diff < MINUTE_MILLIS / 2 -> {
                resources.getString(R.string.just_now)
            }
            diff < MINUTE_MILLIS -> {
                resources.getString(R.string.less_than_minute)
            }
            diff > MINUTE_MILLIS && diff < MINUTE_MILLIS * 2 -> {
                resources.getString(R.string.minute_ago)
            }
            diff < HOUR_MILLIS / 2 -> {
                resources.getString(R.string.minutes_ago, diff / MINUTE_MILLIS)
            }
            diff < HOUR_MILLIS -> {
                resources.getString(R.string.hour_ago)
            }
            diff < 24 * HOUR_MILLIS -> {
                resources.getString(R.string.hours_ago, diff / HOUR_MILLIS)
            }
            diff < 48 * HOUR_MILLIS -> {
                resources.getString(R.string.yesterday)
            }
            diff < WEEK_MILLIS -> {
                resources.getString(R.string.week_ago)
            }
            diff < 2 * WEEK_MILLIS -> {
                resources.getString(R.string.weeks_ago, diff / WEEK_MILLIS)
            }
            else -> {
                resources.getString(R.string.long_time_ago)
            }
        }
    }

    fun getLastUpdatedTime(time: String?, context: Context, locale: Locale): CharSequence? {
        return try {
            time ?: throw KotlinNullPointerException("time was not provided")
            val simpleDateFormatter =
                SimpleDateFormat(RSS_FORMAT, locale)
            simpleDateFormatter.timeZone = TimeZone.getDefault()
            val date = simpleDateFormatter.parse(time)
            getTimeAgo(date.time, context)
        } catch (ex: ParseException) {
            Log.d("TIME_RESOLVER", "exception while parsing: $ex")
            null
        }
    }

    fun getFormattedTimeInCurrentTimeZone(format: String, time: String, locale: Locale) : String? {
       return SimpleDateFormat(format, locale).run {
            val date = parse(time)
            timeZone = TimeZone.getDefault()
            date ?: return@run null
            format(date)
        }
    }

}