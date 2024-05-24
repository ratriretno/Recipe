
package com.ratriretno.recipe

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RecipeApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */

    override fun onCreate() {
        super.onCreate()
//        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
    }
}
