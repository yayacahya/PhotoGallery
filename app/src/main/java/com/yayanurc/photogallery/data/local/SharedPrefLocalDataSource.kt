package com.yayanurc.photogallery.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import javax.inject.Inject

class SharedPrefLocalDataSource @Inject constructor(private val context: Context):
    SharedPrefDataSource {

    private fun getSharedPreference(context: Context): SharedPreferences {
        return PreferenceManager.getDefaultSharedPreferences(context)
    }

    override fun setSharedPref(name: String, value: Any, type: Int) {
        val editor: SharedPreferences.Editor = getSharedPreference(context).edit()
        when (type) {
            SharedPrefTypes.BOOLEAN.ordinal -> editor.putBoolean(name, value as Boolean)
            SharedPrefTypes.FLOAT.ordinal -> editor.putFloat(name, value as Float)
            SharedPrefTypes.INTEGER.ordinal -> editor.putInt(name, value as Int)
            SharedPrefTypes.LONG.ordinal -> editor.putLong(name, value as Long)
            SharedPrefTypes.STRING.ordinal -> editor.putString(name, value as String)
        }
        editor.apply()
    }

    override fun getSharedPref(name: String, type: Int): Any {
        return when (type) {
            SharedPrefTypes.BOOLEAN.ordinal -> getSharedPreference(context).getBoolean(name, false)
            SharedPrefTypes.FLOAT.ordinal -> getSharedPreference(context).getFloat(name, -1f)
            SharedPrefTypes.INTEGER.ordinal -> getSharedPreference(context).getInt(name, -1)
            SharedPrefTypes.LONG.ordinal -> getSharedPreference(context).getLong(name, -1)
            SharedPrefTypes.STRING.ordinal -> getSharedPreference(context).getString(name, "empty") as String
            else -> -1
        }
    }
}