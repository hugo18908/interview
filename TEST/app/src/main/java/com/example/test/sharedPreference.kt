package com.example.fish.day13_localstoragesharedpreferences

import android.content.Context
import android.util.Log

class sharedPreference(context: Context) {
    private val pref = context.getSharedPreferences("City", Context.MODE_PRIVATE)

    fun save(hs:MutableSet<String>) {

        pref.edit().putStringSet("city", hs).apply()
    }

    fun get() : MutableSet<String>?{
        return pref.getStringSet("city", null)
    }
}