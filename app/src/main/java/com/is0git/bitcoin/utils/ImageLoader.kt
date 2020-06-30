package com.is0git.bitcoin.utils

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide


infix fun ImageView.loadImageWith(url: String) {
    Glide.with(this.context).load(url).centerCrop().into(this)
}

infix fun ImageView.loadImageWith(@DrawableRes drawableId: Int) {
    Glide.with(this.context).load(drawableId).centerCrop().into(this)
}