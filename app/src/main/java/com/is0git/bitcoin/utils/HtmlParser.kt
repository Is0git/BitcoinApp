package com.is0git.bitcoin.utils

import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Spanned
import androidx.annotation.StringRes

object HtmlParser {
    fun getStringFromHtmlRes(@StringRes htmlStringRes: Int, context: Context) : Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(context.getString(htmlStringRes), Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(context.getString(htmlStringRes))
        }
    }

    fun getStringFromHtml( string: String, context: Context) : Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(string, Html.FROM_HTML_MODE_COMPACT)
        } else {
            Html.fromHtml(string)
        }
    }
}