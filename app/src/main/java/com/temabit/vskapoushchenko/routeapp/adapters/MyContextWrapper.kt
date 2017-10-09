package com.temabit.vskapoushchenko.routeapp.adapters

import android.annotation.TargetApi
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import java.util.*
import android.os.LocaleList



/**
 * Created by v.skapoushchenko on 04.10.2017.
 */
class MyContextWrapper(base: Context) : ContextWrapper(base) {

    companion object {
//        @TargetApi(Build.VERSION_CODES.N)
        fun wrap(context: Context, newLocale: Locale) : ContextWrapper{
            val resources = context.resources
            val config = resources.configuration
            var newContext = context

            if (Build.VERSION.SDK_INT > 24) {
                config.setLocale(newLocale)

                val localeList = LocaleList(newLocale)
                LocaleList.setDefault(localeList)
                config.locales = localeList

                newContext = context.createConfigurationContext(config)

            } else if (Build.VERSION.SDK_INT > 17) {
                config.setLocale(newLocale)
                newContext = context.createConfigurationContext(config)

            } else {
                config.locale = newLocale
                resources.updateConfiguration(config, resources.displayMetrics)
            }
            return ContextWrapper(newContext)
        }
    }
}