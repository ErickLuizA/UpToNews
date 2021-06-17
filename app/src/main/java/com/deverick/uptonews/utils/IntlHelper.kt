package com.deverick.uptonews.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import com.deverick.uptonews.UpToNewsApplication
import java.util.*

class InternationalizationHelper {
    companion object {
        fun setLocale(context: Context?): Context? {
            context?.let {
                val config = Configuration(it.resources.configuration)

                val languageTag = UpToNewsApplication.getCustomLocale()

                val locale = if (languageTag == null) {
                    Locale.getDefault()
                } else {
                    Locale.forLanguageTag(languageTag)
                }

                Locale.setDefault(locale)

                if (Build.VERSION.SDK_INT >=
                    Build.VERSION_CODES.N
                ) {
                    config.setLocale(locale)
                    config.setLayoutDirection(locale)
                } else {
                    @Suppress("DEPRECATION")
                    config.locale = locale;
                }

                return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    context.createConfigurationContext(config)
                } else {
                    val resources = context.resources

                    @Suppress("DEPRECATION")
                    resources?.updateConfiguration(
                        config,
                        resources.displayMetrics
                    )

                    context
                }
            }

            return context
        }
    }
}