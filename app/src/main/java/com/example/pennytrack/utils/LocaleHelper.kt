package com.example.pennytrack.utils

import android.content.Context
import android.content.res.Configuration
import java.util.*
import android.content.SharedPreferences

object LocaleHelper {



    fun attachBaseContext(context: Context): Context {
        val language = getSavedLanguage(context)
        val locale = Locale(language)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        return context.createConfigurationContext(config)
    }

    fun getSavedLanguage(context: Context): String {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        return sharedPreferences.getString("language", "en") ?: "en"
    }

    fun setLocale(context: Context, languageCode: String) {
        val sharedPreferences = context.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("language", languageCode).apply()
    }



}
