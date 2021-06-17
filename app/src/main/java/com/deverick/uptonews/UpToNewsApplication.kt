package com.deverick.uptonews

import android.app.Application
import android.content.Context
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

@HiltAndroidApp
class UpToNewsApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        context = applicationContext
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
    }
}