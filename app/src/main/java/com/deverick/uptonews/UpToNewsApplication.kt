package com.deverick.uptonews

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

val LOCALE = stringPreferencesKey("LOCALE")
val THEME = stringPreferencesKey("THEME")

@HiltAndroidApp
class UpToNewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        context = applicationContext

        AppCompatDelegate.setDefaultNightMode(UpToNewsApplication.getTheme())
    }

    companion object {
        private var context: Context? = null

        val appContext: Context?
            get() = context

        fun getCustomLocale(): String? {
            if (appContext != null) {
                var currentLang: String? = null

                runBlocking {
                    currentLang = appContext!!.dataStore.data.first()[LOCALE]
                }

                return currentLang
            }

            return null
        }

        suspend fun setCustomLocale(languageTag: String) {
            appContext!!.dataStore.edit { preferences ->
                preferences[LOCALE] = languageTag
            }
        }

        fun getTheme(): Int {
            if (appContext != null) {
                var currentTheme: String? = null

                runBlocking {
                    currentTheme = appContext!!.dataStore.data.first()[THEME]
                }

                return when (currentTheme) {
                    "dark" -> AppCompatDelegate.MODE_NIGHT_YES
                    "light" -> AppCompatDelegate.MODE_NIGHT_NO
                    "system" -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                }
            } else {
                return AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
            }
        }

        suspend fun setTheme(newTheme: String) {
            appContext!!.dataStore.edit { preferences ->
                preferences[THEME] = newTheme
            }

            when (newTheme) {
                "system" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }

                "dark" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }

                "light" -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }

                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                }
            }
        }
    }
}