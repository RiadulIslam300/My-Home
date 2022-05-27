package com.saklayen.techhome.utils

import android.content.Context
import android.preference.PreferenceManager
import com.saklayen.techhome.models.Device
import com.saklayen.techhome.models.Report
import java.util.prefs.Preferences

class Checker {
    companion object{
        var INSERT_OPERATION : Int = 1;
        var DELETE_OPERATION : Int = 2;
        var UPDATE_OPERATION : Int = 3;
        var DELETE_ALL_OPERATION : Int = 4;

        var deviceOperations : Int = 1;
        var reportOperations : Int = 2;

        lateinit var device : Device
        lateinit var report : Report

        var connected = false

        fun getPreference(key: String?, context: Context): String? {
            var value = ""
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            value = prefs.getString(key, "0").toString()
            return value
        }


        fun savePreference(key: String?, value: String?,context: Context) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = sharedPreferences.edit()
            editor.putString(key, value)
            editor.apply()

            //val pref = PreferenceManager.getDefaultSharedPreferences(context)
        }
    }
}