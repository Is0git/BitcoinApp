package com.is0git.bitcoin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.is0git.bitcoin.R
import dagger.hilt.EntryPoint

@EntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
