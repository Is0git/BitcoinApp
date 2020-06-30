package com.is0git.bitcoin.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import com.is0git.bitcoin.R
import java.util.*

fun getImageIdByCode(code: String, context: Context): Int {
    return context.resources.getIdentifier(code, "drawable", context.packageName)
}

fun getImageDrawableById(@DrawableRes id: Int, context: Context): Drawable? {
    return ResourcesCompat.getDrawable(context.resources, id, null)
}

fun ImageView.setImageDrawable(code: String) {
    try {
        val id = getImageIdByCode("ic_${code.toLowerCase(Locale.getDefault())}", context)
        this.setImageDrawable(getImageDrawableById(id, context))
    } catch (ex: Resources.NotFoundException) {
        this.setImageResource(R.drawable.ic_coin)
    }

}