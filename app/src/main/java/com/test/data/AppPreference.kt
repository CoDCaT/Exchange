package com.test.data

import android.content.Context
import javax.inject.Inject

class AppPreference @Inject constructor(val context: Context) : IPreference {

    override fun setIsFirstRun() {
        context
            .getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(FIRST_RUN, false)
            .apply()
    }

    override fun isFirstRun(): Boolean =
        context
            .getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            .getBoolean(FIRST_RUN, true)

    companion object {
        private const val PREFERENCE_NAME = "app_settings"
        private const val FIRST_RUN = "FIRST_RUN"
    }
}