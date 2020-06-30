package com.is0git.bitcoin.utils

import android.view.View
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun View.clicks(): Flow<View> = callbackFlow {
    this@clicks.setOnClickListener { this.offer(it) }
    awaitClose { this@clicks.setOnClickListener(null) }
}

fun EditText.addChannelTextChangedListener() : Flow<CharSequence?> = callbackFlow {
    this@addChannelTextChangedListener.doOnTextChanged { text, start, before, count -> offer(text)}
    awaitClose()
}