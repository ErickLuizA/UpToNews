package com.deverick.uptonews

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.deverick.uptonews.utils.InternationalizationHelper

abstract class BaseActivity : AppCompatActivity() {
    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(InternationalizationHelper.setLocale(newBase))
    }
}