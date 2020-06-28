package com.is0git.bitcoin.ui.fragments

import androidx.fragment.app.Fragment
import dagger.hilt.EntryPoint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@EntryPoint
class HomeFragment : Fragment() {
    var date = Date(1124L).apply {
        val dateFormat = DateFormat.getDateInstance()
    }
}