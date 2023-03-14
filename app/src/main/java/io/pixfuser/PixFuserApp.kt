package io.pixfuser

import android.app.Application
import android.content.ContextWrapper
import io.phantomBridge.SessionHandler

class PixFuserApp: Application() {

    override fun onCreate() {
        super.onCreate()
        SessionHandler.preparePreferences(
            getSharedPreferences(packageName + "_preferences", ContextWrapper.MODE_PRIVATE)
        )
    }
}