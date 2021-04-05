package com.johan.reigntest.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.johan.reigntest.di.App

class SessionManager @SuppressLint("CommitPrefEdits") constructor(private val context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences("session", Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

    companion object {
        private const val PREFS_KEY_IDS = "ids"

        @SuppressLint("StaticFieldLeak")
        private var ourInstance: SessionManager? = null
        val instance: SessionManager?
            get() = if (ourInstance == null) SessionManager(App.get()!!.baseContext).also {
                ourInstance = it
            } else ourInstance
    }

    fun setHitsRemoved(set: MutableSet<String>?) {
        editor.clear()
        editor.putStringSet(PREFS_KEY_IDS, set)
        editor.apply()
    }

    fun getHitsRemoved(): MutableSet<String>? {
        return pref.getStringSet(PREFS_KEY_IDS, null)
    }

    fun hasNetwork(): Boolean {
        var result = false
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val capabilities = cm?.getNetworkCapabilities(cm.activeNetwork)

        capabilities?.let {
            if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
                    result = true
        }

        return result
    }
}